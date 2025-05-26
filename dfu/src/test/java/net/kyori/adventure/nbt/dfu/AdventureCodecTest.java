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
    this.assertComponentCodec(
      Component.text("hello", NamedTextColor.RED)
        .clickEvent(ClickEvent.suggestCommand("/dfu"))
        .hoverEvent(Component.text("hover"))
        .decorate(TextDecoration.BOLD)
    );

    this.assertComponentCodec(
      Component.text("test", NamedTextColor.RED)
        .hoverEvent(HoverEvent.showItem(HoverEvent.ShowItem.showItem(Key.key("diamond"), 1, BinaryTagHolder.binaryTagHolder("{display: {Lore: ['Test']}}"))))
        .shadowColor(ShadowColor.none())
    );

    this.assertComponentCodec(
      Component.translatable("trans.key", TranslationArgument.numeric(1), TranslationArgument.component(Component.text("test")))
        .clickEvent(ClickEvent.openUrl("https://example.com"))
    );

    this.assertComponentCodec(
      Component.blockNBT("pos", BlockNBTComponent.Pos.fromString("1 2 3"))
    );
    this.assertComponentCodec(
      Component.blockNBT("pos", BlockNBTComponent.Pos.fromString("^1 ^2 ^3"))
    );

    this.assertComponentCodec(
      Component.entityNBT("entities", "@e")
    );

    this.assertComponentCodec(
      Component.text("Press ", NamedTextColor.GRAY)
        .append(Component.keybind("key.jump", NamedTextColor.RED))
        .append(Component.text(" to jump", NamedTextColor.GRAY))
    );
  }

  private void assertComponentCodec(final Component component) {
    final JsonElement jsonElement = this.valueOrThrow(AdventureCodecs.COMPONENT.encodeStart(JsonOps.INSTANCE, component));
    final Component decode = this.valueOrThrow(AdventureCodecs.COMPONENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(component, decode);
  }

  @Test
  void testTranslationArgument() {
    final TranslationArgument bool = TranslationArgument.bool(true);
    JsonElement jsonElement = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, bool));
    TranslationArgument valued = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(bool, valued);

    final TranslationArgument numeric = TranslationArgument.numeric(1);
    jsonElement = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, numeric));
    valued = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(numeric, valued);

    final TranslationArgument component = TranslationArgument.component(Component.text("hello"));
    jsonElement = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.encodeStart(JsonOps.INSTANCE, component));
    valued = this.valueOrThrow(AdventureCodecs.TRANSLATION_ARGUMENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(component, valued);
  }
}
