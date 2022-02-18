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
package net.kyori.adventure.animation.displayer;

import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.util.BossBarState;
import net.kyori.adventure.animation.util.SoundInfo;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

/**
 * Something that displays a frame to an audience.
 *
 * @param <F> frame type
 *
 * @since 1.10.0
 */
public interface FrameDisplayer<F, T> {

  /**
   * Action bar displayer.
   *
   * @see Audience#sendActionBar(Component)
   *
   * @since 1.10.0
   */
  FrameDisplayer<Component, Audience> ACTION_BAR = new ActionBarFrameDisplayerImpl();

  /**
   * Boss bar displayer.
   *
   * @see BossBarState#apply(BossBar)
   *
   * @since 1.10.0
   */
  FrameDisplayer<BossBarState, BossBar> BOSS_BAR = new BossBarFrameDisplayerImpl();

  /**
   * Title displayer.
   *
   * @see Audience#showTitle(Title)
   *
   * @since 1.10.0
   */
  FrameDisplayer<Title, Audience> TITLE = new TitleFrameDisplayerImpl();

  /**
   * Sound displayer.
   *
   * @see SoundInfo#apply(Audience)
   *
   * @since 1.10.0
   */
  FrameDisplayer<SoundInfo, Audience> SOUND = new SoundDisplayerImpl();

  /**
   * Displays a frame.
   *
   * @param target an audience to display frame to
   * @param frame the frame to display
   *
   * @since 1.10.0
   */
  void display(T target, F frame);

  /**
   * Returns channel on which frames can be displayed by this displayer.
   *
   * @param target target
   * @return channel
   *
   * @since 1.10.0
   */
  AnimationChannel channel(T target);

}
