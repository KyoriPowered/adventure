/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.format.gradient;

import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

/**
 * A location along a gradient with a defined color.
 *
 * @param <ColorSpace> the color space of this gradient stop
 */
public interface GradientStop<ColorSpace> extends Examinable {

  /**
   * Get the color of the stop.
   *
   * @return the color
   */
  @NotNull ColorSpace color();

  /**
   * Location must be between 0 and 1, inclusive.
   *
   * @return the location
   */
  double location();

  /**
   * Creates a gradient start point.
   *
   * @param color the color of the stop
   * @param <C> the color space of this stop
   * @return a gradient stop representing the start of the gradient
   */
  static <C> GradientStop<C> start(@NotNull C color) {
    return new GradientStopImpl<>(0d, color);
  }

  /**
   * Creates a gradient end point.
   *
   * @param color the color of the stop
   * @param <C> the color space of this stop
   * @return a gradient stop representing the end of the gradient
   */
  static <C> GradientStop<C> end(@NotNull C color) {
    return new GradientStopImpl<>(1d, color);
  }

  /**
   * Creates a gradient end point.
   *
   * @param location the location, in 0 to 1 (inclusive) along the gradient (0 is start, 1 is end).
   * @param color the color of the stop.
   * @param <C> the color space of this stop
   * @return a gradient stop representing the end of the gradient
   */
  static <C> GradientStop<C> gradientStop(double location, @NotNull C color) {
    return new GradientStopImpl<>(location, color);
  }

}
