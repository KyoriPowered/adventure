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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NamedTextColorTest {
  @SuppressWarnings("ConstantConditions") // intentionally passing 'null'
  @Test
  void testNullRejected() {
    assertThrows(NullPointerException.class, () -> NamedTextColor.nearestTo(null), "color");
  }

  @Test
  void testNamedColorsResolveToSelf() {
    for (final NamedTextColor named : NamedTextColor.NAMES.values()) {
      final TextColor nonNamed = TextColor.color(named.value());
      assertEquals(named, NamedTextColor.nearestTo(nonNamed));
    }
  }

  @Test
  void testSimple() {
    // These are fairly subjective values, any changes to the matching should be compared visually
    // We just want to avoid any unintended changes
    assertNearest(NamedTextColor.DARK_RED, 0xff0000);
    assertNearest(NamedTextColor.DARK_PURPLE, 0xff00ff);
    assertNearest(NamedTextColor.DARK_AQUA, 0x11aabb);
    assertNearest(NamedTextColor.GREEN, 0x88ff88);
    assertNearest(NamedTextColor.GRAY, 0xa2a2a2);
    assertNearest(NamedTextColor.DARK_GRAY, 0x4c4c4c);
  }

  private static void assertNearest(final NamedTextColor expected, final int value) {
    final NamedTextColor nearest = NamedTextColor.nearestTo(TextColor.color(value));
    assertEquals(expected, nearest);
  }
}
