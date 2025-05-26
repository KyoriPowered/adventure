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
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdventureCodecTest {
  protected static <T> T valueOrThrow(final DataResult<T> result) {
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

    final Map<Key, DataComponentValue> dataComponentValueMap = new HashMap<>();
    dataComponentValueMap.put(Key.key("name"), BinaryTagHolder.binaryTagHolder("{'text':'Example'}"));
    assertComponentCodec(
      Component.text("Example")
        .hoverEvent(HoverEvent.showItem(Key.key("diamond"), 1, dataComponentValueMap))
    );
  }

  protected static void assertComponentCodec(final Component component) {
    final JsonElement jsonElement = valueOrThrow(AdventureCodecs.COMPONENT.encodeStart(JsonOps.INSTANCE, component));
    final Component decode = valueOrThrow(AdventureCodecs.COMPONENT.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(component, decode);
  }

  protected static <A> void assertCodec(final Codec<A> codec, final A value) {
    final JsonElement jsonElement = valueOrThrow(codec.encodeStart(JsonOps.INSTANCE, value));
    final A decode = valueOrThrow(codec.decode(JsonOps.INSTANCE, jsonElement)).getFirst();
    assertEquals(value, decode);
  }
}
