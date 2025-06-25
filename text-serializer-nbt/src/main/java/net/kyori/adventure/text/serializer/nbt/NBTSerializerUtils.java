package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NBTSerializerUtils {

  private NBTSerializerUtils() {
  }

  static <B extends BinaryTag> @NotNull B getRequiredTag(@NotNull CompoundBinaryTag compoundTag,
                                                         @NotNull String name, @NotNull BinaryTagType<B> tagType) {
    B tag = getOptionalTag(compoundTag, name, tagType);
    if (tag == null) {
      throw new IllegalArgumentException("The specified compound tag does not contain a \"" + name + "\" field");
    }
    return tag;
  }

  static <B extends BinaryTag> @Nullable B getOptionalTag(@NotNull CompoundBinaryTag compoundTag,
                                                          @NotNull String name, @NotNull BinaryTagType<B> tagType) {
    BinaryTag tag = compoundTag.get(name);
    if (tag == null) {
      return null;
    }

    BinaryTagType<?> actualTagType = tag.type();
    if (actualTagType != tagType) {
      throw new IllegalArgumentException(
        "A type of the tag is different than expected." +
          " Expected: " + tagType.getClass().getSimpleName() +
          " Actual: " + actualTagType.getClass().getSimpleName()
      );
    }

    return (B) tag;
  }

  static boolean asBoolean(@NotNull NumberBinaryTag tag) {
    // != 0 might look weird, but it is what vanilla does
    return tag.byteValue() != 0;
  }

  static @NotNull ByteBinaryTag asTag(boolean value) {
    return value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO;
  }
}
