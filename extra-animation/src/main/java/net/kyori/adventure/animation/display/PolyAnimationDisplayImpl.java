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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.displayer.FrameDisplayer;
import net.kyori.adventure.animation.scheduler.AnimationTask;
import net.kyori.adventure.animation.util.GCDCalc;

class PolyAnimationDisplayImpl extends AbstractAnimationDisplay implements PolyAnimationDisplay {

  private final Map<Animation<?, ?>, Object> animations = new HashMap<>();

  private final Set<Animation<?, ?>> playingAnimations;

  private final Map<Object, AnimationChannel> channels = new HashMap<>();

  private final Map<DisplayListener<?, ?>, Animation<?, ?>> listeners = new HashMap<>();

  protected PolyAnimationDisplayImpl(final AnimationTask<?> schedulerTask, final Collection<DisplayRequest<?, ?>> requests) {
    super(schedulerTask, frameInterval(requests));

    if (requests.isEmpty())
      throw new IllegalArgumentException("No display request have been given.");

    for (final DisplayRequest<?, ?> request : requests) {
      this.animations.put(request.animation(), request.target());
      this.channels.put(request.target(), request.channel());
      request.animation().listeners().forEach(l -> this.listeners.put(l, request.animation()));
    }

    this.playingAnimations = new HashSet<>(this.animations.keySet());

  }

  private static Duration frameInterval(final Collection<DisplayRequest<?, ?>> requests) {
    long interval = 0;

    for (final DisplayRequest<?, ?> d : requests) {
      interval = GCDCalc.gcd(interval, d.animation().frameInterval().toNanos());
    }

    if (interval <= 0)
      throw new IllegalArgumentException("Requests must not be empty.");

    return Duration.ofNanos(interval);
  }

  @Override
  public void displayNextFrame() {
    if (!state().equals(DisplayState.PLAYING))
      throw new IllegalStateException("Can not display frames when display state is not DisplayState.PLAYING.");

    final Iterator<Animation<?, ?>> iterator = this.playingAnimations.iterator();

    while (iterator.hasNext()) {
      final Animation<?, ?> animation = iterator.next();

      final long intervalRatio = animation.frameInterval().toNanos() / frameInterval().toNanos();

      if (counter() % intervalRatio == 0) {

        final Object frame = animation.supplier().frame(counter() / intervalRatio);

        if (frame == null) {
          iterator.remove();
          this.sendStateChange(DisplayState.FINISHED, animation);
        } else
          displayFrameUnchecked(animation.displayer(), this.animations.get(animation), frame);
      }
    }

    incrementCounter();

    if (this.playingAnimations.isEmpty()) {
      state(DisplayState.FINISHED);
      schedulerTask().stop();
    }

  }

  @SuppressWarnings("unchecked")
  private static <F, T> void displayFrameUnchecked(final FrameDisplayer<F, T> displayer, final Object target, final Object frame) {
    displayer.display((T) target, (F) frame);
  }

  @Override
  public Map<Animation<?, ?>, Object> animations() {
    return Collections.unmodifiableMap(this.animations);
  }

  @Override
  public Collection<DisplayListener<?, ?>> listeners() {
    return Collections.unmodifiableCollection(this.listeners.keySet());
  }

  @Override
  public Map<Object, AnimationChannel> channels() {
    return Collections.unmodifiableMap(this.channels);
  }

  @Override
  protected void sendStateChange(final DisplayState state) {
    this.listeners.forEach((l, a) -> l.handleStateChange(state, DisplayContextImpl.contextUnchecked(a, this, this.animations.get(a))));
  }

  protected void sendStateChange(final DisplayState state, final Animation<?, ?> animation) {
    animation.listeners().forEach(l -> l.handleStateChange(state, DisplayContextImpl.contextUnchecked(animation, this, this.animations.get(animation))));
  }

}
