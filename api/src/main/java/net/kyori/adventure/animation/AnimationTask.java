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

/**
 * Represent platform scheduler task that arranges in time display of particular animation frames.
 *
 * @since 4.9.2
 */
public interface AnimationTask {

  /**
   * Schedules frames of the display.
   *
   * @param display display whose frames will be scheduled
   * @since 4.9.2
   */
  void schedule(AnimationDisplay<?> display);

  /**
   * Cancels the currently playing animation display.
   *
   * @throws IllegalStateException if no display is actually playing
   * @since 4.9.2
   */
  void cancel() throws IllegalStateException;

  /**
   * Returns the frame interval of currently playing display.
   *
   * @return interval between scheduled frames
   * @since 4.9.2
   */
  Duration frameInterval();

}
