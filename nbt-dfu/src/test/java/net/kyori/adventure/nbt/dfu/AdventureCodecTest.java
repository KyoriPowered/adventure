package net.kyori.adventure.nbt.dfu;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
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
