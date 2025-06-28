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

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.jetbrains.annotations.NotNull;

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

  static @NotNull BinaryTag serialize(@NotNull UUID uuid) {
    long mostSignificantBits = uuid.getMostSignificantBits();
    long leastSignificantBits = uuid.getLeastSignificantBits();
    return IntArrayBinaryTag.intArrayBinaryTag(
      mostSignificantBits(mostSignificantBits), leastSignificantBits(mostSignificantBits),
      mostSignificantBits(leastSignificantBits), leastSignificantBits(leastSignificantBits)
    );
  }

  private static @NotNull UUID createUUIDFromArray(int @NotNull [] array) {
    long mostSignificantBits = binaryConcat(array[0], array[1]);
    long leastSignificantBits = binaryConcat(array[2], array[3]);
    return new UUID(mostSignificantBits, leastSignificantBits);
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
