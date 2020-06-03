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
package net.kyori.adventure.tab;

import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A tab list header and footer.
 */
public interface TabView {
  /**
   * Gets the header.
   *
   * @return the header
   */
  @NonNull Component header();

  /**
   * Sets the header.
   *
   * @param header the header
   * @return the tab view
   */
  @NonNull TabView header(final @NonNull Component header);

  /**
   * Gets the footer.
   *
   * @return the footer
   */
  @NonNull Component footer();

  /**
   * Sets the footer.
   *
   * @param footer the footer
   * @return the tab view
   */
  @NonNull TabView footer(final @NonNull Component footer);

  /**
   * Gets the number of columns.
   *
   * @return the number of columns
   */
  int columns();

  /**
   * Sets the number of columns.
   *
   * @param columns the number of columns
   * @return the tab view
   */
  @NonNull TabView columns(final int columns);

  /**
   * Gets the tab entry list.
   *
   * @return the tab entries
   */
  @NonNull List<TabEntry> entries();

  /**
   * Sets the tab entry list.
   *
   * @param entries the tab entries, traversal order matters
   * @return the tab entries
   */
  @NonNull TabView entries(final @NonNull Iterable<TabEntry> entries);

  /**
   * Adds a tab entry.
   *
   * @param entry the tab entry
   * @return the tab view
   */
  @NonNull TabView entryAdd(final @NonNull TabEntry entry);

  /**
   * Removes a tab entry.
   *
   * @param entry the tab entry
   * @return the tab list
   */
  @NonNull TabView entryRemove(final @NonNull TabEntry entry);

  /**
   * Gets the comparator that sorts tab entries.
   *
   * @return the tab entry comparator
   */
  @NonNull Comparator<TabEntry> comparator();

  /**
   * Sets the comparator that sorts tab entries.
   *
   * @param comparator a comparator, or null for no sorting
   * @return the tab view
   */
  @NonNull TabView comparator(final @Nullable Comparator<TabEntry> comparator);
}
