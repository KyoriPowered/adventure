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
package net.kyori.adventure.animation.channel;

import net.kyori.adventure.bossbar.BossBar;

/**
 * Represents a channel where one animation may be played for one target. E.g. a boss bar or the title for player Steve.
 *
 * <p>Used to prevent double-animating.</p>
 *
 * @since 1.10.0
 */
public class AnimationChannel {

  /**
   * Title Channel.
   *
   * @since 1.10.0
   */
  public static final AnimationChannel TITLE = new AnimationChannel("title");

  /**
   * Actionbar channel.
   *
   * @since 1.10.0
   */
  public static final AnimationChannel ACTION_BAR = new AnimationChannel("action_bar");

  /**
   * Creates boss bar channel for specific boss bar.
   *
   * @param bossBar bossbar
   * @return created channel
   *
   * @since 1.10.0
   */
  public static BossBarAnimationChannel bossBar(final BossBar bossBar) {
    return new BossBarAnimationChannel(bossBar);
  }

  /**
   * Creates custom animation channel of its name.
   *
   * @param name name of the channel
   * @return created channel
   *
   * @since 1.10.0
   */
  public static AnimationChannel animationChannel(final String name) {
    return new AnimationChannel(name);
  }

  private final String name;

  protected AnimationChannel(final String name) {
    this.name = name;
  }

  /**
   * Gets the name of the channel.
   *
   * @return channel name
   *
   * @since 1.10.0
   */
  public String name() {
    return this.name;
  }

  /**
   * This implementation returns also name of the channel.
   *
   * @return channel name
   * @see AnimationChannel#name()
   *
   * @since 1.10.0
   */
  @Override
  public String toString() {
    return this.name;
  }

}
