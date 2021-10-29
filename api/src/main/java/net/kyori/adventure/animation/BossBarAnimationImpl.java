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

import java.util.List;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

class BossBarAnimationImpl extends AbstractLoopableAnimation<Component, BossBarAnimation.Display, BossBarAnimation> implements BossBarAnimation {

  static class DisplayImpl extends AbstractAnimationDisplay<Component> implements Display {

    private final BossBarAnimation animation;

    private final BossBar bossBar;

    protected DisplayImpl(final Audience audience, final BossBarAnimationImpl animation, final AnimationTask schedulerTask) {
      super(animation.frameIterator(), audience, schedulerTask);
      this.animation = animation;

      this.bossBar = BossBar.bossBar(this.animation().bossbar().name(), this.animation().bossbar().progress(),
        this.animation().bossbar().color(), this.animation().bossbar().overlay(), this.animation().bossbar().flags());

      audience.showBossBar(this.bossBar);
    }

    @Override
    public void displayNextFrame() {
      this.bossBar.name(nextFrame());
      if (!hasNextFrame() && this.animation().hideBarOnFinish())
        audience().hideBossBar(this.bossBar);
    }

    @Override
    public BossBarAnimation animation() {
      return this.animation;
    }

    @Override
    public void stop() {
      super.stop();
      audience().hideBossBar(this.bossBar);
    }
  }

  private final BossBar bossBar;

  private final boolean showOnStart;

  private final boolean hideOnFinish;

  private final boolean hideOnStop;

  protected BossBarAnimationImpl(final List<Component> frames, final BossBar bossBar, final int loopIndex, final boolean showOnStart, final boolean hideOnFinish, final boolean hideOnStop) {
    super(frames, loopIndex);
    this.bossBar = bossBar;
    this.showOnStart = showOnStart;
    this.hideOnFinish = hideOnFinish;
    this.hideOnStop = hideOnStop;
  }

  @Override
  public Display createDisplay(final Audience audience, final AnimationTask schedulerTask) {
    return new DisplayImpl(audience, this, schedulerTask);
  }

  @Override
  public BossBar bossbar() {
    return this.bossBar;
  }

  @Override
  public boolean hideBarOnFinish() {
    return this.hideOnFinish;
  }

  @Override
  public boolean showBarOnStart() {
    return this.showOnStart;
  }

  @Override
  public boolean hideBarOnStop() {
    return this.hideOnStop;
  }

  @Override
  public BossBarAnimation loop(final int loopIndex) {
    return new BossBarAnimationImpl(frames(), this.bossBar, loopIndex, this.showOnStart, this.hideOnFinish, this.hideOnStop);
  }
}
