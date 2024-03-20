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
 * A gradient in the RGB Colorspace.
 *
 * @since 4.15.0
 */
public class RGBGradient implements Gradient {

  RGBGradient() {

  }

  /**
   * Creates a RGBGradient instance.
   *
   * @return a new RGBGradient instance
   *
   * @since 4.15.0
   *
   */
  public static RGBGradient rgbGradient() {
    return new RGBGradient();
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

    final int ar = a.red();
    final int br = b.red();
    final int ag = a.green();
    final int bg = b.green();
    final int ab = a.blue();
    final int bb = b.blue();
    return TextColor.color(
      Math.round(ar + clampedT * (br - ar)),
      Math.round(ag + clampedT * (bg - ag)),
      Math.round(ab + clampedT * (bb - ab))
    );
  }
}
