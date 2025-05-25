package net.kyori.adventure.nbt.dfu;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventureCodecTest {
  private <T> T valueOrThrow(final DataResult<T> result) {
    result.error().ifPresent(err -> {
      throw new RuntimeException("Error with data result: " + err.message());
    });

    return result.result().orElseThrow(() -> new IllegalStateException("Neither result or error was present"));
  }

  @Test
  void testComponent() {
    assertComponentCodec(
      Component.text("hello", NamedTextColor.RED)
        .clickEvent(ClickEvent.suggestCommand("/dfu"))
        .hoverEvent(Component.text("hover"))
        .decorate(TextDecoration.BOLD)
    );

    assertComponentCodec(
      Component.text("test", NamedTextColor.RED)
        .hoverEvent(HoverEvent.showItem(HoverEvent.ShowItem.showItem(Key.key("diamond"), 1, BinaryTagHolder.binaryTagHolder("{display: {Lore: ['Test']}}"))))
        .shadowColor(ShadowColor.none())
    );

    assertComponentCodec(
      Component.translatable("trans.key", TranslationArgument.numeric(1), TranslationArgument.component(Component.text("test")))
        .clickEvent(ClickEvent.openUrl("https://example.com"))
    );

    assertComponentCodec(
      Component.blockNBT("pos", BlockNBTComponent.Pos.fromString("1 2 3"))
    );
    assertComponentCodec(
      Component.blockNBT("pos", BlockNBTComponent.Pos.fromString("^1 ^2 ^3"))
    );

    assertComponentCodec(
      Component.entityNBT("entities", "@e")
    );

    assertComponentCodec(
      Component.text("Press ", NamedTextColor.GRAY)
        .append(Component.keybind("key.jump", NamedTextColor.RED))
        .append(Component.text(" to jump", NamedTextColor.GRAY))
    );
  }

  private void assertComponentCodec(Component component) {
    JsonElement jsonElement = valueOrThrow(AdventureCodecs.COMPONENT.encodeStart(JsonOps.INSTANCE, component));
    Component decode = valueOrThrow(AdventureCodecs.COMPONENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(component, decode);
  }

  @Test
  void testTranslationArgument() {
    TranslationArgument bool = TranslationArgument.bool(true);
    JsonElement jsonElement = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, bool));
    TranslationArgument valued = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(bool, valued);

    TranslationArgument numeric = TranslationArgument.numeric(1);
    jsonElement = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, numeric));
    valued = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(numeric, valued);

    TranslationArgument component = TranslationArgument.component(Component.text("hello"));
    jsonElement = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, component));
    valued = valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(component, valued);
  }
}
