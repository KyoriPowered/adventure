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

import java.util.function.Function;
import net.kyori.adventure.animation.display.AnimationDisplay;
import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract animation task.
 *
 * @deprecated internal usage
 * @param <D> display type
 *
 * @since 1.10.0
 */
@Deprecated
@ApiStatus.Internal
public abstract class AbstractAnimationTask<D extends AnimationDisplay> implements AnimationTask<D> {

  private final D display;

  private final AbstractAnimationScheduler scheduler;

  protected AbstractAnimationTask(final Function<AnimationTask<?>, D> displayFactory, final AbstractAnimationScheduler scheduler) {
    this.display = displayFactory.apply(this);
    this.scheduler = scheduler;
  }

  protected void nextFrame() {
    this.display().displayNextFrame();
  }

  @Override
  public D display() {
    return this.display;
  }

  @Override
  public AnimationTask<D> schedule() {
    this.display.init();
    this.scheduleTask();
    return this;
  }

  @Override
  public AnimationTask<D> stop() {
    this.cancelTask();
    this.display.channels().forEach(this.scheduler::releaseChannel);
    return this;
  }

  protected abstract void scheduleTask();

  protected abstract void cancelTask();

}
