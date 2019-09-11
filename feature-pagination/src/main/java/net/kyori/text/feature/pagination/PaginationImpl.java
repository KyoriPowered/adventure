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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.Style;
import net.kyori.text.util.ToStringer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class PaginationImpl<T> implements Pagination<T> {
  private static final int LINE_CHARACTER_LENGTH = 1;

  private final int width;
  private final int resultsPerPage;

  private final Renderer renderer;

  private final char lineCharacter;
  private final Style lineStyle;

  private final char previousPageButtonCharacter;
  private final Style previousPageButtonStyle;
  private final char nextPageButtonCharacter;
  private final Style nextPageButtonStyle;

  private final Component title;
  private final Renderer.RowRenderer<T> rowRenderer;
  private final PageCommandFunction pageCommand;
  private final @Nullable Supplier<@NonNull String> pageLookupCommandSupplier;

  PaginationImpl(final int width, final int resultsPerPage, final @NonNull Renderer renderer, final char lineCharacter, final @NonNull Style lineStyle, final char previousPageButtonCharacter, final @NonNull Style previousPageButtonStyle, final char nextPageButtonCharacter, final @NonNull Style nextPageButtonStyle, final @NonNull Component title, final Renderer.@NonNull RowRenderer<T> rowRenderer, final @NonNull PageCommandFunction pageCommand, final @Nullable Supplier<@NonNull String> pageLookupCommandSupplier) {
    this.width = width;
    this.resultsPerPage = resultsPerPage;
    this.renderer = renderer;
    this.lineCharacter = lineCharacter;
    this.lineStyle = lineStyle;
    this.previousPageButtonCharacter = previousPageButtonCharacter;
    this.previousPageButtonStyle = previousPageButtonStyle;
    this.nextPageButtonCharacter = nextPageButtonCharacter;
    this.nextPageButtonStyle = nextPageButtonStyle;
    this.title = title;
    this.rowRenderer = rowRenderer;
    this.pageCommand = pageCommand;
    this.pageLookupCommandSupplier = pageLookupCommandSupplier;
  }

  @Override
  public @NonNull List<Component> render(final @NonNull Collection<? extends T> content, final int page) {
    if(content.isEmpty()) {
      return Collections.singletonList(this.renderer.renderEmpty());
    }

    final int pages = pages(this.resultsPerPage, content.size());

    if(!pageInRange(page, pages)) {
      return Collections.singletonList(this.renderer.renderUnknownPage(page, pages));
    }

    final List<Component> components = new ArrayList<>();
    components.add(this.renderHeader(page, pages));
    Paginator.forEachPageEntry(content, this.resultsPerPage, page, (value, index) -> {
      components.addAll(this.rowRenderer.renderRow(value, index));
    });
    components.add(this.renderFooter(page, pages));
    return Collections.unmodifiableList(components);
  }

  private Component renderHeader(final int page, final int pages) {
    final Component header = this.renderer.renderHeader(this.title, page, pages, pageLookupCommandSupplier);
    final Component dashes = this.line(header);

    return TextComponent.builder()
      .append(dashes)
      .append(header)
      .append(dashes)
      .build();
  }

  private Component renderFooter(final int page, final int pages) {
    if(page == 1 && page == pages) {
      return this.line(this.width);
    }

    final Component buttons = this.renderFooterButtons(page, pages);
    final Component dashes = this.line(buttons);

    return TextComponent.builder()
      .append(dashes)
      .append(buttons)
      .append(dashes)
      .build();
  }

  private Component renderFooterButtons(final int page, final int pages) {
    final boolean hasPreviousPage = page > 1 && pages > 1;
    final boolean hasNextPage = (page < pages && page == 1) || ((hasPreviousPage && page > 1) && page != pages);

    final TextComponent.Builder buttons = TextComponent.builder();
    if(hasPreviousPage) {
      buttons.append(this.renderer.renderPreviousPageButton(this.previousPageButtonCharacter, this.previousPageButtonStyle, ClickEvent.runCommand(this.pageCommand.pageCommand(page - 1))));

      if(hasNextPage) {
        buttons.append(this.line(8));
      }
    }

    if(hasNextPage) {
      buttons.append(this.renderer.renderNextPageButton(this.nextPageButtonCharacter, this.nextPageButtonStyle, ClickEvent.runCommand(this.pageCommand.pageCommand(page + 1))));
    }

    return buttons.build();
  }

  private @NonNull Component line(final @NonNull Component component) {
    return this.line((this.width - length(component)) / (LINE_CHARACTER_LENGTH * 2));
  }

  private @NonNull Component line(final int characters) {
    return TextComponent.of(repeat(String.valueOf(this.lineCharacter), characters), this.lineStyle);
  }

  static int length(final @NonNull Component component) {
    int length = 0;
    if(component instanceof TextComponent) {
      length += ((TextComponent) component).content().length();
    }
    for(final Component child : component.children()) {
      length += length(child);
    }
    return length;
  }

  static @NonNull String repeat(final @NonNull String character, final int count) {
    return String.join("", Collections.nCopies(count, character));
  }

  static int pages(final int pageSize, final int count) {
    final int pages = count / pageSize + 1;
    if(count % pageSize == 0) {
      return pages - 1;
    }
    return pages;
  }

  static boolean pageInRange(final int page, final int pages) {
    return page > 0 && page <= pages;
  }

  @Override
  public String toString() {
    final Map<String, Object> builder = new LinkedHashMap<>();
    builder.put("width", this.width);
    builder.put("resultsPerPage", this.resultsPerPage);
    builder.put("renderer", this.renderer);
    builder.put("lineCharacter", this.lineCharacter);
    builder.put("lineStyle", this.lineStyle);
    builder.put("previousPageButtonCharacter", this.previousPageButtonCharacter);
    builder.put("previousPageButtonStyle", this.previousPageButtonStyle);
    builder.put("nextPageButtonCharacter", this.nextPageButtonCharacter);
    builder.put("nextPageButtonStyle", this.nextPageButtonStyle);
    builder.put("title", this.title);
    builder.put("rowRenderer", this.rowRenderer);
    builder.put("pageCommand", this.pageCommand);
    builder.put("pageLookupCommandSupplier", String.valueOf(this.pageLookupCommandSupplier));
    return ToStringer.toString(this, builder);
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final PaginationImpl<?> that = (PaginationImpl<?>) other;
    if(this.width != that.width) return false;
    if(this.resultsPerPage != that.resultsPerPage) return false;
    if(this.lineCharacter != that.lineCharacter) return false;
    if(this.previousPageButtonCharacter != that.previousPageButtonCharacter) return false;
    if(this.nextPageButtonCharacter != that.nextPageButtonCharacter) return false;
    if(!this.renderer.equals(that.renderer)) return false;
    if(!this.lineStyle.equals(that.lineStyle)) return false;
    if(!this.previousPageButtonStyle.equals(that.previousPageButtonStyle)) return false;
    if(!this.nextPageButtonStyle.equals(that.nextPageButtonStyle)) return false;
    if(!this.title.equals(that.title)) return false;
    if(!this.rowRenderer.equals(that.rowRenderer)) return false;
    if(!Objects.equals(this.pageLookupCommandSupplier, that.pageLookupCommandSupplier)) return false;
    return this.pageCommand.equals(that.pageCommand);
  }

  @Override
  public int hashCode() {
    int result = this.width;
    result = 31 * result + this.resultsPerPage;
    result = 31 * result + this.renderer.hashCode();
    result = 31 * result + (int) this.lineCharacter;
    result = 31 * result + this.lineStyle.hashCode();
    result = 31 * result + (int) this.previousPageButtonCharacter;
    result = 31 * result + this.previousPageButtonStyle.hashCode();
    result = 31 * result + (int) this.nextPageButtonCharacter;
    result = 31 * result + this.nextPageButtonStyle.hashCode();
    result = 31 * result + this.title.hashCode();
    result = 31 * result + this.rowRenderer.hashCode();
    result = 31 * result + this.pageCommand.hashCode();
    result = 31 * result + Objects.hashCode(this.pageLookupCommandSupplier);
    return result;
  }
}
