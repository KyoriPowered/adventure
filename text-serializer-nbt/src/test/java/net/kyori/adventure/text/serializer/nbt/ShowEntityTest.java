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

import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_IO;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.deserializeStyle;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.name;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.serializeComponent;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.testStyle;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class ShowEntityTest {
  @Test
  void testWithoutName() {
    final UUID uuid = UUID.fromString("c04d19f7-9854-4122-93ab-ad7d4e1af8bc");
    testStyle(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(Key.key("zombie"), uuid))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY))
            .putString(ComponentTreeConstants.SHOW_ENTITY_ID, "minecraft:zombie")
            .put(
              ComponentTreeConstants.SHOW_ENTITY_UUID,
              IntArrayBinaryTag.intArrayBinaryTag(-1068688905, -1739308766, -1817465475, 1310390460)
            )
            .build()
        )
        .build()
    );
  }

  @Test
  void testWithName() {
    final String entityId = "minecraft:spider";
    final UUID uuid = UUID.randomUUID();
    final String entityName = "Adventure spider";

    testStyle(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(Key.key(entityId), uuid, Component.text(entityName, NamedTextColor.RED)))
        .build(),
      CompoundBinaryTag.builder()
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY))
            .putString(ComponentTreeConstants.SHOW_ENTITY_ID, entityId)
            .put(
              ComponentTreeConstants.SHOW_ENTITY_UUID,
              IntArrayBinaryTag.intArrayBinaryTag(
                (int) (uuid.getMostSignificantBits() >> Integer.SIZE),
                (int) uuid.getMostSignificantBits(),
                (int) (uuid.getLeastSignificantBits() >> Integer.SIZE),
                (int) uuid.getLeastSignificantBits()
              )
            )
            .put(
              ComponentTreeConstants.SHOW_ENTITY_NAME,
              CompoundBinaryTag.builder()
                .putString(ComponentTreeConstants.TEXT, entityName)
                .putString(ComponentTreeConstants.COLOR, name(NamedTextColor.RED))
                .build()
            )
            .build()
        )
        .build()
    );
  }

  @Test
  void testLegacyWithoutName() throws IOException {
    final String entityType = "minecraft:blaze";
    final UUID uuid = UUID.randomUUID();

    final CompoundBinaryTag contentsTag = CompoundBinaryTag.builder()
      .putString(ComponentTreeConstants.SHOW_ENTITY_TYPE, entityType)
      .putString(ComponentTreeConstants.SHOW_ENTITY_ID, uuid.toString())
      .build();

    assertEquals(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(Key.key(entityType), uuid))
        .build(),
      deserializeStyle(
        CompoundBinaryTag.builder()
          .put(
            ComponentTreeConstants.HOVER_EVENT_CAMEL,
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY))
              .put(
                ComponentTreeConstants.HOVER_EVENT_CONTENTS,
                CompoundBinaryTag.builder()
                  .putString(ComponentTreeConstants.TEXT, SNBT_IO.asString(contentsTag))
                  .build()
              )
              .build()
          )
          .build()
      )
    );
  }

  @Test
  void testLegacyWithName() throws IOException {
    final String entityType = "minecraft:chicken";
    final UUID uuid = UUID.fromString("a8aa3054-ca11-41bd-ac7e-95967816a135");
    final Component entityName = Component.text("Lava chicken", NamedTextColor.DARK_RED);

    final CompoundBinaryTag contentsTag = CompoundBinaryTag.builder()
      .putString(ComponentTreeConstants.SHOW_ENTITY_TYPE, entityType)
      .putString(ComponentTreeConstants.SHOW_ENTITY_ID, uuid.toString())
      .put(ComponentTreeConstants.SHOW_ENTITY_NAME, serializeComponent(entityName))
      .build();

    assertEquals(
      Style.style()
        .hoverEvent(HoverEvent.showEntity(Key.key(entityType), uuid, entityName))
        .build(),
      deserializeStyle(
        CompoundBinaryTag.builder()
          .put(
            ComponentTreeConstants.HOVER_EVENT_CAMEL,
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY))
              .putString(ComponentTreeConstants.HOVER_EVENT_CONTENTS, SNBT_IO.asString(contentsTag))
              .build()
          )
          .build()
      )
    );
  }
}
