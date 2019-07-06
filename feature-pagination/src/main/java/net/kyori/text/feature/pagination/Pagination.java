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
import net.kyori.text.event.ClickEvent;
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
   * The default interface renderer.
   */
  InterfaceRenderer DEFAULT_INTERFACE_RENDERER = new InterfaceRenderer(){};
  /**
   * The default interface width.
   */
  int INTERFACE_WIDTH = 55;
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
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new PaginationBuilder();
  }

  /**
   * Renders.
   *
   * @param content the content to render
   * @param page the page number
   * @return the rendered results
   */
  @NonNull List<? extends Component> render(final @NonNull List<? extends T> content, final int page);

  /**
   * An interface renderer.
   */
  interface InterfaceRenderer {
    /**
     * Renders an empty result.
     *
     * <p>No header or footer are rendered.</p>
     *
     * @return the rendered component
     */
    default @NonNull Component renderEmpty() {
      return TextComponent.of("No results match.", TextColor.GRAY);
    }

    /**
     * Renders an unknown page.
     *
     * <p>No header or footer are rendered.</p>
     *
     * @param page the unknown page
     * @param pages the total number of pages
     * @return the rendered component
     */
    default @NonNull Component renderUnknownPage(final int page, final int pages) {
      return TextComponent.of("Unknown page selected. " + pages + " total pages.", TextColor.GRAY);
    }

    /**
     * Renders a header.
     *
     * @param title the title
     * @param page the page
     * @param pages the total number of pages
     * @return the rendered component
     */
    default @NonNull Component renderHeader(final @NonNull Component title, final int page, final int pages) {
      return TextComponent.builder()
        .append(title)
        .append(TextComponent.of("(", TextColor.GRAY))
        .append(TextComponent.of(page, TextColor.WHITE))
        .append(TextComponent.of("/", TextColor.GRAY))
        .append(TextComponent.of(pages, TextColor.WHITE))
        .append(TextComponent.of(")", TextColor.GRAY))
        .append(TextComponent.space())
        .build();
    }

    /**
     * Renders a previous page button.
     *
     * @param buttonCharacter the button character
     * @param buttonStyle the button style
     * @param clickEvent the click event for the button
     * @return the rendered component
     */
    default @NonNull Component renderPreviousPageButton(final char buttonCharacter, final @NonNull Style buttonStyle, final @NonNull ClickEvent clickEvent) {
      return TextComponent.builder()
        .append(TextComponent.space())
        .append(TextComponent.of("[", TextColor.WHITE))
        .append(
          TextComponent.builder(String.valueOf(buttonCharacter))
            .style(buttonStyle)
            .clickEvent(clickEvent)
        )
        .append(TextComponent.of("]", TextColor.WHITE))
        .append(TextComponent.space())
        .build();
    }

    /**
     * Renders a next page button.
     *
     * @param buttonCharacter the button character
     * @param buttonStyle the button style
     * @param clickEvent the click event for the button
     * @return the rendered component
     */
    default @NonNull Component renderNextPageButton(final char buttonCharacter, final @NonNull Style buttonStyle, final @NonNull ClickEvent clickEvent) {
      return TextComponent.builder()
        .append(TextComponent.space())
        .append(TextComponent.of("[", TextColor.WHITE))
        .append(
          TextComponent.builder(String.valueOf(buttonCharacter))
            .style(buttonStyle)
            .clickEvent(clickEvent)
        )
        .append(TextComponent.of("]", TextColor.WHITE))
        .append(TextComponent.space())
        .build();
    }
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
   * A pagination builder.
   */
  interface Builder {
    /**
     * Sets the title.
     *
     * @param title the title
     * @return this builder
     */
    @NonNull Builder title(final @NonNull Component title);

    /**
     * Sets the number of results per page.
     *
     * @param resultsPerPage the number of results per page
     * @return this builder
     */
    @NonNull Builder resultsPerPage(final @NonNegative int resultsPerPage);

    /**
     * Sets the line character and style.
     *
     * @param line the line consumer
     * @return this builder
     */
    @NonNull Builder line(final @NonNull Consumer<CharacterAndStyle> line);

    /**
     * Sets the interface renderer.
     *
     * @param renderInterface the interface renderer
     * @return this builder
     */
    @NonNull Builder renderInterface(final @NonNull InterfaceRenderer renderInterface);

    /**
     * Sets the interface width.
     *
     * @param width the interface width
     * @return this builder
     */
    @NonNull Builder interfaceWidth(final int width);

    /**
     * Sets the previous button.
     *
     * @param previousButton the button consumer
     * @return this builder
     */
    @NonNull Builder previousButton(final @NonNull Consumer<CharacterAndStyle> previousButton);

    /**
     * Sets the next button.
     *
     * @param nextButton the button consumer
     * @return this builder
     */
    @NonNull Builder nextButton(final @NonNull Consumer<CharacterAndStyle> nextButton);

    /**
     * Sets the next page command.
     *
     * @param pageCommand the next page command
     * @return this builder
     */
    @NonNull Builder pageCommand(final @NonNull IntFunction<String> pageCommand);

    /**
     * Builds.
     *
     * @param renderRow the row renderer
     * @return pagination
     * @throws IllegalStateException if the title has not been set
     * @throws IllegalStateException if the row renderer has not been set
     */
    @NonNull <T> Pagination<T> build(final @NonNull RowRenderer<T> renderRow);

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
