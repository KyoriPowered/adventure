/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.width;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of the pixel width source which handles serialization with a set of functions
 * for resolving all components except {@link TextComponent}s.
 *
 * @param <CX> a context (player, server, locale)
 * @since 4.10.0
 */

final class PixelWidthSourceImpl<CX> implements PixelWidthSource<CX> {

  private final ComponentFlattener flattener;
  private final Function<CX, CharacterWidthFunction> characterWidthFunction;

  /**
   * Creates a pixel width source with a function used for getting a {@link CharacterWidthFunction}.
   *
   * <p>Any {@link CharacterWidthFunction} returned by the function should accept at least all
   * english alphanumerics and most punctuation and handle {@link TextDecoration#BOLD} in the style.
   * See {@link net.kyori.adventure.text.width.DefaultCharacterWidthFunction#INSTANCE} for an example.</p>
   *
   * @param characterWidthFunction a function that can provide a {@link CharacterWidthFunction} given a context
   * @since 4.10.0
   */
  PixelWidthSourceImpl(final @NotNull ComponentFlattener flattener, final @NotNull Function<@Nullable CX, CharacterWidthFunction> characterWidthFunction) {
    this.flattener = flattener;
    this.characterWidthFunction = characterWidthFunction;
  }

  @Override
  public float width(final @NotNull Component component, final @Nullable CX context) {
    final float[] length = {0};

    this.flattener.flatten(component, new FlattenerListener() {
      final List<Style> styles = new LinkedList<>();
      Style currentStyle = Style.empty();

      @Override
      public void pushStyle(final @NotNull Style style) {
        this.styles.add(style);
        this.calculateStyle();
      }

      @Override
      public void component(final @NotNull String text) {
        length[0] += PixelWidthSourceImpl.this.width(text, this.currentStyle, context);
      }

      @Override
      public void popStyle(final @NotNull Style style) {
        this.styles.remove(this.styles.size() - 1);
        this.calculateStyle();
      }

      private void calculateStyle() {
        final Style.Builder newStyle = Style.style();
        for (final Style style : this.styles) {
          newStyle.merge(style);
        }
        this.currentStyle = newStyle.build();
      }
    });

    return length[0];
  }

  @Override
  public float width(final @NotNull String string, final @NotNull Style style, final @Nullable CX context) {
    return (float) string.codePoints().mapToDouble(codepoint -> this.characterWidthFunction.apply(context).widthOf(codepoint, style)).sum();
  }

  @Override
  public float width(final char c, final @NotNull Style style, final @Nullable CX context) {
    return this.characterWidthFunction.apply(context).widthOf(c, style);
  }

  @Override
  public float width(final int codepoint, final @NotNull Style style, final @Nullable CX context) {
    return this.characterWidthFunction.apply(context).widthOf(codepoint, style);
  }

  @Override
  public @NotNull Builder<CX> toBuilder() {
    return new BuilderImpl<>(this.flattener, this.characterWidthFunction);
  }

  static final class BuilderImpl<CX> implements Builder<CX> {
    private ComponentFlattener flattener;
    private Function<CX, CharacterWidthFunction> characterWidthFunction;

    BuilderImpl() {
      this.flattener = ComponentFlattener.basic();
      this.characterWidthFunction = cx -> DefaultCharacterWidthFunction.INSTANCE;
    }

    BuilderImpl(final @NotNull ComponentFlattener flattener, final @NotNull Function<@Nullable CX, CharacterWidthFunction> characterWidthFunction) {
      this.flattener = flattener;
      this.characterWidthFunction = characterWidthFunction;
    }

    @Override
    public @NotNull Builder<CX> flattener(final @NotNull ComponentFlattener flattener) {
      this.flattener = flattener;
      return this;
    }

    @Override
    public @NotNull Builder<CX> characterWidthFunction(final @NotNull Function<@Nullable CX, CharacterWidthFunction> characterWidthFunction) {
      this.characterWidthFunction = characterWidthFunction;
      return this;
    }

    @Override
    public @NotNull PixelWidthSource<CX> build() {
      return new PixelWidthSourceImpl<>(this.flattener, this.characterWidthFunction);
    }
  }
}
