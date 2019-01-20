/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.format;

import net.kyori.text.Component;
import net.kyori.text.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An enumeration of colors which may be applied to a {@link Component}.
 */
public enum TextColor implements TextFormat {
  BLACK("black", '0'),
  DARK_BLUE("dark_blue", '1'),
  DARK_GREEN("dark_green", '2'),
  DARK_AQUA("dark_aqua", '3'),
  DARK_RED("dark_red", '4'),
  DARK_PURPLE("dark_purple", '5'),
  GOLD("gold", '6'),
  GRAY("gray", '7'),
  DARK_GRAY("dark_gray", '8'),
  BLUE("blue", '9'),
  GREEN("green", 'a'),
  AQUA("aqua", 'b'),
  RED("red", 'c'),
  LIGHT_PURPLE("light_purple", 'd'),
  YELLOW("yellow", 'e'),
  WHITE("white", 'f');
  BLACK("black"),
  DARK_BLUE("dark_blue"),
  DARK_GREEN("dark_green"),
  DARK_AQUA("dark_aqua"),
  DARK_RED("dark_red"),
  DARK_PURPLE("dark_purple"),
  GOLD("gold"),
  GRAY("gray"),
  DARK_GRAY("dark_gray"),
  BLUE("blue"),
  GREEN("green"),
  AQUA("aqua"),
  RED("red"),
  LIGHT_PURPLE("light_purple"),
  YELLOW("yellow"),
  WHITE("white");

  /**
   * The name map.
   */
  public static final NameMap<TextColor> NAMES = NameMap.create(values(), constant -> constant.name);
  /**
   * The name of this color.
   */
  private final String name;
  /**
   * The legacy code.
   */
  @Deprecated private final char legacy;

  TextColor(final String name, final char legacy) {
    this.name = name;
    this.legacy = legacy;
  }

  @Deprecated
  @Override
  public char legacy() {
    return this.legacy;
  }

  @Override
  public @NonNull String toString() {
    return this.name;
  }
}
