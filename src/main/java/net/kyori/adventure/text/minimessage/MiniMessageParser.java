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
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationRegistry;

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
  private static final TransformationRegistry REGISTRY = new TransformationRegistry();

  // regex group names
  private static final String START = "start";
  private static final String TOKEN = "token";
  private static final String INNER = "inner";
  private static final String END = "end";
  // https://regex101.com/r/8VZ7uA/10
  private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?");

  private static final Pattern dumSplitPattern = Pattern.compile("['\"]:['\"]");

  static @NonNull String escapeTokens(final @NonNull String richMessage) {
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

      sb.append("\\").append(start).append(token).append("\\").append(end);
    }

    if (richMessage.length() > lastEnd) {
      sb.append(richMessage.substring(lastEnd));
    }

    return sb.toString();
  }

  static @NonNull String stripTokens(final @NonNull String richMessage) {
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

  static @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull String... placeholders) {
    if (placeholders.length % 2 != 0) {
      throw new ParseException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
    }
    for (int i = 0; i < placeholders.length; i += 2) {
      richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, placeholders[i + 1]);
    }
    return richMessage;
  }


  static @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
    }
    return richMessage;
  }

  static @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull String... placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  static @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  static @NonNull Component parseFormat(@NonNull String input, final @NonNull Template... placeholders) {
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

  static @NonNull Component parseFormat(@NonNull String input, final @NonNull List<Template> placeholders) {
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

  static @NonNull Component parseFormat(final @NonNull String richMessage) {
    return parseFormat0(richMessage, Collections.emptyMap());
  }

  static @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates) {
    MiniMessageLexer lexer = new MiniMessageLexer(richMessage);
    try {
      lexer.scan();
    } catch (IOException e) {
      e.printStackTrace(); // TODO idk how to deal with this
    }
    lexer.clean();
    List<Token> tokens = lexer.getTokens();
    return parse(tokens, REGISTRY, templates);
  }

  @NonNull static Component parse(final @NonNull List<Token> tokens, final @NonNull TransformationRegistry registry, final @NonNull Map<String, Template.ComponentTemplate> templates) {
    final TextComponent.Builder parent = Component.text();
    ArrayDeque<Transformation> transformations = new ArrayDeque<>();
    ArrayDeque<OneTimeTransformation> oneTimeTransformations = new ArrayDeque<>();

    int i = 0;
    while (i < tokens.size()) {
      Token token = tokens.get(i);
      switch (token.type()) {
        case OPEN_TAG_START:
          // next has to be name
          Token name = tokens.get(++i);
          if (name.type() != TokenType.NAME) {
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

            Transformation transformation = registry.get(name.value(), inners);
            System.out.println("got start of " + name.value() + " with params " + inners + " -> " + transformation);
            if (transformation == null) {
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
              System.out.println("no transformation found " + string);
              continue;
            } else {
              if (transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                transformations.addLast(transformation);
              }
            }
          } else if (paramOrEnd.type() == TokenType.TAG_END) {
            // we finished
            Transformation transformation = registry.get(name.value(), Collections.emptyList());
            System.out.println("got start of " + name.value() + " -> " + transformation);
            if (transformation == null) {
              // this isn't a known tag, oh no!
              // lets take a step back, first, create a string
              i -= 2;
              String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              System.out.println("no transformation found " + string);
              continue;
            } else {
              if (transformation instanceof OneTimeTransformation) {
                oneTimeTransformations.addLast((OneTimeTransformation) transformation);
              } else {
                transformations.addLast(transformation);
              }
            }
          } else {
            throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
          }
          break;
        case CLOSE_TAG_START:
          // next has to be name
          name = tokens.get(++i);
          if (name.type() != TokenType.NAME) {
            throw new ParsingException("Expected name after close tag start, but got " + name, -1);
          }
          // after that, we just want end, sometimes end has params tho
          paramOrEnd = tokens.get(++i);
          if (paramOrEnd.type() == TokenType.TAG_END) {
            // we finished, gotta remove name out of the stack
            System.out.println("got end of " + name.value());
            if (!registry.exists(name.value())) {
              // invalid end
              // lets take a step back, first, create a string
              i -= 2;
              String string = tokens.get(i).value() + name.value() + paramOrEnd.value();
              // insert our string
              tokens.set(i, new Token(TokenType.STRING, string));
              // remove the others
              tokens.remove(i + 1);
              tokens.remove(i + 1);
              System.out.println("invalid end " + name.value() + ", string " + string);
              continue;
            } else {
              removeFirst(transformations, t -> t.name().equals(name.value()));
            }
          } else if (paramOrEnd.type() == TokenType.PARAM_SEPARATOR) {
            // read all params
            List<Token> inners = new ArrayList<>();
            Token next;
            while ((next = tokens.get(++i)).type() != TokenType.TAG_END) {
              inners.add(next);
            }

            // check what we need to close, so we create a transformation and try to remove it
            Transformation transformation = registry.get(name.value(), inners);
            transformations.removeFirstOccurrence(transformation);
          } else {
            throw new ParsingException("Expected tag end or param separator after tag name, but got " + paramOrEnd, -1);
          }
          break;
        case STRING:
          System.out.println("got: " + token.value() + " with transformations " + transformations);
          Component current = Component.text(token.value());

          for (Transformation transformation : transformations) {
            System.out.println("applying " + transformation);
            current = transformation.apply(current, parent);
          }

          while (!oneTimeTransformations.isEmpty()) {
            current = oneTimeTransformations.removeLast().applyOneTime(current, parent, transformations);
          }

          if (current != null) {
            parent.append(current);
          }
          break;
        case TAG_END:
        case PARAM_SEPARATOR:
        case QUOTE_START:
        case QUOTE_END:
        case NAME:
          throw new ParsingException("Unexpected token " + token, -1);
      }
      i++;
    }

    // at last, go thru all transformations that insert something
    // TODO not sure I like this
    List<Component> children = parent.asComponent().children();
    Component last = children.get(children.size() - 1);
    for (Transformation transformation : transformations) {
      if(transformation instanceof Inserting) {
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

  private static boolean removeFirst(ArrayDeque<Transformation> transformations, Predicate<Transformation> filter) {
    final Iterator<Transformation> each = transformations.descendingIterator();
    while (each.hasNext()) {
      if (filter.test(each.next())) {
        each.remove();
        return true;
      }
    }
    return false;
  }
}
