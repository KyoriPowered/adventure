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
package net.kyori.adventure.animation.container;

import java.util.List;
import java.util.Objects;
import net.kyori.adventure.animation.supplier.FrameSupplier;

/**
 * A container of animation frames.
 *
 * @param <F> frame type
 * @since 4.10.0
 */
public interface FrameContainer<F> extends FrameSupplier<F> {

  /**
   * Returns an immutable list of contained frames.
   *
   * @return a list of stored frames
   * @since 4.10.0
   */
  List<F> frames();

  /**
   * Returns a number of frames containing.
   *
   * @return size of this container
   * @since 4.10.0
   */
  default int size() {
    return this.frames().size();
  }

  /**
   * Gets a frame on specified index.
   *
   * @param index index of frame
   * @return obtained frame
   * @since 4.10.0
   */
  @Override
  default F frame(long index) {
    if (index < 0 || index >= this.size())
      return null;
    else
      return Objects.requireNonNull(this.frames().get((int) index), "Frame container can not contain null frames. Null frame on index " + index + ".");
  }
}
