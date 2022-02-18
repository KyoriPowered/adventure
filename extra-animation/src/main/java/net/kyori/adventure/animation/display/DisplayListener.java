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

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * Listener of display state change.
 *
 * @param <F> frame type
 * @param <T> target type
 *
 * @since 1.10.0
 */
public interface DisplayListener<F, T> {

  /**
   * Checks if it is a display start.
   *
   * @param <F> frame type
   * @param <T> target type
   * @return created condition
   *
   * @since 1.10.0
   */
  static <F, T> BiPredicate<DisplayState, DisplayContext<F, T>> onStart() {
    return (s, d) -> s.equals(DisplayState.PLAYING);
  }

  /**
   * Checks if it is a display natural finish.
   *
   * @param <F> frame type
   * @param <T> target type
   * @return created condition
   *
   * @since 1.10.0
   */
  static <F, T> BiPredicate<DisplayState, DisplayContext<F, T>> onFinish() {
    return (s, d) -> s.equals(DisplayState.FINISHED);
  }

  /**
   * Checks if display is force-cancelled.
   *
   * @param <F> frame type
   * @param <T> target type
   * @return created condition
   *
   * @since 1.10.0
   */
  static <F, T> BiPredicate<DisplayState, DisplayContext<F, T>> onCancel() {
    return (s, d) -> s.equals(DisplayState.CANCELLED);
  }

  /**
   * Checks if it is a display stop. (finish or cancel)
   *
   * @param <F> frame type
   * @param <T> target type
   * @return created condition
   *
   * @since 1.10.0
   */
  static <F, T> BiPredicate<DisplayState, DisplayContext<F, T>> onStop() {
    return (s, d) -> s.equals(DisplayState.FINISHED) || s.equals(DisplayState.CANCELLED);
  }

  /**
   * Creates listener of condition and execution.
   *
   * @param condition condition predicate
   * @param execution execution consumer
   * @param <F> frame type
   * @param <T> target type
   * @return created listener
   *
   * @since 1.10.0
   */
  static <F, T> DisplayListener<F, T> listener(BiPredicate<DisplayState, DisplayContext<F, T>> condition, BiConsumer<DisplayState, DisplayContext<F, T>> execution) {
    return (s, d) -> {
      if (condition.test(s, d))
        execution.accept(s, d);
    };
  }

  /**
   * Handles display state change.
   *
   * @param newState new state
   * @param context display context
   *
   * @since 1.10.0
   */
  void handleStateChange(DisplayState newState, DisplayContext<F, T> context);

}
