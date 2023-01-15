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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class HoverEventShowItemSerializer implements TypeSerializer<HoverEvent.ShowItem> {
  static final HoverEventShowItemSerializer INSTANCE = new HoverEventShowItemSerializer();
  static final TypeToken<HoverEvent.ShowItem> TYPE = TypeToken.of(HoverEvent.ShowItem.class);

  static final String ID = "id";
  static final String COUNT = "count";
  static final String TAG = "tag";

  private HoverEventShowItemSerializer() {
  }

  @Override
  public HoverEvent.ShowItem deserialize(final @NotNull TypeToken<?> type, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    final Key id = value.getNode(ID).getValue(KeySerializer.INSTANCE.type());
    if (id == null) {
      throw new ObjectMappingException("An id is required to deserialize the show_item hover event");
    }
    final int count = value.getNode(COUNT).getInt(1);
    final String tag = value.getNode(TAG).getString();

    return HoverEvent.ShowItem.of(id, count, tag == null ? null : BinaryTagHolder.binaryTagHolder(tag));
  }

  @Override
  public void serialize(final @NotNull TypeToken<?> type, final HoverEvent.@Nullable ShowItem obj, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (obj == null) {
      value.setValue(null);
      return;
    }

    value.getNode(ID).setValue(KeySerializer.INSTANCE.type(), obj.item());
    value.getNode(COUNT).setValue(obj.count());

    if (obj.nbt() == null) {
      value.getNode(TAG).setValue(null);
    } else {
      value.getNode(TAG).setValue(obj.nbt().string());
    }
  }
}
