/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ANSIComponentSerializerTest {

  @Test
  void testSimpleFrom() {
    Assertions.assertEquals(Component.text("foo"),
      ANSIComponentSerializer.builder()
      .downSample(true)
      .build().deserialize("foo"));
  }

  @Test
  void testSimpleTo() {
    final TextComponent component = Component.text("foo");
    final Ansi expected = Ansi.ansi().a("foo").reset();
    assertEquals(expected.toString(),
      ANSIComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testToColor() {
    final TextComponent component = Component.text("")
      .append(Component.text("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(Component.text("bar").color(NamedTextColor.BLUE));
    final Ansi expected = Ansi.ansi().a(Ansi.ansi().fgBright(Ansi.Color.GREEN)).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().a(Ansi.ansi().fgBright(Ansi.Color.BLUE)).a("bar").reset();
    assertEquals(expected.toString(), ANSIComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testFromColor() {
    final String input = Ansi.ansi().a(Ansi.ansi().fgBright(Ansi.Color.GREEN)).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().fg(Ansi.Color.DEFAULT).a(Ansi.ansi().fgBright(Ansi.Color.BLUE)).a("bar").reset().fg(Ansi.Color.DEFAULT).toString();
    assertEquals(Component.text("foobar"), ANSIComponentSerializer.fullColour().deserialize(input));
  }

  @Test
  void testComplex(){
    final TextComponent component = Component.text("hi there ")
      .append(Component.text("this bit is green ")
        .color(NamedTextColor.GREEN))
      .append(Component.text("this isn't ").style(Style.empty()))
      .append(Component.text("and woa, this is again")
        .color(NamedTextColor.GREEN));
    final Ansi expected = Ansi.ansi()
      .a("hi there ").reset()
      .a(Ansi.ansi().fgBright(Ansi.Color.GREEN))
      .a("this bit is green ").reset()
      .a("this isn't ").reset()
      .a(Ansi.ansi().fgBright(Ansi.Color.GREEN))
      .a("and woa, this is again").reset();
    assertEquals(expected.toString(), ANSIComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testToConsole(){
    final TextComponent c1 = Component.text("hi")
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
      .append(Component.text("baz"));
    final String expected = Ansi.ansi()
      .a(Ansi.Attribute.UNDERLINE_DOUBLE)
      .a("hi").a(Ansi.Attribute.RESET)
      .a(Ansi.ansi().fgBright(Ansi.Color.GREEN))
      .a("foo").a(Ansi.Attribute.RESET)
      .a(Ansi.ansi().fgBright(Ansi.Color.BLUE))
      .a(Ansi.Attribute.UNDERLINE_DOUBLE)
      .a("bar").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .a(Ansi.Attribute.UNDERLINE_DOUBLE)
      .a("baz").a(Ansi.Attribute.RESET)
      .toString();
    assertEquals(expected, ANSIComponentSerializer.fullColour().serialize(c1));

    final TextComponent c2 = Component.text("")
      .color(NamedTextColor.YELLOW)
      .append(Component.text("Hello ")
        .append(
          Component.text("world")
            .color(NamedTextColor.GREEN))
        .append(Component.text("!"))); // Should be yellow
    final Ansi ansi = Ansi.ansi()
      .fgBright(Ansi.Color.YELLOW)
      .a("Hello ").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.GREEN)
      .a("world").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.YELLOW)
      .a("!").reset();
    assertEquals(ansi.toString(), ANSIComponentSerializer.fullColour().serialize(c2));

    final TextComponent c3 = Component.text("")
      .decoration(TextDecoration.BOLD, true)
      .append(Component.text("")
          .color(NamedTextColor.YELLOW)
          .append(Component.text("Hello ")
            .append(
              Component.text("world")
                .color(NamedTextColor.GREEN))
            .append(Component.text("!"))));
    final Ansi expected3 = Ansi.ansi()
      .fgBright(Ansi.Color.YELLOW).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("Hello ").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.GREEN).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("world").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.YELLOW).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("!").reset();
    assertEquals(expected3.toString(), ANSIComponentSerializer.fullColour().serialize(c3));
  }

  @Test
  void testHexColor(){
    final TextColor color = TextColor.color(0x77ff33);
    final TextComponent c0 = Component.text("Kittens!", color);
    final int red = color.red();
    final int blue = color.blue();
    final int green = color.green();
    final Ansi ansiColor = Ansi.ansi().format("\u001b[38;2;%d;%d;%dm", red, green, blue);
    final Ansi expected = Ansi.ansi().a(ansiColor).a("Kittens!").reset();
    final Ansi expectedDown = Ansi.ansi().fgBrightGreen().a("Kittens!").reset();
    assertEquals(expected.toString(), ANSIComponentSerializer.builder().downSample(false).build().serialize(c0));
    assertEquals(expectedDown.toString(), ANSIComponentSerializer.builder().downSample(true).build().serialize(c0));

  }

}
