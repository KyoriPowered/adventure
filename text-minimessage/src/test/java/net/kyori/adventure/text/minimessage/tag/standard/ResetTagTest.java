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
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextColor.color;

class ResetTagTest extends AbstractTest {
  @Test
  void testReset() {
    final String input = "Click <yellow><insert:test>this<rainbow> wooo<reset> to insert!";
    final Component expected = text("Click ")
      .append(empty().color(YELLOW).insertion("test")
        .append(text("this"))
        .append(virtualOfChildren(textOfChildren(
          text(" ", color(0xff0000)),
          text("w", color(0xcbff00)),
          text("o", color(0x00ff66)),
          text("o", color(0x0065ff)),
          text("o", color(0xcc00ff))
        )))
      ).append(text(" to insert!"));

    this.assertParsedEquals(expected, input);
  }
}
