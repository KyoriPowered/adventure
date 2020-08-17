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

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnsiComponentSerializerTest {

  @Test
  void testSimpleFrom() {
    Assertions.assertEquals(TextComponent.of("foo"),
      AnsiComponentSerializer.builder()
      .downSample(true)
      .build().deserialize("foo"));
  }

  @Test
  void testSimpleTo() {
    final TextComponent component = TextComponent.of("foo");
    Ansi expected = Ansi.ansi().a("foo").reset();
    assertEquals(expected.toString(),
      AnsiComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testToColor() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(TextComponent.of("bar").color(NamedTextColor.BLUE))
      .build();
    Ansi expected  = Ansi.ansi().a(Ansi.ansi().fgBright(Ansi.Color.GREEN)).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().a(Ansi.ansi().fgBright(Ansi.Color.BLUE)).a("bar").reset();
    assertEquals(expected.toString(), AnsiComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testFromColor() {
    String input = Ansi.ansi().a(Ansi.ansi().fgBright(Ansi.Color.GREEN)).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().fg(Ansi.Color.DEFAULT).a(Ansi.ansi().fgBright(Ansi.Color.BLUE)).a("bar").reset().fg(Ansi.Color.DEFAULT).toString();
    assertEquals(TextComponent.of("foobar"), AnsiComponentSerializer.fullColour().deserialize(input));
  }

  @Test
  void testComplex(){
    final TextComponent component = TextComponent.builder()
      .content("hi there ")
      .append(TextComponent.builder("this bit is green ")
        .color(NamedTextColor.GREEN)
        .build())
      .append(TextComponent.of("this isn't ").style(Style.empty()))
      .append(TextComponent.builder("and woa, this is again")
        .color(NamedTextColor.GREEN)
        .build())
      .build();
    Ansi expected = Ansi.ansi()
      .a("hi there ").reset()
      .a(Ansi.ansi().fgBright(Ansi.Color.GREEN))
      .a("this bit is green ").reset()
      .a("this isn't ").reset()
      .a(Ansi.ansi().fgBright(Ansi.Color.GREEN))
      .a("and woa, this is again").reset();
    assertEquals(expected.toString(), AnsiComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testToConsole(){
    final TextComponent c1 = TextComponent.builder("hi")
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .append(
        TextComponent.of("foo")
          .color(NamedTextColor.GREEN)
          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      )
      .append(
        TextComponent.of("bar")
          .color(NamedTextColor.BLUE)
      )
      .append(TextComponent.of("baz"))
      .build();
    String expected = Ansi.ansi()
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
    assertEquals(expected, AnsiComponentSerializer.fullColour().serialize(c1));

    final TextComponent c2 = TextComponent.builder()
      .content("")
      .color(NamedTextColor.YELLOW)
      .append(TextComponent.builder()
        .content("Hello ")
        .append(
          TextComponent.builder()
            .content("world")
            .color(NamedTextColor.GREEN)
            .build()
        )
        .append(TextComponent.of("!")) // Should be yellow
        .build()
      )
      .build();
    Ansi ansi = Ansi.ansi()
      .fgBright(Ansi.Color.YELLOW)
      .a("Hello ").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.GREEN)
      .a("world").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.YELLOW)
      .a("!").reset();
    assertEquals(ansi.toString(), AnsiComponentSerializer.fullColour().serialize(c2));

    final TextComponent c3 = TextComponent.builder()
      .content("")
      .decoration(TextDecoration.BOLD, true)
      .append(
        TextComponent.builder()
          .content("")
          .color(NamedTextColor.YELLOW)
          .append(TextComponent.builder()
            .content("Hello ")
            .append(
              TextComponent.builder()
                .content("world")
                .color(NamedTextColor.GREEN)
                .build()
            )
            .append(TextComponent.of("!"))
            .build()
          )
          .build())
      .build();
    Ansi expected3 = Ansi.ansi()
      .fgBright(Ansi.Color.YELLOW).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("Hello ").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.GREEN).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("world").a(Ansi.ansi().a(Ansi.Attribute.RESET))
      .fgBright(Ansi.Color.YELLOW).a(Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE))
      .a("!").reset();
    assertEquals(expected3.toString(), AnsiComponentSerializer.fullColour().serialize(c3));
  }

  @Test
  void testHexColor(){
    TextColor color = TextColor.of(0x77ff33);
    final TextComponent c0 = TextComponent.of("Kittens!", color);
    final int red = color.red();
    final int blue = color.blue();
    final int green = color.green();
    Ansi ansiColor = Ansi.ansi().format("\u001b[38;2;%d;%d;%dm", red, green, blue);
    Ansi expected = Ansi.ansi().a(ansiColor).a("Kittens!").reset();
    Ansi expectedDown = Ansi.ansi().fgBrightGreen().a("Kittens!").reset();
    assertEquals(expected.toString(), AnsiComponentSerializer.builder().downSample(false).build().serialize(c0));
    assertEquals(expectedDown.toString(), AnsiComponentSerializer.builder().downSample(true).build().serialize(c0));

  }

}
