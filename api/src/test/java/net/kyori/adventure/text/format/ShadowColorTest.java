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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ShadowColorTest {
  @Test
  void testZeroIsNone() {
    assertSame(ShadowColor.none(), ShadowColor.shadowColor(0));
  }

  @Test
  void testRedComponent() {
    final ShadowColor clr = ShadowColor.shadowColor(0xff, 0xbb, 0xcc, 0xdd);
    assertEquals(0xff, clr.red());
  }

  @Test
  void testGreenComponent() {
    final ShadowColor clr = ShadowColor.shadowColor(0xaa, 0xff, 0xcc, 0xdd);
    assertEquals(0xff, clr.green());
  }

  @Test
  void testBlueComponent() {
    final ShadowColor clr = ShadowColor.shadowColor(0xaa, 0xbb, 0xff, 0xdd);
    assertEquals(0xff, clr.blue());
  }

  @Test
  void testAlphaComponent() {
    final ShadowColor clr = ShadowColor.shadowColor(0xaa, 0xbb, 0xcc, 0xff);
    assertEquals(0xff, clr.alpha());
  }

  @Test
  @SuppressWarnings("PatternValidation") // we are testing invalid inputs
  void testInvalidHexIsNull() {
    assertNull(ShadowColor.fromHexString("aabbccdd"));
    assertNull(ShadowColor.fromHexString("#abc"));
    assertNull(ShadowColor.fromHexString("#aabbccddtoomuch"));
  }

  @Test
  void testEquality() {
    new EqualsTester()
      .addEqualityGroup(
        ShadowColor.shadowColor(0xDDAABBCC),
        ShadowColor.shadowColor(0xAA, 0xBB, 0xCC, 0xDD),
        ShadowColor.fromHexString("#AABBCCDD")
      )
      .addEqualityGroup(
        ShadowColor.shadowColor(0xFFFFFFFF),
        ShadowColor.shadowColor(0xFF, 0xFF, 0xFF, 0xFF),
        ShadowColor.fromHexString("#FFFFFFFF")
      )
      .addEqualityGroup(
        ShadowColor.none(),
        ShadowColor.shadowColor(0)
      )
      .testEquals();
  }
}
