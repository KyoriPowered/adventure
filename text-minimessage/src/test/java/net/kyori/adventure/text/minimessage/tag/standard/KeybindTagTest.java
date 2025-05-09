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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.keybind;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

class KeybindTagTest extends AbstractTest {
  @Test
  void testSerializeKeyBind() {
    final String expected = "Press <key:key.jump/> to jump!";

    final TextComponent.Builder builder = Component.text()
      .content("Press ")
      .append(Component.keybind("key.jump"))
      .append(Component.text(" to jump!"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeKeyBindWithColor() {
    final String expected = "Press <red><key:key.jump> to jump!";

    final TextComponent.Builder builder = Component.text()
      .content("Press ")
      .append(Component.keybind("key.jump", NamedTextColor.RED)
        .append(Component.text(" to jump!")));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testKeyBind() {
    final String input = "Press <key:key.jump> to jump!";
    final Component expected = text("Press ")
      .append(keybind("key.jump")
        .append(text(" to jump!")));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testKeyBindWithColor() {
    final String input = "Press <red><key:key.jump> to jump!";
    final Component expected = text("Press ")
      .append(
        keybind("key.jump", RED)
          .append(text(" to jump!"))
      );

    this.assertParsedEquals(expected, input);
  }
}
