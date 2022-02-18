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
package net.kyori.adventure.animation.supplier;

import java.util.function.Function;

/**
 * Represents the source of animation frames.
 *
 * @param <F> the frame type
 *
 * @since 1.10.0
 */
public interface FrameSupplier<F> {

  /**
   * Returns frame on index.
   *
   * @param index index of frame
   * @return frame on given index or null if there isn't one
   *
   * @since 1.10.0
   */
  F frame(long index);

  /**
   * Creates another frame supplier but with mapped indexes.
   *
   * @param mapper the index mapper
   * @return frame supplier with mapped indexes
   *
   * @since 1.10.0
   */
  default FrameSupplier<F> mapIndexes(Function<Long, Long> mapper) {
    return index -> this.frame(mapper.apply(index));
  }

}
