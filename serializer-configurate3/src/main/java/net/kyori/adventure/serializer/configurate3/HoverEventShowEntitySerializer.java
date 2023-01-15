/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class HoverEventShowEntitySerializer implements TypeSerializer<HoverEvent.ShowEntity> {
  static final HoverEventShowEntitySerializer INSTANCE = new HoverEventShowEntitySerializer();
  static final TypeToken<HoverEvent.ShowEntity> TYPE = TypeToken.of(HoverEvent.ShowEntity.class);
  private static final TypeToken<UUID> UUID_TYPE = TypeToken.of(UUID.class);

  static final String ENTITY_TYPE = "type";
  static final String ID = "id";
  static final String NAME = "name";

  private HoverEventShowEntitySerializer() {
  }

  @Override
  public HoverEvent.ShowEntity deserialize(final @NotNull TypeToken<?> type, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    final Key typeId = value.getNode(ENTITY_TYPE).getValue(KeySerializer.INSTANCE.type());
    final UUID id = value.getNode(ID).getValue(UUID_TYPE);
    if (typeId == null || id == null) {
      throw new ObjectMappingException("A show entity hover event needs type and id fields to be deserialized");
    }
    final @Nullable Component name = value.getNode(NAME).getValue(ComponentTypeSerializer.TYPE);

    return HoverEvent.ShowEntity.of(typeId, id, name);
  }

  @Override
  public void serialize(final @NotNull TypeToken<?> type, final HoverEvent.@Nullable ShowEntity obj, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (obj == null) {
      value.setValue(null);
      return;
    }

    value.getNode(ENTITY_TYPE).setValue(KeySerializer.INSTANCE.type(), obj.type());
    value.getNode(ID).setValue(UUID_TYPE, obj.id());
    value.getNode(NAME).setValue(ComponentTypeSerializer.TYPE, obj.name());
  }
}
