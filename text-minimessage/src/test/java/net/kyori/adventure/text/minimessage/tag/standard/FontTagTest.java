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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

class FontTagTest extends AbstractTest {
  @Test
  void testSerializeFont() {
    final String expected = "<font:default>This is a </font>test";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text().content("This is a ").font(key("minecraft", "default")))
      .append(Component.text("test"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testFont() {
    final String input = "Nothing <font:minecraft:uniform>Uniform <font:minecraft:alt>Alt  </font> Uniform";
    final Component expected = text("Nothing ")
      .append(empty().style(s -> s.font(key("uniform")))
        .append(text("Uniform "))
        .append(text("Alt  ").style(s -> s.font(key("alt"))))
        .append(text(" Uniform"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testCustomFont() {
    final String input = "Default <font:myfont:best_font>Custom font <font:custom:worst_font>Another custom font </font>Back to previous font";
    final Component expected = text("Default ")
      .append(empty().style(s -> s.font(key("myfont", "best_font")))
        .append(text("Custom font "))
        .append(text("Another custom font ").style(s -> s.font(key("custom", "worst_font"))))
        .append(text("Back to previous font"))
      );

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testFontNoNamespace() {
    final String input = "Nothing <font:uniform>Uniform <font:alt>Alt  </font> Uniform";
    final Component expected = text("Nothing ")
      .append(empty().style(s -> s.font(key("uniform")))
        .append(text("Uniform "))
        .append(text("Alt  ").style(s -> s.font(key("alt"))))
        .append(text(" Uniform"))
      );

    this.assertParsedEquals(expected, input);
  }
}
