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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.Style.style;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageSerializerTest extends AbstractTest {
  @Test
  void testSerializeNestedStyles() {
    // These are mostly arbitrary, but I don't want to test every single combination
    final Component component = text()
      .append(text("b+i+u", style(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED)))
      .append(text("color+insert", style(NamedTextColor.RED)).insertion("meow"))
      .append(text("st+font", style(TextDecoration.STRIKETHROUGH).font(Key.key("uniform"))))
      .append(text("empty"))
      .build();
    final String expected = "<italic><underlined><bold>b+i+u</bold></underlined></italic>" +
      "<insert:meow><red>color+insert</red></insert>" +
      "<strikethrough><font:uniform>st+font</font></strikethrough>" +
      "empty";

    this.assertSerializedEquals(expected, component);
  }

  // https://github.com/KyoriPowered/adventure/issues/526
  @Test
  void testTagsClosedInStrictMode() {
    final MiniMessage serializer = MiniMessage.builder().strict(true).build();

    final String expected = "hello<red>red<bold>bold</bold></red>";
    final Component input = text()
      .content("hello")
      .append(
        text("red", NamedTextColor.RED)
          .append(
            text("bold", style(TextDecoration.BOLD))
          )
      )
      .build();

    assertEquals(expected, serializer.serialize(input));
  }

  @Test
  void testDoubleOpenRoundTrippedEscaped() {
    final String expected = "\\<\\<aqua> a"; // this is valid but there is a redundant serialization
    final Component component = text("<<aqua> a");
    this.assertSerializedEquals(expected, component);
    this.assertParsedEquals(component, expected);
  }

}
