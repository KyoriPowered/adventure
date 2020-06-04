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

import java.util.Set;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A tab list.
 */
public interface TabView extends Set<TabEntry> {
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
   * @throws IndexOutOfBoundsException if the column is less than 0 or greater than 4
   */
  @NonNull TabView columns(final int columns);

  /**
   * Gets an entry at a specific location.
   *
   * @param column the 0-based column
   * @param row the 0-based row
   * @return the tab view
   * @throws IndexOutOfBoundsException if the column or row are out of range
   */
  @NonNull TabEntry get(final int column, final int row);

  /**
   * Inserts an entry at a specific location.
   *
   * @param entry an entry
   * @param column the 0-based column
   * @param row the 0-based row
   * @return the tab view
   * @throws IndexOutOfBoundsException if the column or row are out of range
   */
  @NonNull TabView add(final @NonNull TabEntry entry, final int column, final int row);
}
