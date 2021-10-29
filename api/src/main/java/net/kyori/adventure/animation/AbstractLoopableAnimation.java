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

import java.util.Iterator;
import java.util.List;
import net.kyori.adventure.animation.util.LoopedListIterator;

/*
 * An abstraction of loopable animation.
 *
 * @param <F> type of media used as frames
 * @param <D> type of generated displays used for animation show
 * @param <T> type of extending interface for chaining
 * @since 4.9.2
 */
abstract class AbstractLoopableAnimation<F, D extends AnimationDisplay<? extends F>, T extends LoopableAnimation<F, D, T>> extends AbstractAnimation<F, D> implements LoopableAnimation<F, D, T> {

  private final int loopIndex;

  protected AbstractLoopableAnimation(final F[] frames, final int loopIndex) {
    super(frames);
    this.loopIndex = loopIndex;
  }

  protected AbstractLoopableAnimation(final List<F> frames, final int loopIndex) {
    super(frames);
    this.loopIndex = loopIndex;
  }

  @Override
  public int loopIndex() {
    return this.loopIndex;
  }

  @Override
  public Iterator<F> frameIterator() {
    if (looped()) return LoopedListIterator.iterator(frames(), this.loopIndex);
    else return super.frameIterator();
  }
}
