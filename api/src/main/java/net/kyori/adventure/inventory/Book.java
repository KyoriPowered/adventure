/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A signed book.
 */
public interface Book {

  /**
   * Creates a book.
   *
   * @param title the title
   * @param author the author
   * @param pages the collection of pages
   * @return a book
   */
  static Book of(final @NonNull Component title, final @NonNull Component author, final @NonNull Collection<Component> pages) {
    return new BookImpl(title, author, new ArrayList<>(pages));
  }

  /**
   * Creates a book.
   *
   * @param title the title
   * @param author the author
   * @param pages an array of pages
   * @return a book
   */
  static Book of(final @NonNull Component title, final @NonNull Component author, final @NonNull Component... pages) {
    return of(title, author, Arrays.asList(pages));
  }

  /**
   * Create a new builder that will create a {@link Book}
   *
   * @return a builder
   */
  static Book.Builder builder() {
    return new BookImpl.Builder();
  }

  /**
   * Gets the title.
   *
   * @return the title
   */
  @NonNull Component title();

  /**
   * Changes the book's title
   *
   * @param title the title
   * @return a new book with modifications
   */
  @NonNull Book title(final @NonNull Component title);

  /**
   * Gets the author.
   *
   * @return the author
   */
  @NonNull Component author();

  /**
   * Changes the book's author
   *
   * @param author the author
   * @return a new book with modifications
   */
  @NonNull Book author(final @NonNull Component author);

  /**
   * Gets the list of pages.
   *
   * The returned collection will be unmodifiable.
   *
   * @return the list of pages
   */
  @NonNull List<Component> pages();

  /**
   * Returns an updated book with the provided pages.
   *
   * @param pages the pages to set
   * @return a new book with modifications
   */
  @NonNull Book pages(final @NonNull List<Component> pages);

  /**
   * Returns an updated book with the provided pages.
   *
   * @param pages the pages to set
   * @return a new book with modifications
   */
  default @NonNull Book pages(final @NonNull Component@NonNull... pages) {
    return this.pages(Arrays.asList(pages));
  }

  /**
   * Create a new builder initialized with the attributes of this book.
   *
   * @return the builder
   */
  default @NonNull Builder toBuilder() {
    return builder()
      .title(this.title())
      .author(this.author())
      .pages(this.pages());
  }

  /**
   * A builder for a {@link Book}
   */
  interface Builder {

    /**
     * Set the title.
     *
     * @param title the title
     * @return this
     */
    @NonNull Builder title(final @NonNull Component title);

    /**
     * Set the author.
     *
     * @param author the author
     * @return this
     */
    @NonNull Builder author(final @NonNull Component author);

    /**
     * Add a page to the book.
     *
     * <p>Each page's length will be limited by the size of the client's book viewer.
     * Any text that does not fit will be truncated clientside.</p>
     *
     * @param page the page
     * @return this
     */
    @NonNull Builder page(final @NonNull Component page);

    /**
     * Add pages to the book.
     *
     * @param pages pages to add
     * @return this
     * @see #page(Component) for details on page values.
     */
    @NonNull Builder pages(final @NonNull Collection<Component> pages);

    /**
     * Add pages to the book.
     *
     * @param pages pages to add
     * @return this
     * @see #page(Component) for details on page values.
     */
    @NonNull Builder pages(final @NonNull Component@NonNull... pages);

    /**
     * Create a new book from this builder
     *
     * @return The new book
     */
    @NonNull Book build();
  }
}
