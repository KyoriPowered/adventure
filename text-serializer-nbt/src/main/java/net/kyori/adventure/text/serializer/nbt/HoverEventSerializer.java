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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class HoverEventSerializer {

  private static final String HOVER_EVENT_ACTION = "action";
  private static final String HOVER_EVENT_CONTENTS = "contents";
  @Deprecated
  private static final String HOVER_EVENT_VALUE = "value";

  private static final String HOVER_EVENT_SHOW_TEXT = "show_text";
  private static final String HOVER_EVENT_SHOW_ITEM = "show_item";
  private static final String HOVER_EVENT_SHOW_ENTITY = "show_entity";
  @Deprecated
  private static final String HOVER_EVENT_SHOW_ACHIEVEMENT = "show_achievement";

  private static final String SHOW_ITEM_ID = "id";
  private static final String SHOW_ITEM_COUNT = "count";
  private static final String SHOW_ITEM_COMPONENTS = "components";
  @Deprecated
  private static final String SHOW_ITEM_TAG = "tag";

  private static final String SHOW_ENTITY_TYPE = "type";
  private static final String SHOW_ENTITY_ID = "id";
  private static final String SHOW_ENTITY_NAME = "name";

  private HoverEventSerializer() {
  }

  static @NotNull HoverEvent<?> deserialize(@NotNull CompoundBinaryTag compound, @NotNull NBTComponentSerializerImpl serializer) {
    String actionString = compound.getString(HOVER_EVENT_ACTION);
    HoverEvent.Action<?> action = HoverEvent.Action.NAMES.valueOrThrow(actionString);
    Class<?> actionType = action.type();

    BinaryTag contents = compound.get(HOVER_EVENT_CONTENTS);
    if (contents == null) {
      contents = compound.get(HOVER_EVENT_VALUE);
      if (contents == null) {
        throw new IllegalArgumentException("The hover event doesn't contain any contents");
      }
    }

    if (Component.class.isAssignableFrom(actionType)) {
      return HoverEvent.showText(serializer.deserialize(contents));
    } else if (HoverEvent.ShowItem.class.isAssignableFrom(actionType)) {
      CompoundBinaryTag showItemContents = (CompoundBinaryTag) contents;

      Key itemId = Key.key(showItemContents.getString(SHOW_ITEM_ID));
      int itemCount = showItemContents.getInt(SHOW_ITEM_COUNT);

      BinaryTag components = showItemContents.get(SHOW_ITEM_COMPONENTS);
      BinaryTag tag = showItemContents.get(SHOW_ITEM_TAG);

      if (components != null) {
        CompoundBinaryTag componentsCompound = (CompoundBinaryTag) components;
        Map<Key, DataComponentValue> componentValues = new HashMap<>();

        for (String string : componentsCompound.keySet()) {
          BinaryTag value = componentsCompound.get(string);
          if (value == null) continue;
          componentValues.put(Key.key(string), NBTDataComponentValue.nbtDataComponentValue(value));
        }

        return HoverEvent.showItem(itemId, itemCount, componentValues);
      } else if (tag != null) {
        BinaryTagHolder holder = BinaryTagHolder.binaryTagHolder(((StringBinaryTag) tag).value());
        return HoverEvent.showItem(itemId, itemCount, holder);
      } else {
        return HoverEvent.showItem(itemId, itemCount);
      }
    } else if (HoverEvent.ShowEntity.class.isAssignableFrom(actionType)) {
      CompoundBinaryTag showEntityContents = (CompoundBinaryTag) contents;

      Key entityType = Key.key(showEntityContents.getString(SHOW_ENTITY_TYPE));
      UUID entityId = UUID.fromString(showEntityContents.getString(SHOW_ENTITY_ID));

      BinaryTag entityName = showEntityContents.get(SHOW_ENTITY_NAME);

      if (entityName != null) {
        return HoverEvent.showEntity(entityType, entityId, serializer.deserialize(entityName));
      } else {
        return HoverEvent.showEntity(entityType, entityId);
      }
    } else if (String.class.isAssignableFrom(actionType)) {
      return HoverEvent.showAchievement(((StringBinaryTag) contents).value());
    } else {
      // Never called, but needed for proper compilation
      throw new IllegalArgumentException("Unknown hover event");
    }
  }

  static <V> @Nullable CompoundBinaryTag serialize(@NotNull HoverEvent<V> event, @NotNull NBTComponentSerializerImpl serializer) {
    OptionState flags = serializer.flags();
    HoverEvent.Action<V> action = event.action();

    NBTSerializerOptions.HoverEventValueMode hoverMode = flags.value(NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE);

    if (action == HoverEvent.Action.SHOW_ACHIEVEMENT) {
      if (hoverMode == NBTSerializerOptions.HoverEventValueMode.MODERN_ONLY) {
        return null;
      }

      return CompoundBinaryTag.builder()
        .putString(HOVER_EVENT_ACTION, HOVER_EVENT_SHOW_ACHIEVEMENT)
        .putString(HOVER_EVENT_VALUE, (String) event.value())
        .build();
    }

    BinaryTag contents;
    String actionString;

    if (action == HoverEvent.Action.SHOW_TEXT) {
      contents = serializer.serialize((Component) event.value());
      actionString = HOVER_EVENT_SHOW_TEXT;
    } else if (action == HoverEvent.Action.SHOW_ITEM) {
      HoverEvent.ShowItem item = (HoverEvent.ShowItem) event.value();

      CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
        .putString(SHOW_ITEM_ID, item.item().asString())
        .putInt(SHOW_ITEM_COUNT, item.count());

      Map<Key, NBTDataComponentValue> components = item.dataComponentsAs(NBTDataComponentValue.class);
      NBTSerializerOptions.ShowItemHoverDataMode dataMode = flags.value(NBTSerializerOptions.SHOW_ITEM_HOVER_DATA_MODE);

      if (!components.isEmpty() && dataMode != NBTSerializerOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) {
        CompoundBinaryTag.Builder dataComponentsBuilder = CompoundBinaryTag.builder();

        for (Map.Entry<Key, NBTDataComponentValue> entry : components.entrySet()) {
          dataComponentsBuilder.put(entry.getKey().asString(), entry.getValue().binaryTag());
        }

        builder.put(SHOW_ITEM_COMPONENTS, dataComponentsBuilder.build());
      } else if (dataMode != NBTSerializerOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
        BinaryTagHolder holder = item.nbt();
        if (holder != null) {
          builder.putString(SHOW_ITEM_TAG, holder.string());
        }
      }

      contents = builder.build();
      actionString = HOVER_EVENT_SHOW_ITEM;
    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
      if (hoverMode == NBTSerializerOptions.HoverEventValueMode.LEGACY_ONLY) {
        return null;
      }

      HoverEvent.ShowEntity item = (HoverEvent.ShowEntity) event.value();

      CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
        .putString(SHOW_ENTITY_TYPE, item.type().asString())
        .putString(SHOW_ENTITY_ID, item.id().toString());

      Component customName = item.name();
      if (customName != null) {
        builder.put(SHOW_ENTITY_NAME, serializer.serialize(customName));
      }

      contents = builder.build();
      actionString = HOVER_EVENT_SHOW_ENTITY;
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + event + " as a HoverEvent");
    }

    return CompoundBinaryTag.builder()
      .putString(HOVER_EVENT_ACTION, actionString)
      .put(hoverMode == NBTSerializerOptions.HoverEventValueMode.LEGACY_ONLY ? HOVER_EVENT_VALUE : HOVER_EVENT_CONTENTS, contents)
      .build();
  }
}
