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

import java.util.HexFormat;
import net.kyori.adventure.util.HSVLike;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "asHexString()")
record TextColorImpl(int value) implements TextColor {
  private static final HexFormat HEX_FORMATTER = HexFormat.of().withUpperCase();

  @Override
  public String toString() {
    return this.asHexString();
  }

  /**
   * Returns a distance metric to the other colour.
   *
   * <p>This value is unitless and should only be used to compare with other text colours.</p>
   *
   * @param self the base colour
   * @param other colour to compare to
   * @return distance metric
   */
  static float distance(final HSVLike self, final HSVLike other) {
    // weight hue more heavily than saturation and brightness. kind of magic numbers, but is fine for our use case of downsampling to a set of colors
    final float hueDistance = 3 * Math.min(Math.abs(self.h() - other.h()), 1f - Math.abs(self.h() - other.h()));
    final float saturationDiff = self.s() - other.s();
    final float valueDiff = self.v() - other.v();
    return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
  }

  /**
   * Parses a value into an RGB hex string using the format #RRGGBB.
   *
   * @param value the value in RRGGBB in the range of 0x0 to 0xffffff
   * @return an uppercase hex string
   */
  public static String hexString(final int value) {
    return HEX_CHARACTER + HEX_FORMATTER.toHexDigits(value, 6);
  }
}
