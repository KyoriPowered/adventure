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
package net.kyori.adventure.text.format;

import net.kyori.adventure.util.ARGBLike;
import net.kyori.adventure.util.RGBLike;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * A shadow color which may be applied to a {@link Style}.
 *
 * <p>Similar to {@link TextColor}, except that shadows contain alpha information.</p>
 *
 * @since 4.18.0
 * @sinceMinecraft 1.21.4
 */
public interface ShadowColor extends StyleBuilderApplicable, ARGBLike {
  /**
   * Linearly interpolates between {@code a} and {@code b} by {@code t}.
   *
   * <p>This returns a color blended between color {@code a}, at {@code t=0.0}, and color {@code b}, at {@code t=1.0}.</p>
   *
   * @param t the interpolation value, between {@code 0.0} and {@code 1.0} (both inclusive)
   * @param a the lower bound ({@code t=0.0})
   * @param b the upper bound ({@code t=1.0})
   * @return the interpolated value, a color between the two input colors {@code a} and {@code b}
   * @since 4.18.0
   */
  static ShadowColor lerp(final float t, final ARGBLike a, final ARGBLike b) {
    final float clampedT = Math.min(1.0f, Math.max(0.0f, t)); // clamp between 0 and 1
    final int ar = a.red();
    final int br = b.red();
    final int ag = a.green();
    final int bg = b.green();
    final int ab = a.blue();
    final int bb = b.blue();
    final int aa = a.alpha();
    final int ba = b.alpha();
    return shadowColor(
      Math.round(ar + clampedT * (br - ar)),
      Math.round(ag + clampedT * (bg - ag)),
      Math.round(ab + clampedT * (bb - ab)),
      Math.round(aa + clampedT * (ba - aa))
    );
  }

  /**
   * Return a shadow color that will disable the shadow on a component.
   *
   * @return a disabling shadow color
   * @since 4.18.0
   */
  static ShadowColor none() {
    return ShadowColorImpl.NONE;
  }

  /**
   * Create a new shadow color from the ARGB value packed in an int.
   *
   * <p>This int will be in the format {@code 0xAARRGGBB}</p>
   *
   * @param argb the int-packed ARGB value
   * @return a shadow color
   * @since 4.18.0
   */
  @Contract(pure = true)
  static ShadowColor shadowColor(final int argb) {
    if (argb == ShadowColorImpl.NONE_VALUE) return none();

    return new ShadowColorImpl(argb);
  }

  /**
   * Create a new shadow color from individual red, green, blue, and alpha values.
   *
   * @param red the red value
   * @param green the green value
   * @param blue the blue value
   * @param alpha the alpha
   * @return a shadow colour
   * @since 4.18.0
   */
  @Contract(pure = true)
  static ShadowColor shadowColor(
    final @Range(from = 0x0, to = 0xff) int red,
    final @Range(from = 0x0, to = 0xff) int green,
    final @Range(from = 0x0, to = 0xff) int blue,
    final @Range(from = 0x0, to = 0xff) int alpha
  ) {
    final int value =
      (alpha & 0xff) << 24
        | (red & 0xff) << 16
        | (green & 0xff) << 8
        | (blue & 0xff);

    return shadowColor(value);
  }

  /**
   * Create a shadow color from an existing colour plus an alpha value.
   *
   * @param rgb the existing color
   * @param alpha the alpha
   * @return a shadow colour
   * @since 4.18.0
   */
  @Contract(pure = true)
  static ShadowColor shadowColor(final RGBLike rgb, final @Range(from = 0x0, to = 0xff) int alpha) {
    return shadowColor(rgb.red(), rgb.green(), rgb.blue(), alpha);
  }

  /**
   * Create a shadow color from an existing ARGB colour.
   *
   * @param argb the existing color
   * @return a shadow colour
   * @since 4.18.0
   */
  static ShadowColor shadowColor(final ARGBLike argb) {
    if (argb instanceof ShadowColor shadow) {
      return shadow;
    }

    return shadowColor(argb.red(), argb.green(), argb.blue(), argb.alpha());
  }

  /**
   * Attempt to parse a shadow colour from a {@code #}-prefixed hex string.
   *
   * <p>This string must be in the format {@code #RRGGBBAA}</p>
   *
   * @param hex the input value
   * @return a shadow color if possible, or null if any components are invalid
   * @since 4.18.0
   */
  @Contract(pure = true)
  static @Nullable ShadowColor fromHexString(@Pattern("#[0-9a-fA-F]{8}") final String hex) {
    if (hex.length() != 9) return null;
    if (!hex.startsWith("#")) return null;

    try {
      final int r = Integer.parseInt(hex.substring(1, 3), 16);
      final int g = Integer.parseInt(hex.substring(3, 5), 16);
      final int b = Integer.parseInt(hex.substring(5, 7), 16);
      final int a = Integer.parseInt(hex.substring(7, 9), 16);
      return new ShadowColorImpl((a << 24) | (r << 16) | (g << 8) | b);
    } catch (final NumberFormatException ignored) {
      return null;
    }
  }

  /**
   * Represent this shadow color as a {@code #}-prefixed hex string.
   *
   * <p>This string will be in the format {@code #RRGGBBAA}</p>
   *
   * @return the hex string representation of this shadow colour
   * @since 4.18.0
   */
  default String asHexString() {
    return ShadowColorImpl.asHexString(this.value());
  }

  /**
   * Get the red component of the shadow colour.
   *
   * @return the red component, in the range [0x0, 0xff]
   * @since 4.18.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int red() {
    return (this.value() >> 16) & 0xff;
  }

  /**
   * Get the green component of the shadow colour.
   *
   * @return the green component, in the range [0x0, 0xff]
   * @since 4.18.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int green() {
    return (this.value() >> 8) & 0xff;
  }

  /**
   * Get the blue component of the shadow colour.
   *
   * @return the blue component, in the range [0x0, 0xff]
   * @since 4.18.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int blue() {
    return this.value() & 0xff;
  }

  /**
   * Get the alpha component of the shadow colour.
   *
   * @return the alpha component, in the range [0x0, 0xff]
   * @since 4.18.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int alpha() {
    return (this.value() >> 24) & 0xff;
  }

  /**
   * The int-packed ARGB value of this shadow colour.
   *
   * @return the shadow colour value
   * @since 4.18.0
   */
  int value();

  @Override
  default void styleApply(final Style.Builder style) {
    style.shadowColor(this);
  }
}
