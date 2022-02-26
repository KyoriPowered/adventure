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
package net.kyori.adventure.text.minimessage.tag;

import java.time.LocalDateTime;
import java.time.Month;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.formatDate;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.formatNumber;

public class FormatterTest extends AbstractTest {
  @Test
  void testNumberFormatter() {
    final String input = "<mynumber:'en-EN':'#.00'> is a nice number";
    final Component expected = text("20.00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      formatNumber("mynumber", 20d)
    );
  }

  @Test
  void testNumberFormatterLang() {
    final String input = "<mynumber:'de-DE':'#.00'> is a nice number";
    final Component expected = text("20,00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      formatNumber("mynumber", 20d)
    );
  }

  @Test
  void testNumberFormatterWithDecimal() {
    final String input = "<double:'de-DE':'#,##0.00'> is a nice number";
    final Component expected = text("2.000,00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      formatNumber("double", 2000d)
    );
  }

  @Test
  void testNumberFormatterNegative() {
    final String input = "<double:'en-EN':'<green>#.00;<red>-#.00'><blue> is a nice number";
    final Component expectedNegative = text("-5.00", NamedTextColor.RED).append(text(" is a nice number", NamedTextColor.BLUE));
    final Component expectedPositive = text("5.00", NamedTextColor.GREEN).append(text(" is a nice number", NamedTextColor.BLUE));

    this.assertParsedEquals(
      expectedNegative,
      input,
      formatNumber("double", -5)
    );
    this.assertParsedEquals(
      expectedPositive,
      input,
      formatNumber("double", 5)
    );
  }

  @Test
  void testDateFormatter() {
    final String input = "<date:'yyyy-MM-dd HH:mm:ss'> is a date";
    final Component expected = text("2022-02-26 21:00:00 is a date");

    this.assertParsedEquals(
      expected,
      input,
      formatDate("date", LocalDateTime.of(2022, Month.FEBRUARY, 26, 21, 0, 0))
    );
  }
}
