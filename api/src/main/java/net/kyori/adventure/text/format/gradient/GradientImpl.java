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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class GradientImpl<ColorSpace> implements Gradient<ColorSpace> {
  private final @NotNull List<GradientStop<ColorSpace>> stops;

  GradientImpl(final @NotNull List<GradientStop<ColorSpace>> stops) {
    this.stops = stops;
  }

  @Override
  public @NotNull GradientStop<ColorSpace> start() {
    return this.stops.get(0);
  }

  @Override
  public @NotNull GradientStop<ColorSpace> end() {
    return this.stops.get(this.stops.size() - 1);
  }

  @Override
  public @NotNull List<GradientStop<ColorSpace>> stops() {
    return this.stops;
  }

  @Override
  public @NotNull Iterator<ColorSpace> iterator(final int steps, final @NotNull ColorSpaceInterpolator<ColorSpace> interpolator) {
    // edge cases for steps
    if (steps < 0) {
      throw new IllegalArgumentException("steps (" + steps + ") must not be negative");
    } else if (steps == 0) {
      return Collections.emptyIterator();
    } else if (steps == 1) {
      return Collections.singleton(this.stops.get(0).color()).iterator();
    }
    final double multiplier = 1d / (steps - 1);
    // todo - better performance
    return IntStream.range(0, steps).mapToObj(step -> this.interpolate(step * multiplier, interpolator)).iterator();
  }

  @Override
  public @NotNull ColorSpace interpolate(final double location, final @NotNull ColorSpaceInterpolator<ColorSpace> interpolator) {
    final double boundedLocation = Math.min(Math.max(location, 0d), 1d);

    GradientStop<ColorSpace> start = this.stops.get(0);
    GradientStop<ColorSpace> end = this.stops.get(this.stops.size() - 1);
    if (location == 0d) {
      return start.color();
    } else if (location == 1d) {
      return end.color();
    }

    for (final GradientStop<ColorSpace> stop : this.stops) {
      if (stop.location() == location) { // return early
        return stop.color();
      } else if (stop.location() < location) { // start bound
        if (start == null || start.location() <= stop.location()) {
          start = stop;
        }
      } else if (stop.location() > location) { // end bound
        if (end == null || end.location() >= stop.location()) {
          end = stop;
        }
      }
    }

    final double transformedLocation = (boundedLocation - start.location()) / (end.location() - start.location());
    return interpolator.interpolate(transformedLocation, start.color(), end.color());
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("stops", this.stops));
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final GradientImpl<?> gradient = (GradientImpl<?>) o;

    return this.stops.equals(gradient.stops);
  }

  @Override
  public int hashCode() {
    return this.stops.hashCode();
  }

}

