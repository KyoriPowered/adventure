/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.moshi;

import java.io.IOException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.moshi.MoshiTest.object;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShowItemSerializerTest {
  @Test
  void testDeserializeWithPopulatedTag() throws IOException {
    assertEquals(
      HoverEvent.ShowItem.of(Key.key("minecraft", "diamond"), 1, BinaryTagHolder.of(TagStringIO.get().asString(
        CompoundBinaryTag.builder()
          .put("display", CompoundBinaryTag.builder()
            .put("Name", StringBinaryTag.of("A test!"))
            .build())
          .build()
      ))),
      MoshiComponentSerializer.moshi().serializer().adapter(HoverEvent.ShowItem.class).fromJsonValue(
        object(json -> {
          json.put(ShowItemAdapter.ID, "minecraft:diamond");
          json.put(ShowItemAdapter.COUNT, 1);
          json.put(ShowItemAdapter.TAG, "{display:{Name:\"A test!\"}}");
        })
      )
    );
  }

  @Test
  void testDeserializeWithNullTag() {
    assertEquals(
      HoverEvent.ShowItem.of(Key.key("minecraft", "diamond"), 1, null),
      MoshiComponentSerializer.moshi().serializer().adapter(HoverEvent.ShowItem.class).fromJsonValue(
        object(json -> {
          json.put(ShowItemAdapter.ID, "minecraft:diamond");
          json.put(ShowItemAdapter.COUNT, 1);
          json.put(ShowItemAdapter.TAG, null);
        })
      )
    );
  }
}
