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

import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.display.MonoAnimationDisplay;
import net.kyori.adventure.animation.scheduler.AnimationScheduler;
import net.kyori.adventure.audience.Audience;

/**
 * Special audience for animation plays.
 *
 * @since 1.10.0
 */
public interface AnimationAudience extends Audience {

  /**
   * Plays an animation.
   *
   * @param animation animation to play
   * @param <F> type of frames
   * @return display object to control animation play
   *
   * @since 1.10.0
   */
  default <F> MonoAnimationDisplay<F, Audience> displayAnimation(final Animation<F, Audience> animation) {
    return this.animationScheduler().scheduleAnimation(animation, this);
  }

  /**
   * Finds an animation display of this audience by channel and stops it.
   *
   * @param channel animation channel to identify the display
   *
   * @since 1.10.0
   */
  default void stopAnimation(final AnimationChannel channel) {
    this.animationScheduler().clearChannel(this, channel);
  }

  /**
   * Stops all displays of this audience.
   *
   * @since 1.10.0
   */
  default void stopAllAnimations() {
    this.animationScheduler().clearTarget(this);
  }

  /**
   * Return an animation scheduler of this audience.
   *
   * @return the animation scheduler
   *
   * @since 1.10.0
   */
  AnimationScheduler animationScheduler();

}
