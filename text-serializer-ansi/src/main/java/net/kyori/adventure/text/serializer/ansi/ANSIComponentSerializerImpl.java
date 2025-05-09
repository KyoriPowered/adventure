/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

import java.util.Optional;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Services;
import net.kyori.ansi.ANSIComponentRenderer;
import net.kyori.ansi.ColorLevel;
import net.kyori.ansi.StyleOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

final class ANSIComponentSerializerImpl implements ANSIComponentSerializer {
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);

  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  final ColorLevel colorLevel;
  final ComponentFlattener flattener;

  static final class Instances {
    static final ANSIComponentSerializer INSTANCE = SERVICE
      .map(Provider::ansi)
      .orElseGet(() -> new ANSIComponentSerializerImpl(ColorLevel.compute(), ComponentFlattener.basic()));
  }

  ANSIComponentSerializerImpl(final ColorLevel colorLevel, final ComponentFlattener flattener) {
    this.colorLevel = colorLevel;
    this.flattener = flattener;
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    final ANSIComponentRenderer.ToString<Style> renderer = ANSIComponentRenderer.toString(ComponentStyleOps.INSTANCE, this.colorLevel);
    this.flattener.flatten(component, new ANSIFlattenerListener(renderer));
    renderer.complete();
    return renderer.asString();
  }

  static StyleOps.State mapState(final TextDecoration.State state) {
    switch (state) {
      case NOT_SET:
        return StyleOps.State.UNSET;
      case FALSE:
        return StyleOps.State.FALSE;
      case TRUE:
        return StyleOps.State.TRUE;
    }
    throw new IllegalStateException("Decoration state is not valid");
  }

  static class ComponentStyleOps implements StyleOps<Style> {
    static final ComponentStyleOps INSTANCE = new ComponentStyleOps();

    @Override
    public State bold(final @NotNull Style style) {
      return mapState(style.decoration(TextDecoration.BOLD));
    }

    @Override
    public State italics(final @NotNull Style style) {
      return mapState(style.decoration(TextDecoration.ITALIC));
    }

    @Override
    public State underlined(final @NotNull Style style) {
      return mapState(style.decoration(TextDecoration.UNDERLINED));
    }

    @Override
    public State strikethrough(final @NotNull Style style) {
      return mapState(style.decoration(TextDecoration.STRIKETHROUGH));
    }

    @Override
    public State obfuscated(final @NotNull Style style) {
      return mapState(style.decoration(TextDecoration.OBFUSCATED));
    }

    @Override
    public @Range(from = -1L, to = 16777215L) int color(final @NotNull Style style) {
      final TextColor color = style.color();
      return color == null ? -1 : color.value();
    }

    @Override
    public @Nullable String font(final @NotNull Style style) {
      final Key font = style.font();
      return font == null ? null : font.asString();
    }
  }

  static class ANSIFlattenerListener implements FlattenerListener {
    private final ANSIComponentRenderer<Style> renderer;

    ANSIFlattenerListener(final ANSIComponentRenderer<Style> renderer) {
      this.renderer = renderer;
    }

    @Override
    public void pushStyle(final @NotNull Style style) {
      this.renderer.pushStyle(style);
    }

    @Override
    public void component(final @NotNull String text) {
      this.renderer.text(text);
    }

    @Override
    public void popStyle(final @NotNull Style style) {
      this.renderer.popStyle(style);
    }
  }

  static final class BuilderImpl implements ANSIComponentSerializer.Builder {
    private ColorLevel colorLevel = ColorLevel.compute();
    private ComponentFlattener flattener = ComponentFlattener.basic();

    BuilderImpl() {
      BUILDER.accept(this);
    }

    @Override
    public @NotNull Builder colorLevel(final @NotNull ColorLevel colorLevel) {
      this.colorLevel = colorLevel;
      return this;
    }

    @Override
    public @NotNull Builder flattener(final @NotNull ComponentFlattener componentFlattener) {
      this.flattener = componentFlattener;
      return this;
    }

    @Override
    public @NotNull ANSIComponentSerializer build() {
      return new ANSIComponentSerializerImpl(this.colorLevel, this.flattener);
    }
  }
}
