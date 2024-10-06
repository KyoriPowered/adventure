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

import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class GradientStopImpl<ColorSpace> implements GradientStop<ColorSpace> {
  private final double location;
  private final @NotNull ColorSpace color;

  GradientStopImpl(final double location, final @NotNull ColorSpace color) {
    if (location < 0d || location > 1d) {
      throw new IllegalArgumentException("GradientStop location (" + location + ") must be within the required range [0, 1].");
    }
    this.location = location;
    this.color = color;
  }

  @Override
  public @NotNull ColorSpace color() {
    return this.color;
  }

  @Override
  public double location() {
    return this.location;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("location", this.location),
      ExaminableProperty.of("color", this.color)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final GradientStopImpl<?> that = (GradientStopImpl<?>) o;

    if (Double.compare(that.location, this.location) != 0) return false;
    return this.color.equals(that.color);
  }

  @Override
  public int hashCode() {
    int result;
    final long temp = Double.doubleToLongBits(this.location);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + this.color.hashCode();
    return result;
  }
}
