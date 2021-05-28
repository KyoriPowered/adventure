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
import net.kyori.adventure.text.minimessage.transformation.TransformationType;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MiniMessageTest {

  @Test
  void testMarkdownBuilder() {
    final Component expected = empty().decorate(BOLD).append(empty().color(RED).append(text("BOLD")));
    final Component result = MiniMessage.builder().markdown().build().deserialize("**<red>BOLD**");

    assertEquals(expected, result);
  }

  @Test
  void testNormalBuilder() {
    final Component expected = empty().color(RED).append(text("Test"));
    final Component result = MiniMessage.builder().build().deserialize("<red>Test");

    assertEquals(expected, result);
  }

  @Test
  void testNormal() {
    final Component expected = empty().color(RED).append(text("Test"));
    final Component result = MiniMessage.get().deserialize("<red>Test");

    assertEquals(expected, result);
  }

  @Test
  void testNormalPlaceholders() {
    final Component expected = empty().color(RED).append(text("TEST"));
    final Component result = MiniMessage.get().parse("<red><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testObjectPlaceholders() {
    final Component expected = empty().color(RED)
            .append(text("ONE"))
            .append(text("TWO", GREEN)
                    .append(empty().color(BLUE)
                            .append(text("THREEFOUR"))
                            .append(text("FIVE", YELLOW))
                    )
            );
    final Component result = MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
            "two", text("TWO", GREEN),
            "four", "FOUR",
            "five", text("FIVE", YELLOW));

    assertEquals(expected, result);
  }

  @Test
  void testObjectPlaceholdersUnbalanced() {
    assertThrows(IllegalArgumentException.class, () -> MiniMessage.get().parse("<red>ONE<two><blue>THREE<four><five>",
            "two", text("TWO", GREEN),
            "four", "FOUR",
            "five"));
  }

  @Test
  void testMarkdown() {
    final Component expected = empty().decorate(BOLD).append(empty().color(RED).append(text("BOLD")));
    final Component result = MiniMessage.markdown().deserialize("**<red>BOLD**");

    assertEquals(expected, result);
  }

  @Test
  void testTemplateSimple() {
    final Component expected = text("TEST");
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", "TEST"));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponent() {
    final Component expected = text("TEST", RED);
    final Component result = MiniMessage.get().parse("<test>", Template.of("test", text("TEST", RED)));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponentInheritedStyle() {
    final Component expected = empty().color(GREEN).append(empty().decorate(BOLD).append(text("TEST", RED, UNDERLINED)));
    final Component result = MiniMessage.get().parse("<green><bold><test>", Template.of("test", text("TEST", RED, UNDERLINED)));

    assertEquals(expected, result);
  }

  @Test
  void testTemplateComponentMixed() {
    final Component expected = empty().color(GREEN)
            .append(empty().decorate(BOLD)
                    .append(text("TEST", style(RED, UNDERLINED)))
                    .append(text("Test2"))
            );

    final Template t1 = Template.of("test", text("TEST", style(RED, UNDERLINED)));
    final Template t2 = Template.of("test2", "Test2");
    final Component result = MiniMessage.get().parse("<green><bold><test><test2>", t1, t2);

    assertEquals(expected, result);
  }

  @Test // GH-103
  void testTemplateInHover() {
    final Component expected = empty().hoverEvent(showText(empty().color(color(0xff0000)).append(text("[Plugin]"))))
                    .append(text("This is a test message."));
    final Component result = MiniMessage.get().parse("<hover:show_text:'<prefix>'>This is a test message.", Template.of("prefix", MiniMessage.get().parse("<#FF0000>[Plugin]<reset>")));

    assertEquals(expected, result);
  }

  @Test
  void testCustomRegistry() {
    final Component expected = empty().color(GREEN).append(text("<bold>").append(text("TEST")));
    final Component result = MiniMessage.withTransformations(TransformationType.COLOR)
      .parse("<green><bold><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testCustomRegistryBuilder() {
    final Component expected = empty().color(GREEN).append(text("<bold>").append(text("TEST")));
    final Component result = MiniMessage.builder()
      .removeDefaultTransformations()
      .transformation(TransformationType.COLOR)
      .build()
      .parse("<green><bold><test>", "test", "TEST");

    assertEquals(expected, result);
  }

  @Test
  void testPlaceholderResolver() {
    final Component expected = empty().color(GREEN).append(empty().decorate(BOLD).append(text("TEST", RED)));

    final Function<String, ComponentLike> resolver = name -> {
      if(name.equalsIgnoreCase("test")) {
        return text("TEST").color(RED);
      }
      return null;
    };

    final Component result = MiniMessage.builder().placeholderResolver(resolver).build().parse("<green><bold><test>");

    assertEquals(expected, result);
  }

  @Test
  void testOrderOfPlaceholders() {
    final Component expected = text("A").append(text("B").append(text("C")));

    final Component result = MiniMessage.get().parse(
            "<a><b><_c>",
            "a", text("A"),
            "b", text("B"),
            "_c", text("C")
    );

    assertEquals(expected, result);
  }

  @Test
  void testUnbalancedPlaceholders() {
    final String expected = "Argument 1 in placeholders is a value, must be Component or String, was java.lang.Integer";
    assertEquals(expected, assertThrows(IllegalArgumentException.class, () -> MiniMessage.get().parse("<a>", "a", 2)).getMessage());
  }

  @Test // GH-98
  void testTemplateInsideOfPre() {
    final Component expected = empty().color(RED)
            .append(text("MiniDigger")
                    .append(empty().color(GRAY)
                            .append(text(": "))
                            .append(text("<red><message>"))
                    )
            );
    final String input = "<red><username><gray>: <pre><red><message>";

    final Component result = MiniMessage.get().parse(input, Template.of("username", text("MiniDigger")), Template.of("message", text("Hello world")));

    assertEquals(expected, result);
  }

  @Test // GH-97
  void testUnsafePre() {
    final Component expected = empty().color(RED)
            .append(text("MiniDigger")
                            .append(empty().color(GRAY)
                                    .append(text(": "))
                                    .append(text("<red><message>"))
                            )
            );
    final Component expected2 = empty().color(RED)
            .append(text("MiniDigger"))
            .append(empty().color(GRAY)
                    .append(text(": "))
                    .append(text("<red></pre><red>Test"))
            );
    final String input = "<red><username><gray>: <pre><red><message>";

    final Component result1 = MiniMessage.get().parse(input, Template.of("username", text("MiniDigger")), Template.of("message", text("</pre><red>Test")));
    assertEquals(expected, result1);

    final Component result2 = MiniMessage.get().parse(input, Template.of("username", "MiniDigger"), Template.of("message", "</pre><red>Test"));
    assertEquals(expected2, result2);

    final Component result3 = MiniMessage.get().parse(input, "username", "MiniDigger", "message", "</pre><red>Test");
    assertEquals(expected2, result3);
  }

  @Test
  void testLazyTemplate() {
    final Component expected = empty()
            .append(text("This is a "))
            .append(text("TEST"));
    final String input = "This is a <test>";

    final Component result = MiniMessage.get().parse(input, Template.of("test", () -> text("TEST")));

    assertEquals(expected, result);
  }
}
