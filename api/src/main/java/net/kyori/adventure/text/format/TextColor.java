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
package net.kyori.adventure.text.format;

import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.RGBLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import static java.util.Objects.requireNonNull;

/**
 * A color which may be applied to a {@link Style}.
 *
 * <p>The full range of hexadecimal colors are only supported in <em>Minecraft: Java Edition</em> 1.16 and above.
 * On older versions, platforms may downsample these to {@link NamedTextColor}s.</p>
 *
 * <p>This color does not include any alpha channel information.</p>
 *
 * @see NamedTextColor
 * @since 4.0.0
 */
public interface TextColor extends Comparable<TextColor>, Examinable, RGBLike, StyleBuilderApplicable, TextFormat {
  /**
   * The hex character.
   *
   * @since 4.14.0
   */
  char HEX_CHARACTER = '#';
  /**
   * The hex character, in {@code String} format.
   *
   * @since 4.14.0
   */
  String HEX_PREFIX = "#";

  /**
   * Creates a new text colour.
   *
   * @param value the rgb value
   * @return a new text colour
   * @since 4.0.0
   */
  static @NotNull TextColor color(final int value) {
    final int truncatedValue = value & 0xffffff;
    final NamedTextColor named = NamedTextColor.namedColor(truncatedValue);
    return named != null ? named : new TextColorImpl(truncatedValue);
  }

  /**
   * Creates a new text colour.
   *
   * @param rgb the rgb value
   * @return a new text colour
   * @since 4.0.0
   */
  static @NotNull TextColor color(final @NotNull RGBLike rgb) {
    if (rgb instanceof TextColor) return (TextColor) rgb;
    return color(rgb.red(), rgb.green(), rgb.blue());
  }

  /**
   * Creates a new text color, converting the provided {@link HSVLike} to the RGB color space.
   *
   * @param hsv the hsv value
   * @return a new text color
   * @see <a href="https://en.wikipedia.org/wiki/HSL_and_HSV">https://en.wikipedia.org/wiki/HSL_and_HSV</a>
   * @since 4.6.0
   */
  static @NotNull TextColor color(final @NotNull HSVLike hsv) {
    final float s = hsv.s();
    final float v = hsv.v();
    if (s == 0) {
      // achromatic (grey)
      return color(v, v, v);
    }

    final float h = hsv.h() * 6; // sector 0 to 5
    final int i = (int) Math.floor(h);
    final float f = h - i; // factorial part of h
    final float p = v * (1 - s);
    final float q = v * (1 - s * f);
    final float t = v * (1 - s * (1 - f));

    if (i == 0) {
      return color(v, t, p);
    } else if (i == 1) {
      return color(q, v, p);
    } else if (i == 2) {
      return color(p, v, t);
    } else if (i == 3) {
      return color(p, q, v);
    } else if (i == 4) {
      return color(t, p, v);
    } else {
      return color(v, p, q);
    }
  }

  /**
   * Create a new text colour with the red, green, and blue components individually.
   *
   * @param r red, as a value from 0 to 255
   * @param g green, as a value from 0 to 255
   * @param b blue, as a value from 0 to 255
   * @return a new text colour
   * @since 4.0.0
   */
  static @NotNull TextColor color(final @Range(from = 0x0, to = 0xff) int r, final @Range(from = 0x0, to = 0xff) int g, final @Range(from = 0x0, to = 0xff) int b) {
    return color((r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff));
  }

  /**
   * Create a new color with the individual components as floats.
   *
   * @param r red, from [0, 1]
   * @param g green, within [0, 1]
   * @param b blue, within [0, 1]
   * @return a new text colour
   * @since 4.0.0
   */
  static @NotNull TextColor color(final float r, final float g, final float b) {
    return color((int) (r * 0xff), (int) (g * 0xff), (int) (b * 0xff));
  }

  /**
   * Create a new color from a hex string.
   *
   * @param string the hex string
   * @return a new text colour
   * @since 4.0.0
   */
  static @Nullable TextColor fromHexString(final @NotNull String string) {
    if (string.startsWith(HEX_PREFIX)) {
      try {
        final int hex = Integer.parseInt(string.substring(1), 16);
        return color(hex);
      } catch (final NumberFormatException e) {
        return null;
      }
    }
    return null;
  }

  /**
   * Create a color from a CSS hex string ({@code #rrggbb} or {@code #rgb}).
   *
   * @param string the hex string
   * @return a new text colour
   * @since 4.0.0
   */
  static @Nullable TextColor fromCSSHexString(final @NotNull String string) {
    if (string.startsWith(HEX_PREFIX)) {
      final String hexString = string.substring(1);
      if (hexString.length() != 3 && hexString.length() != 6) {
        return null;
      }
      final int hex;
      try {
        hex = Integer.parseInt(hexString, 16);
      } catch (final NumberFormatException e) {
        return null;
      }

      if (hexString.length() == 6) {
        return color(hex);
      } else {
        final int red = (hex & 0xf00) >> 8 | (hex & 0xf00) >> 4;
        final int green = (hex & 0x0f0) >> 4 | (hex & 0x0f0);
        final int blue = (hex & 0x00f) << 4 | (hex & 0x00f);
        return color(red, green, blue);
      }
    }
    return null;
  }

  /**
   * The color, as an RGB value packed into an int.
   *
   * @return the value
   * @since 4.0.0
   */
  int value();

  /**
   * Gets the color, as a hex string.
   *
   * @return a hex string
   * @since 4.0.0
   */
  default @NotNull String asHexString() {
    return String.format("%c%06x", HEX_CHARACTER, this.value());
  }

  /**
   * Get the red component of the text colour.
   *
   * @return the red component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int red() {
    return (this.value() >> 16) & 0xff;
  }

  /**
   * Get the green component of the text colour.
   *
   * @return the green component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int green() {
    return (this.value() >> 8) & 0xff;
  }

  /**
   * Get the blue component of the text colour.
   *
   * @return the blue component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @Range(from = 0x0, to = 0xff) int blue() {
    return this.value() & 0xff;
  }

  /**
   * Linearly interpolates between {@code a} and {@code b} by {@code t}.
   *
   * <p>This returns a color blended between color {@code a}, at {@code t=0.0}, and color {@code b}, at {@code t=1.0}.</p>
   *
   * @param t the interpolation value, between {@code 0.0} and {@code 1.0} (both inclusive)
   * @param a the lower bound ({@code t=0.0})
   * @param b the upper bound ({@code t=1.0})
   * @return the interpolated value, a color between the two input colors {@code a} and {@code b}
   * @since 4.8.0
   */
  static @NotNull TextColor lerp(final float t, final @NotNull RGBLike a, final @NotNull RGBLike b) {
    final float clampedT = Math.min(1.0f, Math.max(0.0f, t)); // clamp between 0 and 1
    final int ar = a.red();
    final int br = b.red();
    final int ag = a.green();
    final int bg = b.green();
    final int ab = a.blue();
    final int bb = b.blue();
    return color(
      Math.round(ar + clampedT * (br - ar)),
      Math.round(ag + clampedT * (bg - ag)),
      Math.round(ab + clampedT * (bb - ab))
    );
  }

  /**
   * Find the colour nearest to the provided colour.
   *
   * @param values the colours for matching
   * @param any colour to match
   * @param <C> the color type
   * @return nearest named colour. will always return a value
   * @since 4.14.0
   */
  static <C extends TextColor> @NotNull C nearestColorTo(final @NotNull List<C> values, final @NotNull TextColor any) {
    requireNonNull(any, "color");

    float matchedDistance = Float.MAX_VALUE;
    C match = values.get(0);
    for (int i = 0, length = values.size(); i < length; i++) {
      final C potential = values.get(i);
      final float distance = TextColorImpl.distance(any.asHSV(), potential.asHSV());
      if (distance < matchedDistance) {
        match = potential;
        matchedDistance = distance;
      }
      if (distance == 0) {
        break; // same colour! whoo!
      }
    }
    return match;
  }

  @Override
  default void styleApply(final Style.@NotNull Builder style) {
    style.color(this);
  }

  @Override
  default int compareTo(final TextColor that) {
    return Integer.compare(this.value(), that.value());
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.asHexString()));
  }
}
