/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text;

import com.google.common.annotations.VisibleForTesting;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.format.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
public final class LegacyComponent {

  @VisibleForTesting static final char CHARACTER = '\u00A7';
  private static final TextFormat[] FORMATS;
  private static final String FORMAT_LOOKUP;

  static {
    TextColor[] colors = TextColor.values();
    TextDecoration[] decorations = TextDecoration.values();

    FORMATS = new TextFormat[colors.length + decorations.length + 1];
    int c = 0;
    for(final TextColor color : colors) FORMATS[c++] = color;
    for(final TextDecoration decoration : decorations) FORMATS[c++] = decoration;
    FORMATS[c] = Reset.INSTANCE;

    FORMAT_LOOKUP = Arrays.stream(FORMATS).map(format -> String.valueOf(format.legacy())).collect(Collectors.joining());
  }

  private LegacyComponent() {
  }

  @Deprecated
  @Nonnull
  public static TextComponent from(@Nonnull final String input) {
    int next = input.lastIndexOf(CHARACTER, input.length() - 2);
    if(next == -1) {
      return TextComponent.of(input);
    }

    final List<TextComponent.Builder> parts = new ArrayList<>();

    TextComponent.Builder current = null;
    boolean reset = false;

    int pos = input.length();
    do {
      TextFormat format = find(input.charAt(next + 1));
      if(format != null) {
        int from = next + 2;
        if(from != pos) {
          if(current != null) {
            if(reset) {
              parts.add(current);
              reset = false;
              current = TextComponent.builder("");
            } else {
              current = TextComponent.builder("").append(current.build());
            }
          } else {
            current = TextComponent.builder("");
          }

          current.content(input.substring(from, pos));
        } else if(current == null) {
          current = TextComponent.builder("");
        }

        reset |= applyFormat(current, format);
        pos = next;
      }

      next = input.lastIndexOf(CHARACTER, next - 1);
    } while(next != -1);

    if(current != null) {
      parts.add(current);
    }

    Collections.reverse(parts);
    switch(parts.size()) {
      case 0:
        return TextComponent.of(pos > 0 ? input.substring(0, pos) : "");
      case 1:
        return parts.get(0).build();
      case 2:
      default:
        return parts.get(0)
          .append(parts.subList(1, parts.size()).stream().map(TextComponent.Builder::build).collect(Collectors.toList()))
          .build();
    }
  }

  private static boolean applyFormat(@Nonnull final TextComponent.Builder builder, @Nonnull final TextFormat format) {
    if(format instanceof TextColor) {
      builder.color((TextColor) format);
      return true;
    } else if(format instanceof TextDecoration) {
      builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
      return false;
    } else if(format instanceof Reset) {
      builder.color(null);
      for(final TextDecoration decoration : TextDecoration.values()) {
        builder.decoration(decoration, TextDecoration.State.NOT_SET);
      }
      return true;
    }
    throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
  }

  @Deprecated
  @Nonnull
  public static String to(@Nonnull final Component component) {
    final StringBuilder state = new StringBuilder();
    to(state, component);
    return state.toString();
  }

  private static void to(@Nonnull final StringBuilder sb, @Nonnull final Component component) {
    @Nullable final TextColor color = component.color();
    if(color != null) {
      sb.append(CHARACTER).append(color.legacy());
    }

    for(final TextDecoration decoration : component.decorations()) {
      sb.append(CHARACTER).append(decoration.legacy());
    }

    if(component instanceof TextComponent) {
      sb.append(((TextComponent) component).content());
    }

    for(final Component child : component.children()) {
      to(sb, child);
    }
  }

  private static TextFormat find(final char legacy) {
    final int pos = FORMAT_LOOKUP.indexOf(legacy);
    return pos == -1 ? null : FORMATS[pos];
  }

  @Deprecated
  private enum Reset implements TextFormat {
    INSTANCE;

    @Deprecated
    @Override
    public char legacy() {
      return 'r';
    }
  }
}
