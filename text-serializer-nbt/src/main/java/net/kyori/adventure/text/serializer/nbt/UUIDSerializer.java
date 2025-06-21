package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class UUIDSerializer {

  private static final long LONG_HALF = 0xffffffffL;

  private UUIDSerializer() {
  }

  static @NotNull UUID deserialize(@NotNull BinaryTag tag) {
    if (tag instanceof StringBinaryTag) {
      return UUID.fromString(((StringBinaryTag) tag).value());
    } else if (tag instanceof IntArrayBinaryTag) {
      return createUUIDFromArray(((IntArrayBinaryTag) tag).value());
    } else if (tag instanceof ListBinaryTag) {
      ListBinaryTag castTag = (ListBinaryTag) tag;
      int[] array = new int[castTag.size()];

      for (int index = 0; index < array.length; index++) {
        array[index] = castTag.getInt(index);
      }

      return createUUIDFromArray(array);
    } else {
      throw new IllegalArgumentException("Don't know how to deserialize an UUID from the specified binary tag: " + tag.getClass().getSimpleName());
    }
  }

  static @NotNull BinaryTag serialize(@NotNull UUID uuid, NBTSerializerOptions.@NotNull ShowEntityUUIDEmitMode emitMode) {
    switch (emitMode) {
      case EMIT_STRING:
        return StringBinaryTag.stringBinaryTag(uuid.toString());
      case EMIT_INT_ARRAY:
        return IntArrayBinaryTag.intArrayBinaryTag(createArrayFromUUID(uuid));
      case EMIT_LIST:
        List<BinaryTag> tags = new ArrayList<>();
        for (int value : createArrayFromUUID(uuid)) {
          tags.add(IntBinaryTag.intBinaryTag(value));
        }
        return ListBinaryTag.listBinaryTag(BinaryTagTypes.INT, tags);
      default:
        // Never called, but needed for proper compilation
        throw new IllegalStateException("Unknown emit mode: " + emitMode);
    }
  }

  private static @NotNull UUID createUUIDFromArray(int @NotNull [] array) {
    long mostSignificantBits = binaryConcat(array[0], array[1]);
    long leastSignificantBits = binaryConcat(array[2], array[3]);
    return new UUID(mostSignificantBits, leastSignificantBits);
  }

  private static int @NotNull [] createArrayFromUUID(@NotNull UUID uuid) {
    long mostSignificantBits = uuid.getMostSignificantBits();
    long leastSignificantBits = uuid.getLeastSignificantBits();
    return new int[] {
      mostSignificantBits(mostSignificantBits), leastSignificantBits(mostSignificantBits),
      mostSignificantBits(leastSignificantBits), leastSignificantBits(leastSignificantBits)
    };
  }

  private static long binaryConcat(int mostSignificantBits, int leastSignificantBits) {
    return ((long) mostSignificantBits << Integer.SIZE) | ((long) leastSignificantBits & LONG_HALF);
  }

  private static int mostSignificantBits(long value) {
    return (int) ((value >> Integer.SIZE) & LONG_HALF);
  }

  private static int leastSignificantBits(long value) {
    return (int) (value & LONG_HALF);
  }
}
