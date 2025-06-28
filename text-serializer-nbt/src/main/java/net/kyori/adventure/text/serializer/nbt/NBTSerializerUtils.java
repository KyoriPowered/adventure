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
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NBTSerializerUtils {

  private NBTSerializerUtils() {
  }

  static <B extends BinaryTag> @NotNull B getRequiredTag(@NotNull CompoundBinaryTag compound,
                                                         @NotNull String name, @NotNull BinaryTagType<B> tagType) {
    B tag = getOptionalTag(compound, name, tagType);
    if (tag == null) {
      throw new IllegalArgumentException("The specified compound tag does not contain a \"" + name + "\" field");
    }
    return tag;
  }

  static <B extends BinaryTag> @Nullable B getOptionalTag(@NotNull CompoundBinaryTag compound,
                                                          @NotNull String name, @NotNull BinaryTagType<B> tagType) {
    BinaryTag tag = compound.get(name);
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
