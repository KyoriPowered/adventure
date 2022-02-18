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
 * A frame generator that creates reel by replicating another reel.
 *
 * @param <R> reel type
 *
 * @since 1.10.0
 */
public interface ReelReplicator<R extends GenericFrameReel<?, R, ?>> extends FrameReelGenerator<R> {

  /**
   * Gets the replicated reel.
   *
   * @return replicated reel.
   *
   * @since 1.10.0
   */
  R reel();

  /**
   * Repeats stored reel frames minimal number of times to reel size be equal or greater than target size and
   * shortens it to target size if needed.
   *
   * @param targetSize size of target reel
   * @return created reel
   *
   * @since 1.10.0
   */
  @Override
  R createReel(int targetSize);

}
