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
package net.kyori.adventure.serializer.configurate4;

import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEventImpl;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class HoverEventShowItemSerializer implements TypeSerializer<HoverEventImpl.ShowItem> {
  static final HoverEventShowItemSerializer INSTANCE = new HoverEventShowItemSerializer();

  private static final TypeToken<Map<Key, ConfigurateDataComponentValue>> COMPONENT_MAP_TYPE = new TypeToken<Map<Key, ConfigurateDataComponentValue>>() {
  };

  private HoverEventShowItemSerializer() {
  }

  @Override
  public HoverEventImpl.ShowItem deserialize(final @NotNull Type type, final @NotNull ConfigurationNode value) throws SerializationException {
    final Key id = value.node(ComponentTreeConstants.SHOW_ITEM_ID).get(Key.class);
    if (id == null) {
      throw new SerializationException("An id is required to deserialize the show_item hover event");
    }
    final int count = value.node(ComponentTreeConstants.SHOW_ITEM_COUNT).getInt(1);
    final ConfigurationNode components = value.node(ComponentTreeConstants.SHOW_ITEM_COMPONENTS);
    if (!components.virtual()) {
      final Map<Key, ConfigurateDataComponentValue> componentsMap = components.require(COMPONENT_MAP_TYPE);

      return HoverEventImpl.ShowItem.showItem(id, count, new HashMap<>(componentsMap));
    } else {
      // legacy (pre-1.20.5)
      @SuppressWarnings("deprecation")
      final String tag = value.node(ComponentTreeConstants.SHOW_ITEM_TAG).getString();
      return HoverEventImpl.ShowItem.showItem(id, count, tag == null ? null : BinaryTagHolder.binaryTagHolder(tag));
    }
  }

  @Override
  public void serialize(final @NotNull Type type, final HoverEventImpl.@Nullable ShowItem obj, final @NotNull ConfigurationNode value) throws SerializationException {
    if (obj == null) {
      value.set(null);
      return;
    }

    value.node(ComponentTreeConstants.SHOW_ITEM_ID).set(Key.class, obj.item());
    value.node(ComponentTreeConstants.SHOW_ITEM_COUNT).set(obj.count());

    if (!obj.dataComponents().isEmpty()) {
      value.node(ComponentTreeConstants.SHOW_ITEM_TAG).set(null);
      value.node(ComponentTreeConstants.SHOW_ITEM_COMPONENTS).set(COMPONENT_MAP_TYPE, obj.dataComponentsAs(ConfigurateDataComponentValue.class));
    } else if (obj.nbt() != null) {
      // legacy (pre-1.20.5)
      value.node(ComponentTreeConstants.SHOW_ITEM_COMPONENTS).set(null);
      value.node(ComponentTreeConstants.SHOW_ITEM_TAG).set(obj.nbt().string());
    } else {
      value.node(ComponentTreeConstants.SHOW_ITEM_COMPONENTS).set(null);
    }
  }
}
