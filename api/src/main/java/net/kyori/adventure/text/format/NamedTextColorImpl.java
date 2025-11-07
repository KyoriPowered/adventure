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

record NamedTextColorImpl(String name, int value) implements NamedTextColor {
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

  public static class Values {
    static final List<NamedTextColor> VALUES = List.of(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE);
  }

  @Override
  public HSVLike asHSV() {
    return HSVLike.fromRGB(this.red(), this.green(), this.blue());
  }

  @Override
  public String toString() {
    return this.name;
  }
}
