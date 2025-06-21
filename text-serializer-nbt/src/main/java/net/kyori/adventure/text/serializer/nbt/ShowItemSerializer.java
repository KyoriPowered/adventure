package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COMPONENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_COUNT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHOW_ITEM_ID;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getOptionalTag;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class ShowItemSerializer {

  private static final String DATA_COMPONENT_REMOVAL_PREFIX = "!";

  private ShowItemSerializer() {
  }

  static HoverEvent.@NotNull ShowItem deserialize(@NotNull CompoundBinaryTag compound) {
    Key itemId = Key.key(getRequiredTag(compound, SHOW_ITEM_ID, BinaryTagTypes.STRING).value());

    IntBinaryTag countTag = getOptionalTag(compound, SHOW_ITEM_COUNT, BinaryTagTypes.INT);
    int itemCount = countTag == null ? 1 : countTag.value();

    CompoundBinaryTag components = getOptionalTag(compound, SHOW_ITEM_COMPONENTS, BinaryTagTypes.COMPOUND);
    if (components == null) {
      return HoverEvent.ShowItem.showItem(itemId, itemCount);
    } else {
      Map<Key, DataComponentValue> componentValues = new HashMap<>();

      for (String string : components.keySet()) {
        boolean removed = string.startsWith(DATA_COMPONENT_REMOVAL_PREFIX);

        BinaryTag value = components.get(string);
        if (value == null) continue;

        if (removed) {
          string = string.substring(1);
        }

        componentValues.put(Key.key(string), removed ? DataComponentValue.removed() : NBTDataComponentValue.nbtDataComponentValue(value));
      }

      return HoverEvent.ShowItem.showItem(itemId, itemCount, componentValues);
    }
  }

  static @NotNull CompoundBinaryTag serialize(HoverEvent.@NotNull ShowItem showItem, @NotNull NBTComponentSerializerImpl serializer) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(SHOW_ITEM_ID, showItem.item().asString());

    int count = showItem.count();
    if (count != 1 || serializer.flags().value(NBTSerializerOptions.EMIT_DEFAULT_ITEM_HOVER_QUANTITY)) {
      builder.putInt(SHOW_ITEM_COUNT, count);
    }

    if (!showItem.dataComponents().isEmpty()) {
      CompoundBinaryTag.Builder dataComponentsBuilder = CompoundBinaryTag.builder();

      Map<Key, NBTDataComponentValue> components = showItem.dataComponentsAs(NBTDataComponentValue.class);
      for (Map.Entry<Key, NBTDataComponentValue> entry : components.entrySet()) {
        String key = entry.getKey().asString();
        BinaryTag value = entry.getValue().binaryTag();

        if (value instanceof EndBinaryTag) { // removed
          key = DATA_COMPONENT_REMOVAL_PREFIX + key;
        }

        dataComponentsBuilder.put(key, value);
      }

      builder.put(SHOW_ITEM_COMPONENTS, dataComponentsBuilder.build());
    }

    return builder.build();
  }
}
