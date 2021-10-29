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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

/**
 * Represents an animation in which frames are components.
 *
 * <p>Component animation does not create displays, but it can create other animations with component as frames.</p>
 *
 * @since 4.9.2
 */
public interface ComponentAnimation extends Animation<Component, AnimationDisplay<Component>> {

  /**
   * Inverts (reverses the order) of frames of copy of this object.
   *
   * @return the result of described operation
   *
   * @since 4.9.2
   */
  default ComponentAnimation invert() {
    final List<Component> frames = new ArrayList<>(frames());

    Collections.reverse(frames);

    return new ComponentAnimationImpl(frames);
  }

  /**
   * Appends given animation's frames to <b>tail</b> of this instance's copy.
   *
   * @param animation animation to append
   * @return the result of described operation
   *
   * @since 4.9.2
   */
  default ComponentAnimation append(ComponentAnimation animation) {
    final List<Component> frames = new ArrayList<>(frames());

    frames.addAll(animation.frames());

    return new ComponentAnimationImpl(frames);
  }

  /**
   * Appends given number of empty component frames to <b>tail</b> of this instance's copy.
   *
   * @param amount an amount of frames to append
   * @return the result of described operation
   * @see Component#empty()
   *
   * @since 4.9.2
   */
  default ComponentAnimation appendEmpty(int amount) {
    final List<Component> frames = new ArrayList<>(frames());

    for (int i = 0; i < amount; i++) {
      frames.add(Component.empty());
    }

    return new ComponentAnimationImpl(frames);
  }

  /**
   * Appends given number of empty component frames to <b>head</b> of this instance's copy.
   *
   * @param amount an amount of frames to append
   * @return the result of described operation
   * @see Component#empty()
   *
   * @since 4.9.2
   */
  default ComponentAnimation appendEmptyBefore(int amount) {
    final List<Component> frames = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      frames.add(Component.empty());
    }

    frames.addAll(frames());

    return new ComponentAnimationImpl(frames);
  }

  /**
   * Converts copy of this component animation to boss bar animation.
   *
   * @param bossBar a boss bar to animate its name
   * @return resulting action bar animation
   * @see BossBarAnimation#bossbar()
   *
   * @since 4.9.2
   */
  default BossBarAnimation toBossBarAnimation(BossBar bossBar) {
    return new BossBarAnimationImpl(frames(), bossBar, -1, false, false, false);
  }

  /**
   * Converts copy of this component animation to boss bar animation with some essential flags.
   *
   * @param showBarOnStart makes boss bar show on animation beginning
   * @param hideBarOnFinish makes boss bar hidden on animation end
   * @param bossBar a boss bar to animate its name
   * @return resulting action bar animation
   * @see BossBarAnimation#bossbar()
   * @see BossBarAnimation#showBarOnStart()
   * @see BossBarAnimation#hideBarOnFinish()
   *
   * @since 4.9.2
   */
  default BossBarAnimation toBossBarAnimation(BossBar bossBar, boolean showBarOnStart, boolean hideBarOnFinish) {
    return new BossBarAnimationImpl(frames(), bossBar, -1, showBarOnStart, hideBarOnFinish, hideBarOnFinish);
  }

  /**
   * Converts copy of this component animation to boss bar animation with some essential flags.
   *
   * @param showBarOnStart makes boss bar show on animation beginning
   * @param hideBarOnFinish makes boss bar hidden on animation end
   * @param hideBarOnStop makes boss bar invisible when it's force-stopped
   * @param bossBar a boss bar to animate its name
   * @return resulting action bar animation
   * @see BossBarAnimation#bossbar()
   * @see BossBarAnimation#showBarOnStart()
   * @see BossBarAnimation#hideBarOnFinish()
   * @see BossBarAnimation#hideBarOnStop()
   *
   * @since 4.9.2
   */
  default BossBarAnimation toBossBarAnimation(BossBar bossBar, boolean showBarOnStart, boolean hideBarOnFinish, boolean hideBarOnStop) {
    return new BossBarAnimationImpl(frames(), bossBar, -2, showBarOnStart, hideBarOnFinish, hideBarOnStop);
  }

  /**
   * Converts copy of this component animation to action bar animation.
   *
   * @return resulting action bar animation
   *
   * @since 4.9.2
   */
  default ActionBarAnimation toActionBarAnimation() {
    return new ActionBarAnimationImpl(frames(), -1);
  }

}
