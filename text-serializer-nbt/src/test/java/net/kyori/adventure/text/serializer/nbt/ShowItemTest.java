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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_CODEC;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_IO;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.deserializeStyle;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.name;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.testStyle;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ShowItemTest {

  private static final String LEGACY_COUNT = "Count";

  @Test
  void testWithPopulatedTag() throws IOException {
    String item = "minecraft:diamond";
    int count = 2;

    testStyle(
      NBTComponentSerializer.builder()
        .editOptions(builder -> {
          builder.value(
            NBTSerializerOptions.SHOW_ITEM_HOVER_DATA_MODE,
            NBTSerializerOptions.ShowItemHoverDataMode.EMIT_EITHER
          );
          builder.value(
            NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE,
            NBTSerializerOptions.HoverEventValueMode.CAMEL_CASE
          );
        })
        .build(),
      Style.style()
        .hoverEvent(HoverEvent.showItem(
          Key.key(item), count,
          BinaryTagHolder.binaryTagHolder(TagStringIO.tagStringIO().asString(
            CompoundBinaryTag.builder()
              .put("display", CompoundBinaryTag.builder()
                .putString("Name", "A test!")
                .build())
              .build()
          ))
        ))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_CAMEL,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
            .put(
              ComponentTreeConstants.HOVER_EVENT_CONTENTS,
              CompoundBinaryTag.builder()
                .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
                .putInt(ComponentTreeConstants.SHOW_ITEM_COUNT, count)
                .putString(ComponentTreeConstants.SHOW_ITEM_TAG, "{display:{Name:\"A test!\"}}")
                .build()
            )
            .build()
        )
        .build()
    );
  }

  @Test
  void testWithoutAdditionalData() {
    String item = "minecraft:diamond";
    int count = 2;

    testStyle(
      Style.style()
        .hoverEvent(HoverEvent.showItem(Key.key(item), count, Collections.emptyMap()))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
            .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
            .putInt(ComponentTreeConstants.SHOW_ITEM_COUNT, count)
            .build()
        )
        .build()
    );
  }

  @Test
  void testWithCountOfOne() {
    String item = "minecraft:diamond";
    int count = 1;

    testStyle(
      Style.style()
        .hoverEvent(HoverEvent.showItem(Key.key(item), count))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
            .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
            .putInt(ComponentTreeConstants.SHOW_ITEM_COUNT, count)
            .build()
        )
        .build()
    );
  }

  @Test
  void testWithRemovedComponent() {
    String item = "minecraft:diamond";
    int count = 2;
    String component = "minecraft:damage";

    testStyle(
      Style.style()
        .hoverEvent(
          HoverEvent.showItem(
            Key.key(item), count,
            Collections.singletonMap(Key.key(component), DataComponentValue.removed())
          )
        )
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
            .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
            .putInt(ComponentTreeConstants.SHOW_ITEM_COUNT, count)
            .put(
              ComponentTreeConstants.SHOW_ITEM_COMPONENTS,
              CompoundBinaryTag.builder()
                .put("!" + component, EndBinaryTag.endBinaryTag())
                .build()
            )
            .build()
        )
        .build()
    );
  }

  @Test
  void testLegacyWithoutTag() throws IOException {
    String item = "minecraft:diamond";
    byte count = 3;

    CompoundBinaryTag itemData = CompoundBinaryTag.builder()
      .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
      .putByte(LEGACY_COUNT, count)
      .build();

    assertEquals(
      Style.style()
        .hoverEvent(HoverEvent.showItem(Key.key(item), count))
        .build(),
      deserializeStyle(
        CompoundBinaryTag.builder()
          .put(
            ComponentTreeConstants.HOVER_EVENT_CAMEL,
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
              .putString(ComponentTreeConstants.HOVER_EVENT_CONTENTS, SNBT_IO.asString(itemData))
              .build()
          )
          .build()
      )
    );
  }

  @Test
  void testLegacyWithTag() throws IOException {
    String item = "minecraft:diamond";
    byte count = 1;

    CompoundBinaryTag itemTag = CompoundBinaryTag.builder()
      .put(
        "display",
        CompoundBinaryTag.builder()
          .putString("Name", "Legacy test!")
          .build()
      )
      .build();

    CompoundBinaryTag itemData = CompoundBinaryTag.builder()
      .putString(ComponentTreeConstants.SHOW_ITEM_ID, item)
      .putByte(LEGACY_COUNT, count)
      .put(ComponentTreeConstants.SHOW_ITEM_TAG, itemTag)
      .build();

    assertEquals(
      Style.style()
        .hoverEvent(HoverEvent.showItem(Key.key(item), count, BinaryTagHolder.encode(itemTag, SNBT_CODEC)))
        .build(),
      deserializeStyle(
        CompoundBinaryTag.builder()
          .put(
            ComponentTreeConstants.HOVER_EVENT_CAMEL,
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ITEM))
              .put(
                ComponentTreeConstants.HOVER_EVENT_CONTENTS,
                CompoundBinaryTag.builder()
                  .putString(ComponentTreeConstants.TEXT, SNBT_IO.asString(itemData))
                  .build()
              )
              .build()
          )
          .build()
      )
    );
  }
}
