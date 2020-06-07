/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.nbt;

public final class TagTypes {
  public static final TagType<EndTag> END = TagType.create((byte) 0, input -> EndTag.get());
  public static final TagType<ByteTag> BYTE = TagType.createNumeric((byte) 1, input -> ByteTag.of(input.readByte()));
  public static final TagType<ShortTag> SHORT = TagType.createNumeric((byte) 2, input -> ShortTag.of(input.readShort()));
  public static final TagType<IntTag> INT = TagType.createNumeric((byte) 3, input -> IntTag.of(input.readInt()));
  public static final TagType<LongTag> LONG = TagType.createNumeric((byte) 4, input -> LongTag.of(input.readLong()));
  public static final TagType<FloatTag> FLOAT = TagType.createNumeric((byte) 5, input -> FloatTag.of(input.readFloat()));
  public static final TagType<DoubleTag> DOUBLE = TagType.createNumeric((byte) 6, input -> DoubleTag.of(input.readDouble()));
  public static final TagType<ByteArrayTag> BYTE_ARRAY = TagType.create((byte) 7, input -> {
    final int length = input.readInt();
    final byte[] value = new byte[length];
    input.readFully(value);
    return ByteArrayTag.of(value);
  });
  public static final TagType<StringTag> STRING = TagType.create((byte) 8, input -> StringTag.of(input.readUTF()));
  public static final TagType<ListTag> LIST = null; // TODO: ListTag
  public static final TagType<CompoundTag> COMPOUND = null; // TODO: CompoundTag
  public static final TagType<IntArrayTag> INT_ARRAY = TagType.create((byte) 11, input -> {
    final int length = input.readInt();
    final int[] value = new int[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readInt();
    }
    return IntArrayTag.of(value);
  });
  public static final TagType<LongArrayTag> LONG_ARRAY = TagType.create((byte) 12, input -> {
    final int length = input.readInt();
    final long[] value = new long[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readLong();
    }
    return LongArrayTag.of(value);
  });

  private TagTypes() {
  }
}
