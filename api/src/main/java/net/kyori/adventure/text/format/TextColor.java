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
  static @NonNull TextColor of(final int value) {
    return new TextColorImpl(value);
  }

  /**
   * Create a new text colour with the red, green, and blue components individually
   *
   * @param r Red, as a value from 0 to 255
   * @param g Green, as a value from 0 to 255
   * @param b Blue, as a value from 0 to 255
   * @return A new text colour
   */
  static @NonNull TextColor of(final @IntRange(from = 0, to = 0xff) int r, final @IntRange(from = 0, to = 0xff) int g, final @IntRange(from = 0, to = 0xff) int b) {
      return new TextColorImpl(r, g, b);
  }

  /**
   * Create a new color with the individual components as floats
   *
   * @param r red, from [0, 1]
   * @param g green, within [0, 1]
   * @param b blue, within [0, 1]
   * @return The newly created text colour
   */
  static @NonNull TextColor of(final float r, final float g, final float b) {
      return of(r * 0xff, g * 0xff, b * 0xff);
  }

  /**
   * The color, as an RGB value packed into an int
   *
   * @return The color value
   */
  int value();

  /**
   * Get the red component of the text colour
   *
   * @return The red component, in the range [0, 0xff]
   */
  default @IntRange(from = 0, to = 0xff) short red() {
      return (short) ((this.value() >> 16) & 0xff);
  }

  /**
   * Get the green component of the text colour
   *
   * @return The green component, in the range [0, 0xff]
   */
  default @IntRange(from = 0, to = 0xff) short green() {
      return (short) ((this.value() >> 8) & 0xff);
  }

  /**
   * Get the blue component of the text colour
   *
   * @return The blue component, in the range [0, 0xff]
   */
  default @IntRange(from = 0, to = 0xff) short blue() {
      return (short) (this.value() & 0xff);
  }

}
