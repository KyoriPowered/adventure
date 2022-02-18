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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.scheduler.AnimationTask;

class MonoAnimationDisplayImpl<F, T> extends AbstractAnimationDisplay implements MonoAnimationDisplay<F, T> {

  private final T target;

  private final Animation<F, T> animation;

  private final AnimationChannel channel;

  private final Set<DisplayListener<F, T>> listeners;

  private final DisplayContext<F, T> displayContext;

  MonoAnimationDisplayImpl(final T target, final Animation<F, T> animation, final AnimationTask<?> schedulerTask) {
    super(schedulerTask, animation.frameInterval());
    this.target = target;
    this.animation = animation;
    this.channel = animation.displayer().channel(target);
    this.listeners = new HashSet<>(animation.listeners());
    this.displayContext = DisplayContext.context(this.animation, this, this.target);
  }

  @Override
  public T target() {
    return this.target;
  }

  @Override
  public Animation<F, T> animation() {
    return this.animation;
  }

  @Override
  public AnimationChannel channel() {
    return this.channel;
  }

  @Override
  public void displayNextFrame() {
    if (!state().equals(DisplayState.PLAYING))
      throw new IllegalStateException("Can not display frames when display state is not DisplayState.PLAYING.");

    final F frame = this.animation().supplier().frame(counter());

    incrementCounter();

    if (frame == null) {
      this.listeners().forEach(l -> this.sendStateChange(DisplayState.FINISHED));
      state(DisplayState.FINISHED);
      schedulerTask().stop();
    } else {
      this.animation().displayer().display(this.target(), frame);
    }
  }

  @Override
  public Collection<DisplayListener<F, T>> listeners() {
    return Collections.unmodifiableCollection(this.listeners);
  }

  @Override
  protected void sendStateChange(final DisplayState state) {
    for (final DisplayListener<F, T> listener : this.listeners)
      listener.handleStateChange(state, this.displayContext);
  }
}
