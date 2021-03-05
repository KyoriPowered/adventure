/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.renderer;

import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A listener accepting styled information from flattened components.
 *
 * @since 4.7.0
 */
@FunctionalInterface
public interface FlattenerListener {

  /**
   * Begin a region of style in the component.
   *
   * @param style the style to push
   * @since 4.7.0
   */
  default void pushStyle(final @NonNull Style style) {
  }

  /**
   * Accept the plain-text content of a single component.
   *
   * @param text the component text
   * @since 4.7.0
   */
  void component(final @NonNull String text);

  /**
   * Pop a pushed style.
   *
   * <p>The popped style will always be the most recent un-popped style that has been {@link #pushStyle(Style) pushed}.</p>
   *
   * @param style the style popped, as passed to {@link #pushStyle(Style)}
   * @since 4.7.0
   */
  default void popStyle(final @NonNull Style style) {
  }
}
