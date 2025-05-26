package net.kyori.adventure.nbt.dfu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

public class TextComponentCodecTest extends AdventureCodecTest {
  @Test
  void test() {
    assertComponentCodec(
      Component.text("Hello, world!")
    );
    assertComponentCodec(
      Component.text()
        .content("c")
        .color(NamedTextColor.GOLD)
        .append(Component.text("o", NamedTextColor.DARK_AQUA))
        .append(Component.text("l", NamedTextColor.LIGHT_PURPLE))
        .append(Component.text("o", NamedTextColor.DARK_PURPLE))
        .append(Component.text("u", NamedTextColor.BLUE))
        .append(Component.text("r", NamedTextColor.DARK_GREEN))
        .append(Component.text("s", NamedTextColor.RED))
        .build()
    );
  }
}
