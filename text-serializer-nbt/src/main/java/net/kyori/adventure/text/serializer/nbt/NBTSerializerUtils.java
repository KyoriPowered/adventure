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

import java.io.IOException;
import java.util.function.Consumer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NBTSerializerUtils {

  static final TagStringIO SNBT_IO = TagStringIO.tagStringIO();
  static final Codec<BinaryTag, String, IOException, IOException> SNBT_CODEC = Codec.codec(SNBT_IO::asTag, SNBT_IO::asString);

  private NBTSerializerUtils() {
  }

  static @NotNull BinaryTag requiredTag(final @NotNull CompoundBinaryTag compound, final @NotNull String name) {
    final BinaryTag tag = compound.get(name);
    if (tag == null) {
      throw noSuchField(name);
    }
    return tag;
  }

  static <B extends BinaryTag> @NotNull B requiredTag(final @NotNull CompoundBinaryTag compound,
                                                      final @NotNull String name, final @NotNull BinaryTagType<B> tagType) {
    final B tag = optionalTag(compound, name, tagType);
    if (tag == null) {
      throw noSuchField(name);
    }
    return tag;
  }

  static <B extends BinaryTag> @Nullable B optionalTag(final @NotNull CompoundBinaryTag compound,
                                                       final @NotNull String name, final @NotNull BinaryTagType<B> tagType) {
    final BinaryTag tag = compound.get(name);
    if (tag == null) {
      return null;
    }

    final BinaryTagType<?> actualTagType = tag.type();
    if (actualTagType != tagType) {
      throw new IllegalArgumentException(
        "A type of the tag is different than expected." +
          " Expected: " + tagType.getClass().getSimpleName() +
          " Actual: " + actualTagType.getClass().getSimpleName()
      );
    }

    return (B) tag;
  }

  static boolean asBoolean(final @NotNull NumberBinaryTag tag) {
    // != 0 might look weird, but it is what vanilla does
    return tag.byteValue() != 0;
  }

  static @NotNull ByteBinaryTag asTag(final boolean value) {
    return value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO;
  }

  static void forEach(final @NotNull BinaryTag tag, final @NotNull Consumer<? super BinaryTag> action) {
    if (tag instanceof ListBinaryTag) {
      ((ListBinaryTag) tag).unwrapHeterogeneity().forEach(action);
    } else if (tag instanceof ByteArrayBinaryTag) {
      for (final byte value : ((ByteArrayBinaryTag) tag).value()) {
        action.accept(ByteBinaryTag.byteBinaryTag(value));
      }
    } else if (tag instanceof IntArrayBinaryTag) {
      for (final int value : ((IntArrayBinaryTag) tag).value()) {
        action.accept(IntBinaryTag.intBinaryTag(value));
      }
    } else if (tag instanceof LongArrayBinaryTag) {
      for (final long value : ((LongArrayBinaryTag) tag).value()) {
        action.accept(LongBinaryTag.longBinaryTag(value));
      }
    } else {
      throw new IllegalArgumentException("The specified tag (" + tag + ") does not represent an aggregate");
    }
  }

  private static @NotNull IllegalArgumentException noSuchField(final @NotNull String name) {
    return new IllegalArgumentException("The specified compound tag does not contain a field with name of \"" + name + "\"");
  }
}
