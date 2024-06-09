package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import org.jetbrains.annotations.NotNull;

final class NBTDataComponentValueImpl implements NBTDataComponentValue {

  private final BinaryTag binaryTag;

  NBTDataComponentValueImpl(@NotNull BinaryTag binaryTag) {
    this.binaryTag = binaryTag;
  }

  @Override
  public @NotNull BinaryTag binaryTag() {
    return this.binaryTag;
  }
}
