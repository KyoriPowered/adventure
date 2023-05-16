/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.plain;

import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlainTextComponentSerializerTest {
  @Test
  void testSimpleFrom() {
    assertEquals(Component.text("foo"), PlainTextComponentSerializer.plainText().deserialize("foo"));
  }

  @Test
  void testPreprocessing() {
    final Component expected = PlainTextComponentSerializer.plainText().deserialize("Hello, world!");

    final String input = "Hello";
    final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.builder().preProcessor(str -> str + ", world!").build();
    this.assertParsedEquals(plainText, expected, input);
  }

  @Test
  void testPostprocessing() {
    final Component expected = PlainTextComponentSerializer.plainText().deserialize("Hello, world!");

    final String input = "Hello, you!";
    final PlainTextComponentSerializer plainText = PlainTextComponentSerializer.builder().postProcessor(comp -> comp.replaceText(
      TextReplacementConfig.builder()
        .matchLiteral("you!")
        .replacement("world!")
        .build())
    ).build();
    this.assertParsedEquals(plainText, expected, input);
  }

  @Test
  void testToLegacy() {
    final TextComponent c1 = Component.text().content("hi")
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .append(
        Component.text("foo")
          .color(NamedTextColor.GREEN)
          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      )
      .append(
        Component.text("bar")
          .color(NamedTextColor.BLUE)
      )
      .append(Component.text("baz"))
      .build();
    assertEquals("hifoobarbaz", PlainTextComponentSerializer.plainText().serialize(c1));

    final TextComponent c2 = Component.text().content("Hello there, ")
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .append(
        Component.text("you")
          .color(NamedTextColor.GREEN)
          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      )
      .append(
        Component.text("!")
          .color(NamedTextColor.BLUE)
      )
      .build();
    assertEquals("Hello there, you!", PlainTextComponentSerializer.plainText().serialize(c2));
  }

  // temp
  protected void assertParsedEquals(final ComponentSerializer<Component, TextComponent, String> serializer, final Component expected, final String input) {
    final String expectedSerialized = this.prettyPrint(expected.compact());
    final String actual = this.prettyPrint(serializer.deserialize(input).compact());
    assertEquals(expectedSerialized, actual);
  }

  protected final String prettyPrint(final Component component) {
    return component.examine(MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining("\n"));
  }
}
