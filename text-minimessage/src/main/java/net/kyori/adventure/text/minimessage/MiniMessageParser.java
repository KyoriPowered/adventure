/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenParser;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.text.minimessage.transformation.Modifying;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.jetbrains.annotations.NotNull;

final class MiniMessageParser {
  final TransformationRegistry registry;
  final PlaceholderResolver placeholderResolver;

  MiniMessageParser() {
    this.registry = TransformationRegistry.standard();
    this.placeholderResolver = PlaceholderResolver.empty();
  }

  MiniMessageParser(final TransformationRegistry registry, final PlaceholderResolver placeholderResolver) {
    this.registry = registry;
    this.placeholderResolver = placeholderResolver;
  }

  @NotNull String escapeTokens(final @NotNull String richMessage, final @NotNull ContextImpl context) {
    final StringBuilder sb = new StringBuilder(richMessage.length());
    this.escapeTokens(sb, richMessage, context);
    return sb.toString();
  }

  void escapeTokens(final StringBuilder sb, final @NotNull String richMessage, final @NotNull ContextImpl context) {
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

  @NotNull String stripTokens(final @NotNull String richMessage, final @NotNull ContextImpl context) {
    final StringBuilder sb = new StringBuilder(richMessage.length());
    this.processTokens(sb, richMessage, context, (token, builder) -> {});
    return sb.toString();
  }

  private void processTokens(final @NotNull StringBuilder sb, final @NotNull String richMessage, final @NotNull ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
    final PlaceholderResolver combinedResolver = PlaceholderResolver.combining(context.placeholderResolver(), this.placeholderResolver);
    final List<Token> root = TokenParser.tokenize(richMessage);
    for (final Token token : root) {
      switch (token.type()) {
        case TEXT:
          sb.append(richMessage, token.startIndex(), token.endIndex());
          break;
        case OPEN_TAG:
        case CLOSE_TAG:
          // extract tag name
          if (token.childTokens().isEmpty()) {
            sb.append(richMessage, token.startIndex(), token.endIndex());
            continue;
          }
          final String sanitized = this.sanitizePlaceholderName(token.childTokens().get(0).get(richMessage).toString());
          if (this.registry.exists(sanitized, combinedResolver) || combinedResolver.resolve(sanitized) != null) {
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

  @NotNull Component parseFormat(final @NotNull String richMessage, final @NotNull ContextImpl context) {
    final PlaceholderResolver combinedResolver = PlaceholderResolver.combining(context.placeholderResolver(), this.placeholderResolver);
    final Appendable debug = context.debugOutput();
    if (debug != null) {
      try {
        debug.append("Beginning parsing message ").append(richMessage).append('\n');
      } catch (final IOException ignored) {
      }
    }

    final Function<TagNode, Transformation> transformationFactory;
    if (debug != null) {
      transformationFactory = node -> {
        try {
          try {
            debug.append("Attempting to match node '").append(node.name()).append("' at column ")
            .append(String.valueOf(node.token().startIndex())).append('\n');
          } catch (final IOException ignored) {
          }

          final Transformation transformation = this.registry.get(this.sanitizePlaceholderName(node.name()), node.parts(), combinedResolver, context);

          try {
            if (transformation == null) {
              debug.append("Could not match node '").append(node.name()).append("'\n");
            } else {
              debug.append("Successfully matched node '").append(node.name()).append("' to transformation ")
              .append(transformation.examinableName()).append('\n');
            }
          } catch (final IOException ignored) {
          }

          return transformation;
        } catch (final ParsingException e) {
          try {
            if (e.tokens().length == 0) {
              e.tokens(new Token[]{node.token()});
            }
            debug.append("Could not match node '").append(node.name()).append("' - ").append(e.getMessage()).append('\n');
          } catch (final IOException ignored) {
          }
          return null;
        }
      };
    } else {
      transformationFactory = node -> {
        try {
          return this.registry.get(this.sanitizePlaceholderName(node.name()), node.parts(), combinedResolver, context);
        } catch (final ParsingException ignored) {
          return null;
        }
      };
    }
    final BiPredicate<String, Boolean> tagNameChecker = (name, includePlaceholders) -> {
      final String sanitized = this.sanitizePlaceholderName(name);
      return this.registry.exists(sanitized) || (includePlaceholders && combinedResolver.resolve(name) != null);
    };

    final ElementNode root = TokenParser.parse(transformationFactory, tagNameChecker, combinedResolver, richMessage, context.strict());

    if (debug != null) {
      try {
        debug.append("Text parsed into element tree:\n");
        debug.append(root.toString());
      } catch (final IOException ignored) {
      }
    }

    return Objects.requireNonNull(context.postProcessor().apply(this.treeToComponent(root, context)), "Post-processor must not return null");
  }

  @NotNull Component treeToComponent(final @NotNull ElementNode node, final @NotNull ContextImpl context) {
    Component comp;
    Transformation transformation = null;
    if (node instanceof ValueNode) {
      comp = Component.text(((ValueNode) node).value());
    } else if (node instanceof TagNode) {
      final TagNode tag = (TagNode) node;

      transformation = tag.transformation();

      // special case for gradient and stuff
      if (transformation instanceof Modifying) {
        final Modifying modTransformation = (Modifying) transformation;

        // first walk the tree
        final LinkedList<ElementNode> toVisit = new LinkedList<>(node.children());
        while (!toVisit.isEmpty()) {
          final ElementNode curr = toVisit.removeFirst();
          modTransformation.visit(curr);
          toVisit.addAll(0, curr.children());
        }
      }
      comp = transformation.apply();
    } else {
      comp = Component.empty();
    }

    for (final ElementNode child : node.children()) {
      comp = comp.append(this.treeToComponent(child, context));
    }

    // special case for gradient and stuff
    if (transformation instanceof Modifying) {
      comp = this.handleModifying((Modifying) transformation, comp, 0);
    }

    final Appendable debug = context.debugOutput();
    if (debug != null) {
      try {
        debug.append("==========\ntreeToComponent \n").append(node.toString()).append("\n").append(comp.examine(MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining("\n"))).append("\n==========\n");
      } catch (final IOException ignored) {
      }
    }

    return comp;
  }

  private Component handleModifying(final Modifying modTransformation, final Component current, final int depth) {
    Component newComp = modTransformation.apply(current, depth);
    for (final Component child : current.children()) {
      newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1));
    }
    return newComp;
  }

  private String sanitizePlaceholderName(final String name) {
    return name.toLowerCase(Locale.ROOT);
  }
}
