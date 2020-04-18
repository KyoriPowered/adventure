/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class AbstractLegacyComponentSerializer implements LegacyComponentSerializer {
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private static final String LEGACY_CHARS = "0123456789abcdefklmnor";
  private static final List<TextFormat> FORMATS;
  static {
    final List<TextFormat> formats = new ArrayList<>();
    Collections.addAll(formats, TextColor.values());
    Collections.addAll(formats, DECORATIONS);
    formats.add(Reset.INSTANCE);
    FORMATS = Collections.unmodifiableList(formats);

    // assert same length
    if(FORMATS.size() != LEGACY_CHARS.length()) {
      throw new IllegalStateException("FORMATS length differs from LEGACY_CHARS length");
    }
  }

  private static @Nullable TextFormat formatByLegacyChar(final char legacy) {
    final int index = LEGACY_CHARS.indexOf(legacy);
    return index == -1 ? null : FORMATS.get(index);
  }

  private static char getLegacyChar(final TextFormat legacy) {
    final int index = FORMATS.indexOf(legacy);
    return LEGACY_CHARS.charAt(index);
  }

  @Override
  public @NonNull TextComponent deserialize(final @NonNull String input, final char character) {
    int next = input.lastIndexOf(character, input.length() - 2);
    if(next == -1) {
      return this.finish(TextComponent.of(input));
    }

    final List<TextComponent> parts = new ArrayList<>();

    TextComponent.Builder current = null;
    boolean reset = false;

    int pos = input.length();
    do {
      final TextFormat format = formatByLegacyChar(input.charAt(next + 1));
      if(format != null) {
        final int from = next + 2;
        if(from != pos) {
          if(current != null) {
            if(reset) {
              parts.add(current.build());
              reset = false;
              current = TextComponent.builder();
            } else {
              current = TextComponent.builder().append(current.build());
            }
          } else {
            current = TextComponent.builder();
          }

          current.content(input.substring(from, pos));
        } else if(current == null) {
          current = TextComponent.builder();
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
    return this.finish(TextComponent.builder(pos > 0 ? input.substring(0, pos) : "").append(parts).build());
  }

  protected abstract @NonNull TextComponent finish(final TextComponent component);

  @Override
  public @NonNull String serialize(final @NonNull Component component, final char character) {
    final Cereal state = new Cereal(character);
    state.append(component);
    return state.toString();
  }

  private static boolean applyFormat(final TextComponent.@NonNull Builder builder, final @NonNull TextFormat format) {
    if(format instanceof TextColor) {
      builder.colorIfAbsent((TextColor) format);
      return true;
    } else if(format instanceof TextDecoration) {
      builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
      return false;
    } else if(format instanceof Reset) {
      builder.colorIfAbsent(null);
      for(int i = 0, length = DECORATIONS.length; i < length; i++) {
        final TextDecoration decoration = DECORATIONS[i];
        builder.decoration(decoration, TextDecoration.State.NOT_SET);
      }
      return true;
    }
    throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
  }

  private enum Reset implements TextFormat {
    INSTANCE;
  }

  // Are you hungry?
  private static final class Cereal {
    private final StringBuilder sb = new StringBuilder();
    private final Style style = new Style();
    private final char character;

    Cereal(final char character) {
      this.character = character;
    }

    void append(final @NonNull Component component) {
      this.append(component, new Style());
    }

    private void append(final @NonNull Component component, final @NonNull Style style) {
      style.apply(component);

      if(component instanceof TextComponent) {
        final String content = ((TextComponent) component).content();
        if(!content.isEmpty()) {
          style.applyFormat();
          this.sb.append(content);
        }
      }

      final List<Component> children = component.children();
      if(!children.isEmpty()) {
        final Style childrenStyle = new Style(style);
        for(final Component child : children) {
          this.append(child, childrenStyle);
          childrenStyle.set(style);
        }
      }
    }

    private void append(final @NonNull TextFormat format) {
      this.sb.append(this.character).append(getLegacyChar(format));
    }

    @Override
    public String toString() {
      return this.sb.toString();
    }

    private final class Style {
      private @Nullable TextColor color;
      private final Set<TextDecoration> decorations;

      Style() {
        this.decorations = EnumSet.noneOf(TextDecoration.class);
      }

      Style(final @NonNull Style that) {
        this.color = that.color;
        this.decorations = EnumSet.copyOf(that.decorations);
      }

      void set(final @NonNull Style that) {
        this.color = that.color;
        this.decorations.clear();
        this.decorations.addAll(that.decorations);
      }

      void apply(final @NonNull Component component) {
        final TextColor color = component.color();
        if(color != null) {
          this.color = color;
        }

        for(int i = 0, length = DECORATIONS.length; i < length; i++) {
          final TextDecoration decoration = DECORATIONS[i];
          switch(component.decoration(decoration)) {
            case TRUE:
              this.decorations.add(decoration);
              break;
            case FALSE:
              this.decorations.remove(decoration);
              break;
          }
        }
      }

      void applyFormat() {
        // If color changes, we need to do a full reset
        if(this.color != Cereal.this.style.color) {
          this.applyFullFormat();
          return;
        }

        // Does current have any decorations we don't have?
        // Since there is no way to undo decorations, we need to reset these cases
        if(!this.decorations.containsAll(Cereal.this.style.decorations)) {
          this.applyFullFormat();
          return;
        }

        // Apply new decorations
        for(final TextDecoration decoration : this.decorations) {
          if(Cereal.this.style.decorations.add(decoration)) {
            Cereal.this.append(decoration);
          }
        }
      }

      private void applyFullFormat() {
        if(this.color != null) {
          Cereal.this.append(this.color);
        } else {
          Cereal.this.append(Reset.INSTANCE);
        }
        Cereal.this.style.color = this.color;

        for(final TextDecoration decoration : this.decorations) {
          Cereal.this.append(decoration);
        }

        Cereal.this.style.decorations.clear();
        Cereal.this.style.decorations.addAll(this.decorations);
      }
    }
  }
}
