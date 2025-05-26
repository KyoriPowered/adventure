package net.kyori.adventure.nbt.dfu;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleCodecTest extends AdventureCodecTest {
  private static void assertStyleCodec(final Style style) {
    final JsonElement jsonElement = valueOrThrow(AdventureCodecs.STYLE.encodeStart(JsonOps.INSTANCE, style));
    final Style decode = valueOrThrow(AdventureCodecs.STYLE.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(style, decode);
  }

  @Test
  void test() {
    assertStyleCodec(
      Style.style()
        .hoverEvent(Component.text("Hover Text"))
        .clickEvent(ClickEvent.changePage(10))
        .insertion("abc")
        .color(NamedTextColor.GREEN)
        .shadowColor(ShadowColor.none())
        .decoration(TextDecoration.BOLD, true)
        .font(Key.key("default"))
        .build()
    );
  }
}
