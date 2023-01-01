/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Represents the in-game interface of a book.
 *
 *
 * <p>Components exceeding the text limit for a page will be truncated client-side
 * and not moved automatically to the next page.</p>
 *
 * @see Audience#openBook(Book)
 * @since 4.0.0
 */
@ApiStatus.NonExtendable
public interface Book extends Buildable<Book, Book.Builder>, Examinable {
  /**
   * Creates a book.
   *
   * @param title the title
   * @param author the author
   * @param pages the collection of pages
   * @return a book
   * @since 4.0.0
   */
  static @NotNull Book book(final @NotNull Component title, final @NotNull Component author, final @NotNull Collection<Component> pages) {
    return new BookImpl(title, author, new ArrayList<>(pages));
  }

  /**
   * Creates a book.
   *
   * @param title the title
   * @param author the author
   * @param pages an array of pages
   * @return a book
   * @since 4.0.0
   */
  static @NotNull Book book(final @NotNull Component title, final @NotNull Component author, final @NotNull Component@NotNull... pages) {
    return book(title, author, Arrays.asList(pages));
  }

  /**
   * Create a new builder that will create a {@link Book}.
   *
   * @return a builder
   * @since 4.0.0
   */
  static @NotNull Builder builder() {
    return new BookImpl.BuilderImpl();
  }

  /**
   * Gets the title.
   *
   * @return the title
   * @since 4.0.0
   */
  @NotNull Component title();

  /**
   * Changes the book's title.
   *
   * @param title the title
   * @return a new book with modifications
   * @since 4.0.0
   */
  @Contract(value = "_ -> new", pure = true)
  @NotNull Book title(final @NotNull Component title);

  /**
   * Gets the author.
   *
   * @return the author
   * @since 4.0.0
   */
  @NotNull Component author();

  /**
   * Changes the book's author.
   *
   * @param author the author
   * @return a new book with modifications
   * @since 4.0.0
   */
  @Contract(value = "_ -> new", pure = true)
  @NotNull Book author(final @NotNull Component author);

  /**
   * Gets the list of pages.
   *
   * <p>The returned collection will be unmodifiable.</p>
   *
   * @return the list of pages
   * @since 4.0.0
   */
  @Unmodifiable @NotNull List<Component> pages();

  /**
   * Returns an updated book with the provided pages.
   *
   * @param pages the pages to set
   * @return a new book with modifications
   * @since 4.0.0
   */
  @Contract(value = "_ -> new", pure = true)
  default @NotNull Book pages(final @NotNull Component@NotNull... pages) {
    return this.pages(Arrays.asList(pages));
  }

  /**
   * Returns an updated book with the provided pages.
   *
   * @param pages the pages to set
   * @return a new book with modifications
   * @since 4.0.0
   */
  @Contract(value = "_ -> new", pure = true)
  @NotNull Book pages(final @NotNull List<Component> pages);

  /**
   * Create a new builder initialized with the attributes of this book.
   *
   * @return the builder
   */
  @Override
  default @NotNull Builder toBuilder() {
    return builder()
      .title(this.title())
      .author(this.author())
      .pages(this.pages());
  }

  /**
   * A builder for a {@link Book}.
   *
   * @since 4.0.0
   */
  interface Builder extends AbstractBuilder<Book>, Buildable.Builder<Book> {
    /**
     * Set the title.
     *
     * @param title the title
     * @return this
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder title(final @NotNull Component title);

    /**
     * Set the author.
     *
     * @param author the author
     * @return this
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder author(final @NotNull Component author);

    /**
     * Add a page to the book.
     *
     * <p>Each page's length will be limited by the size of the client's book viewer.
     * Any text that does not fit will be truncated clientside.</p>
     *
     * @param page the page
     * @return this
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder addPage(final @NotNull Component page);

    /**
     * Add pages to the book.
     *
     * @param pages pages to add
     * @return this
     * @see #addPage(Component) for details on page values
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder pages(final @NotNull Component@NotNull... pages);

    /**
     * Add pages to the book.
     *
     * @param pages pages to add
     * @return this
     * @see #addPage(Component) for details on page values
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder pages(final @NotNull Collection<Component> pages);

    /**
     * Builds.
     *
     * @return a new book
     */
    @Override
    @NotNull Book build();
  }
}
