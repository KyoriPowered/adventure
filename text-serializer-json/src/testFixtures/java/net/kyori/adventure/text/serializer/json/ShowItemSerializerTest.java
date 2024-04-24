/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.serializer.json;

import java.io.IOException;
import java.util.Collections;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

final class ShowItemSerializerTest extends SerializerTest {
  @Test
  void testDeserializeWithPopulatedTag() throws IOException {
    final JSONComponentSerializer serializer = JSONComponentSerializer.builder()
      .editOptions(opts -> opts.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, JSONOptions.ShowItemHoverDataMode.EMIT_EITHER))
      .build();

    this.testObject(
      serializer,
      Component.text().hoverEvent(
        HoverEvent.showItem(
          Key.key("minecraft", "diamond"),
          2,
          BinaryTagHolder.binaryTagHolder(TagStringIO.get().asString(
            CompoundBinaryTag.builder()
              .put("display", CompoundBinaryTag.builder()
                .put("Name", StringBinaryTag.stringBinaryTag("A test!"))
                .build())
              .build()
          )))
        ).build(),
      json -> {
        json.addProperty(JSONComponentConstants.TEXT, "");
        json.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM));
          hover.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(contents -> {
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_ID, "minecraft:diamond");
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_COUNT, 2);
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_TAG, "{display:{Name:\"A test!\"}}");
          }));
        }));
      }
    );
  }

  @Test
  void testDeserializeWithNullTag() {
    this.testObject(
      Component.text().hoverEvent(
        HoverEvent.showItem(
          Key.key("minecraft", "diamond"),
          2,
          Collections.emptyMap()
        )
      ).build(),
      json -> {
        json.addProperty(JSONComponentConstants.TEXT, "");
        json.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM));
          hover.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(contents -> {
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_ID, "minecraft:diamond");
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_COUNT, 2);
          }));
        }));
      }
    );
  }

  @Test
  void testDeserializeWithCountOfOne() throws IOException {
    final JSONComponentSerializer serializer = JSONComponentSerializer.builder()
      .editOptions(opts -> opts.value(JSONOptions.SHOW_ITEM_HOVER_DATA_MODE, JSONOptions.ShowItemHoverDataMode.EMIT_EITHER))
      .build();
    this.testObject(
      serializer,
      Component.text().hoverEvent(
        HoverEvent.showItem(
          Key.key("minecraft", "diamond"),
          1,
          BinaryTagHolder.binaryTagHolder(TagStringIO.get().asString(
            CompoundBinaryTag.builder()
              .put("display", CompoundBinaryTag.builder()
                .put("Name", StringBinaryTag.stringBinaryTag("A test!"))
                .build())
              .build()
          )))
      ).build(),
      json -> {
        json.addProperty(JSONComponentConstants.TEXT, "");
        json.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM));
          hover.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(contents -> {
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_ID, "minecraft:diamond");
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_COUNT, 1);
            contents.addProperty(JSONComponentConstants.SHOW_ITEM_TAG, "{display:{Name:\"A test!\"}}");
          }));
        }));
      }
    );
  }
}
