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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Pagination.
 *
 * @param <T> the content type
 */
public interface Pagination<T> {
  /**
   * The default number of results per page.
   */
  int RESULTS_PER_PAGE = 6;
  /**
   * The default line character.
   */
  char LINE_CHARACTER = '-';
  /**
   * The default line style.
   */
  Style LINE_STYLE = Style.of(TextColor.DARK_GRAY);
  /**
   * The default character for the previous page button.
   */
  char PREVIOUS_BUTTON_CHARACTER = '\u00AB'; // «
  /**
   * The default style for the next page button.
   */
  Style PREVIOUS_BUTTON_STYLE = Style.builder()
    .color(TextColor.RED)
    .hoverEvent(HoverEvent.showText(TextComponent.of("Previous Page", TextColor.RED)))
    .build();
  /**
   * The default character for the next page button.
   */
  char NEXT_BUTTON_CHARACTER = '\u00BB'; // »
  /**
   * The default style for the next page button.
   */
  Style NEXT_BUTTON_STYLE = Style.builder()
    .color(TextColor.GREEN)
    .hoverEvent(HoverEvent.showText(TextComponent.of("Next Page", TextColor.GREEN)))
    .build();

  /**
   * Creates a pagination builder.
   *
   * @param <T> the content type
   * @return a builder
   */
  static <T> @NonNull Builder<T> builder() {
    return new PaginationBuilder<>();
  }

  /**
   * Renders.
   *
   * @param results the content to render
   * @param page the page number
   * @return the rendered results
   */
  @NonNull List<? extends Component> render(final @NonNull List<? extends T> results, final int page);

  /**
   * An empty result renderer.
   *
   * <p>No header or footer are rendered.</p>
   */
  @FunctionalInterface
  interface EmptyRenderer {
    /**
     * Renders an empty result.
     *
     * @return the rendered component
     */
    @NonNull Component renderEmpty();
  }

  /**
   * A row renderer.
   *
   * @param <T> the content type
   */
  @FunctionalInterface
  interface RowRenderer<T> {
    /**
     * Renders a row.
     *
     * @param value the value
     * @param index the index
     * @return the rendered row
     */
    @NonNull Component renderRow(final @Nullable T value, final int index);
  }

  /**
   * An unknown page renderer.
   *
   * <p>No header or footer are rendered.</p>
   */
  @FunctionalInterface
  interface UnknownPageRenderer {
    /**
     * Renders an unknown page.
     *
     * @param page the unknown page
     * @param pages the total number of pages
     * @return the rendered component
     */
    @NonNull Component renderUnknownPage(final int page, final int pages);
  }

  /**
   * A pagination builder.
   *
   * @param <T> the content type
   */
  interface Builder<T> {
    /**
     * Sets the title.
     *
     * @param title the title
     * @return this builder
     */
    @NonNull Builder<T> title(final @NonNull Component title);

    /**
     * Sets the number of results per page.
     *
     * @param resultsPerPage the number of results per page
     * @return this builder
     */
    @NonNull Builder<T> resultsPerPage(final @NonNegative int resultsPerPage);

    /**
     * Sets the line character and style.
     *
     * @param line the line consumer
     * @return this builder
     */
    @NonNull Builder<T> line(final @NonNull Consumer<CharacterAndStyle> line);

    /**
     * Sets the row renderer.
     *
     * @param renderRow the row renderer
     * @return this builder
     */
    @NonNull Builder<T> renderRow(final @NonNull RowRenderer<T> renderRow);

    /**
     * Sets the empty result.
     *
     * @param renderEmpty the empty result
     * @return this builder
     */
    default @NonNull Builder<T> renderEmpty(final @NonNull Component renderEmpty) {
      return this.renderEmpty(() -> renderEmpty);
    }

    /**
     * Sets the empty result renderer.
     *
     * @param renderEmpty the empty result renderer
     * @return this builder
     */
    @NonNull Builder<T> renderEmpty(final @NonNull EmptyRenderer renderEmpty);

    /**
     * Sets the unknown page renderer.
     *
     * @param renderUnknownPage the unknown page renderer
     * @return this builder
     */
    @NonNull Builder<T> renderUnknownPage(final @NonNull UnknownPageRenderer renderUnknownPage);

    /**
     * Sets the previous button.
     *
     * @param previousButton the button consumer
     * @return this builder
     */
    @NonNull Builder<T> previousButton(final @NonNull Consumer<CharacterAndStyle> previousButton);

    /**
     * Sets the next button.
     *
     * @param nextButton the button consumer
     * @return this builder
     */
    @NonNull Builder<T> nextButton(final @NonNull Consumer<CharacterAndStyle> nextButton);

    /**
     * Sets the next page command.
     *
     * @param pageCommand the next page command
     * @return this builder
     */
    @NonNull Builder<T> pageCommand(final @NonNull IntFunction<String> pageCommand);

    /**
     * Builds.
     *
     * @return pagination
     * @throws IllegalStateException if the title has not been set
     * @throws IllegalStateException if the row renderer has not been set
     */
    @NonNull Pagination<T> build();

    /**
     * A builder for a character and style pair.
     */
    interface CharacterAndStyle {
      /**
       * Sets the character.
       *
       * @param character the character
       * @return this builder
       */
      @NonNull CharacterAndStyle character(final char character);

      /**
       * Sets the style.
       *
       * @param style the style
       * @return this builder
       */
      @NonNull CharacterAndStyle style(final @NonNull Style style);
    }
  }
}
