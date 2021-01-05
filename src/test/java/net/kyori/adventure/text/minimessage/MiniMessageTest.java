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
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageTest {

  @Test
  void testMarkdownBuilder() {
    final Component expected = Component.text("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    final Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testNormalBuilder() {
    final Component expected = Component.text("Test").color(NamedTextColor.RED);
    final Component result = MiniMessage.builder().build().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testNormal() {
    final Component expected = Component.text("Test").color(NamedTextColor.RED);
    final Component result = MiniMessage.get().deserialize("<red>Test");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testNormalPlaceholders() {
    final Component expected = Component.text("TEST").color(NamedTextColor.RED);
    final Component result = MiniMessage.get().parse("<red><test>", "test", "TEST");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testObjectPlaceholders() {
    final Component expected = Component.text("")
            .append(Component.text("ONE").color(NamedTextColor.RED))
            .append(Component.text("TWO").color(NamedTextColor.GREEN))
            .append(Component.text("THREEFOUR").color(NamedTextColor.BLUE))
            .append(Component.text("FIVE").color(NamedTextColor.YELLOW));
    final Component result = MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
            "two", Component.text("TWO").color(NamedTextColor.GREEN),
            "four", "FOUR",
            "five", Component.text("FIVE").color(NamedTextColor.YELLOW));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testMarkdown() {
    final Component expected = Component.text("BOLD").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED);
    final Component result = MiniMessage.markdown().deserialize("**<red>BOLD**");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testTemplateSimple() {
    final Component expected = Component.text("TEST");
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", "TEST"));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testTemplateComponent() {
    final Component expected = Component.text("TEST").color(NamedTextColor.RED);
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", Component.text("TEST").color(NamedTextColor.RED)));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testTemplateComponentInheritedStyle() {
    final Component expected = Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true).decoration(TextDecoration.BOLD, true);
    final Component result = MiniMessage.get().parse("<green><bold><test>", Template.of("test", Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testTemplateComponentMixed() {
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

  @Test
  void testCustomRegistry() {
    Component root = Component.text("");
    root = root.append(Component.text("<bold>").color(NamedTextColor.GREEN));
    root = root.append(Component.text("TEST").color(NamedTextColor.GREEN));
    final Component result = MiniMessage.withTransformations(TransformationType.COLOR).parse("<green><bold><test>", "test", "TEST");

    final String out1 = GsonComponentSerializer.gson().serialize(root);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testCustomRegistryBuilder() {
    Component root = Component.text("");
    root = root.append(Component.text("<bold>").color(NamedTextColor.GREEN));
    root = root.append(Component.text("TEST").color(NamedTextColor.GREEN));
    final Component result = MiniMessage.builder().removeDefaultTransformations().transformation(TransformationType.COLOR).build().parse("<green><bold><test>", "test", "TEST");

    final String out1 = GsonComponentSerializer.gson().serialize(root);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }

  @Test
  void testPlaceholderResolver() {
    final Component expected = Component.text("TEST").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, true);

    final Function<String, ComponentLike> resolver = name -> {
      if(name.equalsIgnoreCase("test")) {
        return Component.text("TEST").color(NamedTextColor.RED);
      }
      return null;
    };

    final Component result = MiniMessage.builder().placeholderResolver(resolver).build().parse("<green><bold><test>");

    final String out1 = GsonComponentSerializer.gson().serialize(expected);
    final String out2 = GsonComponentSerializer.gson().serialize(result);

    assertEquals(out1, out2);
  }
}
