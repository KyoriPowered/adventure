/*
 * This file is part of adventure, licensed under the MIT License.
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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AnsiComponentSerializerImpl implements AnsiComponentSerializer {

  private final boolean colorDownSample;

  @SuppressWarnings({"WeakerAccess"})
  public static AnsiComponentSerializer trueColor = new AnsiComponentSerializerImpl(false);

  private AnsiComponentSerializerImpl(final boolean colorDownSample) {
    this.colorDownSample = colorDownSample;
  }

  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  protected static final String RGB_FORMAT = ESC_CHAR + "[38;2;%d;%d;%dm";
  private static final Pattern STRIP_ESC_CODES = Pattern.compile(ESC_CHAR + "\\[[;\\d]*m");
  private static final List<TextFormat> FORMATS;

  static {
    final List<TextFormat> formats = new ArrayList<>(NamedTextColor.values());
    Collections.addAll(formats, DECORATIONS);
    formats.add(Reset.INSTANCE);
    FORMATS = Collections.unmodifiableList(formats);

  }

  private Ansi toAnsi(final TextFormat format) {
    if(format instanceof NamedTextColor) {
      return Formats.getByFormat(format).getAnsi();
    }
    if(format instanceof TextDecoration) {
      return Formats.getByFormat(format).getAnsi();
    }
    if(format instanceof TextColor) {
      if(this.colorDownSample) {
        return Formats.getByFormat(NamedTextColor.nearestTo((TextColor) format)).getAnsi();
      }
      final int red = ((TextColor) format).red();
      final int blue = ((TextColor) format).blue();
      final int green = ((TextColor) format).green();
      return Ansi.ansi().format(RGB_FORMAT, red, green, blue);
    } else {
      return null;
    }
  }

  @NonNull
  @Override
  public Component deserialize(final @NonNull String input) {
    final Matcher matcher = STRIP_ESC_CODES.matcher(input);
    String out = input;
    while(matcher.find()) {
      out = out.replace(matcher.group(), "");
    }
    return TextComponent.of(out);
  }

  @NonNull
  @Override
  public String serialize(final @NonNull Component component) {
    final Porridge state = new Porridge();
    state.append(component);
    return state.toString();
  }

  @NonNull
  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private boolean colorDownSample = true;

    BuilderImpl() {

    }

    BuilderImpl(final @NonNull AnsiComponentSerializerImpl serializer) {
      this.colorDownSample = serializer.colorDownSample;
    }

    @Override
    public @NonNull Builder downSample(final boolean downSample) {
      this.colorDownSample = downSample;
      return this;
    }

    @Override
    public @NonNull AnsiComponentSerializer build() {
      return new AnsiComponentSerializerImpl(this.colorDownSample);
    }
  }

  private final class Porridge {
    private final Ansi ansi = Ansi.ansi();
    private final Style style = new Style();

    @Override
    public String toString() {
      return this.ansi.toString();
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
          this.ansi.a(content);
          this.ansi.a(Formats.SOFT_RESET);
        }
      }
      if(component instanceof TranslatableComponent) {
        final Component c = TranslatableComponentRenderer.get().render(component, Locale.getDefault());
        if(c instanceof TextComponent) {
          final String translatedContent = ((TextComponent) c).content();
          if(!translatedContent.isEmpty()) {
            style.applyFormat();
            this.ansi.a(translatedContent);
            this.ansi.a(Formats.SOFT_RESET);
          }
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

    void append(final @NonNull TextFormat format) {
      final Ansi a = AnsiComponentSerializerImpl.this.toAnsi(format);
      if(a != null) {
        this.ansi.a(a);
      }
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

        for(final TextDecoration decoration : DECORATIONS) {
          switch (component.decoration(decoration)) {
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
        if(this.color != Porridge.this.style.color) {
          this.applyFullFormat();
          return;
        }

        // Does current have any decorations we don't have?
        // Since there is no way to undo decorations, we need to reset these cases
        if(!this.decorations.containsAll(Porridge.this.style.decorations)) {
          this.applyFullFormat();
          return;
        }

        // Apply new decorations
        for(final TextDecoration decoration : this.decorations) {
          if(Porridge.this.style.decorations.add(decoration)) {
            Porridge.this.append(decoration);
          }
        }
      }

      private void applyFullFormat() {
        if(this.color != null) {
          Porridge.this.append(this.color);
        } else {
          Porridge.this.append(Reset.INSTANCE);
        }
        Porridge.this.style.color = this.color;

        for(final TextDecoration decoration : this.decorations) {
          Porridge.this.append(decoration);
        }

        Porridge.this.style.decorations.clear();
        Porridge.this.style.decorations.addAll(this.decorations);
      }
    }
  }

  enum Reset implements TextFormat {
    INSTANCE
  }
}
