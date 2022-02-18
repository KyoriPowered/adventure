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
package net.kyori.adventure.animation.container.reel.generator;

import net.kyori.adventure.animation.container.reel.GenericFrameReel;

/**
 * Represents a semi-flexible frame reel generator that converts instance into a frame reel and adjusts it to specified size. (It can shrink but can not grow)
 *
 * @param <R> reel type
 *
 * @since 1.10.0
 */
public interface FrameReelLike<R extends GenericFrameReel<?, R, ?>> extends FrameReelGenerator<R> {

  /**
   * Converts this object to frame reel.
   *
   * @return result of conversion
   *
   * @since 1.10.0
   */
  R asFrameReel();

  /**
   * Converts this object to reel and eventually adjusts it by shortening to target size.
   *
   * @param targetSize size of target reel
   * @return created reel
   * @throws IllegalArgumentException when target size is larger than size of conversion result reel
   *
   * @since 1.10.0
   */
  @Override
  default R createReel(int targetSize) {
    final R reel = this.asFrameReel();

    if (targetSize <= 0)
      return reel.empty();
    else if (reel.size() == targetSize)
      return reel;
    else if (reel.size() > targetSize)
      return reel.subReel(0, targetSize - 1);
    else
      throw new IllegalArgumentException("Can not create a reel of size " + targetSize + " because it is larger than adjusting reel's size (" + reel.size() + ")");
  }

  /**
   * Returns an inflexible representation of this reel like.
   *
   * @return created representation in generator form
   *
   * @since 1.10.0
   */
  default InflexibleFrameReelLike<R> asInflexible() {
    return this::asFrameReel;
  }
}
