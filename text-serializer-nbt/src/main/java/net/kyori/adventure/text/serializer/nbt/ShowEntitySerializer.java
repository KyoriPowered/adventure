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
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_TYPE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_UUID;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_IO;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.requiredTag;

final class ShowEntitySerializer {

  private ShowEntitySerializer() {
  }

  static HoverEvent.@NotNull ShowEntity deserialize(final @NotNull BinaryTag tag, final boolean snakeCase,
                                                    final @NotNull NBTComponentSerializerImpl serializer) {
    try {
      return deserializeModern((CompoundBinaryTag) tag, snakeCase, serializer);
    } catch (final Exception exception) {
      if (snakeCase) {
        throw notSureHowToDeserialize(tag);
      } else {
        return deserializeLegacy(tag, serializer);
      }
    }
  }

  static @NotNull CompoundBinaryTag serialize(final HoverEvent.@NotNull ShowEntity showEntity, final boolean snakeCase,
                                              final @NotNull NBTComponentSerializerImpl serializer) {
    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .put(snakeCase ? SHOW_ENTITY_ID : SHOW_ENTITY_TYPE, KeySerializer.serialize(showEntity.type()))
      .put(snakeCase ? SHOW_ENTITY_UUID : SHOW_ENTITY_ID, UUIDSerializer.serialize(showEntity.id()));

    final Component entityName = showEntity.name();
    if (entityName != null) {
      builder.put(SHOW_ENTITY_NAME, serializer.serialize(entityName));
    }

    return builder.build();
  }

  private static HoverEvent.@NotNull ShowEntity deserializeModern(final @NotNull CompoundBinaryTag compound, final boolean snakeCase,
                                                                  final @NotNull NBTComponentSerializerImpl serializer) {
    final Key entityType = KeySerializer.deserialize(requiredTag(compound, snakeCase ? SHOW_ENTITY_ID : SHOW_ENTITY_TYPE, BinaryTagTypes.STRING));
    final BinaryTag entityIdTag = requiredTag(compound, snakeCase ? SHOW_ENTITY_UUID : SHOW_ENTITY_ID);
    final BinaryTag entityNameTag = compound.get(SHOW_ENTITY_NAME);

    final UUID entityId = UUIDSerializer.deserialize(entityIdTag);
    if (entityNameTag == null) {
      return HoverEvent.ShowEntity.showEntity(entityType, entityId);
    } else {
      return HoverEvent.ShowEntity.showEntity(entityType, entityId, serializer.deserialize(entityNameTag));
    }
  }

  private static HoverEvent.@NotNull ShowEntity deserializeLegacy(final @NotNull BinaryTag tag, final @NotNull NBTComponentSerializerImpl serializer) {
    try {
      final Component component = serializer.deserialize(tag);
      if (!(component instanceof TextComponent)) {
        throw notSureHowToDeserialize(tag);
      }

      final String content = ((TextComponent) component).content();
      final CompoundBinaryTag compound = SNBT_IO.asCompound(content);
      return deserializeModern(compound, false, serializer);
    } catch (final IOException exception) {
      throw notSureHowToDeserialize(tag);
    }
  }

  private static @NotNull IllegalArgumentException notSureHowToDeserialize(final @NotNull BinaryTag tag) {
    return new IllegalArgumentException("Don't know how to turn " + tag + " into a show entity hover event data");
  }
}
