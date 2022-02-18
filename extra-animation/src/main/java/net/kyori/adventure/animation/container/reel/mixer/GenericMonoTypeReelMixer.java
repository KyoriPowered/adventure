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
package net.kyori.adventure.animation.container.reel.mixer;

import java.util.List;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.ReelTransformer;
import net.kyori.adventure.util.Buildable;

/**
 * A reel mixer with only one type of reel prepared for extension.
 *
 * @param <R> reel type
 * @param <M> mixer type
 * @param <B> mixer builder type
 *
 * @since 1.10.0
 */
public interface GenericMonoTypeReelMixer<R extends GenericFrameReel<?, R, ?>, M extends GenericMonoTypeReelMixer<R, M, B>, B extends GenericMonoTypeReelMixerBuilder<R, M, B>> extends Buildable<M, B>, ReelMixer {

  /**
   * Gets contained reels.
   *
   * @return contained reels
   *
   * @since 1.10.0
   */
  List<R> reels();

  /**
   * Gets contained reel on some index.
   *
   * @param index reel index
   * @return reel on index or null
   *
   * @since 1.10.0
   */
  R reel(int index);

  /**
   * Sets the reel on some index.
   *
   * @param reel reel to set
   * @param index reel index
   * @return this instance
   *
   * @since 1.10.0
   */
  M reel(R reel, int index);

  /**
   * Transforms reel on some index.
   *
   * @param index reel index
   * @param transformer transformer
   * @return this instance
   *
   * @since 1.10.0
   */
  M transformReel(int index, ReelTransformer<R> transformer);

  /**
   * Appends another reel.
   *
   * @param reel reel to append
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  M append(R reel);

}
