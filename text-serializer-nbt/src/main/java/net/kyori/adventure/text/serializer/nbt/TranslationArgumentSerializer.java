package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.DoubleBinaryTag;
import net.kyori.adventure.nbt.FloatBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.kyori.adventure.nbt.ShortBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import org.jetbrains.annotations.NotNull;

final class TranslationArgumentSerializer {

  private TranslationArgumentSerializer() {
  }

  static @NotNull TranslationArgument deserialize(@NotNull BinaryTag tag, @NotNull NBTComponentSerializerImpl serializer) {
    /* Serialized booleans are not deserialized as booleans because Minecraft also does that - NbtOps serializes
       booleans as byte tags and there is no way to distinguish the original type during deserialization.*/
    if (tag instanceof NumberBinaryTag) {
      return TranslationArgument.numeric(((NumberBinaryTag) tag).numberValue());
    } else {
      return TranslationArgument.component(serializer.deserialize(tag));
    }
  }

  static @NotNull BinaryTag serialize(@NotNull TranslationArgument argument, @NotNull NBTComponentSerializerImpl serializer) {
    Object value = argument.value();
    if (value instanceof Boolean) {
      return NBTSerializerUtils.asTag((boolean) value);
    } else if (value instanceof Byte) {
      return ByteBinaryTag.byteBinaryTag((byte) value);
    } else if (value instanceof Short) {
      return ShortBinaryTag.shortBinaryTag((short) value);
    } else if (value instanceof Integer) {
      return IntBinaryTag.intBinaryTag((int) value);
    } else if (value instanceof Long) {
      return LongBinaryTag.longBinaryTag((long) value);
    } else if (value instanceof Float) {
      return FloatBinaryTag.floatBinaryTag((float) value);
    } else if (value instanceof Number) {
      return DoubleBinaryTag.doubleBinaryTag(((Number) value).doubleValue());
    } else if (value instanceof Component) {
      return serializer.serialize((Component) value);
    } else {
      throw new IllegalArgumentException("Don't know how to serialize the specified translation argument value: " + value);
    }
  }
}
