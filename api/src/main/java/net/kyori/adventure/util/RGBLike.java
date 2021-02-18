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
package net.kyori.adventure.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Something that can provide red, green, and blue colour components.
 *
 * @since 4.0.0
 */
public interface RGBLike {
  /**
   * Gets the red component.
   *
   * @return the red component
   * @since 4.0.0
   */
  @IntRange(from = 0x0, to = 0xff) int red();

  /**
   * Gets the green component.
   *
   * @return the green component
   * @since 4.0.0
   */
  @IntRange(from = 0x0, to = 0xff) int green();

  /**
   * Gets the blue component.
   *
   * @return the blue component
   * @since 4.0.0
   */
  @IntRange(from = 0x0, to = 0xff) int blue();

  /**
   * Converts the color represented by this RGBLike to the HSV color space. Result values will be in the range [0, 1].
   *
   * @return an array of three elements containing the hue, saturation, and value (in that order), of the color represented by this RGBLike
   * @since 4.6.0
   */
  default float@NonNull[] asHSV() {
    final float r = this.red() / 255.0f;
    final float g = this.green() / 255.0f;
    final float b = this.blue() / 255.0f;

    final float min = Math.min(r, Math.min(g, b));
    final float max = Math.max(r, Math.max(g, b)); // v
    final float delta = max - min;

    final float s;
    if(max != 0) {
      s = delta / max; // s
    } else {
      // r = g = b = 0
      s = 0;
    }
    if(s == 0) { // s = 0, h is undefined
      return new float[]{0, s, max};
    }

    float h;
    if(r == max) {
      h = (g - b) / delta; // between yellow & magenta
    } else if(g == max) {
      h = 2 + (b - r) / delta; // between cyan & yellow
    } else {
      h = 4 + (r - g) / delta; // between magenta & cyan
    }
    h *= 60; // degrees
    if(h < 0) {
      h += 360;
    }

    return new float[]{h / 360.0f, s, max};
  }
}
