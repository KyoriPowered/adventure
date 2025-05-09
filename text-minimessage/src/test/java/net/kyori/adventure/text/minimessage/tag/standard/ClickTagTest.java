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
package net.kyori.adventure.text.minimessage.tag.standard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;

class ClickTagTest extends AbstractTest {
  @Test
  void testSerializeClick() {
    final String expected = "<click:run_command:'test'>Some click</click> that ends here";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text("Some click").clickEvent(ClickEvent.runCommand("test")))
      .append(Component.text(" that ends here"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeContinuedClick() {
    final String expected = "<click:run_command:'test'>Some click<red> that doesn't end here";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text("Some click").clickEvent(ClickEvent.runCommand("test"))
        .append(Component.text(" that doesn't end here", NamedTextColor.RED)));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testClick() {
    final String input = "<click:run_command:test>TEST";
    final Component expected = text("TEST").clickEvent(runCommand("test"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testClickExtendedCommand() {
    final String input = "<click:run_command:/test command>TEST";
    final Component expected = text("TEST").clickEvent(runCommand("/test command"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testInvalidClick() {
    final String input = "<click:pet_a_kitty:'a very cute one'>best click event";

    final Component expected = text("<click:pet_a_kitty:'a very cute one'>best click event");

    this.assertParsedEquals(expected, input);
  }
}
