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
package net.kyori.adventure.animation.container.reel;

import net.kyori.adventure.animation.container.FrameContainer;
import net.kyori.adventure.util.Buildable;

/**
 * The builder of {@link GenericFrameReel}.
 *
 * @param <F> frame type
 * @param <R> reel type
 * @param <B> reel builder type
 *
 * @since 1.10.0
 */
public interface GenericFrameReelBuilder<F, R extends GenericFrameReel<F, R, B>, B extends GenericFrameReelBuilder<F, R, B>> extends Buildable.Builder<R>, FrameContainer<F> {

  /**
   * Appends a frame to this builder.
   *
   * @param frame the frame to append
   * @return this instance
   *
   * @since 1.10.0
   */
  B append(F frame);

  /**
   * Appends the frames of a frame container to this builder.
   *
   * @param frameContainer the frame container to append
   * @return this instance
   *
   * @since 1.10.0
   */
  B append(FrameContainer<F> frameContainer);

  /**
   * Repeats the last appended frame specified number of times. (frame to repeat excluded)
   *
   * @param times the number of last frame repetitions
   * @return this instance
   *
   * @since 1.10.0
   */
  B repeatLastFrame(int times);

  /**
   * Repeats the all frame specified number of times. (frames to repeat excluded)
   *
   * @param times the number of frames repetitions
   * @return this instance
   *
   * @since 1.10.0
   */
  B repeatAllFrames(int times);

}
