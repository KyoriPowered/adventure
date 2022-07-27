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

import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextColor.color;

public class ProgressTagTest extends AbstractTest {
  @Test
  void testConstantProgress() {
    final String input = "<progress:0.5:|:10:green:#696969>";
    final Component filled = text("|", GREEN);
    final Component empty = text("|", color(0x696969));
    final Component expected = empty()
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(empty)
      .append(empty)
      .append(empty)
      .append(empty)
      .append(empty);
    assertParsedEquals(expected, input);
  }

  @Test
  void testSupplierProgress() {
    final Supplier<Double> progressSupplier = () -> 0.777;
    final String input = "<why_am_i_doing_this:|:12:blue:#FF5555>";
    final Component filled = text("|", BLUE);
    final Component empty = text("|", color(0xFF5555));
    final Component expected = empty()
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(filled)
      .append(empty)
      .append(empty)
      .append(empty);
    assertParsedEquals(expected, input, Formatter.progress("why_am_i_doing_this", progressSupplier));
  }
}
