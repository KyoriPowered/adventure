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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BinaryTagTypes {
  public static final BinaryTagType<EndBinaryTag> END = BinaryTagType.register((byte) 0, input -> EndBinaryTag.get(), null); // nothing to write
  public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagType.registerNumeric((byte) 1, input -> ByteBinaryTag.of(input.readByte()), (tag, output) -> output.writeByte(tag.value()));
  public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagType.registerNumeric((byte) 2, input -> ShortBinaryTag.of(input.readShort()), (tag, output) -> output.writeShort(tag.value()));
  public static final BinaryTagType<IntBinaryTag> INT = BinaryTagType.registerNumeric((byte) 3, input -> IntBinaryTag.of(input.readInt()), (tag, output) -> output.writeInt(tag.value()));
  public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagType.registerNumeric((byte) 4, input -> LongBinaryTag.of(input.readLong()), (tag, output) -> output.writeLong(tag.value()));
  public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagType.registerNumeric((byte) 5, input -> FloatBinaryTag.of(input.readFloat()), (tag, output) -> output.writeFloat(tag.value()));
  public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagType.registerNumeric((byte) 6, input -> DoubleBinaryTag.of(input.readDouble()), (tag, output) -> output.writeDouble(tag.value()));
  public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagType.register((byte) 7, input -> {
    final int length = input.readInt();
    final byte[] value = new byte[length];
    input.readFully(value);
    return ByteArrayBinaryTag.of(value);
  }, (tag, output) -> {
    final byte[] value = (tag instanceof ByteArrayBinaryTagImpl) ? ((ByteArrayBinaryTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    output.write(value);
  });
  public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagType.register((byte) 8, input -> StringBinaryTag.of(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
  @SuppressWarnings("unchecked")
  public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagType.register((byte) 9, input -> {
    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
    final int length = input.readInt();
    final List<BinaryTag> tags = new ArrayList<>(length);
    for(int i = 0; i < length; i++) {
      tags.add(type.read(input));
    }
    return ListBinaryTag.of(type, tags);
  }, (tag, output) -> {
    output.writeByte(tag.listType().id());
    final int size = tag.size();
    output.writeInt(size);
    for(BinaryTag item : tag) {
      ((BinaryTagType<BinaryTag>) item.type()).write(item, output);
    }
  });
  @SuppressWarnings("unchecked")
  public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagType.register((byte) 10, input -> {
    final Map<String, BinaryTag> tags = new HashMap<>();
    BinaryTagType<? extends BinaryTag> type;
    while((type = BinaryTagType.of(input.readByte())) != BinaryTagTypes.END) {
      final String key = input.readUTF();
      final BinaryTag tag = type.read(input);
      tags.put(key, tag);
    }
    return new CompoundBinaryTagImpl(tags);
  }, (tag, output) -> {
    for(Map.Entry<String, ? extends BinaryTag> entry : tag) {
      if(entry.getValue() != null) {
        final BinaryTagType<? extends BinaryTag> type = entry.getValue().type();
        output.writeByte(type.id());
        if(type != BinaryTagTypes.END) {
          output.writeUTF(entry.getKey());
          ((BinaryTagType<BinaryTag>) type).write(entry.getValue(), output);
        }
      }
    }
    output.writeByte(BinaryTagTypes.END.id());
  });
  public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagType.register((byte) 11, input -> {
    final int length = input.readInt();
    final int[] value = new int[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readInt();
    }
    return IntArrayBinaryTag.of(value);
  }, (tag, output) -> {
    final int[] value = (tag instanceof IntArrayBinaryTagImpl) ? ((IntArrayBinaryTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    for(int i = 0, length = value.length; i < length; i++) {
      output.writeInt(value[i]);
    }
  });
  public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagType.register((byte) 12, input -> {
    final int length = input.readInt();
    final long[] value = new long[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readLong();
    }
    return LongArrayBinaryTag.of(value);
  }, (tag, output) -> {
    final long[] value = (tag instanceof LongArrayBinaryTagImpl) ? ((LongArrayBinaryTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    for(int i = 0, length = value.length; i < length; i++) {
      output.writeLong(value[i]);
    }
  });

  private BinaryTagTypes() {
  }
}
