/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;

public class RepeatTagTest extends AbstractTest {

  @Test
  void testSimpleRepeat() {
    final String input = "<repeat:5:'Hello World!'>";
    final Component expected = empty()
      .append(text("Hello World!"))
      .append(text("Hello World!"))
      .append(text("Hello World!"))
      .append(text("Hello World!"))
      .append(text("Hello World!"));
    assertParsedEquals(expected, input);
  }

  @Test
  void spaceRepeat() {
    final String input = "<repeat:5:' '>";
    final Component expected = empty()
      .append(text(" "))
      .append(text(" "))
      .append(text(" "))
      .append(text(" "))
      .append(text(" "));
    assertParsedEquals(expected, input);
  }

  @Test
  void fancyRepeat() {
    final String input = "A<idk_what_to_name><repeat:12:'<red>Mini</red><green>Message</green><newline><click:run_command:'/hello world'><hover:show_text:'AAA'>Test</hover></click>'>";
    final Component toRepeat = empty()
      .append(text("Mini", RED))
      .append(text("Message", GREEN))
      .append(newline())
      .append(text("Test").clickEvent(runCommand("/hello world")).hoverEvent(showText(text("AAA"))));
    final Component expected = empty().append(text("A")).append(
      text("cool placeholder value", style(BOLD))
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
      .append(toRepeat)
    );
    assertParsedEquals(expected, input, parsed("idk_what_to_name", "<bold>cool placeholder value"));
  }

}
