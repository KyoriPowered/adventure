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

import java.util.List;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.Nullable;

/**
 * The named text colours in Minecraft: Java Edition.
 *
 * @since 4.0.0
 */
public final class NamedTextColor implements TextColor {
  static final int BLACK_VALUE = 0x000000;
  static final int DARK_BLUE_VALUE = 0x0000aa;
  static final int DARK_GREEN_VALUE = 0x00aa00;
  static final int DARK_AQUA_VALUE = 0x00aaaa;
  static final int DARK_RED_VALUE = 0xaa0000;
  static final int DARK_PURPLE_VALUE = 0xaa00aa;
  static final int GOLD_VALUE = 0xffaa00;
  static final int GRAY_VALUE = 0xaaaaaa;
  static final int DARK_GRAY_VALUE = 0x555555;
  static final int BLUE_VALUE = 0x5555ff;
  static final int GREEN_VALUE = 0x55ff55;
  static final int AQUA_VALUE = 0x55ffff;
  static final int RED_VALUE = 0xff5555;
  static final int LIGHT_PURPLE_VALUE = 0xff55ff;
  static final int YELLOW_VALUE = 0xffff55;
  static final int WHITE_VALUE = 0xffffff;

  /**
   * The standard {@code black} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor BLACK = new NamedTextColor("black", BLACK_VALUE);
  /**
   * The standard {@code dark_blue} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_BLUE = new NamedTextColor("dark_blue", DARK_BLUE_VALUE);
  /**
   * The standard {@code dark_green} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_GREEN = new NamedTextColor("dark_green", DARK_GREEN_VALUE);
  /**
   * The standard {@code dark_aqua} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_AQUA = new NamedTextColor("dark_aqua", DARK_AQUA_VALUE);
  /**
   * The standard {@code dark_red} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_RED = new NamedTextColor("dark_red", DARK_RED_VALUE);
  /**
   * The standard {@code dark_purple} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_PURPLE = new NamedTextColor("dark_purple", DARK_PURPLE_VALUE);
  /**
   * The standard {@code gold} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor GOLD = new NamedTextColor("gold", GOLD_VALUE);
  /**
   * The standard {@code gray} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor GRAY = new NamedTextColor("gray", GRAY_VALUE);
  /**
   * The standard {@code dark_gray} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor DARK_GRAY = new NamedTextColor("dark_gray", DARK_GRAY_VALUE);
  /**
   * The standard {@code blue} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor BLUE = new NamedTextColor("blue", BLUE_VALUE);
  /**
   * The standard {@code green} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor GREEN = new NamedTextColor("green", GREEN_VALUE);
  /**
   * The standard {@code aqua} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor AQUA = new NamedTextColor("aqua", AQUA_VALUE);
  /**
   * The standard {@code red} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor RED = new NamedTextColor("red", RED_VALUE);
  /**
   * The standard {@code light_purple} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor LIGHT_PURPLE = new NamedTextColor("light_purple", LIGHT_PURPLE_VALUE);
  /**
   * The standard {@code yellow} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor YELLOW = new NamedTextColor("yellow", YELLOW_VALUE);
  /**
   * The standard {@code white} colour.
   *
   * @since 4.0.0
   */
  public static final NamedTextColor WHITE = new NamedTextColor("white", WHITE_VALUE);

  static final List<NamedTextColor> VALUES = List.of(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE);

  /**
   * An index of name to color.
   *
   * @since 4.0.0
   */
  public static final Index<String, NamedTextColor> NAMES = Index.create(NamedTextColor::name, VALUES);

  /**
   * Gets the named color exactly matching the provided color.
   *
   * @param value the color to match
   * @return the matched color, or null
   * @since 4.10.0
   */
  public static @Nullable NamedTextColor namedColor(final int value) {
    return switch (value) {
      case NamedTextColor.BLACK_VALUE -> BLACK;
      case NamedTextColor.DARK_BLUE_VALUE -> DARK_BLUE;
      case NamedTextColor.DARK_GREEN_VALUE -> DARK_GREEN;
      case NamedTextColor.DARK_AQUA_VALUE -> DARK_AQUA;
      case NamedTextColor.DARK_RED_VALUE -> DARK_RED;
      case NamedTextColor.DARK_PURPLE_VALUE -> DARK_PURPLE;
      case NamedTextColor.GOLD_VALUE -> GOLD;
      case NamedTextColor.GRAY_VALUE -> GRAY;
      case NamedTextColor.DARK_GRAY_VALUE -> DARK_GRAY;
      case NamedTextColor.BLUE_VALUE -> BLUE;
      case NamedTextColor.GREEN_VALUE -> GREEN;
      case NamedTextColor.AQUA_VALUE -> AQUA;
      case NamedTextColor.RED_VALUE -> RED;
      case NamedTextColor.LIGHT_PURPLE_VALUE -> LIGHT_PURPLE;
      case NamedTextColor.YELLOW_VALUE -> YELLOW;
      case NamedTextColor.WHITE_VALUE -> WHITE;
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
  public static NamedTextColor nearestTo(final TextColor any) {
    if (any instanceof final NamedTextColor namedTextColor) return namedTextColor;
    return TextColor.nearestColorTo(VALUES, any);
  }

  private final String name;
  private final int value;
  private final HSVLike hsvLike;

  private NamedTextColor(final String name, final int value) {
    this.name = name;
    this.value = value;
    this.hsvLike = HSVLike.fromRGB(this.red(), this.green(), this.blue());
  }

  /**
   * The name of this named text color.
   *
   * @return the name
   * @since 5.0.0
   */
  public String name() {
    return this.name;
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public HSVLike asHSV() {
    return this.hsvLike;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
