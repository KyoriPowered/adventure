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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.animation.display.DisplayListener;
import net.kyori.adventure.animation.displayer.FrameDisplayer;
import net.kyori.adventure.animation.supplier.FrameSupplier;

class AnimationImpl<F, T> implements Animation<F, T> {

  private final FrameSupplier<F> supplier;

  private final FrameDisplayer<F, T> displayer;

  private final Duration frameInterval;

  private final Set<DisplayListener<F, T>> listeners;

  AnimationImpl(final FrameSupplier<F> supplier, final FrameDisplayer<F, T> displayer, final Duration frameInterval, final Collection<DisplayListener<F, T>> listeners) {
    this.supplier = supplier;
    this.displayer = displayer;
    this.frameInterval = frameInterval;
    this.listeners = new HashSet<>(listeners);
  }

  @Override
  public FrameSupplier<F> supplier() {
    return this.supplier;
  }

  @Override
  public Animation<F, T> supplier(final FrameSupplier<F> supplier) {
    return new AnimationImpl<>(supplier, this.displayer, this.frameInterval, this.listeners);
  }

  @Override
  public FrameDisplayer<F, T> displayer() {
    return this.displayer;
  }

  @Override
  public Animation<F, T> displayer(final FrameDisplayer<F, T> displayer) {
    return new AnimationImpl<>(this.supplier, displayer, this.frameInterval, this.listeners);
  }

  @Override
  public Duration frameInterval() {
    return this.frameInterval;
  }

  @Override
  public Animation<F, T> frameInterval(final Duration frameInterval) {
    return new AnimationImpl<>(this.supplier, this.displayer, frameInterval, this.listeners);
  }

  @Override
  public final Animation<F, T> listeners(final Collection<DisplayListener<F, T>> listeners) {
    return new AnimationImpl<>(this.supplier, this.displayer, this.frameInterval, listeners);
  }

  @Override
  public final Animation<F, T> addListeners(final Collection<DisplayListener<F, T>> listeners) {
    final Set<DisplayListener<F, T>> newListeners = new HashSet<>(this.listeners);

    newListeners.addAll(listeners);

    return new AnimationImpl<>(this.supplier, this.displayer, this.frameInterval, newListeners);
  }

  @Override
  public Collection<DisplayListener<F, T>> listeners() {
    return Collections.unmodifiableSet(this.listeners);
  }

}
