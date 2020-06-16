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

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A tab view, aka. the player list.
 */
public interface TabView {
  /**
   * Gets the header.
   *
   * @return the header, or null if not present
   */
  @Nullable Component header();

  /**
   * Sets the header.
   *
   * @param header the header, or null to respect existing header
   * @return the tab view
   */
  @NonNull TabView header(final @Nullable Component header);

  /**
   * Gets the footer.
   *
   * @return the footer, or null of not preesent
   */
  @Nullable Component footer();

  /**
   * Sets the footer.
   *
   * @param footer the footer, or null to respect existing footer
   * @return the tab view
   */
  @NonNull TabView footer(final @Nullable Component footer);

  /**
   * Gets the number of columns.
   *
   * @return the number of columns
   */
  int columns();

  /**
   * Gets the number of rows.
   *
   * @return the number of rows
   */
  int rows();

  /**
   * Gets an entry.
   *
   * @param column the column
   * @param row the row
   * @return the tab view
   * @throws IndexOutOfBoundsException if the column or row are out of range
   */
  @NonNull TabEntry entry(final int column, final int row);

  /**
   * Inserts an entry.
   *
   * @param entry an entry, or null to remove
   * @param column the column
   * @param row the row
   * @return the tab view
   * @throws IndexOutOfBoundsException if the column or row are out of range
   */
  @NonNull TabView entry(final @Nullable TabEntry entry, final int column, final int row);

  // TODO: resizing, clearing
}
