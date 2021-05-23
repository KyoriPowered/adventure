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

import java.util.Deque;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
//import net.kyori.adventure.text.minimessage.parser.MiniMessageLexer;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.InstantApplyTransformation;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;
import net.kyori.adventure.text.minimessage.transformation.inbuild.PreTransformation;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.Tokens.PRE;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_END;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_START;

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

  @NonNull String sanitizePlaceholder(final String input) {
    return input.replace("</pre>", "\\</pre>");
  }

  @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull Context context, final @NonNull String... placeholders) {
    if(placeholders.length % 2 != 0) {
      throw new ParseException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
    }
    for(int i = 0; i < placeholders.length; i += 2) {
      richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, this.sanitizePlaceholder(placeholders[i + 1]));
    }
    context.replacedMessage(richMessage);
    return richMessage;
  }

  @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull Context context, final @NonNull Map<String, String> placeholders) {
    for(final Map.Entry<String, String> entry : placeholders.entrySet()) {
      richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
    }
    context.replacedMessage(richMessage);
    return richMessage;
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Context context, final @NonNull String... placeholders) {
    return this.parseFormat(this.handlePlaceholders(richMessage, context, placeholders), context);
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Map<String, String> placeholders, final Context context) {
    return this.parseFormat(this.handlePlaceholders(richMessage, context, placeholders), context);
  }

  @NonNull Component parseFormat(@NonNull String input, final Context context, final @NonNull Template... placeholders) {
    final Map<String, Template.ComponentTemplate> map = new HashMap<>();
    for(final Template placeholder : placeholders) {
      if(placeholder instanceof Template.StringTemplate) {
        final Template.StringTemplate stringTemplate = (Template.StringTemplate) placeholder;
        input = input.replace(TAG_START + stringTemplate.key() + TAG_END, this.sanitizePlaceholder(stringTemplate.value()));
      } else if(placeholder instanceof Template.ComponentTemplate) {
        final Template.ComponentTemplate componentTemplate = (Template.ComponentTemplate) placeholder;
        map.put(componentTemplate.key(), componentTemplate);
      }
    }
    return this.parseFormat0(input, map, context);
  }

  @NonNull Component parseFormat(@NonNull String input, final @NonNull List<Template> placeholders, final @NonNull Context context) {
    final Map<String, Template.ComponentTemplate> map = new HashMap<>();
    for(final Template placeholder : placeholders) {
      if(placeholder instanceof Template.StringTemplate) {
        final Template.StringTemplate stringTemplate = (Template.StringTemplate) placeholder;
        input = input.replace(TAG_START + stringTemplate.key() + TAG_END, stringTemplate.value());
      } else if(placeholder instanceof Template.ComponentTemplate) {
        final Template.ComponentTemplate componentTemplate = (Template.ComponentTemplate) placeholder;
        map.put(componentTemplate.key(), componentTemplate);
      }
    }
    return this.parseFormat0(input, map, context);
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Context context) {
    return this.parseFormat0(richMessage, Collections.emptyMap(), context);
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull Context context) {
    return this.parseFormat0(richMessage, templates, this.registry, this.placeholderResolver, context);
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull TransformationRegistry registry, final @NonNull Function<String, ComponentLike> placeholderResolver, final Context context) {
    /*
    final MiniMessageLexer lexer = new MiniMessageLexer(richMessage, context);
    try {
      lexer.scan();
    } catch(final IOException e) {
      e.printStackTrace(); // TODO idk how to deal with this
    }
    lexer.clean();
    final List<Token> tokens = lexer.getTokens();
    context.tokens(tokens);
    return this.parse(tokens, registry, templates, placeholderResolver, context);
     */
    throw new IllegalStateException();
  }

  @NonNull Component parse(final @NonNull List<Token> tokens, final @NonNull TransformationRegistry registry, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull Function<String, ComponentLike> placeholderResolver, final @NonNull Context context) {
    final TextComponent.Builder parent = Component.text();
    final Deque<Transformation> transformations = new ArrayDeque<>();
    final Deque<OneTimeTransformation> oneTimeTransformations = new ArrayDeque<>();
    boolean preActive = false;

    int i = 0;
    while(i < tokens.size()) {
      final Token token = tokens.get(i);
      switch (token.type()) {
        case ESCAPED_OPEN_TAG_START:
        case OPEN_TAG_START:
          // next has to be name
          if(tokens.size() - 1 == i) {
            if(context.isStrict()) {
              throw new ParsingException("Expected name after open tag, but got nothing", -1);
            } else {
              tokens.set(i, new Token(TokenType.STRING, token.value()));
              continue;
            }
          }
          Token name = tokens.get(++i);
          // if we have an escaped token before a real token, we need special handling, see GH-78
          if(name.type() == TokenType.OPEN_TAG_START && token.type() == TokenType.ESCAPED_OPEN_TAG_START) {
            i -= 1;
            tokens.set(i, new Token(TokenType.STRING, name.value()));
            continue;
          }
          if(name.type() != TokenType.NAME && token.type() != TokenType.ESCAPED_OPEN_TAG_START) {
            if(context.isStrict()) {
              throw new ParsingException("Expected name after open tag, but got " + name, -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected name after open tag, but got " + name));
              continue;
            }
          }
          // after that, we get a param separator or the end
          if(tokens.size() - 1 == i) {
            if(context.isStrict()) {
              throw new ParsingException("Expected param or end after open tag + name, but got nothing", -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected param or end after open tag + name, but got nothing"));
              continue;
            }
          }
          Token paramOrEnd = tokens.get(++i);
          if(paramOrEnd.type() == TokenType.PARAM_SEPARATOR) {
            // we need to handle params, so read till end of tag
            final List<Token> inners = new ArrayList<>();
            Token next = null;
            while(i < tokens.size() - 1 && (next = tokens.get(++i)).type() != TokenType.TAG_END) {
              inners.add(next);
            }

            if(next == null) {
              if(context.isStrict()) {
                throw new ParsingException("Expected end sometimes after open tag + name, but got name = " + name + " and inners = " + inners, -1);
              } else {
                // TODO: handle
                context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected end sometimes after open tag + name, but got name = " + name + " and inners = " + inners));
                continue;
              }
            }

            final Transformation transformation = registry.get(name.value(), inners, templates, placeholderResolver, context);
            if(transformation == null || (preActive && !transformation.allowedInPre()) || token.type() == TokenType.ESCAPED_OPEN_TAG_START) {
              // this isn't a known tag, oh no!
              // lets take a step back, first, create a string
              i -= 3 + inners.size();
              final StringBuilder string = new StringBuilder(tokens.get(i).value()).append(name.value()).append(paramOrEnd.value());
              inners.forEach(t -> string.append(t.value()));
              string.append(next.value());
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string.toString()));
              // remove the others
              for(int c = 0; c < inners.size() + 3; c++) {
                tokens.remove(i + 1);
              }
              continue;
            } else {
              if(transformation instanceof InstantApplyTransformation) {
                ((InstantApplyTransformation) transformation).applyInstant(parent, transformations);
              } else if(transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                if(transformation instanceof PreTransformation) {
                  preActive = true;
                }
                transformations.addLast(transformation);
              }
            }
          } else if(paramOrEnd.type() == TokenType.TAG_END || paramOrEnd.type() == TokenType.ESCAPED_CLOSE_TAG_START) {
            // we finished
            final Transformation transformation = registry.get(name.value(), Collections.emptyList(), templates, placeholderResolver, context);
            if(transformation == null || (preActive && !transformation.allowedInPre()) || token.type() == TokenType.ESCAPED_OPEN_TAG_START) {
              // this isn't a known tag, oh no!
              // lets take a step back, first, create a string
              i -= 2;
              final String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              continue;
            } else {
              if(transformation instanceof InstantApplyTransformation) {
                ((InstantApplyTransformation) transformation).applyInstant(parent, transformations);
              } else if(transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                if(transformation instanceof PreTransformation) {
                  preActive = true;
                }
                transformations.addLast(transformation);
              }
            }
          } else {
            if(context.isStrict()) {
              throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected tag end or param separator after tag name, but got " + paramOrEnd));
              continue;
            }
          }
          break;
        case ESCAPED_CLOSE_TAG_START:
        case CLOSE_TAG_START:
          // next has to be name
          if(tokens.size() - 1 == i) {
            if(context.isStrict()) {
              throw new ParsingException("Expected name after open tag, but got nothing", -1);
            } else {
              tokens.set(i, new Token(TokenType.STRING, token.value()));
              continue;
            }
          }
          name = tokens.get(++i);
          if(name.type() != TokenType.NAME && token.type() != TokenType.ESCAPED_CLOSE_TAG_START) {
            if(context.isStrict()) {
              throw new ParsingException("Expected name after close tag start, but got " + name, -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected name after close tag start, but got " + name));
              continue;
            }
          }
          // after that, we just want end, sometimes end has params tho
          if(tokens.size() - 1 == i) {
            if(context.isStrict()) {
              throw new ParsingException("Expected param or end after open tag + name, but got nothing", -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected param or end after open tag + name, but got nothing"));
              continue;
            }
          }
          paramOrEnd = tokens.get(++i);
          if(paramOrEnd.type() == TokenType.TAG_END) {
            // we finished, gotta remove name out of the stack
            if(!registry.exists(name.value()) || (preActive && !name.value().equalsIgnoreCase(PRE)) || token.type() == TokenType.ESCAPED_CLOSE_TAG_START) {
              // invalid end
              // lets take a step back, first, create a string
              i -= 2;
              final String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              continue;
            } else {
              final Transformation removed = this.removeFirst(transformations, t -> t.name().equals(name.value()));
              if(removed instanceof PreTransformation) {
                preActive = false;
              } else if(removed == null && !registry.couldBeOnetimeTransformation(name.value())) {
                // invalid end
                // lets take a step back, first, create a string
                i -= 2;
                final String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
                // insert our string
                tokens.set(i, new Token(TokenType.STRING, string));
                // remove the others
                tokens.remove(i + 1);
                tokens.remove(i + 1);
                i -= 1;
              }
            }
          } else if(paramOrEnd.type() == TokenType.PARAM_SEPARATOR) {
            // read all params
            final List<Token> inners = new ArrayList<>();
            Token next;
            while((next = tokens.get(++i)).type() != TokenType.TAG_END) {
              inners.add(next);
            }

            // check what we need to close, so we create a transformation and try to remove it
            final Transformation transformation = registry.get(name.value(), inners, templates, placeholderResolver, context);
            transformations.removeFirstOccurrence(transformation);
          } else {
            if(context.isStrict()) {
              throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
            } else {
              // TODO: handle
              context.miniMessage().parsingErrorMessageConsumer().accept(Collections.singletonList("Expected tag end or param separator after tag name, but got " + paramOrEnd));
              continue;
            }
          }
          break;
        default:
          Component current = Component.text(token.value());

          for(final Transformation transformation : transformations) {
            current = transformation.apply(current, parent);
          }

          while(!oneTimeTransformations.isEmpty()) {
            current = oneTimeTransformations.removeLast().applyOneTime(current, parent, transformations);
          }

          if(current != null) {
            parent.append(current);
          }
          break;
      }
      i++;
    }

    // at last, go thru all transformations that insert something
    final List<Component> children = parent.asComponent().children();
    final Component last = children.isEmpty() ? Component.empty() : children.get(children.size() - 1);
    for(final Transformation transformation : transformations) {
      if(transformation instanceof Inserting) {
        transformation.apply(last, parent);
      }
    }

    // then insert missing onetime transformations
    boolean didApplyOneTime = false;
    while(!oneTimeTransformations.isEmpty()) {
      didApplyOneTime = true;
      oneTimeTransformations.removeFirst().applyOneTime(last, parent, transformations);
    }

    TextComponent parentBuild;

    // then do one iteration of transformations again
    if(didApplyOneTime && !transformations.isEmpty()) {
      List<Component> newChildren = parent.asComponent().children();
      Component newLast = newChildren.isEmpty() ? Component.empty() : newChildren.get(newChildren.size() - 1);
      for(final Transformation transformation : transformations) {
        newLast = transformation.apply(newLast, parent);
      }
      // we got a new last, set that as last
      parentBuild = parent.build();
      newChildren = new ArrayList<>(newChildren);
      newChildren.set(newChildren.size() - 1, newLast);
      parentBuild = parentBuild.children(newChildren);
    } else {
      parentBuild = parent.build();
    }

    // optimization, ignore empty parent
    if(parentBuild.content().equals("") && parentBuild.children().size() == 1) {
      return parentBuild.children().get(0);
    } else {
      return parentBuild;
    }
  }

  private Transformation removeFirst(final Deque<Transformation> transformations, final Predicate<Transformation> filter) {
    final Iterator<Transformation> each = transformations.descendingIterator();
    while(each.hasNext()) {
      final Transformation next = each.next();
      if(filter.test(next)) {
        each.remove();
        return next;
      }
    }
    return null;
  }
}
