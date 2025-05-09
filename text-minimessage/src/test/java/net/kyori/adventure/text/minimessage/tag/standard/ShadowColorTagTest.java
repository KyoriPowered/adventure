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
package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.format.ShadowColor.shadowColor;

class ShadowColorTagTest extends AbstractTest {
  @Test
  void testNoShadow() {
    final String input = "now i'm here <!shadow>and now i'm not!";
    final Component expected = Component.text("now i'm here ")
      .append(Component.text("and now i'm not!", Style.style(ShadowColor.none())));

    this.assertParsedEquals(expected, input);
    this.assertSerializedEquals(input, expected);
  }

  @Test
  void testRoundtripNamedShadow() {
    final String input = "<shadow:red:0.8>i have a red shadow";
    final Component expected = Component.text("i have a red shadow")
      .shadowColor(shadowColor(NamedTextColor.RED, 0xCC));

    this.assertParsedEquals(expected, input);
    this.assertSerializedEquals(input, expected);
  }

  @Test
  void testParseHexComponentShadow() {
    final String input = "<shadow:#FF0000:0.8>i have a redder shadow";
    final Component expected = Component.text("i have a redder shadow")
      .shadowColor(shadowColor(0xFF, 0, 0, 0xCC));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeShadow() {
    final String expected = "<shadow:#054D79FF>This is a test";

    final Component builder = Component.text("This is a test")
      .shadowColor(shadowColor(0xff_05_4d_79));

    this.assertSerializedEquals(expected, builder);
    this.assertParsedEquals(builder, expected);
  }

  @Test
  void testSerializeShadowClosing() {
    final String expected = "<shadow:#054D79FF>This is a</shadow> test";

    final Component builder = Component.text()
      .append(Component.text("This is a")
        .shadowColor(shadowColor(0xff_05_4d_79)))
      .append(Component.text(" test")).asComponent();

    this.assertSerializedEquals(expected, builder);
    this.assertParsedEquals(builder, expected);
  }
}
