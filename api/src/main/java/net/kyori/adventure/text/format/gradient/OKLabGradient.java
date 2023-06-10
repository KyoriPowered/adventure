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
import net.kyori.adventure.util.LABLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;

/**
 * A gradient in the OkLab Colorspace.
 *
 * @since 4.15.0
 */
public class OKLabGradient implements Gradient {

  OKLabGradient() {

  }

  /**
   * Creates a OkLabGradient instance.
   *
   * @return a new OkLabGradient instance
   *
   * @since 4.15.0
   *
   */
  public static OKLabGradient okLabGradient() {
    return new OKLabGradient();
  }

  /**
   * Converts an RGB color to the corresponding OkLab color.
   *
   * @param rgb the RGB color
   *
   * @return the OkLab color
   *
   * @since 4.15.0
   */
  public static @NotNull LABLike rgbToOkLab(
    final @NotNull RGBLike rgb
  ) {
    final double r = LinearRGBGradient.rgbToLinearRgb(rgb.red());
    final double g = LinearRGBGradient.rgbToLinearRgb(rgb.green());
    final double b = LinearRGBGradient.rgbToLinearRgb(rgb.blue());

    final double L = Math.cbrt(
      0.41222147079999993f * r + 0.5363325363f * g + 0.0514459929f * b
    );
    final double M = Math.cbrt(
      0.2119034981999999d * r + 0.6806995450999999d * g + 0.1073969566d * b
    );
    final double S = Math.cbrt(
      0.08830246189999998d * r + 0.2817188376d * g + 0.6299787005000002d * b
    );

    return LABLike.labLike(
      0.2104542553d * L + 0.793617785d * M - 0.0040720468d * S,
      1.9779984951d * L - 2.428592205d * M + 0.4505937099d * S,
      0.0259040371d * L + 0.7827717662d * M - 0.808675766d * S
    );
  }

  /**
   * Converts an OkLab color to the corresponding RGB color.
   *
   * @param okLab the OkLab color
   *
   * @return the RGB color
   *
   * @since 4.15.0
   */
  public static @NotNull RGBLike okLabToRgb(
    final @NotNull LABLike okLab
  ) {
    final double l = okLab.lightness();
    final double a = okLab.a();
    final double b = okLab.b();

    final double L = Math.pow(
      l * 0.99999999845051981432d +
        0.39633779217376785678d * a +
        0.21580375806075880339d * b,
      3
    );
    final double M = Math.pow(
      l * 1.0000000088817607767d -
        0.1055613423236563494d * a -
        0.063854174771705903402d * b,
      3
    );
    final double S = Math.pow(
      l * 1.0000000546724109177d -
        0.089484182094965759684d * a -
        1.2914855378640917399d * b,
      3
    );

    return TextColor.color(
      (float)LinearRGBGradient.linearRgbToRgb(
        4.076741661347994d * L -
        3.307711590408193d * M +
        0.230969928729428d * S),
      (float)LinearRGBGradient.linearRgbToRgb(
        -1.2684380040921763 * L +
        2.6097574006633715 * M -
        0.3413193963102197 * S),
      (float)LinearRGBGradient.linearRgbToRgb(
        -0.004196086541837188 * L -
        0.7034186144594493 * M +
        1.7076147009309444 * S)
    );

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

    final @NotNull LABLike okLabA = rgbToOkLab(a);
    final @NotNull LABLike okLabB = rgbToOkLab(b);

    final double al = okLabA.lightness();
    final double bl = okLabB.lightness();
    final double aa = okLabA.a();
    final double ba = okLabB.a();
    final double ab = okLabA.a();
    final double bb = okLabB.b();

    return TextColor.color(okLabToRgb(LABLike.labLike(
      (al + clampedT * (bl - al)),
      (aa + clampedT * (ba - aa)),
      (ab + clampedT * (bb - ab))
    )));
  }
}
