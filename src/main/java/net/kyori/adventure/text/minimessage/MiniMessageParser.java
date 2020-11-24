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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.parser.MiniMessageLexer;
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
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.Tokens.*;

class MiniMessageParser {

  // regex group names
  private static final String START = "start";
  private static final String TOKEN = "token";
  private static final String INNER = "inner";
  private static final String END = "end";
  // https://regex101.com/r/8VZ7uA/10
  private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?");

  private final TransformationRegistry registry;

  public MiniMessageParser() {
    this.registry = new TransformationRegistry();
  }

  public MiniMessageParser(TransformationRegistry registry) {
    this.registry = registry;
  }

  @NonNull String escapeTokens(final @NonNull String richMessage) {
    final StringBuilder sb = new StringBuilder();
    final Matcher matcher = pattern.matcher(richMessage);
    int lastEnd = 0;
    while (matcher.find()) {
      int startIndex = matcher.start();
      int endIndex = matcher.end();

      if (startIndex > lastEnd) {
        sb.append(richMessage, lastEnd, startIndex);
      }
      lastEnd = endIndex;

      final String start = matcher.group(START);
      String token = matcher.group(TOKEN);
      final String inner = matcher.group(INNER);
      final String end = matcher.group(END);

      // also escape inner
      if (inner != null) {
        token = token.replace(inner, escapeTokens(inner));
      }

      sb.append("\\").append(start).append(token).append(end);
    }

    if (richMessage.length() > lastEnd) {
      sb.append(richMessage.substring(lastEnd));
    }

    return sb.toString();
  }

  @NonNull String stripTokens(final @NonNull String richMessage) {
    final StringBuilder sb = new StringBuilder();
    final Matcher matcher = pattern.matcher(richMessage);
    int lastEnd = 0;
    while (matcher.find()) {
      final int startIndex = matcher.start();
      final int endIndex = matcher.end();

      if (startIndex > lastEnd) {
        sb.append(richMessage, lastEnd, startIndex);
      }
      lastEnd = endIndex;
    }

    if (richMessage.length() > lastEnd) {
      sb.append(richMessage.substring(lastEnd));
    }

    return sb.toString();
  }

  @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull String... placeholders) {
    if (placeholders.length % 2 != 0) {
      throw new ParseException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
    }
    for (int i = 0; i < placeholders.length; i += 2) {
      richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, placeholders[i + 1]);
    }
    return richMessage;
  }


  @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
    }
    return richMessage;
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull String... placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  @NonNull Component parseFormat(@NonNull String input, final @NonNull Template... placeholders) {
    Map<String, Template.ComponentTemplate> map = new HashMap<>();
    for (Template placeholder : placeholders) {
      if (placeholder instanceof Template.StringTemplate) {
        Template.StringTemplate stringTemplate = (Template.StringTemplate) placeholder;
        input = input.replace(TAG_START + stringTemplate.getKey() + TAG_END, stringTemplate.getValue());
      } else if (placeholder instanceof Template.ComponentTemplate) {
        Template.ComponentTemplate componentTemplate = (Template.ComponentTemplate) placeholder;
        map.put(componentTemplate.getKey(), componentTemplate);
      }
    }
    return parseFormat0(input, map);
  }

  @NonNull Component parseFormat(@NonNull String input, final @NonNull List<Template> placeholders) {
    Map<String, Template.ComponentTemplate> map = new HashMap<>();
    for (Template placeholder : placeholders) {
      if (placeholder instanceof Template.StringTemplate) {
        Template.StringTemplate stringTemplate = (Template.StringTemplate) placeholder;
        input = input.replace(TAG_START + stringTemplate.getKey() + TAG_END, stringTemplate.getValue());
      } else if (placeholder instanceof Template.ComponentTemplate) {
        Template.ComponentTemplate componentTemplate = (Template.ComponentTemplate) placeholder;
        map.put(componentTemplate.getKey(), componentTemplate);
      }
    }
    return parseFormat0(input, map);
  }

  @NonNull Component parseFormat(final @NonNull String richMessage) {
    return parseFormat0(richMessage, Collections.emptyMap());
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates) {
    return parseFormat0(richMessage, templates, registry);
  }

  @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates, final @NonNull TransformationRegistry registry) {
    MiniMessageLexer lexer = new MiniMessageLexer(richMessage);
    try {
      lexer.scan();
    } catch (IOException e) {
      e.printStackTrace(); // TODO idk how to deal with this
    }
    lexer.clean();
    List<Token> tokens = lexer.getTokens();
    return parse(tokens, registry, templates);
  }

  @NonNull Component parse(final @NonNull List<Token> tokens, final @NonNull TransformationRegistry registry, final @NonNull Map<String, Template.ComponentTemplate> templates) {
    final TextComponent.Builder parent = Component.text();
    ArrayDeque<Transformation> transformations = new ArrayDeque<>();
    ArrayDeque<OneTimeTransformation> oneTimeTransformations = new ArrayDeque<>();
    boolean preActive = false;

    int i = 0;
    while (i < tokens.size()) {
      Token token = tokens.get(i);
      switch (token.type()) {
        case ESCAPED_OPEN_TAG_START:
        case OPEN_TAG_START:
          // next has to be name
          Token name = tokens.get(++i);
          if (name.type() != TokenType.NAME && token.type() != TokenType.ESCAPED_OPEN_TAG_START) {
            throw new ParsingException("Expected name after open tag, but got " + name, -1);
          }
          // after that, we get a param seperator or the end
          Token paramOrEnd = tokens.get(++i);
          if (paramOrEnd.type() == TokenType.PARAM_SEPARATOR) {
            // we need to handle params, so read till end of tag
            List<Token> inners = new ArrayList<>();
            Token next;
            while ((next = tokens.get(++i)).type() != TokenType.TAG_END) {
              inners.add(next);
            }

            Transformation transformation = registry.get(name.value(), inners, templates);
            if (transformation == null || preActive || token.type() == TokenType.ESCAPED_OPEN_TAG_START) {
              // this isn't a known tag, oh no!
              // lets take a step back, first, create a string
              i -= 3 + inners.size();
              StringBuilder string = new StringBuilder(tokens.get(i).value()).append(name.value()).append(paramOrEnd.value());
              inners.forEach(t -> string.append(t.value()));
              string.append(next.value());
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string.toString()));
              // remove the others
              for (int c = 0; c < inners.size() + 3; c++) {
                tokens.remove(i + 1);
              }
              continue;
            } else {
              if (transformation instanceof InstantApplyTransformation) {
                ((InstantApplyTransformation) transformation).applyInstant(parent, transformations);
              } else if (transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                if (transformation instanceof PreTransformation) {
                  preActive = true;
                }
                transformations.addLast(transformation);
              }
            }
          } else if (paramOrEnd.type() == TokenType.TAG_END || paramOrEnd.type() == TokenType.ESCAPED_CLOSE_TAG_START) {
            // we finished
            Transformation transformation = registry.get(name.value(), Collections.emptyList(), templates);
            if (transformation == null || preActive || token.type() == TokenType.ESCAPED_OPEN_TAG_START) {
              // this isn't a known tag, oh no!
              // lets take a step back, first, create a string
              i -= 2;
              String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              continue;
            } else {
              if (transformation instanceof InstantApplyTransformation) {
                ((InstantApplyTransformation) transformation).applyInstant(parent, transformations);
              } else if (transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                if (transformation instanceof PreTransformation) {
                  preActive = true;
                }
                transformations.addLast(transformation);
              }
            }
          } else {
            throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
          }
          break;
        case ESCAPED_CLOSE_TAG_START:
        case CLOSE_TAG_START:
          // next has to be name
          name = tokens.get(++i);
          if (name.type() != TokenType.NAME && token.type() != TokenType.ESCAPED_CLOSE_TAG_START) {
            throw new ParsingException("Expected name after close tag start, but got " + name, -1);
          }
          // after that, we just want end, sometimes end has params tho
          paramOrEnd = tokens.get(++i);
          if (paramOrEnd.type() == TokenType.TAG_END) {
            // we finished, gotta remove name out of the stack
            if (!registry.exists(name.value()) || (preActive && !name.value().equalsIgnoreCase(PRE)) || token.type() == TokenType.ESCAPED_CLOSE_TAG_START) {
              // invalid end
              // lets take a step back, first, create a string
              i -= 2;
              String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              continue;
            } else {
              Transformation removed = removeFirst(transformations, t -> t.name().equals(name.value()));
              if (removed instanceof PreTransformation) {
                preActive = false;
              }
            }
          } else if (paramOrEnd.type() == TokenType.PARAM_SEPARATOR) {
            // read all params
            List<Token> inners = new ArrayList<>();
            Token next;
            while ((next = tokens.get(++i)).type() != TokenType.TAG_END) {
              inners.add(next);
            }

            // check what we need to close, so we create a transformation and try to remove it
            Transformation transformation = registry.get(name.value(), inners, templates);
            transformations.removeFirstOccurrence(transformation);
          } else {
            throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
          }
          break;
        case TAG_END:
        case PARAM_SEPARATOR:
        case QUOTE_START:
        case QUOTE_END:
        case NAME:
        case STRING:
          Component current = Component.text(token.value());

          for (Transformation transformation : transformations) {
            current = transformation.apply(current, parent);
          }

          while (!oneTimeTransformations.isEmpty()) {
            current = oneTimeTransformations.removeLast().applyOneTime(current, parent, transformations);
          }

          if (current != null) {
            parent.append(current);
          }
          break;
      }
      i++;
    }

    // at last, go thru all transformations that insert something
    List<Component> children = parent.asComponent().children();
    Component last = children.isEmpty() ? Component.empty() : children.get(children.size() - 1);
    for (Transformation transformation : transformations) {
      if (transformation instanceof Inserting) {
        transformation.apply(last, parent);
      }
    }

    while (!oneTimeTransformations.isEmpty()) {
      oneTimeTransformations.removeLast().applyOneTime(last, parent, transformations);
    }

    // optimization, ignore empty parent
    final TextComponent comp = parent.build();
    if (comp.content().equals("") && comp.children().size() == 1) {
      return comp.children().get(0);
    } else {
      return comp;
    }
  }

  private Transformation removeFirst(ArrayDeque<Transformation> transformations, Predicate<Transformation> filter) {
    final Iterator<Transformation> each = transformations.descendingIterator();
    while (each.hasNext()) {
      Transformation next = each.next();
      if (filter.test(next)) {
        each.remove();
        return next;
      }
    }
    return null;
  }
}
