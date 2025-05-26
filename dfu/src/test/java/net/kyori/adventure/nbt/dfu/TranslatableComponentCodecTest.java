package net.kyori.adventure.nbt.dfu;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import java.util.UUID;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslatableComponentCodecTest extends AdventureCodecTest {
  private static void assertTranslationArgument(final TranslationArgument bool) {
    JsonElement jsonElement = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, bool));
    TranslationArgument valued = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(bool, valued);
  }

  @Test
  void argument() {
    assertTranslationArgument(
      TranslationArgument.bool(true)
    );
    assertTranslationArgument(
      TranslationArgument.numeric(1)
    );
    assertTranslationArgument(
      TranslationArgument.numeric(1.0)
    );
    assertTranslationArgument(
      TranslationArgument.numeric(1f)
    );
    assertTranslationArgument(
      TranslationArgument.numeric(((short) 100))
    );
    assertTranslationArgument(
      TranslationArgument.numeric(((byte) 255))
    );
    assertTranslationArgument(
      TranslationArgument.component(Component.text("test"))
    );
  }

  @Test
  void component() {
    assertComponentCodec(
      translatable("key")
    );
    assertComponentCodec(
      translatable("key", TranslationArgument.bool(true))
    );
    assertComponentCodec(
      translatable("key", TranslationArgument.component(text("test")))
        .fallback("fallback")
    );
  }

  @Test
  void test() {
    final UUID id = UUID.fromString("83889e3d-b79e-45d0-94af-a88d2b87a44e");
    final String name = "huanmeng_qwq";
    final String command = String.format("/msg %s ", name);

    assertComponentCodec(
      Component.translatable(
        "multiplayer.player.join",
        Component.text().content(name)
          .clickEvent(ClickEvent.suggestCommand(command))
          .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.showEntity(
            Key.key("minecraft", "player"),
            id,
            Component.text(name)
          )))
          .build()
      ).color(NamedTextColor.YELLOW)
    );
  }
}
