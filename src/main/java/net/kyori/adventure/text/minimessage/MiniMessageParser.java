/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenParser;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import net.kyori.adventure.text.minimessage.parser.node.TagNode;
import net.kyori.adventure.text.minimessage.parser.node.ValueNode;
import net.kyori.adventure.text.minimessage.transformation.Modifying;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

class MiniMessageParser {

  // regex group names
  private static final String START = "start";
  private static final String TOKEN = "token";
  private static final String INNER = "inner";
  private static final String END = "end";
  // https://regex101.com/r/8VZ7uA/10
  private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?");

  private final TransformationRegistry registry;
  private final Function<String, ComponentLike> placeholderResolver;

  MiniMessageParser() {
    this.registry = new TransformationRegistry();
    this.placeholderResolver = MiniMessageImpl.DEFAULT_PLACEHOLDER_RESOLVER;
  }

  MiniMessageParser(final TransformationRegistry registry, final Function<String, ComponentLike> placeholderResolver) {
    this.registry = registry;
    this.placeholderResolver = placeholderResolver;
  }

  @NonNull String escapeTokens(final @NonNull String richMessage) {
    final StringBuilder sb = new StringBuilder();
    final Matcher matcher = pattern.matcher(richMessage);
    int lastEnd = 0;
    while(matcher.find()) {
      final int startIndex = matcher.start();
      final int endIndex = matcher.end();

      if(startIndex > lastEnd) {
        sb.append(richMessage, lastEnd, startIndex);
      }
      lastEnd = endIndex;

      final String start = matcher.group(START);
      String token = matcher.group(TOKEN);
      final String inner = matcher.group(INNER);
      final String end = matcher.group(END);

      // also escape inner
      if(inner != null) {
        token = token.replace(inner, this.escapeTokens(inner));
      }

      sb.append("\\").append(start).append(token).append(end);
    }

    if(richMessage.length() > lastEnd) {
      sb.append(richMessage.substring(lastEnd));
    }

    return sb.toString();
  }

  @NonNull String stripTokens(final @NonNull String richMessage) {
    final StringBuilder sb = new StringBuilder();
    final Matcher matcher = pattern.matcher(richMessage);
    int lastEnd = 0;
    while(matcher.find()) {
      final int startIndex = matcher.start();
      final int endIndex = matcher.end();

      if(startIndex > lastEnd) {
        sb.append(richMessage, lastEnd, startIndex);
      }
      lastEnd = endIndex;
    }

    if(richMessage.length() > lastEnd) {
      sb.append(richMessage.substring(lastEnd));
    }

    return sb.toString();
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Context context, final @NonNull String... placeholders) {
    if(placeholders.length % 2 != 0) {
      throw new ParsingException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
    }

    final Template[] t = new Template[placeholders.length / 2];
    for(int i = 0; i < placeholders.length; i += 2) {
      t[i / 2] = Template.of(placeholders[i], placeholders[i + 1]);
    }

    return this.parseFormat(richMessage, context, t);
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Map<String, String> placeholders, final Context context) {
    final Template[] t = new Template[placeholders.size()];
    int i = 0;
    for(final Map.Entry<String, String> entry : placeholders.entrySet()) {
      t[i++] = Template.of(entry.getKey(), entry.getValue());
    }
    return this.parseFormat(richMessage, context, t);
  }

  @NonNull Component parseFormat(final @NonNull String input, final Context context, final @NonNull Template... placeholders) {
    final Map<String, Template> map = new HashMap<>();
    for(final Template placeholder : placeholders) {
      map.put(placeholder.key(), placeholder);
    }
    return this.parseFormat0(input, map, context);
  }

  @NonNull Component parseFormat(final @NonNull String input, final @NonNull List<Template> placeholders, final @NonNull Context context) {
    final Map<String, Template> map = new HashMap<>();
    for(final Template placeholder : placeholders) {
      map.put(placeholder.key(), placeholder);
    }
    return this.parseFormat0(input, map, context);
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Context context) {
    return this.parseFormat0(richMessage, Collections.emptyMap(), context);
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template> templates, final @NonNull Context context) {
    return this.parseFormat0(richMessage, templates, this.registry, this.placeholderResolver, context);
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template> templates, final @NonNull TransformationRegistry registry, final @NonNull Function<String, ComponentLike> placeholderResolver, final Context context) {
    final Appendable debug = context.debugOutput();
    if(debug != null) {
      try {
        debug.append("Beginning parsing message ").append(richMessage).append('\n');
      } catch(final IOException ignored) {
      }
    }

    final Function<TagNode, Transformation> transformationFactory;
    if(debug != null) {
      transformationFactory = node -> {
        try {
          try {
            debug.append("Attempting to match node '").append(node.name()).append("' at column ")
              .append(String.valueOf(node.token().startIndex())).append('\n');
          } catch(final IOException ignored) {
          }

          final Transformation transformation = registry.get(node.name(), node.parts(), templates, placeholderResolver, context);

          try {
            if(transformation == null) {
              debug.append("Could not match node '").append(node.name()).append("'\n");
            } else {
              debug.append("Successfully matched node '").append(node.name()).append("' to transformation ")
                .append(transformation.examinableName()).append('\n');
            }
          } catch(final IOException ignored) {
          }

          return transformation;
        } catch(final ParsingException e) {
          try {
            if(e.tokens().length == 0) {
              e.tokens(new Token[]{node.token()});
            }
            debug.append("Could not match node '").append(node.name()).append("' - ").append(e.getMessage()).append('\n');
          } catch(final IOException ignored) {
          }
          return null;
        }
      };
    } else {
      transformationFactory = node -> {
        try {
          return registry.get(node.name(), node.parts(), templates, placeholderResolver, context);
        } catch(final ParsingException ignored) {
          return null;
        }
      };
    }
    final BiPredicate<String, Boolean> tagNameChecker = (name, includeTemplates) ->
      registry.exists(name, placeholderResolver) || (includeTemplates && templates.containsKey(name));

    final ElementNode root = TokenParser.parse(transformationFactory, tagNameChecker, templates, richMessage, context.isStrict());

    if(debug != null) {
      try {
        debug.append("Text parsed into element tree:\n");
        debug.append(root.toString());
      } catch(final IOException ignored) {
      }
    }

    context.root(root);
    final Component comp = this.parse(root);
    // at the end, take a look if we can flatten the tree a bit
    return this.flatten(comp);
  }

  @NonNull Component parse(final @NonNull ElementNode node) {
    Component comp;
    Transformation transformation = null;
    if(node instanceof ValueNode) {
      comp = Component.text(((ValueNode) node).value());
    } else if(node instanceof TagNode) {
      final TagNode tag = (TagNode) node;

      transformation = tag.transformation();

      // special case for gradient and stuff
      if(transformation instanceof Modifying) {
        final Modifying modTransformation = (Modifying) transformation;

        // first walk the tree
        final LinkedList<ElementNode> toVisit = new LinkedList<>(node.children());
        while(!toVisit.isEmpty()) {
          final ElementNode curr = toVisit.removeFirst();
          modTransformation.visit(curr);
          toVisit.addAll(0, curr.children());
        }
      }
      comp = transformation.apply();
    } else {
      comp = Component.empty();
    }

    for(final ElementNode child : node.children()) {
      comp = comp.append(this.parse(child));
    }

    // special case for gradient and stuff
    if(transformation instanceof Modifying) {
      comp = this.handleModifying((Modifying) transformation, comp, 0);
    }

    return comp;
  }

  private Component handleModifying(final Modifying modTransformation, final Component current, final int depth) {
    Component newComp = modTransformation.apply(current, depth);
    for(final Component child : current.children()) {
      newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1));
    }
    return newComp;
  }

  private @NonNull Component flatten(@NonNull Component comp) {
    if(comp.children().isEmpty()) {
      return comp;
    }

    final List<Component> oldChildren = comp.children();
    final ArrayList<Component> newChildren = new ArrayList<>(oldChildren.size());
    for(final Component child : oldChildren) {
      newChildren.add(this.flatten(child));
    }

    comp = comp.children(newChildren);

    if(!(comp instanceof TextComponent)) {
      return comp;
    }

    final TextComponent root = (TextComponent) comp;

    if(root.content().isEmpty()) {
      // this seems to be some kind of empty node, lets see if we can discard it, or if we have to merge it
      final boolean hasNoStyle = !root.hasStyling() && root.hoverEvent() == null && root.clickEvent() == null;
      if(root.children().size() == 1 && hasNoStyle) {
        // seems to be the root node, just discord it
        return root.children().get(0);
      } else if(!root.children().isEmpty() && hasNoStyle) {
        // see if we can at least flatten the first child
        final Component child = newChildren.get(0);
        if(child.hasStyling()) {
          // We can't, the child styling might interfere with a sibling
          return comp;
        }

        final ArrayList<Component> copiedChildren = new ArrayList<>(root.children().size() - 1 + child.children().size());

        copiedChildren.addAll(child.children());
        copiedChildren.addAll(newChildren.subList(1, newChildren.size()));

        return root.content(child instanceof TextComponent ? ((TextComponent) child).content() : "")
          .style(mergeStyle(root, child))
          .children(copiedChildren);
      } else if(root.children().size() == 1) {
        // we got something we can merge
        final Component child = newChildren.get(0);
        return child.style(mergeStyle(root, child));
      }
    }

    return comp;
  }

  private static @NonNull Style mergeStyle(final @NonNull Component base, final @NonNull Component target) {
    return target.style().merge(base.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET, Style.Merge.all());
  }
}
