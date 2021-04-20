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
package net.kyori.adventure.text;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * The iterator types.
 *
 * @see Component#iterator(ComponentIteratorType)
 * @see Component#spliterator(ComponentIteratorType)
 * @see Component#iterable(ComponentIteratorType)
 * @since 4.8.0
 */
public enum ComponentIteratorType {
  /**
   * A depth first search.
   *
   * @since 4.8.0
   */
  DEPTH_FIRST,

  /**
   * A breadth first search.
   *
   * @since 4.8.0
   */
  BREADTH_FIRST;

  /**
   * The default iterator type used in the implementation of {@link Iterable} in {@link Component}.
   *
   * @return the default type
   * @see Component#iterator()
   * @see Component#spliterator()
   * @since 4.8.0
   */
  public static @NonNull ComponentIteratorType defaultType() {
    return DEPTH_FIRST;
  }
}
