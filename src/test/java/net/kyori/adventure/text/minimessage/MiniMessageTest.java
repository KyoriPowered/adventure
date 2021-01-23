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

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static net.kyori.adventure.text.format.Style.style;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiniMessageTest {

  @Test
  void testMarkdownBuilder() {
    final Component expected = Component.text("BOLD", style(NamedTextColor.RED, TextDecoration.BOLD));
    final Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    assertEquals(expected, result);
  }

  @Test
  void testNormalBuilder() {
    final Component expected = Component.text("Test", NamedTextColor.RED);
    final Component result = MiniMessage.builder().build().deserialize("<red>Test");

    assertEquals(expected, result);
  }

  @Test
  void testNormal() {
    final Component expected = Component.text("Test", NamedTextColor.RED);
    final Component result = MiniMessage.get().deserialize("<red>Test");

    assertEquals(expected, result);
  }

  @Test
  void testNormalPlaceholders() {
    final Component expected = Component.text("TEST").color(NamedTextColor.RED);
    final Component result = MiniMessage.get().parse("<red><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testObjectPlaceholders() {
    final Component expected = Component.text()
      .append(Component.text("ONE", NamedTextColor.RED))
      .append(Component.text("TWO", NamedTextColor.GREEN))
      .append(Component.text("THREEFOUR", NamedTextColor.BLUE))
      .append(Component.text("FIVE", NamedTextColor.YELLOW))
      .build();
    final Component result = MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
            "two", Component.text("TWO", NamedTextColor.GREEN),
            "four", "FOUR",
            "five", Component.text("FIVE", NamedTextColor.YELLOW));

    assertEquals(expected, result);
  }

  @Test
  void testObjectPlaceholdersUnbalanced() {
    assertThrows(IllegalArgumentException.class, () -> MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
            "two", Component.text("TWO", NamedTextColor.GREEN),
            "four", "FOUR",
            "five"));
  }

  @Test
  void testMarkdown() {
    final Component expected = Component.text("BOLD", style(NamedTextColor.RED, TextDecoration.BOLD));
    final Component result = MiniMessage.markdown().deserialize("**<red>BOLD**");

    assertEquals(expected, result);
  }

  @Test
  void testTemplateSimple() {
    final Component expected = Component.text("TEST");
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", "TEST"));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponent() {
    final Component expected = Component.text("TEST", NamedTextColor.RED);
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", Component.text("TEST", NamedTextColor.RED)));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponentInheritedStyle() {
    final Component expected = Component.text("TEST", style(NamedTextColor.RED, TextDecoration.UNDERLINED, TextDecoration.BOLD));
    final Component result = MiniMessage.get().parse("<green><bold><test>", Template.of("test", Component.text("TEST", NamedTextColor.RED, TextDecoration.UNDERLINED)));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponentMixed() {
    final Component expected = Component.text()
      .append(Component.text("TEST", style(NamedTextColor.RED, TextDecoration.UNDERLINED, TextDecoration.BOLD)))
      .append(Component.text("Test2", style(NamedTextColor.GREEN, TextDecoration.BOLD)))
      .build();

    final Template t1 = Template.of("test", Component.text("TEST", style(NamedTextColor.RED, TextDecoration.UNDERLINED)));
    final Template t2 = Template.of("test2", "Test2");
    final Component result = MiniMessage.get().parse("<green><bold><test><test2>", t1, t2);

    assertEquals(expected, result);
  }

  @Test
  void testCustomRegistry() {
    final Component expected = Component.text()
      .append(Component.text("<bold>", NamedTextColor.GREEN))
      .append(Component.text("TEST", NamedTextColor.GREEN))
      .build();
    final Component result = MiniMessage.withTransformations(TransformationType.COLOR)
      .parse("<green><bold><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testCustomRegistryBuilder() {
    final Component expected = Component.text()
      .append(Component.text("<bold>", NamedTextColor.GREEN))
      .append(Component.text("TEST", NamedTextColor.GREEN))
      .build();
    final Component result = MiniMessage.builder()
      .removeDefaultTransformations()
      .transformation(TransformationType.COLOR)
      .build()
      .parse("<green><bold><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testPlaceholderResolver() {
    final Component expected = Component.text("TEST", style(NamedTextColor.RED, TextDecoration.BOLD));

    final Function<String, ComponentLike> resolver = name -> {
      if(name.equalsIgnoreCase("test")) {
        return Component.text("TEST").color(NamedTextColor.RED);
      }
      return null;
    };

    final Component result = MiniMessage.builder().placeholderResolver(resolver).build().parse("<green><bold><test>");

    assertEquals(expected, result);
  }

  @Test
  void testOrderOfPlaceholders() {
    final Component expected = Component.text()
      .append(Component.text("A"))
      .append(Component.text("B"))
      .append(Component.text("C"))
      .build();

    final Component result = MiniMessage.get().parse(
            "<a><b><_c>",
            "a", Component.text("A"),
            "b", Component.text("B"),
            "_c", Component.text("C")
    );

    assertEquals(expected, result);
  }
}
