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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;
import net.kyori.adventure.animation.display.AnimationDisplay;
import net.kyori.adventure.animation.display.DisplayRequest;
import net.kyori.adventure.animation.display.MonoAnimationDisplay;
import net.kyori.adventure.animation.display.PolyAnimationDisplay;
import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract animation scheduler.
 *
 * @deprecated internal usage
 *
 * @since 1.10.0
 */
@Deprecated
@ApiStatus.Internal
public abstract class AbstractAnimationScheduler implements AnimationScheduler {

  private final Map<Object, Map<AnimationChannel, AnimationDisplay>> channelRegistry = new HashMap<>();

  protected abstract <D extends AnimationDisplay> AnimationTask<D> createTask(Function<AnimationTask<?>, D> displayFactory);

  protected Map<Object, Map<AnimationChannel, AnimationDisplay>> channelRegistry() {
    return this.channelRegistry;
  }

  @Override
  public <F, T> MonoAnimationDisplay<F, T> scheduleAnimation(final Animation<F, T> animation, final T target) {
    return this.createTask(task -> AnimationDisplay.singleAnimation(target, animation, task)).schedule().display();
  }

  @Override
  public PolyAnimationDisplay scheduleAnimations(final DisplayRequest<?, ?>... requests) {
    return this.createTask(task -> AnimationDisplay.multiAnimation(task, Arrays.asList(requests))).schedule().display();
  }

  protected void releaseChannel(final Object target, final AnimationChannel channel) {
    if (this.channelRegistry.containsKey(target)) {
      this.channelRegistry.get(target).remove(channel);
    }
  }

  @Override
  public void clearChannel(final Object target, final AnimationChannel channel) {
    if (this.channelRegistry.containsKey(target) && this.channelRegistry.get(target).containsKey(channel)) {
      this.channelRegistry.get(target).remove(channel).stop();
    }
  }

  @Override
  public void clearTarget(final Object target) {
    if (this.channelRegistry.containsKey(target)) {
      this.channelRegistry.get(target).values().forEach(AnimationDisplay::stop);
    }
  }

  @Override
  public void shutdown() {
    this.channelRegistry.values().forEach(v -> v.values().forEach(AnimationDisplay::stop));
  }
}
