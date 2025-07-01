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

  static @NotNull TranslationArgument deserialize(final @NotNull BinaryTag tag, final @NotNull NBTComponentSerializerImpl serializer) {
    /* Serialized booleans are not deserialized as booleans because Minecraft also does that - NbtOps serializes
       booleans as byte tags and there is no way to distinguish the original type during deserialization.*/
    if (tag instanceof NumberBinaryTag) {
      return TranslationArgument.numeric(((NumberBinaryTag) tag).numberValue());
    } else {
      return TranslationArgument.component(serializer.deserialize(tag));
    }
  }

  static @NotNull BinaryTag serialize(final @NotNull TranslationArgument argument, final @NotNull NBTComponentSerializerImpl serializer) {
    final Object value = argument.value();
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
