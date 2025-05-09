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
package net.kyori.adventure.text.minimessage.tag;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlaceholderTest extends AbstractTest {

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/190
  @Test
  void testCaseOfPlaceholders() {
    assertThrows(IllegalArgumentException.class, () -> Placeholder.parsed("HI", "hi"));
    assertThrows(IllegalArgumentException.class, () -> Placeholder.component("HI", text("hi")));
    assertThrows(IllegalArgumentException.class, () -> Placeholder.component("HI", () -> text("hi")));
  }

  @Test
  void checkPlaceholder() {
    final String input = "<test>";
    final Component expected = text("Hello!");
    final Component comp = PARSER.deserialize(input, parsed("test", "Hello!"));

    assertEquals(expected, comp);
  }

  @Test
  void testPlaceholderOrder() {
    final Component expected = empty().color(GRAY)
      .append(text("ONE"))
      .append(empty().color(RED)
        .append(text("TWO"))
        .append(text(" "))
        .append(text("THREE"))
        .append(text(" "))
        .append(text("FOUR"))
      );
    final String input = "<gray><arg1><red><arg2> <arg3> <arg4>";

    this.assertParsedEquals(
      expected,
      input,
      component("arg1", text("ONE")),
      component("arg2", text("TWO")),
      component("arg3", text("THREE")),
      component("arg4", text("FOUR"))
    );
  }

  @Test
  void testPlaceholderOrder2() {
    final Component expected = empty()
      .append(text("ONE").color(GRAY))
      .append(text("TWO").color(RED))
      .append(text("THREE").color(BLUE))
      .append(text(" "))
      .append(text("FOUR").color(GREEN));
    final String input = "<gray><arg1></gray><red><arg2></red><blue><arg3></blue> <green><arg4>";

    this.assertParsedEquals(
      expected,
      input,
      component("arg1", text("ONE")),
      component("arg2", text("TWO")),
      component("arg3", text("THREE")),
      component("arg4", text("FOUR"))
    );
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/146
  @Test
  void testStringPlaceholderInCommand() {
    final String input = "<click:run_command:'word <word>'><gold>Click to run the word!";
    final Component expected = text("Click to run the word!", GOLD)
      .clickEvent(ClickEvent.runCommand("word Adventure"));
    this.assertParsedEquals(expected, input, parsed("word", "Adventure"));
  }

  @Test
  void testInvalidStringPlaceholderInCommand() {
    final String input = "<click:run_command:'word <unknown> </word>'><gold>Click to run the word!";
    final Component expected = text("Click to run the word!", GOLD)
      .clickEvent(ClickEvent.runCommand("word <unknown> </word>"));
    this.assertParsedEquals(expected, input, component("word", text("Adventure")));
  }

  @Test
  void testRepeatedResolvingOfStringPlaceholders() {
    final String input = "<animal> makes a sound";

    final Component expected = text("cat makes a sound", RED);

    this.assertParsedEquals(
      expected,
      input,
      parsed("animal", "<red><feline>"),
      component("feline", text("cat"))
    );
  }
}
