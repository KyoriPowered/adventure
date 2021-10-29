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

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

/**
 * An animation of a boss bar name.
 *
 * @since 4.9.2
 */
public interface BossBarAnimation extends LoopableAnimation<Component, BossBarAnimation.Display, BossBarAnimation> {

  /**
   * A display of a boss bar animation. Every frame changes name of a boss bar
   * but does not create new boss bar instances. Every display has its own boss bar.
   * One display can not have multiple bars.
   *
   * @since 4.9.2
   */
  interface Display extends AnimationDisplay<Component> {

    /**
     * Returns the animation which this display should play.
     *
     * @return played animation
     * @since 4.9.2
     */
    BossBarAnimation animation();

  }

  /**
   * Returns the template boss bar - an original of every bar of displays coming from this animation.
   *
   * @return template boss bar of this animation
   * @since 4.9.2
   */
  BossBar bossbar();

  /**
   * A flag determining whether bar should be hidden on a natural end of an animation or not.
   *
   * @return true - if bar hides after finish, false - in other cases.
   * @since 4.9.2
   */
  boolean hideBarOnFinish();

  /**
   * A flag responsible for showing boss bar on the start of the animation.
   *
   * @return true - if bar shows on start, else - false.
   * @since 4.9.2
   */
  boolean showBarOnStart();

  /**
   * A flag which hides bar after forced finish (by the @link{AnimationDisplay#stop()} method).
   *
   * @return positive response if bar hides on forced stop or negative otherwise.
   * @since 4.9.2
   */
  boolean hideBarOnStop();

  /**
   * Transforms this animation to component form.
   *
   * @return the effect of transformation
   * @since 4.9.2
   */
  default ComponentAnimation toComponentAnimation() {
    return new ComponentAnimationImpl(frames());
  }

}
