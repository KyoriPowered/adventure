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
package net.kyori.adventure.animation.scheduler;

import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.display.DisplayRequest;
import net.kyori.adventure.animation.display.MonoAnimationDisplay;
import net.kyori.adventure.animation.display.PolyAnimationDisplay;

/**
 * An object that manages animation scheduling.
 *
 * @since 1.10.0
 */
public interface AnimationScheduler {

  /**
   * Schedules single animation.
   *
   * @param animation animation
   * @param target animation target
   * @param <F> frame type
   * @param <T> target type
   * @return scheduled display
   *
   * @since 1.10.0
   */
  <F, T> MonoAnimationDisplay<F, T> scheduleAnimation(Animation<F, T> animation, T target);

  /**
   * Schedules multiple animations.
   *
   * @param requests display request
   * @return scheduled display
   *
   * @since 1.10.0
   */
  PolyAnimationDisplay scheduleAnimations(DisplayRequest<?, ?>... requests);

  /**
   * Clears entire channel on target.
   *
   * @param target target
   * @param channel channel to clear
   *
   * @since 1.10.0
   */
  void clearChannel(Object target, AnimationChannel channel);

  /**
   * Clears target from animations.
   *
   * @param target target to clear
   *
   * @since 1.10.0
   */
  void clearTarget(Object target);

  /**
   * Shutdowns the scheduler.
   *
   * @since 1.10.0
   */
  void shutdown();

}
