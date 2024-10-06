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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

/**
 * Gradient have starts, ends, and several stops through which to interpolate colors.
 *
 * @param <ColorSpace> the ColorSpace in which the gradient is defined
 */
public interface Gradient<ColorSpace> extends Examinable {
  /**
   * Get the start color of the gradient.
   *
   * @return the start color
   */
  @NotNull GradientStop<ColorSpace> start();

  /**
   * Get the end color of the gradient.
   *
   * @return the end color
   */
  @NotNull GradientStop<ColorSpace> end();


  /**
   * Get the stops of the gradient.
   *
   * @return the stops
   */
  @NotNull List<GradientStop<ColorSpace>> stops();

  /**
   * Create a new iterator with the specified amount of steps and the interpolator to use.
   *
   * @param steps the amount of steps
   * @param interpolator the interpolator to use
   *
   * @return an iterator with the colors of a gradient with {@code steps} steps.
   */
  @NotNull Iterator<ColorSpace> iterator(final int steps, final @NotNull ColorSpaceInterpolator<ColorSpace> interpolator);

  /**
   * Interpolate at {@code location} using the {@code interpolator}.
   *
   * @param location the location, from 0 to 1 (inclusive) to interpolate
   * @param interpolator the interpolator to use.
   *
   * @return the interpolated color.
   */
  @NotNull ColorSpace interpolate(final double location, final @NotNull ColorSpaceInterpolator<ColorSpace> interpolator);

  /**
   * Creates a new gradient with the endpoints.
   *
   * @param start the start color
   * @param end the end color
   * @param <C> the ColorSpace of the gradient
   *
   * @return a gradient with the configured end points
   */
  static <C> Gradient<C> gradient(final @NotNull C start, final @NotNull C end) {
    return new GradientImpl<>(Collections.unmodifiableList(Arrays.asList(GradientStop.start(start), GradientStop.end(end))));
  }

  /**
   * Creates a new gradient with the endpoints and intermediate stops.
   *
   * @param start the start color
   * @param end the end color
   * @param stops the stops in between the gradient. The stops must be in order; i.e. stops[i].location() &lt; stops[i + 1].location()
   * @param <C> the ColorSpace of the gradient
   *
   * @return a gradient with the configured end points
   */
  static <C> Gradient<C> gradient(final @NotNull C start, final @NotNull List<GradientStop<C>> stops, @NotNull C end) {
    final List<GradientStop<C>> copy = new ArrayList<>(stops.size() + 2);
    copy.add(GradientStop.start(start));
    copy.addAll(stops);
    copy.add(GradientStop.end(end));
    return new GradientImpl<>(Collections.unmodifiableList(copy));
  }

  /**
   * Creates a new gradient with stops; the first and last stops are the endpoints.
   *
   * @param stops the stops of the gradient. The stops must be in order; i.e. stops[i].location() &lt; stops[i + 1].location()
   * @param <C> the ColorSpace of the gradient
   *
   * @return a gradient with the configured end points
   */
  static <C> Gradient<C> gradient(final @NotNull List<GradientStop<C>> stops) {
    return new GradientImpl<>(Collections.unmodifiableList(new ArrayList<>(stops)));
  }
}
