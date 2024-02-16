/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * Common methods between {@link ListBinaryTag} and {@link ListBinaryTag.Builder}.
 *
 * @param <R> the return type
 * @param <T> the element type
 * @since 4.0.0
 */
public interface ListTagSetter<R, T extends BinaryTag> {
  /**
   * Adds a tag.
   *
   * @param tag the tag
   * @return a list tag
   * @since 4.0.0
   */
  @NotNull R add(final T tag);

  /**
   * Adds multiple tags.
   *
   * @param tags the tags
   * @return a list tag
   * @since 4.4.0
   */
  @NotNull R add(final Iterable<? extends T> tags);
}
