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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMessageTemplateProcessorTest {
  private static final StringTemplate.Processor<Component, ParsingException> MM = MiniMessageTemplateProcessor.templateProcessor(MiniMessage.miniMessage());
  @Test
  void testNoArgs() {
    final var simple = MM."hello";

    assertEquals(Component.text("hello"), simple);
  }

  @Test
  void testStyleApplication() {
    final var withStyle = MM."hello \{NamedTextColor.RED}world";
    final Component expected = Component.text()
      .content("hello ")
      .append(Component.text("world", NamedTextColor.RED))
      .build();

    assertEquals(expected, withStyle);
  }

  @Test
  void testComponentTag() {
    final Component input = Component.text("meow :3").hoverEvent(Component.text("hii", NamedTextColor.AQUA));
    final var template = MM."<u>Hello there \{input}";

    final Component expected = Component.text()
      .content("Hello there ")
      .decorate(TextDecoration.UNDERLINED)
      .append(input).build();

    assertEquals(expected, template);
  }

  @Test
  void testOnlyComponent() {
    final var template = MM."\{Component.text("hello world!", NamedTextColor.RED)}";
    assertEquals(Component.text("hello world!", NamedTextColor.RED), template);
  }
}
