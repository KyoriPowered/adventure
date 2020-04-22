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

import net.kyori.adventure.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum NamedTextColor implements TextColor {
  BLACK("black", 0x000000),
  DARK_BLUE("dark_blue", 0x0000aa),
  DARK_GREEN("dark_green", 0x00aa00),
  DARK_AQUA("dark_aqua", 0x00aaaa),
  DARK_RED("dark_red", 0xaa0000),
  DARK_PURPLE("dark_purple", 0xaa00aa),
  GOLD("gold", 0xffaa00),
  GRAY("gray", 0xaaaaaa),
  DARK_GRAY("dark_gray", 0x555555),
  BLUE("blue", 0x5555ff),
  GREEN("green", 0x55ff55),
  AQUA("aqua", 0x55ffff),
  RED("red", 0xff5555),
  LIGHT_PURPLE("light_purple", 0xff55ff),
  YELLOW("yellow", 0xffff55),
  WHITE("white", 0xffffff);

  /**
   * The name map.
   */
  public static final NameMap<NamedTextColor> NAMES = NameMap.create(NamedTextColor.class, constant -> constant.name);
  /**
   * The name of this color.
   */
  private final String name;
  private final int value;

  NamedTextColor(final String name, final int value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public @NonNull String toString() {
    return this.name;
  }
}
