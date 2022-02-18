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
package net.kyori.adventure.animation.display;

import java.time.Duration;
import net.kyori.adventure.animation.scheduler.AnimationTask;
import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract animation display.
 *
 * @deprecated internal usage
 *
 * @since 1.10.0
 */
@Deprecated
@ApiStatus.Internal
public abstract class AbstractAnimationDisplay implements AnimationDisplay {

  private final AnimationTask<?> schedulerTask;

  private final Duration frameInterval;

  private long counter = 0;

  private DisplayState state = DisplayState.PRE_START;

  protected AbstractAnimationDisplay(final AnimationTask<?> schedulerTask, final Duration frameInterval) {
    this.schedulerTask = schedulerTask;
    this.frameInterval = frameInterval;
  }

  @Override
  public DisplayState state() {
    return this.state;
  }

  protected void state(final DisplayState state) {
    this.state = state;
  }

  @Override
  public void stop() {
    if (this.state.equals(DisplayState.PLAYING)) {
      this.sendStateChange(DisplayState.CANCELLED);
      this.state = DisplayState.CANCELLED;
    }
    this.schedulerTask.stop();
  }

  protected abstract void sendStateChange(final DisplayState state);

  @Override
  public void init() {
    if (this.state.equals(DisplayState.PRE_START)) {
      this.sendStateChange(DisplayState.PLAYING);
      this.state = DisplayState.PLAYING;
    }
  }

  protected AnimationTask<?> schedulerTask() {
    return this.schedulerTask;
  }

  @Override
  public Duration frameInterval() {
    return this.frameInterval;
  }

  protected long counter() {
    return this.counter;
  }

  protected void incrementCounter() {
    this.counter += 1;
  }

}
