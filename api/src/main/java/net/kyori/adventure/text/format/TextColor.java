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

import java.util.stream.Stream;
import net.kyori.adventure.util.RGBLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;

/**
 * A color which may be applied to a {@link Style}.
 *
 * <p>The full range of hexadecimal colors are only supported in <em>Minecraft: Java Edition</em> 1.16 and above.
 * On older versions, platforms may downsample these to {@link NamedTextColor}s.</p>
 *
 * @see NamedTextColor
 * @since 4.0.0
 */
public interface TextColor extends Comparable<TextColor>, Examinable, RGBLike, StyleBuilderApplicable, TextFormat {
  /**
   * Creates a new text colour.
   *
   * @param value the rgb value
   * @return a new text colour
   * @since 4.0.0
   */
  static @NonNull TextColor color(final int value) {
    final NamedTextColor named = NamedTextColor.ofExact(value);
    return named != null ? named : new TextColorImpl(value);
  }

  /**
   * Creates a new text colour.
   *
   * @param rgb the rgb value
   * @return a new text colour
   * @since 4.0.0
   */
  static @NonNull TextColor color(final RGBLike rgb) {
    return color(rgb.red(), rgb.green(), rgb.blue());
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
  static @NonNull TextColor color(final @IntRange(from = 0x0, to = 0xff) int r, final @IntRange(from = 0x0, to = 0xff) int g, final @IntRange(from = 0x0, to = 0xff) int b) {
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
  static @NonNull TextColor color(final float r, final float g, final float b) {
    return color((int) (r * 0xff), (int) (g * 0xff), (int) (b * 0xff));
  }

  /**
   * Create a new color from a hex string.
   *
   * @param string the hex string
   * @return a new text colour
   * @since 4.0.0
   */
  static @Nullable TextColor fromHexString(final @NonNull String string) {
    if(string.startsWith("#")) {
      try {
        final int hex = Integer.parseInt(string.substring(1), 16);
        return color(hex);
      } catch(final NumberFormatException e) {
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
  static @Nullable TextColor fromCSSHexString(final @NonNull String string) {
    if(string.startsWith("#")) {
      final String hexString = string.substring(1);
      if(hexString.length() != 3 && hexString.length() != 6) {
        return null;
      }
      final int hex;
      try {
        hex = Integer.parseInt(hexString, 16);
      } catch(final NumberFormatException e) {
        return null;
      }

      if(hexString.length() == 6) {
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
  default @NonNull String asHexString() {
    return String.format("#%06x", this.value());
  }

  /**
   * Get the red component of the text colour.
   *
   * @return the red component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @IntRange(from = 0x0, to = 0xff) int red() {
    return (this.value() >> 16) & 0xff;
  }

  /**
   * Get the green component of the text colour.
   *
   * @return the green component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @IntRange(from = 0x0, to = 0xff) int green() {
    return (this.value() >> 8) & 0xff;
  }

  /**
   * Get the blue component of the text colour.
   *
   * @return the blue component, in the range [0x0, 0xff]
   * @since 4.0.0
   */
  @Override
  default @IntRange(from = 0x0, to = 0xff) int blue() {
    return this.value() & 0xff;
  }

  @Override
  default void styleApply(final Style.@NonNull Builder style) {
    style.color(this);
  }

  @Override
  default int compareTo(final TextColor that) {
    return Integer.compare(this.value(), that.value());
  }

  @Override
  default @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.asHexString()));
  }
}
