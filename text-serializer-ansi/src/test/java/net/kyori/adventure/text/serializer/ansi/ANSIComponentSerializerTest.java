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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.ansi.ColorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ANSIComponentSerializerTest {
  @Test
  void testSimple() {
    final ANSIComponentSerializer indexed16 = ANSIComponentSerializer.builder().colorLevel(ColorLevel.INDEXED_16).build();

    assertEquals("foo", indexed16.serialize(
      Component.text("foo")));
    assertEquals("\u001B[91mfoo\u001B[0m", indexed16.serialize(
      Component.text("foo", NamedTextColor.RED)));
    assertEquals("\u001B[1mfoo\u001B[0m", indexed16.serialize(
      Component.text("foo").decorate(TextDecoration.BOLD)));

    TextComponent component = Component.text().content("")
      .append(Component.text("foo", NamedTextColor.GREEN))
      .append(Component.text("bar", NamedTextColor.BLUE))
      .build();
    assertEquals("\u001B[92mfoo\u001B[94mbar\u001B[0m",
      indexed16.serialize(component));

    component = Component.text().content("")
      .append(Component.text("foo", NamedTextColor.GREEN, TextDecoration.BOLD))
      .append(Component.text("bar", NamedTextColor.BLUE))
      .build();
    assertEquals("\u001B[1m\u001B[92mfoo\u001B[0m\u001B[94mbar\u001B[0m",
      indexed16.serialize(component));
  }
}
