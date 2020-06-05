/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.format;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

/**
 * A color which may be applied to a {@link Style}.
 */
public interface TextColor extends TextFormat {
  /**
   * Creates a new text colour.
   *
   * @param value the rgb value
   * @return a new text colour
   */
  static @NonNull TextColor of(final int value) {
    return new TextColorImpl(value);
  }

  /**
   * Create a new text colour with the red, green, and blue components individually
   *
   * @param r red, as a value from 0 to 255
   * @param g green, as a value from 0 to 255
   * @param b blue, as a value from 0 to 255
   * @return a new text colour
   */
  static @NonNull TextColor of(final @IntRange(from = 0x0, to = 0xff) int r, final @IntRange(from = 0x0, to = 0xff) int g, final @IntRange(from = 0x0, to = 0xff) int b) {
    return of((r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff));
  }

  /**
   * Create a new color with the individual components as floats
   *
   * @param r red, from [0, 1]
   * @param g green, within [0, 1]
   * @param b blue, within [0, 1]
   * @return a new text colour
   */
  static @NonNull TextColor of(final float r, final float g, final float b) {
    return of((int) (r * 0xff), (int) (g * 0xff), (int) (b * 0xff));
  }

  // TODO: fromHexString

  /**
   * The color, as an RGB value packed into an int
   *
   * @return the value
   */
  int value();

  /**
   * Gets the color, as a hex string.
   *
   * @return a hex string
   */
  default @NonNull String asHexString() {
    return String.format("#%06x", this.value());
  }

  /**
   * Get the red component of the text colour
   *
   * @return the red component, in the range [0x0, 0xff]
   */
  default @IntRange(from = 0x0, to = 0xff) short red() {
    return (short) ((this.value() >> 16) & 0xff);
  }

  /**
   * Get the green component of the text colour
   *
   * @return the green component, in the range [0x0, 0xff]
   */
  default @IntRange(from = 0x0, to = 0xff) short green() {
    return (short) ((this.value() >> 8) & 0xff);
  }

  /**
   * Get the blue component of the text colour
   *
   * @return the blue component, in the range [0x0, 0xff]
   */
  default @IntRange(from = 0x0, to = 0xff) short blue() {
    return (short) (this.value() & 0xff);
  }

  /**
   * Returns a distance metric to the other colour.
   *
   * <p>This value is unitless and should only be used to compare with other text colours.
   *
   * @param other colour to compare to
   * @return distance metric
   */
  default int distanceSquared(@NonNull TextColor other) {
    final int rAvg = (red() + other.red()) / 2;
    final int dR = red() - other.red();
    final int dG = green() - other.green();
    final int dB = blue() - other.blue();
    return ((2 + (rAvg / 256)) * (dR * dR)) + (4 * (dG * dG)) + ((2 + ((255 - rAvg) / 256)) * (dB * dB));
  }
}
