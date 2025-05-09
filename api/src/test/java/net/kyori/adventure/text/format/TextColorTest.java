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

import com.google.common.testing.EqualsTester;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TextColorTest {
  @Test
  void testFromHexString() {
    assertEquals(TextColor.color(0xaa00aa), TextColor.fromHexString("#aa00aa"));
  }

  @Test
  void testFromRGBLike() {
    assertEquals(TextColor.color(0xaa00aa), TextColor.color(new RGBLike() {
      @Override
      public @Range(from = 0x0, to = 0xff) int red() {
        return 0xaa;
      }

      @Override
      public @Range(from = 0x0, to = 0xff) int green() {
        return 0x00;
      }

      @Override
      public @Range(from = 0x0, to = 0xff) int blue() {
        return 0xaa;
      }
    }));
  }

  @Test
  void testFromMalformedHexString() {
    assertNull(TextColor.fromHexString("aa00aa")); // does not begin with #
    assertNull(TextColor.fromHexString("#aa00az"));
  }

  @Test
  void testFromExcessData() {
    assertEquals(0xabcdef, TextColor.color(0xffabcdef).value());
  }

  @Test
  void testPureColors() {
    final TextColor redInt = TextColor.color(0xff0000);
    final TextColor greenInt = TextColor.color(0x00ff00);
    final TextColor blueInt = TextColor.color(0x0000ff);

    final TextColor red = TextColor.color(0xff, 0x00, 0x00);
    final TextColor green = TextColor.color(0x00, 0xff, 0x00);
    final TextColor blue = TextColor.color(0x00, 0x00, 0xff);

    assertEquals(redInt, red);
    assertEquals(greenInt, green);
    assertEquals(blueInt, blue);
  }

  @Test
  void testExtractComponents() {
    final TextColor purple = TextColor.color(0xff00ff);
    assertEquals(0xff, purple.red());
    assertEquals(0x00, purple.green());
    assertEquals(0xff, purple.blue());

    final TextColor color = TextColor.color(0xbada04);
    assertEquals(0xba, color.red());
    assertEquals(0xda, color.green());
    assertEquals(0x04, color.blue());
  }

  @Test
  void testLerp() {
    assertEquals(TextColor.color(0x808080), TextColor.lerp(0.50f, TextColor.color(0xffffff), TextColor.color(0x000000)));
    assertEquals(TextColor.color(0x3399FF), TextColor.lerp(0.50f, TextColor.color(0x3366FF), TextColor.color(0x33CCFF)));
  }

  @Test
  void testEquality() {
    new EqualsTester()
      .addEqualityGroup(
        TextColor.color(0xff0000),
        TextColor.color(0xff, 0x00, 0x00)
      )
      .addEqualityGroup(
        TextColor.color(0x00ff00),
        TextColor.color(0x00, 0xff, 0x00)
      )
      .testEquals();
  }

  @Test
  void testCSSHexStringMalformedHexString() {
    assertNull(TextColor.fromCSSHexString("7f1e2d")); // no # in front
    assertNull(TextColor.fromCSSHexString("#7f1e2")); // only five characters
    assertNull(TextColor.fromCSSHexString("#7fze2d")); // invalid hex character
  }

  @Test
  void testCSSHexStringIsSameAsHexStringForSixDigitRGB() {
    assertEquals(TextColor.fromHexString("#7f1e2d"), TextColor.fromCSSHexString("#7f1e2d"));
  }

  @Test
  void testCSSHexStringThreeDigit() {
    final TextColor original = TextColor.color(0x77ff11);
    assertEquals(original, TextColor.fromCSSHexString("#7f1"));
  }
}
