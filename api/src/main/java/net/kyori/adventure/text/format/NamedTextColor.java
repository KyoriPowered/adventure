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

import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The named text colours in Minecraft: Java Edition.
 *
 * @since 4.0.0
 */
public sealed interface NamedTextColor extends TextColor permits NamedTextColorImpl {
  /**
   * The standard {@code black} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor BLACK = new NamedTextColorImpl("black", NamedTextColorImpl.BLACK_VALUE);
  /**
   * The standard {@code dark_blue} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_BLUE = new NamedTextColorImpl("dark_blue", NamedTextColorImpl.DARK_BLUE_VALUE);
  /**
   * The standard {@code dark_green} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_GREEN = new NamedTextColorImpl("dark_green", NamedTextColorImpl.DARK_GREEN_VALUE);
  /**
   * The standard {@code dark_aqua} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_AQUA = new NamedTextColorImpl("dark_aqua", NamedTextColorImpl.DARK_AQUA_VALUE);
  /**
   * The standard {@code dark_red} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_RED = new NamedTextColorImpl("dark_red", NamedTextColorImpl.DARK_RED_VALUE);
  /**
   * The standard {@code dark_purple} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_PURPLE = new NamedTextColorImpl("dark_purple", NamedTextColorImpl.DARK_PURPLE_VALUE);
  /**
   * The standard {@code gold} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor GOLD = new NamedTextColorImpl("gold", NamedTextColorImpl.GOLD_VALUE);
  /**
   * The standard {@code gray} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor GRAY = new NamedTextColorImpl("gray", NamedTextColorImpl.GRAY_VALUE);
  /**
   * The standard {@code dark_gray} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor DARK_GRAY = new NamedTextColorImpl("dark_gray", NamedTextColorImpl.DARK_GRAY_VALUE);
  /**
   * The standard {@code blue} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor BLUE = new NamedTextColorImpl("blue", NamedTextColorImpl.BLUE_VALUE);
  /**
   * The standard {@code green} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor GREEN = new NamedTextColorImpl("green", NamedTextColorImpl.GREEN_VALUE);
  /**
   * The standard {@code aqua} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor AQUA = new NamedTextColorImpl("aqua", NamedTextColorImpl.AQUA_VALUE);
  /**
   * The standard {@code red} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor RED = new NamedTextColorImpl("red", NamedTextColorImpl.RED_VALUE);
  /**
   * The standard {@code light_purple} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor LIGHT_PURPLE = new NamedTextColorImpl("light_purple", NamedTextColorImpl.LIGHT_PURPLE_VALUE);
  /**
   * The standard {@code yellow} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor YELLOW = new NamedTextColorImpl("yellow", NamedTextColorImpl.YELLOW_VALUE);
  /**
   * The standard {@code white} colour.
   *
   * @since 4.0.0
   */
  NamedTextColor WHITE = new NamedTextColorImpl("white", NamedTextColorImpl.WHITE_VALUE);
  /**
   * An index of name to color.
   *
   * @since 4.0.0
   */
  Index<String, NamedTextColor> NAMES = Index.create(NamedTextColor::toString, NamedTextColorImpl.VALUES);

  /**
   * Gets the named color exactly matching the provided color.
   *
   * @param value the color to match
   * @return the matched color, or null
   * @since 4.10.0
   */
  static @Nullable NamedTextColor namedColor(int value) {
    return switch (value) {
      case NamedTextColorImpl.BLACK_VALUE -> BLACK;
      case NamedTextColorImpl.DARK_BLUE_VALUE -> DARK_BLUE;
      case NamedTextColorImpl.DARK_GREEN_VALUE -> DARK_GREEN;
      case NamedTextColorImpl.DARK_AQUA_VALUE -> DARK_AQUA;
      case NamedTextColorImpl.DARK_RED_VALUE -> DARK_RED;
      case NamedTextColorImpl.DARK_PURPLE_VALUE -> DARK_PURPLE;
      case NamedTextColorImpl.GOLD_VALUE -> GOLD;
      case NamedTextColorImpl.GRAY_VALUE -> GRAY;
      case NamedTextColorImpl.DARK_GRAY_VALUE -> DARK_GRAY;
      case NamedTextColorImpl.BLUE_VALUE -> BLUE;
      case NamedTextColorImpl.GREEN_VALUE -> GREEN;
      case NamedTextColorImpl.AQUA_VALUE -> AQUA;
      case NamedTextColorImpl.RED_VALUE -> RED;
      case NamedTextColorImpl.LIGHT_PURPLE_VALUE -> LIGHT_PURPLE;
      case NamedTextColorImpl.YELLOW_VALUE -> YELLOW;
      case NamedTextColorImpl.WHITE_VALUE -> WHITE;
      default -> null;
    };
  }

  /**
   * Find the named colour nearest to the provided colour.
   *
   * @param any colour to match
   * @return nearest named colour. will always return a value
   * @since 4.0.0
   */
  static @NotNull NamedTextColor nearestTo(final @NotNull TextColor any) {
    if (any instanceof final NamedTextColor namedTextColor) return namedTextColor;
    return TextColor.nearestColorTo(NamedTextColorImpl.VALUES, any);
  }
}
