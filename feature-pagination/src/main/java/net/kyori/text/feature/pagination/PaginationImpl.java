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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings({"unused", "Duplicates", "ConstantConditions"})
final class PaginationImpl<T> implements Pagination<T> {
  private static final int LINE_CHARACTER_LENGTH = 1;

  private static final int CS_MAX_WIDTH = 55;

  private static final String PAGES_OPEN = "(";
  private static final String PAGES_BETWEEN = "/";
  private static final String PAGES_CLOSE = ")";

  static final EmptyRenderer RENDER_EMPTY = () -> TextComponent.of("No results match.", TextColor.GRAY);
  static final UnknownPageRenderer RENDER_UNKNOWN_PAGE = (page, pages) -> TextComponent.of("Unknown page selected. " + pages + " total pages.", TextColor.GRAY);

  private final Component title;

  private final int resultsPerPage;

  private final char lineCharacter;
  private final Style lineStyle;

  private final RowRenderer<T> renderRow;
  private final EmptyRenderer renderEmpty;
  private final UnknownPageRenderer renderUnknownPage;

  private final char previousButtonCharacter;
  private final Style previousButtonStyle;
  private final char nextButtonCharacter;
  private final Style nextButtonStyle;

  private final IntFunction<String> pageCommand;

  PaginationImpl(final Component title, final int resultsPerPage, final char lineCharacter, final Style lineStyle, final RowRenderer<T> renderRow, final EmptyRenderer renderEmpty, final UnknownPageRenderer renderUnknownPage, final char previousButtonCharacter, final Style previousButtonStyle, final char nextButtonCharacter, final Style nextButtonStyle, final IntFunction<String> pageCommand) {
    this.pageCommand = pageCommand;
    this.title = title;
    this.resultsPerPage = resultsPerPage;
    this.lineCharacter = lineCharacter;
    this.lineStyle = lineStyle;
    this.renderRow = renderRow;
    this.renderEmpty = renderEmpty;
    this.renderUnknownPage = renderUnknownPage;
    this.previousButtonCharacter = previousButtonCharacter;
    this.previousButtonStyle = previousButtonStyle;
    this.nextButtonCharacter = nextButtonCharacter;
    this.nextButtonStyle = nextButtonStyle;
  }

  @Override
  public @NonNull List<? extends Component> render(final @NonNull List<? extends T> results, final int page) {
    final int size = results.size();

    if(size == 0) {
      return Collections.singletonList(this.renderEmpty.renderEmpty());
    }

    final int pages = this.pages(size);

    if(page <= 0 || page > pages) {
      return Collections.singletonList(this.renderUnknownPage.renderUnknownPage(page, pages));
    }

    final List<Component> components = new ArrayList<>();

    components.add(this.renderHeader(page, pages));

    for(int i = this.resultsPerPage * (page - 1); i < this.resultsPerPage * page && i < size; i++) {
      components.add(this.renderRow.renderRow(results.get(i), i));
    }

    components.add(this.renderFooter(page, pages));

    return Collections.unmodifiableList(components);
  }

  private int pages(final int size) {
    int pages = size / this.resultsPerPage + 1;
    if(size % this.resultsPerPage == 0) {
      pages--;
    }
    return pages;
  }

  protected @NonNull Component renderHeader(final int page, final int pages) {
    final Component title = TextComponent.space().append(this.title).append(TextComponent.space());
    final Component dashes = this.line((CS_MAX_WIDTH - (length(title) + 1 + (PAGES_OPEN + page + PAGES_BETWEEN + pages + PAGES_CLOSE).length())) / (LINE_CHARACTER_LENGTH * 2));
    return TextComponent.builder()
      .append(dashes)
      .append(title)
      .append(TextComponent.of(PAGES_OPEN, TextColor.GRAY))
      .append(TextComponent.of(String.valueOf(page), TextColor.WHITE))
      .append(TextComponent.of(PAGES_BETWEEN, TextColor.GRAY))
      .append(TextComponent.of(String.valueOf(pages), TextColor.WHITE))
      .append(TextComponent.of(PAGES_CLOSE, TextColor.GRAY))
      .append(TextComponent.space())
      .append(dashes)
      .build();
  }

  protected @NonNull Component renderFooter(final int page, final int pages) {
    if(page == 1 && page == pages) {
      return this.line(CS_MAX_WIDTH);
    }

    final Component buttons = this.buildFooterButtons(page, pages);

    final int buttonsLength = length(buttons);

    final int eachSizeLength = ((CS_MAX_WIDTH - buttonsLength)) / 2;

    return TextComponent.builder()
      .append(this.line(eachSizeLength))
      .append(buttons)
      .append(this.line(eachSizeLength))
      .build();
  }

  private Component buildFooterButtons(final int page, final int pages) {
    final boolean hasPreviousPage = page > 1 && pages > 1;
    final boolean hasNextPage = (page < pages && page == 1) || ((hasPreviousPage && page > 1) && !(page == pages));

    final TextComponent.Builder buttons = TextComponent.builder();
    if(hasPreviousPage) {
      buttons
        .append(TextComponent.space())
        .append(TextComponent.of("[", TextColor.WHITE))
        .append(
          TextComponent.builder(String.valueOf(this.previousButtonCharacter))
            .style(this.previousButtonStyle)
            .clickEvent(ClickEvent.runCommand(this.pageCommand.apply(page - 1)))
        )
        .append(TextComponent.of("]", TextColor.WHITE))
        .append(TextComponent.space());

      if(hasNextPage) {
        buttons.append(this.line(8));
      }
    }

    if(hasNextPage) {
      buttons
        .append(TextComponent.space())
        .append(TextComponent.of("[", TextColor.WHITE))
        .append(
          TextComponent.builder(String.valueOf(this.nextButtonCharacter))
            .style(this.nextButtonStyle)
            .clickEvent(ClickEvent.runCommand(this.pageCommand.apply(page + 1)))
        )
        .append(TextComponent.of("]", TextColor.WHITE))
        .append(TextComponent.space());
    }

    return buttons.build();
  }

  private @NonNull Component line(final int characters) {
    return TextComponent.of(repeat(this.lineCharacter, characters)).style(this.lineStyle);
  }

  private static @NonNull String repeat(final char character, final int count) {
    return repeat(String.valueOf(character), count);
  }

  private static @NonNull String repeat(final @NonNull String character, final int count) {
    return String.join("", Collections.nCopies(count, character));
  }

  private static int length(final @NonNull Component component) {
    int length = 0;
    if(component instanceof TextComponent) {
      length += ((TextComponent) component).content().length();
    }
    for(final Component child : component.children()) {
      length += length(child);
    }
    return length;
  }
}
