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
import net.kyori.adventure.util.Buildable;

/**
 * The builder of {@link GenericMonoTypeReelMixer}.
 *
 * @param <R> reel type
 * @param <M> mixer type
 * @param <B> mixer builder type
 *
 * @since 1.10.0
 */
public interface GenericMonoTypeReelMixerBuilder<R extends GenericFrameReel<?, R, ?>, M extends GenericMonoTypeReelMixer<R, M, B>, B extends GenericMonoTypeReelMixerBuilder<R, M, B>> extends Buildable.Builder<M> {

  /**
   * Gets currently stored reels.
   *
   * @return stored reels
   *
   * @since 1.10.0
   */
  List<R> reels();

  /**
   * Appends new reel.
   *
   * @param reel reel to append
   * @return this builder
   *
   * @since 1.10.0
   */
  B append(R reel);

}
