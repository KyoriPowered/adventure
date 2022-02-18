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
package net.kyori.adventure.animation.display;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.scheduler.AnimationTask;

/**
 * Represents a display of animation.
 *
 * @since 1.10.0
 */
public interface AnimationDisplay {

  /**
   * Creates single-animation display.
   *
   * @param target target of display
   * @param animation animation to display
   * @param schedulerTask animation task to schedule display
   * @param <F> frame type
   * @param <T> target type
   * @return created display
   *
   * @since 1.10.0
   */
  static <F, T> MonoAnimationDisplay<F, T> singleAnimation(T target, Animation<F, T> animation, AnimationTask<?> schedulerTask) {
    return new MonoAnimationDisplayImpl<>(target, animation, schedulerTask);
  }

  /**
   * Creates multi-animation display.
   *
   * @param schedulerTask animation task to schedule display
   * @param requests display request array
   * @return created display
   *
   * @since 1.10.0
   */
  static PolyAnimationDisplay multiAnimation(AnimationTask<?> schedulerTask, Collection<DisplayRequest<?, ?>> requests) {
    return new PolyAnimationDisplayImpl(schedulerTask, requests);
  }

  /**
   * Makes the display displays next frame if present.
   *
   * @since 1.10.0
   */
  void displayNextFrame();

  /**
   * Stops the display.
   *
   * @since 1.10.0
   */
  void stop();

  /**
   * Initializes the display if it has not been yet.
   *
   * @since 1.10.0
   */
  void init();

  /**
   * Gets current state of display.
   *
   * @return display state
   *
   * @since 1.10.0
   */
  DisplayState state();

  /**
   * Gets time interval between {@link AnimationDisplay#displayNextFrame()} calls.
   *
   * @return frame interval
   *
   * @since 1.10.0
   */
  Duration frameInterval();

  /**
   * Gets animation target - animation channel map.
   *
   * @return channels map
   *
   * @since 1.10.0
   */
  Map<Object, AnimationChannel> channels();

}
