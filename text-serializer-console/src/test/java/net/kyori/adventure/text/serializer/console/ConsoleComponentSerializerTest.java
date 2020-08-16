package net.kyori.adventure.text.serializer.console;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Narimm on 17/08/2020.
 */
class ConsoleComponentSerializerTest {

  @Test
  void testSimpleFrom() {
    assertEquals(TextComponent.of("foo"),
      ConsoleComponentSerializer.builder()
      .downSample(true)
      .build().deserialize("foo"));
  }

  @Test
  void testSimpleTo() {
    final TextComponent component = TextComponent.of("foo");
    assertEquals("foo"+ConsoleComponentSerializer.ESC_CHAR+"[0;39m",
      ConsoleComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testToColor() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(TextComponent.of("bar").color(NamedTextColor.BLUE))
      .build();
    String expected = Ansi.ansi().a(Ansi.ansi().fg(Ansi.Color.GREEN).bold()).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().a(Ansi.ansi().fg(Ansi.Color.BLUE).bold()).a("bar").reset().toString();
    assertEquals(expected, ConsoleComponentSerializer.fullColour().serialize(component));
  }

  @Test
  void testFromColor() {
    String input = Ansi.ansi().a(Ansi.ansi().fg(Ansi.Color.GREEN).bold()).a(Ansi.Attribute.UNDERLINE_DOUBLE).a("foo").reset().fg(Ansi.Color.DEFAULT).a(Ansi.ansi().fg(Ansi.Color.BLUE).bold()).a("bar").reset().fg(Ansi.Color.DEFAULT).toString();
    assertEquals(TextComponent.of("foobar"), ConsoleComponentSerializer.fullColour().deserialize(input));
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
    String expected = Ansi.ansi()
      .a("hi there ").reset()
      .a(Ansi.ansi().fg(Ansi.Color.GREEN).bold())
      .a("this bit is green ").reset()
      .a("this isn't ").reset()
      .a(Ansi.ansi().fg(Ansi.Color.GREEN).bold())
      .a("and woa, this is again").reset()
      .toString();
    assertEquals(expected,ConsoleComponentSerializer.fullColour().serialize(component));
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
      .a("hi").reset()
      .a(Ansi.ansi().fg(Ansi.Color.GREEN).bold())
      .a("foo").reset()
      .a(Ansi.ansi().fg(Ansi.Color.BLUE).bold())
      .a(Ansi.Attribute.UNDERLINE_DOUBLE)
      .a("bar").reset()
      .a(Ansi.Attribute.UNDERLINE_DOUBLE)
      .a("baz").reset()
      .toString();
    assertEquals(expected,ConsoleComponentSerializer.fullColour().serialize(c1));

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
    assertEquals("§eHello §aworld§e!", ConsoleComponentSerializer.fullColour().serialize(c2));

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
    assertEquals("§e§lHello §a§lworld§e§l!", ConsoleComponentSerializer.fullColour().serialize(c3));
  }

}
