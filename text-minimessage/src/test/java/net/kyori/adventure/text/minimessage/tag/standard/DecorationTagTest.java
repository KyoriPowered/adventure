/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

class DecorationTagTest extends AbstractTest {

  @Test
  void testDisabledDecoration() {
    final String input = "<italic:false>Test<bold:false>Test2<bold>Test3";
    final Component expected = text().decoration(ITALIC, false)
      .append(text("Test"))
      .append(text().decoration(BOLD, false)
        .append(text("Test2"))
        .append(text("Test3").decorate(BOLD))
      ).build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testDisabledDecorationShorthand() {
    final String input = "<!italic>Test<!bold>Test2<bold>Test3";
    final Component expected = text().decoration(ITALIC, false)
      .append(text("Test"))
      .append(text().decoration(BOLD, false)
        .append(text("Test2"))
        .append(text("Test3").decorate(BOLD))
      ).build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testErrorOnShorthandAndLongHand() {
    final String input = "<!italic:true>Go decide on something, god dammit!";
    final Component expected = text("<!italic:true>Go decide on something, god dammit!");
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testDecorationShorthandClosing() {
    final String input = "<italic:false>Hello! <italic>spooky</italic> not spooky</italic:false>";
    final Component expected = text().decoration(ITALIC, false)
      .append(text("Hello! "))
      .append(text().decoration(ITALIC, true)
        .append(text("spooky")))
      .append(text(" not spooky"))
      .build();
    this.assertParsedEquals(expected, input);
  }
}
