/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Locale;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.Nullable;

/**
 * Color spaces that gradient colors can be interpolated in.
 *
 * <p>Hue interpolation takes the shortest path around the hue circle, and the hue of an
 * achromatic color is treated as missing, taking the hue of the other endpoint.</p>
 *
 * @since 5.3.0
 */
enum ColorSpace {
  RGB {
    @Override
    TextColor interpolate(final float t, final TextColor a, final TextColor b) {
      return TextColor.lerp(t, a, b);
    }
  },
  HSV {
    @Override
    TextColor interpolate(final float t, final TextColor a, final TextColor b) {
      final HSVLike hsvA = a.asHSV();
      final HSVLike hsvB = b.asHSV();
      final double hueA = hsvA.s() == 0 || hsvA.v() == 0 ? Double.NaN : hsvA.h();
      final double hueB = hsvB.s() == 0 || hsvB.v() == 0 ? Double.NaN : hsvB.h();
      return TextColor.color(HSVLike.hsvLike(
        (float) lerpHue(t, hueA, hueB),
        (float) lerp(t, hsvA.s(), hsvB.s()),
        (float) lerp(t, hsvA.v(), hsvB.v())
      ));
    }
  },
  OKLAB {
    @Override
    TextColor interpolate(final float t, final TextColor a, final TextColor b) {
      final double[] labA = rgbToOklab(a);
      final double[] labB = rgbToOklab(b);
      return oklabToRgb(
        lerp(t, labA[0], labB[0]),
        lerp(t, labA[1], labB[1]),
        lerp(t, labA[2], labB[2])
      );
    }
  },
  OKLCH {
    @Override
    TextColor interpolate(final float t, final TextColor a, final TextColor b) {
      final double[] labA = rgbToOklab(a);
      final double[] labB = rgbToOklab(b);
      final double chromaA = Math.hypot(labA[1], labA[2]);
      final double chromaB = Math.hypot(labB[1], labB[2]);
      final double hueA = chromaA < ACHROMATIC_CHROMA_THRESHOLD ? Double.NaN : Math.atan2(labA[2], labA[1]) / (2 * Math.PI);
      final double hueB = chromaB < ACHROMATIC_CHROMA_THRESHOLD ? Double.NaN : Math.atan2(labB[2], labB[1]) / (2 * Math.PI);
      final double lightness = lerp(t, labA[0], labB[0]);
      final double chroma = lerp(t, chromaA, chromaB);
      final double hue = lerpHue(t, hueA, hueB) * (2 * Math.PI);
      return oklabToRgb(lightness, chroma * Math.cos(hue), chroma * Math.sin(hue));
    }
  };

  static final Index<String, ColorSpace> NAMES = Index.create(ColorSpace.class, colorSpace -> colorSpace.name().toLowerCase(Locale.ROOT));

  private static final double ACHROMATIC_CHROMA_THRESHOLD = 1e-6d;

  static @Nullable ColorSpace byName(final String name) {
    return NAMES.value(name.toLowerCase(Locale.ROOT));
  }

  String serializedName() {
    return NAMES.keyOrThrow(this);
  }

  /**
   * Interpolate between two colors in this color space.
   *
   * @param t the interpolation factor, in the range {@code [0, 1]}
   * @param a the lower bound
   * @param b the upper bound
   * @return the interpolated color
   * @since 5.3.0
   */
  abstract TextColor interpolate(final float t, final TextColor a, final TextColor b);

  private static double lerp(final double t, final double a, final double b) {
    return a + t * (b - a);
  }

  /**
   * Interpolate a hue along the shortest path around the hue circle.
   *
   * <p>Hues are in the range {@code [0, 1]}, {@link Double#NaN} marks an undefined (achromatic) hue.</p>
   *
   * @param t the interpolation factor, in the range {@code [0, 1]}
   * @param a the lower bound
   * @param b the upper bound
   * @return the interpolated hue
   */
  private static double lerpHue(final double t, final double a, final double b) {
    if (Double.isNaN(a)) return Double.isNaN(b) ? 0 : b;
    if (Double.isNaN(b)) return a;
    double difference = b - a;
    if (difference > 0.5d) {
      difference -= 1;
    } else if (difference < -0.5d) {
      difference += 1;
    }
    double hue = (a + t * difference) % 1d;
    if (hue < 0) {
      hue += 1;
    }
    return hue;
  }

  private static double[] rgbToOklab(final TextColor color) {
    final double r = srgbToLinear(color.red() / 255d);
    final double g = srgbToLinear(color.green() / 255d);
    final double b = srgbToLinear(color.blue() / 255d);

    final double l = Math.cbrt(0.4122214708d * r + 0.5363325363d * g + 0.0514459929d * b);
    final double m = Math.cbrt(0.2119034982d * r + 0.6806995451d * g + 0.1073969566d * b);
    final double s = Math.cbrt(0.0883024619d * r + 0.2817188376d * g + 0.6299787005d * b);

    final double okL = 0.2104542553d * l + 0.7936177850d * m - 0.0040720468d * s;
    final double okA = 1.9779984951d * l - 2.4285922050d * m + 0.4505937099d * s;
    final double okB = 0.0259040371d * l + 0.7827717662d * m - 0.8086757660d * s;
    return new double[]{okL, okA, okB};
  }

  private static TextColor oklabToRgb(final double lightness, final double a, final double b) {
    double l = lightness + 0.3963377774d * a + 0.2158037573d * b;
    double m = lightness - 0.1055613458d * a - 0.0638541728d * b;
    double s = lightness - 0.0894841775d * a - 1.2914855480d * b;

    l = l * l * l;
    m = m * m * m;
    s = s * s * s;

    final double red = 4.0767416621d * l - 3.3077115913d * m + 0.2309699292d * s;
    final double green = -1.2684380046d * l + 2.6097574011d * m - 0.3413193965d * s;
    final double blue = -0.0041960863d * l - 0.7034186147d * m + 1.7076147010d * s;

    return TextColor.color(
      clampComponent(linearToSrgb(red)),
      clampComponent(linearToSrgb(green)),
      clampComponent(linearToSrgb(blue))
    );
  }

  private static double srgbToLinear(final double component) {
    return component <= 0.04045d
      ? component / 12.92d
      : Math.pow((component + 0.055d) / 1.055d, 2.4d);
  }

  private static double linearToSrgb(final double component) {
    return component <= 0.0031308d
      ? component * 12.92d
      : 1.055d * Math.pow(component, 1 / 2.4d) - 0.055d;
  }

  private static int clampComponent(final double component) {
    return (int) Math.max(0, Math.min(255, Math.round(component * 255d)));
  }
}
