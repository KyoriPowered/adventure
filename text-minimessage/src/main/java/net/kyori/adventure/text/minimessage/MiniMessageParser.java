/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.minimessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.examination.Examinable;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MiniMessageParser {
  final TagResolver tagResolver;

  MiniMessageParser() {
    this.tagResolver = TagResolver.standard();
  }

  MiniMessageParser(final TagResolver tagResolver) {
    this.tagResolver = tagResolver;
  }

  @NotNull String escapeTokens(final @NotNull ContextImpl context) {
    final StringBuilder sb = new StringBuilder(context.message().length());
    this.escapeTokens(sb, context);
    return sb.toString();
  }

  void escapeTokens(final StringBuilder sb, final @NotNull ContextImpl context) {
    this.escapeTokens(sb, context.message(), context);
  }

  private void escapeTokens(final StringBuilder sb, final String richMessage, final ContextImpl context) {
    this.processTokens(sb, richMessage, context, (token, builder) -> {
      builder.append('\\').append(TokenParser.TAG_START);
      if (token.type() == TokenType.CLOSE_TAG) {
        builder.append(TokenParser.CLOSE_TAG);
      }
      final List<Token> childTokens = token.childTokens();
      for (int i = 0; i < childTokens.size(); i++) {
        if (i != 0) {
          builder.append(TokenParser.SEPARATOR);
        }
        this.escapeTokens(builder, childTokens.get(i).get(richMessage).toString(), context); // todo: do we need to unwrap quotes on this?
      }
      builder.append(TokenParser.TAG_END);
    });
  }

  @NotNull String stripTokens(final @NotNull ContextImpl context) {
    final StringBuilder sb = new StringBuilder(context.message().length());
    this.processTokens(sb, context, (token, builder) -> {});
    return sb.toString();
  }

  private void processTokens(final @NotNull StringBuilder sb, final @NotNull ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
    this.processTokens(sb, context.message(), context, tagHandler);
  }

  private void processTokens(final @NotNull StringBuilder sb, final @NotNull String richMessage, final @NotNull ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
    final TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
    final List<Token> root = TokenParser.tokenize(richMessage, true);
    for (final Token token : root) {
      switch (token.type()) {
        case TEXT:
          sb.append(richMessage, token.startIndex(), token.endIndex());
          break;
        case OPEN_TAG:
        case CLOSE_TAG:
        case OPEN_CLOSE_TAG:
          // extract tag name
          if (token.childTokens().isEmpty()) {
            sb.append(richMessage, token.startIndex(), token.endIndex());
            continue;
          }
          final String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(token.childTokens().get(0).get(richMessage).toString());
          if (combinedResolver.has(sanitized)) {
            tagHandler.accept(token, sb);
          } else {
            sb.append(richMessage, token.startIndex(), token.endIndex());
          }
          break;
        default:
          throw new IllegalArgumentException("Unsupported token type " + token.type());
      }
    }
  }

  @NotNull RootNode parseToTree(final @NotNull ContextImpl context) {
    final TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
    final String processedMessage = context.preProcessor().apply(context.message());
    final Consumer<String> debug = context.debugOutput();
    if (debug != null) {
      debug.accept("Beginning parsing message ");
      debug.accept(processedMessage);
      debug.accept("\n");
    }

    final TokenParser.TagProvider transformationFactory;
    if (debug != null) {
      transformationFactory = (name, args, token) -> {
        try {
          debug.accept("Attempting to match node '");
          debug.accept(name);
          debug.accept("'");
          if (token != null) {
            debug.accept(" at column ");
            debug.accept(String.valueOf(token.startIndex()));
          }
          debug.accept("\n");

          final @Nullable Tag transformation = combinedResolver.resolve(name, new ArgumentQueueImpl<>(context, args), context);

          if (transformation == null) {
            debug.accept("Could not match node '");
            debug.accept(name);
            debug.accept("'\n");
          } else {
            debug.accept("Successfully matched node '");
            debug.accept(name);
            debug.accept("' to tag ");
            debug.accept(transformation instanceof Examinable ? ((Examinable) transformation).examinableName() : transformation.getClass().getName());
            debug.accept("\n");
          }

          return transformation;
        } catch (final ParsingException e) {
          if (token != null && e instanceof ParsingExceptionImpl) {
            final ParsingExceptionImpl impl = (ParsingExceptionImpl) e;
            if (impl.tokens().length == 0) {
              impl.tokens(new Token[] {token});
            }
          }
          debug.accept("Could not match node '");
          debug.accept(name);
          debug.accept("' - ");
          debug.accept(e.getMessage());
          debug.accept("\n");
          return null;
        }
      };
    } else {
      transformationFactory = (name, args, token) -> {
        try {
          return combinedResolver.resolve(name, new ArgumentQueueImpl<>(context, args), context);
        } catch (final ParsingException ignored) {
          return null;
        }
      };
    }
    final Predicate<String> tagNameChecker = name -> {
      final String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(name);
      return combinedResolver.has(sanitized);
    };

    final String preProcessed = TokenParser.resolvePreProcessTags(processedMessage, transformationFactory);
    context.message(preProcessed);
    // Then, once MiniMessage placeholders have been inserted, we can do the real parse
    final RootNode root = TokenParser.parse(transformationFactory, tagNameChecker, preProcessed, processedMessage, context.strict());

    if (debug != null) {
      debug.accept("Text parsed into element tree:\n");
      debug.accept(root.toString());
    }

    return root;
  }

  @NotNull Component parseFormat(final @NotNull ContextImpl context) {
    final ElementNode root = this.parseToTree(context);
    return Objects.requireNonNull(context.postProcessor().apply(this.treeToComponent(root, context)), "Post-processor must not return null");
  }

  @NotNull Component treeToComponent(final @NotNull ElementNode node, final @NotNull ContextImpl context) {
    Component comp = Component.empty();
    Tag tag = null;
    if (node instanceof ValueNode) {
      comp = Component.text(((ValueNode) node).value());
    } else if (node instanceof TagNode) {
      final TagNode tagNode = (TagNode) node;

      tag = tagNode.tag();

      // special case for gradient and stuff
      if (tag instanceof Modifying) {
        final Modifying modTransformation = (Modifying) tag;

        // first walk the tree
        this.visitModifying(modTransformation, tagNode, 0);
        modTransformation.postVisit();
      }

      if (tag instanceof Inserting) {
        comp = ((Inserting) tag).value();
      }
    }

    if (!node.unsafeChildren().isEmpty()) {
      final List<Component> children = new ArrayList<>(comp.children().size() + node.children().size());
      children.addAll(comp.children());
      for (final ElementNode child : node.unsafeChildren()) {
        children.add(this.treeToComponent(child, context));
      }
      comp = comp.children(children);
    }

    // special case for gradient and stuff
    if (tag instanceof Modifying) {
      comp = this.handleModifying((Modifying) tag, comp, 0);
    }

    final Consumer<String> debug = context.debugOutput();
    if (debug != null) {
      debug.accept("==========\ntreeToComponent \n");
      debug.accept(node.toString());
      debug.accept("\n");
      debug.accept(comp.examine(MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining("\n")));
      debug.accept("\n==========\n");
    }

    return comp;
  }

  private void visitModifying(final Modifying modTransformation, final ElementNode node, final int depth) {
    modTransformation.visit(node, depth);
    for (final ElementNode child : node.unsafeChildren()) {
      this.visitModifying(modTransformation, child, depth + 1);
    }
  }

  private Component handleModifying(final Modifying modTransformation, final Component current, final int depth) {
    Component newComp = modTransformation.apply(current, depth);
    for (final Component child : current.children()) {
      newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1));
    }
    return newComp;
  }
}
