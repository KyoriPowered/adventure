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
import net.kyori.adventure.title.Title;

class TitleAnimationImpl extends AbstractAnimation<Title, TitleAnimation.Display> implements TitleAnimation {

  protected TitleAnimationImpl(final List<Title> frames) {
    super(frames);
  }

  protected TitleAnimationImpl(final Title[] frames) {
    super(frames);
  }

  static class DisplayImpl extends AbstractAnimationDisplay<Title> implements Display {

    private final TitleAnimationImpl animation;

    protected DisplayImpl(final TitleAnimationImpl animation, final Audience audience, final AnimationTask schedulerTask) {
      super(animation.frameIterator(), audience, schedulerTask);

      this.animation = animation;
    }

    @Override
    public void displayNextFrame() {
      audience().showTitle(nextFrame());
    }

    @Override
    public TitleAnimation animation() {
      return this.animation;
    }
  }

  @Override
  public Display createDisplay(final Audience audience, final AnimationTask schedulerTask) {
    return new DisplayImpl(this, audience, schedulerTask);
  }

}
