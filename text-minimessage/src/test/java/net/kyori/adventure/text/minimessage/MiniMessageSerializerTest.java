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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.format.Style.style;

public class MiniMessageSerializerTest extends AbstractTest {
  @Test
  void testSerializeNestedStyles() {
    // These are mostly arbitrary, but I don't want to test every single combination
    final Component component = Component.text()
      .append(Component.text("b+i+u", style(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED)))
      .append(Component.text("color+insert", style(NamedTextColor.RED)).insertion("meow"))
      .append(Component.text("st+font", style(TextDecoration.STRIKETHROUGH).font(Key.key("uniform"))))
      .append(Component.text("empty"))
      .build();
    final String expected = "<italic><underlined><bold>b+i+u</bold></underlined></italic>" +
      "<insert:meow><red>color+insert</red></insert>" +
      "<strikethrough><font:uniform>st+font</font></strikethrough>" +
      "empty";

    this.assertSerializedEquals(expected, component);
  }
}
