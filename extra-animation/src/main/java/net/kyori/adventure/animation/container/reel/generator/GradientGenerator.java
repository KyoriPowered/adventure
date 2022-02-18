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

import java.util.function.Function;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;

/**
 * A reel generator that creates reel by adding new frames created basing on their position in the reel.
 *
 * @param <F> frame type
 * @param <R> reel type
 *
 * @since 1.10.0
 */
public interface GradientGenerator<F, R extends GenericFrameReel<F, R, ?>> extends FrameReelGenerator<R> {

  /**
   * Gets the frame mapper.
   *
   * @return frame mapper
   *
   * @since 1.10.0
   */
  Function<Float, F> frameMapper();

  /**
   * Creates frame reel of frames generated from position gradient.
   *
   * <p>The generator calculates position gradient for each frame.</p>
   *
   * <p>The position gradient is similar to progress, but differs a bit. Every frame has its position gradient value. Last frame has always 0, First has 0 unless it is also last.
   * Any other has value of index / (target size - 1).</p>
   *
   * <p>Gradient values are passed to frame mapper ({@link GradientGenerator#frameMapper()}) and converted to frame values.</p>
   *
   * <p>Next, values are composed to target reel.</p>
   *
   * @param targetSize size of target reel
   * @return target reel
   *
   * @since 1.10.0
   */
  @Override
  R createReel(int targetSize);
}
