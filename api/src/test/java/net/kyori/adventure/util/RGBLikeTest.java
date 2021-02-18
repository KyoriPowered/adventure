/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

class RGBLikeTest {
  @Test
  void testHSVConversionAgainstJavaAwtColor() {
    NamedTextColor.NAMES.values().forEach(this::assertConversionRoughlyMatchesJavaAwtColor);
  }

  private void assertConversionRoughlyMatchesJavaAwtColor(final @NonNull TextColor color) {
    Assertions.assertArrayEquals(
      roundFloats(Color.RGBtoHSB(color.red(), color.green(), color.blue(), null)),
      roundFloats(color.asHSV()),
      color.toString()
    );
  }

  private static float[] roundFloats(final float @NonNull [] floats) {
    final float[] result = new float[floats.length];
    for(int i = 0; i < floats.length; i++) {
      result[i] = BigDecimal.valueOf(floats[i]).setScale(7, RoundingMode.UP).floatValue();
    }
    return result;
  }
}
