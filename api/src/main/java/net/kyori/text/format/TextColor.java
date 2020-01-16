/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text.format;

import net.kyori.text.Component;
import net.kyori.text.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An enumeration of colors which may be applied to a {@link Component}.
 */
public enum TextColor implements TextFormat {
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

  TextColor(final String name) {
    this.name = name;
  }

  @Override
  public @NonNull String toString() {
    return this.name;
  }
}
