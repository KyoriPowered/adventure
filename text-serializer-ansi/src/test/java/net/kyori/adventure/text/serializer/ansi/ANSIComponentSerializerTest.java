package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.ansi.ColorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ANSIComponentSerializerTest {
  @Test
  void testSimple() {
    assertEquals("foo", ANSIComponentSerializer.ansi().serialize(
      Component.text("foo"),
      ColorLevel.INDEXED_16));
    assertEquals("\u001B[91mfoo\u001B[0m", ANSIComponentSerializer.ansi().serialize(
      Component.text("foo", NamedTextColor.RED),
      ColorLevel.INDEXED_16));
    assertEquals("\u001B[1mfoo\u001B[0m", ANSIComponentSerializer.ansi().serialize(
      Component.text("foo").decorate(TextDecoration.BOLD),
      ColorLevel.INDEXED_16));

    TextComponent component = Component.text().content("")
      .append(Component.text("foo", NamedTextColor.GREEN))
      .append(Component.text("bar", NamedTextColor.BLUE))
      .build();
    assertEquals("\u001B[92mfoo\u001B[94mbar\u001B[0m",
      ANSIComponentSerializer.ansi().serialize(component, ColorLevel.INDEXED_16));

    component = Component.text().content("")
      .append(Component.text("foo", NamedTextColor.GREEN, TextDecoration.BOLD))
      .append(Component.text("bar", NamedTextColor.BLUE))
      .build();
    assertEquals("\u001B[1m\u001B[92mfoo\u001B[0m\u001B[94mbar\u001B[0m",
      ANSIComponentSerializer.ansi().serialize(component, ColorLevel.INDEXED_16));
  }
}
