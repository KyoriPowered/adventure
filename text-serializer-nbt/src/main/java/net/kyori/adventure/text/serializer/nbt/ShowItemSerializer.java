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
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COMPONENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COUNT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_TAG;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_CODEC;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_IO;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getOptionalTag;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class ShowItemSerializer {

  private static final String DATA_COMPONENT_REMOVAL_PREFIX = "!";
  private static final String LEGACY_ITEM_COUNT = "Count";

  private static final int DEFAULT_ITEM_QUANTITY = 1;

  private ShowItemSerializer() {
  }

  static HoverEvent.@NotNull ShowItem deserialize(@NotNull BinaryTag tag, boolean snakeCase,
                                                  @NotNull NBTComponentSerializerImpl serializer) {
    try {
      return deserializeModern(tag, snakeCase);
    } catch (Exception exception) {
      if (snakeCase) {
        throw notSureHowToDeserialize(tag);
      } else {
        return deserializeLegacy(tag, serializer);
      }
    }
  }

  static @NotNull CompoundBinaryTag serialize(HoverEvent.@NotNull ShowItem showItem, boolean snakeCase,
                                              @NotNull NBTComponentSerializerImpl serializer) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .put(SHOW_ITEM_ID, KeySerializer.serialize(showItem.item()));

    int count = showItem.count();
    if (count != DEFAULT_ITEM_QUANTITY || serializer.options().value(NBTSerializerOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY)) {
      builder.putInt(SHOW_ITEM_COUNT, count);
    }

    NBTSerializerOptions.ShowItemHoverDataMode dataMode = serializer.options().value(NBTSerializerOptions.SHOW_ITEM_HOVER_DATA_MODE);
    if ((snakeCase || dataMode != NBTSerializerOptions.ShowItemHoverDataMode.EMIT_LEGACY_NBT) && !showItem.dataComponents().isEmpty()) {
      CompoundBinaryTag.Builder componentsTagBuilder = CompoundBinaryTag.builder();

      Map<Key, NBTDataComponentValue> components = showItem.dataComponentsAs(NBTDataComponentValue.class);
      for (Map.Entry<Key, NBTDataComponentValue> entry : components.entrySet()) {
        String key = entry.getKey().asString();
        BinaryTag value = entry.getValue().binaryTag();

        if (value instanceof EndBinaryTag) { // removed
          key = DATA_COMPONENT_REMOVAL_PREFIX + key;
        }

        componentsTagBuilder.put(key, value);
      }

      builder.put(SHOW_ITEM_COMPONENTS, componentsTagBuilder.build());
    } else if (!snakeCase && dataMode != NBTSerializerOptions.ShowItemHoverDataMode.EMIT_DATA_COMPONENTS) {
      BinaryTagHolder nbt = showItem.nbt();
      if (nbt != null) {
        builder.putString(SHOW_ITEM_TAG, nbt.string());
      }
    }

    return builder.build();
  }

  private static HoverEvent.@NotNull ShowItem deserializeModern(@NotNull BinaryTag tag, boolean snakeCase) {
    if (tag instanceof StringBinaryTag && !snakeCase) {
      StringBinaryTag castTag = (StringBinaryTag) tag;
      return HoverEvent.ShowItem.showItem(KeySerializer.deserialize(castTag), DEFAULT_ITEM_QUANTITY);
    } else if (!(tag instanceof CompoundBinaryTag)) {
      if (snakeCase) {
        throw new IllegalArgumentException("The specified binary tag isn't a compound tag");
      } else {
        throw new IllegalArgumentException("The specified binary tag isn't either a string tag or compound tag");
      }
    }

    CompoundBinaryTag compound = (CompoundBinaryTag) tag;

    Key itemId = KeySerializer.deserialize(getRequiredTag(compound, SHOW_ITEM_ID, BinaryTagTypes.STRING));
    IntBinaryTag countTag = getOptionalTag(compound, SHOW_ITEM_COUNT, BinaryTagTypes.INT);
    int itemCount = countTag == null ? DEFAULT_ITEM_QUANTITY : countTag.value();

    CompoundBinaryTag componentsTag = getOptionalTag(compound, SHOW_ITEM_COMPONENTS, BinaryTagTypes.COMPOUND);
    StringBinaryTag nbtTag = getOptionalTag(compound, SHOW_ITEM_TAG, BinaryTagTypes.STRING);

    if (componentsTag == null) {
      if (snakeCase || nbtTag == null) {
        return HoverEvent.ShowItem.showItem(itemId, itemCount);
      }
      return HoverEvent.ShowItem.showItem(itemId, itemCount, BinaryTagHolder.binaryTagHolder(nbtTag.value()));
    } else {
      Map<Key, DataComponentValue> componentValues = new HashMap<>();

      for (String string : componentsTag.keySet()) {
        boolean removed = string.startsWith(DATA_COMPONENT_REMOVAL_PREFIX);

        BinaryTag valueTag = componentsTag.get(string);
        if (valueTag == null) continue;

        if (removed) {
          string = string.substring(1);
        }

        componentValues.put(Key.key(string), removed ? DataComponentValue.removed() : NBTDataComponentValue.nbtDataComponentValue(valueTag));
      }

      return HoverEvent.ShowItem.showItem(itemId, itemCount, componentValues);
    }
  }

  private static HoverEvent.@NotNull ShowItem deserializeLegacy(@NotNull BinaryTag tag, @NotNull NBTComponentSerializerImpl serializer) {
    try {
      Component component = serializer.deserialize(tag);
      if (!(component instanceof TextComponent)) {
        throw notSureHowToDeserialize(tag);
      }

      String content = ((TextComponent) component).content();
      CompoundBinaryTag compound = SNBT_IO.asCompound(content);

      Key key = KeySerializer.deserialize(getRequiredTag(compound, SHOW_ITEM_ID, BinaryTagTypes.STRING));
      byte count = getRequiredTag(compound, LEGACY_ITEM_COUNT, BinaryTagTypes.BYTE).value();

      CompoundBinaryTag nbtTag = getOptionalTag(compound, SHOW_ITEM_TAG, BinaryTagTypes.COMPOUND);
      if (nbtTag == null) {
        return HoverEvent.ShowItem.showItem(key, count);
      } else {
        return HoverEvent.ShowItem.showItem(key, count, BinaryTagHolder.encode(nbtTag, SNBT_CODEC));
      }
    } catch (IOException exception) {
      throw notSureHowToDeserialize(tag);
    }
  }

  private static @NotNull IllegalArgumentException notSureHowToDeserialize(@NotNull BinaryTag tag) {
    return new IllegalArgumentException("Don't know how to turn " + tag + " into a show item hover event data");
  }
}
