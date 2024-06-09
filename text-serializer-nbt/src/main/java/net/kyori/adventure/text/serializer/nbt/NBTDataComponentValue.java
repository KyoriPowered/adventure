package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.event.DataComponentValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface NBTDataComponentValue extends DataComponentValue {
  @NotNull BinaryTag binaryTag();

  static @NotNull NBTDataComponentValue nbtDataComponentValue(@NotNull BinaryTag binaryTag) {
    return new NBTDataComponentValueImpl(binaryTag);
  }
}
