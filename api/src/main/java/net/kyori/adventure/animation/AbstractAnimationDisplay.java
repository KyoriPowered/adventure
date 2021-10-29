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

import java.time.Duration;
import java.util.Iterator;
import net.kyori.adventure.audience.Audience;

abstract class AbstractAnimationDisplay<F> implements AnimationDisplay<F> {

  private final Audience audience;

  private final Duration frameInterval;

  private final Iterator<F> frameIterator;

  private final AnimationTask schedulerTask;

  protected AbstractAnimationDisplay(final Iterator<F> frameIterator, final Audience audience, final AnimationTask schedulerTask) {
    this.audience = audience;
    this.frameInterval = schedulerTask.frameInterval();
    this.schedulerTask = schedulerTask;
    this.frameIterator = frameIterator;

    schedulerTask.schedule(this);
  }

  @Override
  public Audience audience() {
    return this.audience;
  }

  @Override
  public Duration frameInterval() {
    return this.frameInterval;
  }

  @Override
  public boolean hasNextFrame() {
    return this.frameIterator.hasNext();
  }

  @Override
  public F nextFrame() {
    return this.frameIterator.next();
  }

  @Override
  public void stop() {
    this.schedulerTask.cancel();
  }
}
