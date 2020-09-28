/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MiniMessageTest {

  @Test
  public void testMarkdownBuilder() {
    Component expected = Component.text("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormalBuilder() {
    Component expected = Component.text("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.builder().build().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormal() {
    Component expected = Component.text("Test").color(NamedTextColor.RED);
    Component result = MiniMessage.get().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testNormalPlaceholders() {
    Component expected = Component.text("TEST").color(NamedTextColor.RED);
    Component result = MiniMessage.get().parse("<red><test>", "test", "TEST");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testMarkdown() {
    Component expected = Component.text("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    Component result = MiniMessage.markdown().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testTemplateSimple() {
    Component expected = Component.text("TEST");
    Component result = MiniMessage.get().parse("<test>", Template.of("test", "TEST"));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testTemplateComponent() {
    Component expected = Component.text("TEST").color(NamedTextColor.RED);
    Component result = MiniMessage.get().parse("<test>", Template.of("test", Component.text("TEST").color(NamedTextColor.RED)));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testTemplateComponentInheritedStyle() {
    Component expected = Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true);
    Component result = MiniMessage.get().parse("<green><bold><test>", Template.of("test", Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  public void testTemplateComponentMixed() {
    Component root = Component.text("");
    root = root.append(Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true));
    root = root.append(Component.text("Test2").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, true));

    final Template t1 = Template.of("test", Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true));
    final Template t2 = Template.of("test2", "Test2");
    final Component result = MiniMessage.get().parse("<green><bold><test><test2>", t1, t2);

    final String out1 = GsonComponentSerializer.gson().serialize(root);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }
}
