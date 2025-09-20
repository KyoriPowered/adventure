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
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;

public class StyleTagTest extends AbstractTest {

  @Test
  void testEmptyTag() {
    final String input = "<style>Hey there!</style>";
    final Component expected = text("Hey there!");
    assertParsedEquals(expected, input);
  }

  @Test
  void testBasic() {
    final String input = "<style color=#f00 b i u>Fancy</style>";
    final Component expected = text("Fancy", TextColor.fromCSSHexString("#f00"), TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED);
    assertParsedEquals(expected, input);
  }

  @Test
  void testInversion() {
    final String input = "<b>Bold, <style !b>not bold</style>, bold!";
    final Component expected = text()
      .decorate(TextDecoration.BOLD)
      .append(text("Bold, "))
      .append(text("not bold", Style.style(b -> b.decoration(TextDecoration.BOLD, TextDecoration.State.FALSE))))
      .append(text(", bold!"))
      .build();
    assertParsedEquals(expected, input);
  }

  @Test
  void testPrecedence() {
    final String input = "<style c=#fff color=#000>A cool color";
    final Component expected = text("A cool color", TextColor.fromCSSHexString("#000"));
    assertParsedEquals(expected, input);
  }

  @Test
  void testShadowColor() {
    final String input = "<style c=#2f2 s=#ffaa00bb i>F a n c y";
    final Component expected = text("F a n c y", TextColor.fromCSSHexString("#2f2"), TextDecoration.ITALIC)
      .shadowColor(ShadowColor.fromHexString("#ffaa00bb"));
    assertParsedEquals(expected, input);
  }
}
