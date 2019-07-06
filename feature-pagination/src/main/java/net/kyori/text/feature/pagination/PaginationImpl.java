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
import java.util.Objects;
import java.util.function.IntFunction;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

final class PaginationImpl<T> implements Pagination<T> {
  private static final int LINE_CHARACTER_LENGTH = 1;

  private final Component title;

  private final int resultsPerPage;

  private final char lineCharacter;
  private final Style lineStyle;

  private final RowRenderer<T> renderRow;
  private final InterfaceRenderer renderInterface;

  private final int interfaceWidth;
  private final char previousButtonCharacter;
  private final Style previousButtonStyle;
  private final char nextButtonCharacter;
  private final Style nextButtonStyle;

  private final IntFunction<String> pageCommand;

  PaginationImpl(final @NonNull Component title, final int resultsPerPage, final char lineCharacter, final @NonNull Style lineStyle, final @NonNull RowRenderer<T> renderRow, final @NonNull InterfaceRenderer renderInterface, final int interfaceWidth, final char previousButtonCharacter, final @NonNull Style previousButtonStyle, final char nextButtonCharacter, final @NonNull Style nextButtonStyle, final @NonNull IntFunction<String> pageCommand) {
    this.title = Objects.requireNonNull(title, "title");
    this.resultsPerPage = resultsPerPage;
    this.lineCharacter = lineCharacter;
    this.lineStyle = Objects.requireNonNull(lineStyle, "lineStyle");
    this.renderRow = Objects.requireNonNull(renderRow, "renderRow");
    this.renderInterface = Objects.requireNonNull(renderInterface, "renderInterface");
    this.interfaceWidth = interfaceWidth;
    this.previousButtonCharacter = previousButtonCharacter;
    this.previousButtonStyle = Objects.requireNonNull(previousButtonStyle, "previousButtonStyle");
    this.nextButtonCharacter = nextButtonCharacter;
    this.nextButtonStyle = Objects.requireNonNull(nextButtonStyle, "nextButtonStyle");
    this.pageCommand = Objects.requireNonNull(pageCommand, "pageCommand");
  }

  @Override
  public @NonNull List<? extends Component> render(final @NonNull List<? extends T> content, final int page) {
    final int size = content.size();

    if(size == 0) {
      return Collections.singletonList(this.renderInterface.renderEmpty());
    }

    final int pages = this.pages(size);

    if(page <= 0 || page > pages) {
      return Collections.singletonList(this.renderInterface.renderUnknownPage(page, pages));
    }

    final List<Component> components = new ArrayList<>();

    components.add(this.renderHeader(page, pages));
    for(int i = this.resultsPerPage * (page - 1); i < this.resultsPerPage * page && i < size; i++) {
      components.add(this.renderRow.renderRow(content.get(i), i));
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

  private Component renderHeader(final int page, final int pages) {
    final Component title = TextComponent.space().append(this.title).append(TextComponent.space());
    final Component header = this.renderInterface.renderHeader(title, page, pages);
    final Component dashes = this.line((this.interfaceWidth - length(header)) / (LINE_CHARACTER_LENGTH * 2));

    return TextComponent.builder()
      .append(dashes)
      .append(header)
      .append(dashes)
      .build();
  }

  private Component renderFooter(final int page, final int pages) {
    if(page == 1 && page == pages) {
      return this.line(this.interfaceWidth);
    }

    final Component buttons = this.buildFooterButtons(page, pages);
    final Component dashes = this.line(((this.interfaceWidth - length(buttons))) / (LINE_CHARACTER_LENGTH * 2));

    return TextComponent.builder()
      .append(dashes)
      .append(buttons)
      .append(dashes)
      .build();
  }

  private Component buildFooterButtons(final int page, final int pages) {
    final boolean hasPreviousPage = page > 1 && pages > 1;
    final boolean hasNextPage = (page < pages && page == 1) || ((hasPreviousPage && page > 1) && !(page == pages));

    final TextComponent.Builder buttons = TextComponent.builder();
    if(hasPreviousPage) {
      final ClickEvent clickEvent = ClickEvent.runCommand(this.pageCommand.apply(page - 1));
      buttons.append(this.renderInterface.renderPreviousPageButton(this.previousButtonCharacter, this.previousButtonStyle, clickEvent));

      if(hasNextPage) {
        buttons.append(this.line(8));
      }
    }

    if(hasNextPage) {
      final ClickEvent clickEvent = ClickEvent.runCommand(this.pageCommand.apply(page + 1));
      buttons.append(this.renderInterface.renderNextPageButton(this.nextButtonCharacter, this.nextButtonStyle, clickEvent));
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
