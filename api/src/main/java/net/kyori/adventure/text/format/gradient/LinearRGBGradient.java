/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * A gradient in the Linear RGB Colorspace.
 *
 * @since 4.15.0
 */
public class LinearRGBGradient implements Gradient {

  LinearRGBGradient() {

  }

  /**
   * Creates a LinearRGBGradient instance.
   *
   * @return a new LinearRGBGradient instance
   *
   * @since 4.15.0
   *
   */
  public static LinearRGBGradient linearRgbGradient() {
    return new LinearRGBGradient();
  }

  /**
   * Converts a Linear RGB channel to the corresponding RGB channel.
   * 
   * @param linearRgbChannel the Linear RGB channel
   *
   * @return the RGB channel
   *
   * @since 4.15.0
   */
  public static double linearRgbToRgb(double linearRgbChannel) {
    if (linearRgbChannel > 0.0031308d) {
      return ((1.055d) * (float)Math.pow(linearRgbChannel, (1.0d/2.4d))) - 0.055d;
    }

    return linearRgbChannel * 12.92d;
  }

  /**
   * Converts an RGB channel to the corresponding Linear RGB channel.
   *
   * @param rgbChannel the RGB channel
   *
   * @return the Linear RGB channel
   *
   * @since 4.15.0
   */
  public static double rgbToLinearRgb(double rgbChannel) {
    if (rgbChannel < 0.04045d) {
      return rgbChannel / 12.92d;
    }

    return (rgbChannel >= 0 ? 1d:-1d) *
      Math.pow((rgbChannel + 0.055d) / 1.055d, 2.4d);

  }

  @Override
  public @NotNull TextColor interpolate(
    final float t,
    final @NotNull TextColor a,
    final @NotNull TextColor b
  ) {
    if (a.value() == b.value()) return a;
    final float clampedT = Math.min(1.0f, Math.max(0.0f, t)); // clamp between 0 and 1

    if (clampedT == 0.0f) return a;
    if (clampedT == 1.0f) return b;

    final double ar = rgbToLinearRgb(a.red());
    final double br = rgbToLinearRgb(b.red());
    final double ag = rgbToLinearRgb(a.green());
    final double bg = rgbToLinearRgb(b.green());
    final double ab = rgbToLinearRgb(a.blue());
    final double bb = rgbToLinearRgb(b.blue());
    return TextColor.color(
      Math.round(linearRgbToRgb(ar + clampedT * (br - ar))),
      Math.round(linearRgbToRgb(ag + clampedT * (bg - ag))),
      Math.round(linearRgbToRgb(ab + clampedT * (bb - ab)))
    );
  }
}
