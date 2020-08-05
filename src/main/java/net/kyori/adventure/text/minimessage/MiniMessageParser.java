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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.fancy.Fancy;
import net.kyori.adventure.text.minimessage.fancy.Gradient;
import net.kyori.adventure.text.minimessage.fancy.Rainbow;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.minimessage.Tokens.CLICK;
import static net.kyori.adventure.text.minimessage.Tokens.CLOSE_TAG;
import static net.kyori.adventure.text.minimessage.Tokens.COLOR;
import static net.kyori.adventure.text.minimessage.Tokens.GRADIENT;
import static net.kyori.adventure.text.minimessage.Tokens.HEX;
import static net.kyori.adventure.text.minimessage.Tokens.HOVER;
import static net.kyori.adventure.text.minimessage.Tokens.INSERTION;
import static net.kyori.adventure.text.minimessage.Tokens.KEYBIND;
import static net.kyori.adventure.text.minimessage.Tokens.PRE;
import static net.kyori.adventure.text.minimessage.Tokens.RAINBOW;
import static net.kyori.adventure.text.minimessage.Tokens.RESET;
import static net.kyori.adventure.text.minimessage.Tokens.SEPARATOR;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_END;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_START;
import static net.kyori.adventure.text.minimessage.Tokens.TRANSLATABLE;
import static net.kyori.adventure.text.minimessage.Tokens.FONT;

/* package */ class MiniMessageParser {

  // regex group names
  private static final String START = "start";
  private static final String TOKEN = "token";
  private static final String INNER = "inner";
  private static final String END = "end";
  // https://regex101.com/r/8VZ7uA/10
  private static final Pattern pattern = Pattern.compile("((?<start><)(?<token>[^<>]+(:(?<inner>['\"]?([^'\"](\\\\['\"])?)+['\"]?))*)(?<end>>))+?");

  private static final Pattern dumSplitPattern = Pattern.compile("['\"]:['\"]");

  /* package */ static @NonNull String escapeTokens(final @NonNull String richMessage) {
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

  /* package */ static @NonNull String stripTokens(final @NonNull String richMessage) {
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

  /* package */ static @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull String... placeholders) {
    if (placeholders.length % 2 != 0) {
      throw new ParseException(
        "Invalid number placeholders defined, usage: parseFormat(format, key, value, key, value...)");
    }
    for (int i = 0; i < placeholders.length; i += 2) {
      richMessage = richMessage.replace(TAG_START + placeholders[i] + TAG_END, placeholders[i + 1]);
    }
    return richMessage;
  }


  /* package */ static @NonNull String handlePlaceholders(@NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      richMessage = richMessage.replace(TAG_START + entry.getKey() + TAG_END, entry.getValue());
    }
    return richMessage;
  }

  /* package */ static @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull String... placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  /* package */ static @NonNull Component parseFormat(final @NonNull String richMessage, final @NonNull Map<String, String> placeholders) {
    return parseFormat(handlePlaceholders(richMessage, placeholders));
  }

  /* package */ static @NonNull Component parseFormat(@NonNull String input, final @NonNull Template... placeholders) {
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

  /* package */ static @NonNull Component parseFormat(@NonNull String input, final @NonNull List<Template> placeholders) {
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

  /* package */ static @NonNull Component parseFormat(final @NonNull String richMessage) {
    return parseFormat0(richMessage, Collections.emptyMap());
  }

  /* package */ static @NonNull Component parseFormat0(final @NonNull String richMessage, final @NonNull Map<String, Template.ComponentTemplate> templates) {
    final TextComponent.Builder parent = TextComponent.builder("");

    final ArrayDeque<ClickEvent> clickEvents = new ArrayDeque<>();
    final ArrayDeque<HoverEvent<?>> hoverEvents = new ArrayDeque<>();
    final ArrayDeque<TextColor> colors = new ArrayDeque<>();
    final ArrayDeque<String> insertions = new ArrayDeque<>();
    final ArrayDeque<Key> fonts = new ArrayDeque<>();
    final EnumSet<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
    boolean isPreformatted = false;
    final Map<Class<? extends Fancy>, Fancy> fancy = new LinkedHashMap<>();

    final Matcher matcher = pattern.matcher(richMessage);
    int lastEnd = 0;
    while (matcher.find()) {
      Component current = null;
      int startIndex = matcher.start();
      int endIndex = matcher.end();

      String msg = null;
      if (startIndex > lastEnd) {
        msg = richMessage.substring(lastEnd, startIndex);
      }
      lastEnd = endIndex;

      // handle message
      if (msg != null && msg.length() != 0) {
        // append message
        current = TextComponent.of(msg);
        current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);

      }

      final String token = matcher.group(TOKEN);
      final String inner = matcher.group(INNER);

      TextDecoration deco;
      TextColor color;

      // handle pre
      if (isPreformatted) {
        if (token.startsWith(CLOSE_TAG + PRE)) {
          isPreformatted = false;
          if (current != null) {
            parent.append(current);
          }
        } else {
          if (current != null) {
            parent.append(current);
          }
          current = TextComponent.of(TAG_START + token + TAG_END);
          current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);
          parent.append(current);
        }
        continue;
      }

      // click
      if (token.startsWith(CLICK + SEPARATOR)) {
        clickEvents.push(handleClick(token, inner));
      } else if (token.equals(CLOSE_TAG + CLICK)) {
        clickEvents.pollFirst();
      }
      // hover
      else if (token.startsWith(HOVER + SEPARATOR)) {
        hoverEvents.push(handleHover(token, inner));
      } else if (token.equals(CLOSE_TAG + HOVER)) {
        hoverEvents.pollFirst();
      }
      // decoration
      else if ((deco = resolveDecoration(token)) != null) {
        decorations.add(deco);
      } else if (token.startsWith(CLOSE_TAG) && (deco = resolveDecoration(token.replace(CLOSE_TAG, ""))) != null) {
        decorations.remove(deco);
      }
      // color
      else if ((color = resolveColor(token)) != null) {
        colors.push(color);
      } else if (token.startsWith(CLOSE_TAG) && resolveColor(token.replace(CLOSE_TAG, "")) != null) {
        colors.pollFirst();
      }
      // color hex or named syntax
      else if (token.startsWith(COLOR + SEPARATOR) && (color = resolveColorNew(token)) != null) {
        colors.push(color);
      } else if (token.startsWith(CLOSE_TAG + COLOR) && resolveColorNew(token.replace(CLOSE_TAG, "")) != null) {
        colors.pollFirst();
      }
      // color: short hex
      else if (token.startsWith(HEX) && (color = parseColor(token)) != null) {
        colors.push(color);
      } else if (token.startsWith(CLOSE_TAG + HEX) && parseColor(token.replace(CLOSE_TAG, "")) != null) {
        colors.pollFirst();
      }
      // keybind
      else if (token.startsWith(KEYBIND + SEPARATOR)) {
        if (current != null) {
          parent.append(current);
        }
        current = handleKeybind(token);
        current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);
      }
      // translatable
      else if (token.startsWith(TRANSLATABLE + SEPARATOR)) {
        if (current != null) {
          parent.append(current);
        }
        current = handleTranslatable(token, inner);
        current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);
      }
      // insertion
      else if (token.startsWith(INSERTION + SEPARATOR)) {
        insertions.push(handleInsertion(token));
      } else if (token.startsWith(CLOSE_TAG + INSERTION)) {
        insertions.pop();
      }
      // rainbow
      else if (token.startsWith(RAINBOW)) {
        fancy.put(Rainbow.class, handleRainbow(token));
      } else if (token.startsWith(CLOSE_TAG + RAINBOW)) {
        fancy.remove(Rainbow.class);
      }
      // gradient
      else if (token.startsWith(GRADIENT)) {
        fancy.put(Gradient.class, handleGradient(token));
      } else if (token.startsWith(CLOSE_TAG + GRADIENT)) {
        fancy.remove(Gradient.class);
      }
      // font
      else if (token.startsWith(FONT)) {
        fonts.push(handleFont(token));
      } else if (token.startsWith(CLOSE_TAG + FONT)) {
        fonts.pop();
      }
      // reset
      else if (token.startsWith(RESET)) {
        clickEvents.clear();
        hoverEvents.clear();
        colors.clear();
        insertions.clear();
        decorations.clear();
        fancy.clear();
        fonts.clear();
      }
      // pre
      else if (token.startsWith(PRE)) {
        isPreformatted = true;
      }
      // template
      else if (templates.containsKey(token)) {
        current = templates.get(token).getValue();
        current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);
      }
      // invalid tag
      else {
        if (current != null) {
          parent.append(current);
        }
        current = TextComponent.of(TAG_START + token + TAG_END);
        current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);
      }

      if (current != null) {
        parent.append(current);
      }
    }

    // handle last message part
    if (richMessage.length() > lastEnd) {
      final String msg = richMessage.substring(lastEnd);
      // append message
      Component current = TextComponent.of(msg);

      // set everything that is not closed yet
      current = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, current, fancy, fonts);

      parent.append(current);
    }

    // optimization, ignore empty parent
    final TextComponent comp = parent.build();
    if (comp.content().equals("") && comp.children().size() == 1) {
      return comp.children().get(0);
    } else {
      return comp;
    }
  }

  private static @NonNull Component applyFormatting(@NonNull final Deque<ClickEvent> clickEvents,
                                                    @NonNull final Deque<HoverEvent<?>> hoverEvents,
                                                    @NonNull final Deque<TextColor> colors,
                                                    @NonNull final Deque<String> insertions,
                                                    @NonNull final EnumSet<TextDecoration> decorations,
                                                    @NonNull Component current,
                                                    @NonNull final Map<Class<? extends Fancy>, Fancy> fancy, ArrayDeque<Key> fonts) {
    // set everything that is not closed yet
    if (!clickEvents.isEmpty() && current.clickEvent() == null) {
      current = current.clickEvent(clickEvents.peek());
    }
    if (!hoverEvents.isEmpty() && current.hoverEvent() == null) {
      current = current.hoverEvent(hoverEvents.peek());
    }
    if (!colors.isEmpty()) {
      current = current.colorIfAbsent(colors.peek());
    }
    if (!decorations.isEmpty()) {
      for (TextDecoration decor : decorations) {
        if (!current.hasDecoration(decor)) {
          current = current.decoration(decor, true);
        }
      }
    }
    if (!insertions.isEmpty()) {
      current = current.insertion(insertions.peek());
    }
    if (!fonts.isEmpty()) {
      current = current.style(current.style().font(fonts.peek()));
    }

    if (current instanceof TextComponent && fancy.size() != 0) {
      Component parent = null;
      final TextComponent bigComponent = (TextComponent) current;

      final Fancy next = fancy.entrySet().iterator().next().getValue();
      next.init(bigComponent.content().length());
      // split into multiple components
      for (int i = 0; i < bigComponent.content().length(); i++) {
        Component smallComponent = TextComponent.of(bigComponent.content().charAt(i));
        // apply formatting
        smallComponent = applyFormatting(clickEvents, hoverEvents, colors, insertions, decorations, smallComponent, Collections.emptyMap(), fonts);
        smallComponent = next.apply(smallComponent);
        // append
        if (parent == null) {
          parent = smallComponent;
        } else {
          parent = parent.append(smallComponent);
        }
      }
      if (parent != null) {
        current = parent;
      }
    }

    return current;
  }

  private static @NonNull Rainbow handleRainbow(final @NonNull String token) {
    if (token.contains(SEPARATOR)) {
      final String phase = token.substring(token.indexOf(SEPARATOR) + 1);
      try {
        return new Rainbow(Integer.parseInt(phase));
      } catch (NumberFormatException ex) {
        throw new ParseException("Can't parse rainbow phase (not a int) " + token);
      }
    }
    return new Rainbow();
  }

  private static @NonNull Gradient handleGradient(final @NonNull String token) {
    if (token.contains(SEPARATOR)) {
      final String[] split = token.split(":");
      if (split.length >= 3) {
        TextColor[] colors = new TextColor[split.length - 1];
        int phase = 0;
        for (int i = 1; i < split.length; i++) {
          final TextColor color = parseColor(split[i]);
          if (color == null) {
            // might be the phase
            if (i == split.length - 1) {
              try {
                phase = Integer.parseInt(split[i]);
                // we created one entry to much, lets get rid of it
                colors = Arrays.copyOfRange(colors, 0, colors.length - 1);
              } catch (NumberFormatException ex) {
                throw new ParseException("Can't parse gradient phase (not a color nor a phase " + i + ") " + token);
              }
            } else {
              throw new ParseException("Can't parse gradient phase (not a color " + i + ") " + token);
            }
          } else {
            colors[i - 1] = color;
          }
        }
        return new Gradient(phase, colors);
      } else {
        throw new ParseException("Can't parse gradient (wrong args) " + token);
      }
    }
    return new Gradient();
  }


  private static @NonNull Key handleFont(final @NonNull String token) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse font (too few args) " + token);
    }
    return Key.of(token.replace(args[0] + SEPARATOR, ""));
  }


  private static @NonNull String handleInsertion(final @NonNull String token) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse insertion (too few args) " + token);
    }
    return token.replace(args[0] + SEPARATOR, "");
  }


  private static @NonNull Component handleTranslatable(final @NonNull String token, final String inner) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse translatable (too few args) " + token);
    }
    if (inner == null) {
      return TranslatableComponent.of(args[1]);
    } else {
      final List<Component> inners = new ArrayList<>();
      final String toSplit = token.replace(args[0] + ":" + args[1] + ":", "");
      final String[] split = dumSplitPattern.split(cleanInner(toSplit));
      for (String someInner : split) {
        inners.add(parseFormat(someInner));
      }
      return TranslatableComponent.of(args[1], inners);
    }
  }


  private static @NonNull KeybindComponent handleKeybind(final @NonNull String token) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse keybind (too few args) " + token);
    }
    return KeybindComponent.of(args[1]);
  }

  private static @NonNull ClickEvent handleClick(final @NonNull String token, final @NonNull String inner) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse click action (too few args) " + token);
    }
    final ClickEvent.Action action = ClickEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT));
    if (action == null) throw new ParseException("Can't parse click action (invalid action) " + token);
    return ClickEvent.of(action, token.replace(CLICK + SEPARATOR + args[1] + SEPARATOR, ""));
  }


  private static @NonNull HoverEvent<?> handleHover(final @NonNull String token, @NonNull String inner) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse hover action (too few args) " + token);
    }
    // regex doesnt seem to match inner if it contains a multiline, so lets get it ourself
    if (inner == null) {
      inner = cleanInner(token.replace(args[0] + SEPARATOR + args[1] + SEPARATOR, ""));
    } else {
      inner = cleanInner(inner);
    }
    // TODO figure out support for all hover actions
    final HoverEvent.Action action = HoverEvent.Action.NAMES.value(args[1].toLowerCase(Locale.ROOT));
    if (action == null) throw new ParseException("Can't parse hover action (invalid action) " + token);
    return HoverEvent.of(action, parseFormat(inner));
  }


  private static @Nullable TextColor resolveColor(final @NonNull String token) {
    return NamedTextColor.NAMES.value(token.toLowerCase(Locale.ROOT));
  }

  private static @Nullable TextColor resolveColorNew(final @NonNull String token) {
    final String[] args = token.split(SEPARATOR);
    if (args.length < 2) {
      throw new ParseException("Can't parse color (too few args) " + token);
    }

    return parseColor(args[1]);
  }

  private static @Nullable TextColor parseColor(final String color) {
    if (color.charAt(0) == '#') {
      return TextColor.fromHexString(color);
    } else {
      return NamedTextColor.NAMES.value(color.toLowerCase(Locale.ROOT));
    }
  }

  private static @Nullable TextDecoration resolveDecoration(final @NonNull String token) {
    try {
      return TextDecoration.NAMES.value(token.toLowerCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  private static String cleanInner(final String inner) {
    return inner.substring(1).substring(0, inner.length() - 2); // cut off first and last "/'
  }
}
