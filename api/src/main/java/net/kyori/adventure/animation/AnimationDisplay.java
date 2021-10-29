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
package net.kyori.adventure.animation;

import java.time.Duration;
import net.kyori.adventure.audience.Audience;

/**
 * A display of animation frames. Provides sending frames to audience.
 *
 * @param <F> type of media used as frames
 *
 * @since 4.9.2
 */
public interface AnimationDisplay<F> {

  /**
   * Returns true if next frame of animation is available.
   *
   * @return true if next frame is present otherwise false.
   *
   * @since 4.9.2
   */
  boolean hasNextFrame();

  /**
   * Return a next frame if it's  presents, else throws @link{java.util.NoSuchElementException}.
   *
   * @return next frame
   * @throws java.util.NoSuchElementException when there is no next frame
   *
   * @since 4.9.2
   */
  F nextFrame();

  /**
   * Gets and displays next frame by display method dependent on animation type.
   *
   * @see AnimationDisplay#nextFrame()
   *
   * @since 4.9.2
   */
  void displayNextFrame();

  /**
   * Returns the audience which watches this display.
   *
   * @return audience watching display
   *
   * @since 4.9.2
   */
  Audience audience();

  /**
   * Return the interval between frames.
   *
   * @return frame interval
   * @since 4.9.2
   */
  Duration frameInterval();

  /**
   * Stops this display.
   *
   * @since 4.9.2
   */
  void stop();

}
