/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniMessageTest {

  @Test
  public void testMarkdownBuilder() {
    Component expected = TextComponent.of("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormalBuilder() {
    Component expected = TextComponent.of("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.builder().build().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormal() {
    Component expected = TextComponent.of("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.instance().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormalPlaceholders() {
    Component expected = TextComponent.of("TEST").color(NamedTextColor.RED);
    Component result = MiniMessage.instance().parse("<red><test>", "test", "TEST");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testMarkdown() {
    Component expected = TextComponent.of("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.withMarkDown().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }
}
