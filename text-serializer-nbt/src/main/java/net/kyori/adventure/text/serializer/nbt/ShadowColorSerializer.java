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
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.text.format.ShadowColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ShadowColorSerializer {

  private ShadowColorSerializer() {
  }

  static @NotNull ShadowColor deserialize(final @NotNull BinaryTag tag) {
    if (tag instanceof IntBinaryTag) {
      final IntBinaryTag castTag = (IntBinaryTag) tag;
      return ShadowColor.shadowColor(castTag.value());
    } else if (tag instanceof ListBinaryTag) {
      final ListBinaryTag castTag = (ListBinaryTag) tag;
      return ShadowColor.shadowColor(
        shadowColorComponent(castTag, 0),
        shadowColorComponent(castTag, 1),
        shadowColorComponent(castTag, 2),
        shadowColorComponent(castTag, 3)
      );
    } else {
      throw new IllegalArgumentException("The binary tag representing the shadow color is of an invalid type");
    }
  }

  static @Nullable BinaryTag serialize(final @NotNull ShadowColor color, final @NotNull NBTComponentSerializerImpl serializer) {
    return serializer.options().value(NBTSerializerOptions.EMIT_SHADOW_COLOR) ? IntBinaryTag.intBinaryTag(color.value()) : null;
  }

  private static int shadowColorComponent(final @NotNull ListBinaryTag tag, final int index) {
    return (int) (tag.getFloat(index) * 0xff);
  }
}
