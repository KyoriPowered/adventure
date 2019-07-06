/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.feature.pagination;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import net.kyori.text.Component;
import net.kyori.text.format.Style;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

final class PaginationBuilder implements Pagination.Builder {
  private int width = Pagination.WIDTH;
  private int resultsPerPage = Pagination.RESULTS_PER_PAGE;

  private char lineCharacter = Pagination.LINE_CHARACTER;
  private Style lineStyle = Pagination.LINE_STYLE;

  private Pagination.Renderer renderer = Pagination.DEFAULT_RENDERER;

  private char previousButtonCharacter = Pagination.PREVIOUS_BUTTON_CHARACTER;
  private Style previousButtonStyle = Pagination.PREVIOUS_BUTTON_STYLE;
  private char nextButtonCharacter = Pagination.NEXT_BUTTON_CHARACTER;
  private Style nextButtonStyle = Pagination.NEXT_BUTTON_STYLE;

  @Override
  public Pagination.@NonNull Builder width(final int width) {
    this.width = width;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder resultsPerPage(@NonNegative final int resultsPerPage) {
    this.resultsPerPage = resultsPerPage;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder renderer(final Pagination.@NonNull Renderer renderer) {
    this.renderer = renderer;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder line(final @NonNull Consumer<CharacterAndStyle> line) {
    line.accept(new CharacterAndStyle() {
      @Override
      public @NonNull CharacterAndStyle character(final char character) {
        PaginationBuilder.this.lineCharacter = character;
        return this;
      }

      @Override
      public @NonNull CharacterAndStyle style(final @NonNull Style style) {
        PaginationBuilder.this.lineStyle = style;
        return this;
      }
    });
    return this;
  }

  @Override
  public Pagination.@NonNull Builder previousButton(final @NonNull Consumer<CharacterAndStyle> previousButton) {
    previousButton.accept(new CharacterAndStyle() {
      @Override
      public @NonNull CharacterAndStyle character(final char character) {
        PaginationBuilder.this.previousButtonCharacter = character;
        return this;
      }

      @Override
      public @NonNull CharacterAndStyle style(final @NonNull Style style) {
        PaginationBuilder.this.previousButtonStyle = style;
        return this;
      }
    });
    return this;
  }

  @Override
  public Pagination.@NonNull Builder nextButton(final @NonNull Consumer<CharacterAndStyle> nextButton) {
    nextButton.accept(new CharacterAndStyle() {
      @Override
      public @NonNull CharacterAndStyle character(final char character) {
        PaginationBuilder.this.nextButtonCharacter = character;
        return this;
      }

      @Override
      public @NonNull CharacterAndStyle style(final @NonNull Style style) {
        PaginationBuilder.this.nextButtonStyle = style;
        return this;
      }
    });
    return this;
  }

  @Override
  public <T> @NonNull Pagination<T> build(final @NonNull Component title, final Pagination.Renderer.@NonNull RowRenderer<T> rowRenderer, final @NonNull IntFunction<String> pageCommand) {
    return new PaginationImpl<>(
      this.width,
      this.resultsPerPage,
      this.renderer,
      this.lineCharacter,
      this.lineStyle,
      this.previousButtonCharacter,
      this.previousButtonStyle,
      this.nextButtonCharacter,
      this.nextButtonStyle,
      title,
      rowRenderer,
      pageCommand
    );
  }
}
