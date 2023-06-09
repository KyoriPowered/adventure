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
import net.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.NotNull;

/**
 * A Gradient in the HSV color space.
 *
 * @since 4.15.0
 */
public class HSVGradient extends HueGradient {

  HSVGradient(final HueGradient.@NotNull HueInterpolationMethod hueDirection) {
    super(hueDirection);
  }

  /**
   * Creates a HSVGradient with the default hue interpolation method, {@link net.kyori.adventure.text.format.gradient.HueGradient.HueInterpolationMethod#SHORTER}.
   *
   * @return a new instance with the default method
   *
   * @since 4.15.0
   */
  public static HSVGradient hsvGradient() {
    return new HSVGradient(HueGradient.HueInterpolationMethod.SHORTER);
  }

  /**
   * Creates a HSVGradient with the given hue interpolation method.
   *
   * @param method the hue interpolation method
   *
   * @return a new instance configured with the method.
   *
   * @since 4.15.0
   */
  public static HSVGradient hsvGradient(final HueGradient.@NotNull HueInterpolationMethod method) {
    return new HSVGradient(method);
  }

  @Override
  public final @NotNull TextColor interpolate(final float t, final @NotNull TextColor a, final @NotNull TextColor b) {
    if (a.value() == b.value()) return a;

    final float clampedT = Math.min(Math.max(t, 0.0f), 1.0f);

    if (clampedT == 0.0f) return a;
    if (clampedT == 1.0f) return b;

    final HSVLike aHsv = a.asHSV();
    final HSVLike bHsv = b.asHSV();

    final float ah = aHsv.h();
    final float bh = bHsv.h();
    final float as = aHsv.s();
    final float bs = bHsv.s();
    final float av = aHsv.v();
    final float bv = bHsv.v();

    return TextColor.color(HSVLike.hsvLike(
      this.interpolateHue(t, this.hueInterpolationMethod(), ah, bh),
      Math.min(Math.max(as + t * (bs - as), 0.0f), 1.0f),
      Math.min(Math.max(av + t * (bv - av), 0.0f), 1.0f)
    ));
  }
}
