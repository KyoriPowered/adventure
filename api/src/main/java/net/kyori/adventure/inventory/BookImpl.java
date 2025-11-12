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
package net.kyori.adventure.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;

import static java.util.Objects.requireNonNull;

record BookImpl(Component title, Component author, List<Component> pages) implements Book {
  BookImpl(final Component title, final Component author, final List<Component> pages) {
    this.title = requireNonNull(title, "title");
    this.author = requireNonNull(author, "author");
    this.pages = List.copyOf(requireNonNull(pages, "pages"));
  }

  @Override
  public Component title() {
    return this.title;
  }

  @Override
  public Book title(final Component title) {
    return new BookImpl(requireNonNull(title, "title"), this.author, this.pages);
  }

  @Override
  public Component author() {
    return this.author;
  }

  @Override
  public Book author(final Component author) {
    return new BookImpl(this.title, requireNonNull(author, "author"), this.pages);
  }

  @Override
  public List<Component> pages() {
    return this.pages;
  }

  @Override
  public Book pages(final List<Component> pages) {
    return new BookImpl(this.title, this.author, new ArrayList<>(requireNonNull(pages, "pages")));
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl()
      .title(this.title)
      .author(this.author)
      .pages(this.pages);
  }

  @Override
  public Book asBook() {
    return this;
  }

  static final class BuilderImpl implements Builder {
    private Component title = Component.empty();
    private Component author = Component.empty();
    private final List<Component> pages = new ArrayList<>();

    @Override
    public Builder title(final Component title) {
      this.title = requireNonNull(title, "title");
      return this;
    }

    @Override
    public Builder author(final Component author) {
      this.author = requireNonNull(author, "author");
      return this;
    }

    @Override
    public Builder addPage(final Component page) {
      this.pages.add(requireNonNull(page, "page"));
      return this;
    }

    @Override
    public Builder pages(final Collection<Component> pages) {
      this.pages.addAll(requireNonNull(pages, "pages"));
      return this;
    }

    @Override
    public Builder pages(final Component ... pages) {
      Collections.addAll(this.pages, pages);
      return this;
    }

    @Override
    public Book build() {
      return new BookImpl(this.title, this.author, List.copyOf(this.pages));
    }
  }
}
