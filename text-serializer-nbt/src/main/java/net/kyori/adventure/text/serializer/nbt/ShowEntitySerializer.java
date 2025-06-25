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
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_TYPE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ENTITY_UUID;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class ShowEntitySerializer {
  
  private ShowEntitySerializer() {
  }
  
  static HoverEvent.@NotNull ShowEntity deserialize(@NotNull CompoundBinaryTag compound, boolean snakeCase,
                                                    @NotNull NBTComponentSerializerImpl serializer) {
    Key entityType = Key.key(getRequiredTag(compound, snakeCase ? SHOW_ENTITY_ID : SHOW_ENTITY_TYPE, BinaryTagTypes.STRING).value());

    BinaryTag entityIdTag = compound.get(snakeCase ? SHOW_ENTITY_UUID : SHOW_ENTITY_ID);
    if (entityIdTag == null) {
      throw new IllegalArgumentException("The show entity compound tag does not contain an entity id field");
    }

    UUID entityId = UUIDSerializer.deserialize(entityIdTag);
    BinaryTag entityName = compound.get(SHOW_ENTITY_NAME);

    if (entityName == null) {
      return HoverEvent.ShowEntity.showEntity(entityType, entityId);
    } else {
      return HoverEvent.ShowEntity.showEntity(entityType, entityId, serializer.deserialize(entityName));
    }
  }
  
  static @NotNull CompoundBinaryTag serialize(HoverEvent.@NotNull ShowEntity showEntity, boolean snakeCase,
                                              @NotNull NBTComponentSerializerImpl serializer) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(snakeCase ? SHOW_ENTITY_ID : SHOW_ENTITY_TYPE, showEntity.type().asString());

    UUID entityId = showEntity.id();
    if (snakeCase) {
      NBTSerializerOptions.ShowEntityUUIDEmitMode uuidEmitMode = serializer.flags().value(NBTSerializerOptions.EMIT_SHOW_ENTITY_UUID_TYPE);
      builder.put(SHOW_ENTITY_UUID, UUIDSerializer.serialize(entityId, uuidEmitMode));
    } else {
      builder.put(SHOW_ENTITY_ID, UUIDSerializer.serialize(entityId, NBTSerializerOptions.ShowEntityUUIDEmitMode.EMIT_STRING));
    }

    Component customName = showEntity.name();
    if (customName != null) {
      builder.put(SHOW_ENTITY_NAME, serializer.serialize(customName));
    }

    return builder.build();
  }
}
