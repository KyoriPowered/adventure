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
 * A reel generator that can only produce reel of one size.
 *
 * @param <R> reel type
 *
 * @since 1.10.0
 */
public interface InflexibleFrameReelLike<R extends GenericFrameReel<?, R, ?>> extends FrameReelGenerator<R> {

  /**
   * Converts this object to frame reel.
   *
   * @return frame reel
   *
   * @since 1.10.0
   */
  R createFrameReel();

  /**
   * If target size matches with frame reel size ({@link InflexibleFrameReelLike#createFrameReel()}) returns it. Else throws an exception.
   *
   * @param targetSize size of target reel
   * @return created reel
   * @throws IllegalArgumentException when size do not match size of return value of {@link InflexibleFrameReelLike#createFrameReel()}
   *
   * @since 1.10.0
   */
  @Override
  default R createReel(int targetSize) {
    final R reel = this.createFrameReel();

    if (reel.size() == targetSize)
      return reel;
    else
      throw new IllegalArgumentException("This (inflexible) generator can not create reel with size " + targetSize + " because it is different from allowed size " + reel.size());
  }

}
