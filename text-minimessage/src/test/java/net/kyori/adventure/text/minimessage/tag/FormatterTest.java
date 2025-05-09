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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.booleanChoice;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.choice;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.date;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.joining;
import static net.kyori.adventure.text.minimessage.tag.resolver.Formatter.number;

public class FormatterTest extends AbstractTest {
  @Test
  void testNumberFormatter() {
    final String input = "<mynumber:'en-EN':'#.00'> is a nice number";
    final Component expected = text("20.00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      number("mynumber", 20d)
    );
  }

  @Test
  void testNumberFormatterLang() {
    final String input = "<mynumber:'de-DE':'#.00'> is a nice number";
    final Component expected = text("20,00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      number("mynumber", 20d)
    );
  }

  @Test
  void testNumberFormatterWithDecimal() {
    final String input = "<double:'de-DE':'#,##0.00'> is a nice number";
    final Component expected = text("2.000,00 is a nice number");

    this.assertParsedEquals(
      expected,
      input,
      number("double", 2000d)
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
      number("double", -5)
    );
    this.assertParsedEquals(
      expectedPositive,
      input,
      number("double", 5)
    );
  }

  @Test
  void testDateFormatter() {
    final String input = "<date:'yyyy-MM-dd HH:mm:ss'> is a date";
    final Component expected = text("2022-02-26 21:00:00 is a date");

    this.assertParsedEquals(
      expected,
      input,
      date("date", LocalDateTime.of(2022, Month.FEBRUARY, 26, 21, 0, 0))
    );
  }

  @Test
  void testChoiceFormatter() {
    final String input = "<choice:'-2#is small|-1#minus one|0#zero|1#one|1<is big'> result";
    final Component verySmall = text("is small result");
    final Component minusOne = text("minus one result");
    final Component zero = text("zero result");
    final Component one = text("one result");
    final Component bigResult = text("is big result");

    this.assertParsedEquals(verySmall, input, choice("choice", -5));
    this.assertParsedEquals(minusOne, input, choice("choice", -1));
    this.assertParsedEquals(zero, input, choice("choice", 0));
    this.assertParsedEquals(one, input, choice("choice", 1));
    this.assertParsedEquals(bigResult, input, choice("choice", 2));
  }

  @Test
  void testBooleanChoice() {
    final String input = "<first:'<second:\\'<third:\"bah\":\"\">\\':\\'\\'>':''>";
    final Component expected = text("bah");
    this.assertParsedEquals(expected, input, booleanChoice("first", true), booleanChoice("second", true), booleanChoice("third", true));
  }

  @Test
  void testJoinSeparator() {
    final String input = "<list:, >";
    final Iterable<? extends Component> components = Arrays.asList(text("one"), text("two"), text("three"));
    final Component expected = Component.join(JoinConfiguration.separator(text(", ")), components);
    this.assertParsedEquals(expected, input, joining("list", components));
  }

  @Test
  void testJoinSeparatorWithLastSeparator() {
    final String input = "<list:, : and >";
    final Iterable<? extends Component> components = Arrays.asList(text("one"), text("two"), text("three"));
    final Component expected = Component.join(JoinConfiguration.separators(text(", "), text(" and ")), components);
    this.assertParsedEquals(expected, input, joining("list", components));
  }

  @Test
  void testJoinSeparatorWithLastSeparatorIfSerialAndManyComponents() {
    final String input = "<list:, : and :, and >";
    final Iterable<? extends Component> components = Arrays.asList(text("one"), text("two"), text("three"));
    final Component expected = Component.join(JoinConfiguration.builder().separator(text(", ")).lastSeparator(text(" and ")).lastSeparatorIfSerial(text(", and ")), components);
    this.assertParsedEquals(expected, input, joining("list", components));
  }

  @Test
  void testJoinSeparatorWithLastSeparatorIfSerialAndTwoComponents() {
    final String input = "<list:, : and :, and >";
    final Iterable<? extends Component> components = Arrays.asList(text("one"), text("two"));
    final Component expected = Component.join(JoinConfiguration.builder().separator(text(", ")).lastSeparator(text(" and ")).lastSeparatorIfSerial(text(", and ")), components);
    this.assertParsedEquals(expected, input, joining("list", components));
  }

}
