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
package net.kyori.adventure.text.minimessage.tag.extra;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CssColorTagTest {

  protected static final MiniMessage PARSER = MiniMessage.builder().editTags(b -> b.resolver(ExtraTags.cssColors())).debug(System.out::print).build();

  @Test
  void singleCssColor() {
    final String input = "<aliceblue>This is a test";

    final Component expected = Component.text("This is a test", TextColor.color(0xf0f8ff));

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }

  @Test
  void multipleCssColors() {
    final String input = "<aliceblue>Blue</aliceblue> White <orange>Orange";

    final Component expected = Component.empty()
      .append(Component.text("Blue").color(TextColor.color(0xf0f8ff)))
      .append(Component.text(" White "))
      .append(Component.text("Orange").color(TextColor.color(0xffa500)));

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }

  @Test
  void multipleCssColorsUsingArgs() {
    final String input = "<css:aliceblue>Blue</css:aliceblue> White <css:orange>Orange";

    final Component expected = Component.empty()
      .append(Component.text("Blue").color(TextColor.color(0xf0f8ff)))
      .append(Component.text(" White "))
      .append(Component.text("Orange").color(TextColor.color(0xffa500)));

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }

  @Test
  void mcColorThatExistsInCss() {
    final String input = "<aqua>This should be css aqua";

    final Component expected = Component.text("This should be css aqua", TextColor.color(0x00ffff));

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }

  @Test
  void specifyMcColorThatExistsInCss() {
    final String input = "<c:aqua>This should be minecraft aqua";

    final Component expected = Component.text("This should be minecraft aqua", NamedTextColor.AQUA);

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }

  @Test
  void cssAndMcColors() {
    final String input = "<c:aqua>MC Aqua</c:aqua> White <css:aqua>CSS Aqua</css:aqua>";

    final Component expected = Component.empty()
      .append(Component.text("MC Aqua", NamedTextColor.AQUA))
      .append(Component.text(" White "))
      .append(Component.text("CSS Aqua", TextColor.color(0x00ffff)));

    Assertions.assertEquals(expected, PARSER.deserialize(input));
  }
}
