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
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

final class PaginationBuilder<T> implements Pagination.Builder<T> {
  private @MonotonicNonNull Component title;

  private int resultsPerPage = Pagination.RESULTS_PER_PAGE;

  private char lineCharacter = Pagination.LINE_CHARACTER;
  private Style lineStyle = Pagination.LINE_STYLE;

  private Pagination.@MonotonicNonNull RowRenderer<T> renderRow;
  private Pagination.InterfaceRenderer renderInterface = Pagination.DEFAULT_INTERFACE_RENDERER;

  private int interfaceWidth = Pagination.INTERFACE_WIDTH;
  private char previousButtonCharacter = Pagination.PREVIOUS_BUTTON_CHARACTER;
  private Style previousButtonStyle = Pagination.PREVIOUS_BUTTON_STYLE;
  private char nextButtonCharacter = Pagination.NEXT_BUTTON_CHARACTER;
  private Style nextButtonStyle = Pagination.NEXT_BUTTON_STYLE;

  private @MonotonicNonNull IntFunction<String> pageCommand;

  @Override
  public Pagination.@NonNull Builder<T> title(final @NonNull Component title) {
    this.title = title;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder<T> resultsPerPage(@NonNegative final int resultsPerPage) {
    this.resultsPerPage = resultsPerPage;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder<T> line(final @NonNull Consumer<CharacterAndStyle> line) {
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
  public Pagination.@NonNull Builder<T> renderRow(final Pagination.@NonNull RowRenderer<T> renderRow) {
    this.renderRow = renderRow;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder<T> renderInterface(final Pagination.@NonNull InterfaceRenderer renderInterface) {
    this.renderInterface = renderInterface;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder<T> interfaceWidth(final int width) {
    this.interfaceWidth = width;
    return this;
  }

  @Override
  public Pagination.@NonNull Builder<T> previousButton(final @NonNull Consumer<CharacterAndStyle> previousButton) {
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
  public Pagination.@NonNull Builder<T> nextButton(final @NonNull Consumer<CharacterAndStyle> nextButton) {
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
  public Pagination.@NonNull Builder<T> pageCommand(final @NonNull IntFunction<String> pageCommand) {
    this.pageCommand = pageCommand;
    return this;
  }

  @Override
  public @NonNull Pagination<T> build() {
    if(this.title == null) throw new IllegalStateException("title not set");
    if(this.renderRow == null) throw new IllegalStateException("render row not set");
    if(this.pageCommand == null) throw new IllegalStateException("page command not set");
    return new PaginationImpl<>(
      this.title,
      this.resultsPerPage,
      this.lineCharacter,
      this.lineStyle,
      this.renderRow,
      this.renderInterface,
      this.interfaceWidth,
      this.previousButtonCharacter,
      this.previousButtonStyle,
      this.nextButtonCharacter,
      this.nextButtonStyle,
      this.pageCommand
    );
  }
}
