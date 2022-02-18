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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.kyori.adventure.animation.display.DisplayListener;
import net.kyori.adventure.animation.display.DisplayRequest;
import net.kyori.adventure.animation.displayer.FrameDisplayer;
import net.kyori.adventure.animation.supplier.FrameSupplier;

/**
 * Represents the animation.
 *
 * @param <F> frame type
 * @param <T> target type
 *
 * @since 1.10.0
 */
public interface Animation<F, T> {

  /**
   * Creates new animation.
   *
   * @param supplier supplier which gives frames
   * @param displayer displayer which displays frames
   * @param frameInterval time interval between frames
   * @param listeners display listeners
   * @param <F> frame type
   * @param <T> target type
   * @return created animation
   *
   * @since 1.10.0
   */
  @SafeVarargs
  static <F, T> Animation<F, T> animation(FrameSupplier<F> supplier, FrameDisplayer<F, T> displayer, Duration frameInterval, DisplayListener<F, T>... listeners) {
    return new AnimationImpl<>(supplier, displayer, frameInterval, Arrays.asList(listeners));
  }

  /**
   * Gets frame supplier.
   *
   * @return frame supplier
   *
   * @since 1.10.0
   */
  FrameSupplier<F> supplier();

  /**
   * Sets frame supplier.
   *
   * @param supplier frame supplier
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  Animation<F, T> supplier(FrameSupplier<F> supplier);

  /**
   * Gets frame displayer.
   *
   * @return frame displayer
   *
   * @since 1.10.0
   */
  FrameDisplayer<F, T> displayer();

  /**
   * Sets frame displayer.
   *
   * @param displayer frame displayer
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  Animation<F, T> displayer(FrameDisplayer<F, T> displayer);

  /**
   * Gets time interval between frames.
   *
   * @return frame interval
   *
   * @since 1.10.0
   */
  Duration frameInterval();

  /**
   * Sets frame interval.
   *
   * @param frameInterval frame interval
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  Animation<F, T> frameInterval(Duration frameInterval);

  /**
   * Gets display listeners.
   *
   * @return display listeners
   *
   * @since 1.10.0
   */
  Collection<DisplayListener<F, T>> listeners();

  /**
   * Sets display listeners.
   *
   * @param listeners display listeners
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  Animation<F, T> listeners(Collection<DisplayListener<F, T>> listeners);

  /**
   * Adds multiple display listeners.
   *
   * @param listeners listeners to add
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  Animation<F, T> addListeners(Collection<DisplayListener<F, T>> listeners);

  /**
   * Adds one display listener.
   *
   * @param listener listener to add
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  default Animation<F, T> addListener(DisplayListener<F, T> listener) {
    return this.addListeners(Collections.singleton(listener));
  }

  /**
   * Makes display request of this animation.
   *
   * @param target animation target
   * @return a new instance with change performed
   * @see DisplayRequest#request(Animation, Object)
   *
   * @since 1.10.0
   */
  default DisplayRequest<Animation<F, T>, T> displayRequest(T target) {
    return DisplayRequest.request(this, target);
  }

}
