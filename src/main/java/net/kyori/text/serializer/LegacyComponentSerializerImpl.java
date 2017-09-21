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
package net.kyori.text.serializer;

import com.google.common.collect.ObjectArrays;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.format.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Deprecated
class LegacyComponentSerializerImpl implements LegacyComponentSerializer {

  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private static final TextFormat[] FORMATS = ObjectArrays.concat(Stream.concat(Arrays.stream(TextColor.values()), Arrays.stream(DECORATIONS)).toArray(TextFormat[]::new), Reset.INSTANCE);
  private static final String FORMAT_LOOKUP = Arrays.stream(FORMATS).map(format -> String.valueOf(format.legacy())).collect(Collectors.joining());

  @Nonnull
  @Override
  public TextComponent deserialize(@Nonnull String input, char character) {
    int next = input.lastIndexOf(character, input.length() - 2);
    if(next == -1) {
      return TextComponent.of(input);
    }

    final List<TextComponent> parts = new ArrayList<>();

    TextComponent.Builder current = null;
    boolean reset = false;

    int pos = input.length();
    do {
      final TextFormat format = find(input.charAt(next + 1));
      if(format != null) {
        final int from = next + 2;
        if(from != pos) {
          if(current != null) {
            if(reset) {
              parts.add(current.build());
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

      next = input.lastIndexOf(character, next - 1);
    } while(next != -1);

    if(current != null) {
      parts.add(current.build());
    }

    Collections.reverse(parts);
    return TextComponent.builder(pos > 0 ? input.substring(0, pos) : "").append(parts).build();
  }

  @Nonnull
  @Override
  public String serialize(@Nonnull Component component, final char character) {
    final Cereal state = new Cereal(character);
    state.append(component);
    return state.toString();
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
      for(final TextDecoration decoration : DECORATIONS) {
        builder.decoration(decoration, TextDecoration.State.NOT_SET);
      }
      return true;
    }
    throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
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

  // Are you hungry?
  @Deprecated
  private static final class Cereal {

    private final StringBuilder sb = new StringBuilder();
    private final char character;
    @Nullable private TextColor oldColor;
    @Nullable private TextColor newColor;
    private final Set<TextDecoration> oldDecorations = EnumSet.noneOf(TextDecoration.class);
    private final Set<TextDecoration> newDecorations = EnumSet.noneOf(TextDecoration.class);

    Cereal(final char character) {
      this.character = character;
    }

    void append(@Nonnull final Component component) {
      this.append(null, component);
    }

    private void append(@Nullable final Component parent, @Nonnull final Component component) {
      if(parent != null) {
        this.format(
          color(component.color(), parent.color()),
          component.decorations(parent.decorations())
        );
      } else {
        this.format(
          color(component.color(), null),
          component.decorations()
        );
      }

      if(component instanceof TextComponent) {
        this.append(((TextComponent) component).content());
      }

      for(final Component child : component.children()) {
        this.append(component, child);
      }
    }

    private void format(@Nullable final TextColor color, @Nonnull final Set<TextDecoration> decorations) {
      this.newColor = color;

      for(final TextDecoration decoration : DECORATIONS) {
        if(decorations.contains(decoration)) {
          this.newDecorations.add(decoration);
        } else {
          this.newDecorations.remove(decoration);
        }
      }
    }

    private void append(@Nonnull final String string) {
      if(string.isEmpty()) {
        return;
      }
      this.applyFormat(false);
      this.sb.append(string);
    }

    private void append(@Nonnull final TextFormat format) {
      this.sb.append(this.character).append(format.legacy());
    }

    private void applyFormat(final boolean full) {
      if(full) {
        this.oldColor = this.newColor;
        if(this.newColor == null) {
          this.append(Reset.INSTANCE);
        } else {
          this.append(this.newColor);
        }

        this.oldDecorations.clear();
        for(final TextDecoration decoration : this.newDecorations) {
          this.oldDecorations.add(decoration);
          this.append(decoration);
        }
      } else {
        if(this.oldColor != this.newColor) {
          this.applyFormat(true);
          return;
        }

        for(final TextDecoration decoration : this.oldDecorations) {
          if(!this.newDecorations.contains(decoration)) {
            this.applyFormat(true);
            return;
          }
        }

        for(final TextDecoration decoration : this.newDecorations) {
          if(this.oldDecorations.add(decoration)) {
            this.append(decoration);
          }
        }
      }
    }

    @Override
    public String toString() {
      return this.sb.toString();
    }

    @Nullable
    private static TextColor color(@Nullable TextColor color, @Nullable TextColor defaultValue) {
      return color != null ? color : defaultValue;
    }
  }
}
