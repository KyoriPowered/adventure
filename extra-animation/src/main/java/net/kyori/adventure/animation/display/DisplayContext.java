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

import net.kyori.adventure.animation.Animation;

/**
 * Context of display state change event.
 *
 * @param <F> frame type
 * @param <T> target type
 *
 * @since 1.10.0
 */
public interface DisplayContext<F, T> {

  /**
   * Creates new context.
   *
   * @param animation animation
   * @param display display
   * @param target target
   * @param <F> frame type
   * @param <T> target type
   * @return created context
   *
   * @since 1.10.0
   */
  static <F, T> DisplayContext<F, T> context(final Animation<F, T> animation, final AnimationDisplay display, final T target) {
    return new DisplayContextImpl<>(animation, display, target);
  }

  /**
   * Gets the animation.
   *
   * @return animation
   *
   * @since 1.10.0
   */
  Animation<F, T> animation();

  /**
   * Gets the display.
   *
   * @return display
   *
   * @since 1.10.0
   */
  AnimationDisplay display();

  /**
   * Gets the target.
   *
   * @return target.
   *
   * @since 1.10.0
   */
  T target();

  /**
   * Gets pre-change state.
   *
   * @return state
   *
   * @since 1.10.0
   */
  default DisplayState state() {
    return this.display().state();
  }

}
