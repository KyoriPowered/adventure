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

public final class TagTypes {
  public static final TagType<EndTag> END = TagType.register((byte) 0, input -> EndTag.get(), null); // nothing to write
  public static final TagType<ByteTag> BYTE = TagType.registerNumeric((byte) 1, input -> ByteTag.of(input.readByte()), (tag, output) -> output.writeByte(tag.value()));
  public static final TagType<ShortTag> SHORT = TagType.registerNumeric((byte) 2, input -> ShortTag.of(input.readShort()), (tag, output) -> output.writeShort(tag.value()));
  public static final TagType<IntTag> INT = TagType.registerNumeric((byte) 3, input -> IntTag.of(input.readInt()), (tag, output) -> output.writeInt(tag.value()));
  public static final TagType<LongTag> LONG = TagType.registerNumeric((byte) 4, input -> LongTag.of(input.readLong()), (tag, output) -> output.writeLong(tag.value()));
  public static final TagType<FloatTag> FLOAT = TagType.registerNumeric((byte) 5, input -> FloatTag.of(input.readFloat()), (tag, output) -> output.writeFloat(tag.value()));
  public static final TagType<DoubleTag> DOUBLE = TagType.registerNumeric((byte) 6, input -> DoubleTag.of(input.readDouble()), (tag, output) -> output.writeDouble(tag.value()));
  public static final TagType<ByteArrayTag> BYTE_ARRAY = TagType.register((byte) 7, input -> {
    final int length = input.readInt();
    final byte[] value = new byte[length];
    input.readFully(value);
    return ByteArrayTag.of(value);
  }, (tag, output) -> {
    final byte[] value = (tag instanceof ByteArrayTagImpl) ? ((ByteArrayTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    output.write(value);
  });
  public static final TagType<StringTag> STRING = TagType.register((byte) 8, input -> StringTag.of(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
  @SuppressWarnings("unchecked")
  public static final TagType<ListTag> LIST = TagType.register((byte) 9, input -> {
    final TagType<? extends Tag> type = TagType.of(input.readByte());
    final int length = input.readInt();
    final List<Tag> tags = new ArrayList<>(length);
    for(int i = 0; i < length; i++) {
      tags.add(type.read(input));
    }
    return ListTag.of(type, tags);
  }, (tag, output) -> {
    output.writeByte(tag.listType().id());
    final int size = tag.size();
    output.writeInt(size);
    for(int i = 0; i < size; i++) {
      final Tag item = tag.get(i);
      ((TagType<Tag>) item.type()).write(item, output);
    }
  });
  @SuppressWarnings("unchecked")
  public static final TagType<CompoundTag> COMPOUND = TagType.register((byte) 10, input -> {
    final Map<String, Tag> tags = new HashMap<>();
    TagType<? extends Tag> type;
    while((type = TagType.of(input.readByte())) != TagTypes.END) {
      final String key = input.readUTF();
      final Tag tag = type.read(input);
      tags.put(key, tag);
    }
    return new CompoundTagImpl(tags);
  }, (tag, output) -> {
    for(final String key : tag.keySet()) {
      final Tag item = tag.get(key);
      if(item != null) {
        final TagType<? extends Tag> type = item.type();
        output.writeByte(type.id());
        if(type != TagTypes.END) {
          output.writeUTF(key);
          ((TagType<Tag>) type).write(item, output);
        }
      }
    }
    output.writeByte(TagTypes.END.id());
  });
  public static final TagType<IntArrayTag> INT_ARRAY = TagType.register((byte) 11, input -> {
    final int length = input.readInt();
    final int[] value = new int[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readInt();
    }
    return IntArrayTag.of(value);
  }, (tag, output) -> {
    final int[] value = (tag instanceof IntArrayTagImpl) ? ((IntArrayTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    for(int i = 0, length = value.length; i < length; i++) {
      output.writeInt(value[i]);
    }
  });
  public static final TagType<LongArrayTag> LONG_ARRAY = TagType.register((byte) 12, input -> {
    final int length = input.readInt();
    final long[] value = new long[length];
    for(int i = 0; i < length; i++) {
      value[i] = input.readLong();
    }
    return LongArrayTag.of(value);
  }, (tag, output) -> {
    final long[] value = (tag instanceof LongArrayTagImpl) ? ((LongArrayTagImpl) tag).value : tag.value();
    output.writeInt(value.length);
    for(int i = 0, length = value.length; i < length; i++) {
      output.writeLong(value[i]);
    }
  });

  private TagTypes() {
  }
}
