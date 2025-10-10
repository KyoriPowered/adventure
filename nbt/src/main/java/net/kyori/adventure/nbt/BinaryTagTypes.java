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
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All known binary tag types.
 *
 * @since 4.0.0
 */
public final class BinaryTagTypes {
  /**
   * {@link EndBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<EndBinaryTag> END = BinaryTagTypeImpl.register(EndBinaryTag.class, (byte) 0, input -> EndBinaryTag.endBinaryTag(), null); // nothing to write
  /**
   * {@link ByteBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagTypeImpl.registerNumeric(ByteBinaryTag.class, (byte) 1, input -> ByteBinaryTag.byteBinaryTag(input.readByte()), (tag, output) -> output.writeByte(tag.value()));
  /**
   * {@link ShortBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagTypeImpl.registerNumeric(ShortBinaryTag.class, (byte) 2, input -> ShortBinaryTag.shortBinaryTag(input.readShort()), (tag, output) -> output.writeShort(tag.value()));
  /**
   * {@link IntBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<IntBinaryTag> INT = BinaryTagTypeImpl.registerNumeric(IntBinaryTag.class, (byte) 3, input -> IntBinaryTag.intBinaryTag(input.readInt()), (tag, output) -> output.writeInt(tag.value()));
  /**
   * {@link LongBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagTypeImpl.registerNumeric(LongBinaryTag.class, (byte) 4, input -> LongBinaryTag.longBinaryTag(input.readLong()), (tag, output) -> output.writeLong(tag.value()));
  /**
   * {@link FloatBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagTypeImpl.registerNumeric(FloatBinaryTag.class, (byte) 5, input -> FloatBinaryTag.floatBinaryTag(input.readFloat()), (tag, output) -> output.writeFloat(tag.value()));
  /**
   * {@link DoubleBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagTypeImpl.registerNumeric(DoubleBinaryTag.class, (byte) 6, input -> DoubleBinaryTag.doubleBinaryTag(input.readDouble()), (tag, output) -> output.writeDouble(tag.value()));
  /**
   * {@link ByteArrayBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagTypeImpl.register(ByteArrayBinaryTag.class, (byte) 7, input -> {
    final int length = input.readInt();
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length)) {
      final byte[] value = new byte[length];
      input.readFully(value);
      return ByteArrayBinaryTag.byteArrayBinaryTag(value);
    }
  }, (tag, output) -> {
    final byte[] value = ByteArrayBinaryTagImpl.value(tag);
    output.writeInt(value.length);
    output.write(value);
  });
  /**
   * {@link StringBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagTypeImpl.register(StringBinaryTag.class, (byte) 8, input -> StringBinaryTag.stringBinaryTag(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
  /**
   * {@link ListBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagTypeImpl.register(ListBinaryTag.class, (byte) 9, input -> {
    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
    final int length = input.readInt();
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length * 8L)) {
      final List<BinaryTag> tags = new ArrayList<>(length);
      for (int i = 0; i < length; i++) {
        tags.add(type.read(input));
      }
      return ListBinaryTag.listBinaryTag(type, tags);
    }
  }, (rawTag, output) -> {
    final ListBinaryTag tag = rawTag.wrapHeterogeneity();
    output.writeByte(tag.elementType().id());
    final int size = tag.size();
    output.writeInt(size);
    for (final BinaryTag item : tag) {
      BinaryTagTypeImpl.writeUntyped(item.type(), item, output);
    }
  });
  /**
   * {@link CompoundBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagTypeImpl.register(CompoundBinaryTag.class, (byte) 10, input -> {
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input)) {
      final Map<String, BinaryTag> tags = new HashMap<>();
      BinaryTagType<? extends BinaryTag> type;
      while ((type = BinaryTagType.binaryTagType(input.readByte())) != BinaryTagTypes.END) {
        final String key = input.readUTF();
        final BinaryTag tag = type.read(input);
        tags.put(key, tag);
      }
      return CompoundBinaryTagImpl.create(tags);
    }
  }, (tag, output) -> {
    for (final Map.Entry<String, ? extends BinaryTag> entry : tag) {
      final BinaryTag value = entry.getValue();
      if (value != null) {
        final BinaryTagType<? extends BinaryTag> type = value.type();
        output.writeByte(type.id());
        if (type != BinaryTagTypes.END) {
          output.writeUTF(entry.getKey());
          BinaryTagTypeImpl.writeUntyped(type, value, output);
        }
      }
    }
    output.writeByte(BinaryTagTypes.END.id());
  });
  /**
   * {@link IntArrayBinaryTag}.
   *
   * @since 4.0.0
   * @sinceMinecraft 1.2.1
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagTypeImpl.register(IntArrayBinaryTag.class, (byte) 11, input -> {
    final int length = input.readInt();
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length * 4L)) {
      final int[] value = new int[length];
      for (int i = 0; i < length; i++) {
        value[i] = input.readInt();
      }
      return IntArrayBinaryTag.intArrayBinaryTag(value);
    }
  }, (tag, output) -> {
    final int[] value = IntArrayBinaryTagImpl.value(tag);
    final int length = value.length;
    output.writeInt(length);
    for (final int j : value) {
      output.writeInt(j);
    }
  });
  /**
   * {@link LongArrayBinaryTag}.
   *
   * @since 4.0.0
   * @sinceMinecraft 1.12
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagTypeImpl.register(LongArrayBinaryTag.class, (byte) 12, input -> {
    final int length = input.readInt();
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length * 8L)) {
      final long[] value = new long[length];
      for (int i = 0; i < length; i++) {
        value[i] = input.readLong();
      }
      return LongArrayBinaryTag.longArrayBinaryTag(value);
    }
  }, (tag, output) -> {
    final long[] value = LongArrayBinaryTagImpl.value(tag);
    final int length = value.length;
    output.writeInt(length);
    for (final long l : value) {
      output.writeLong(l);
    }
  });
  /**
   * Synthetic tag type used as a list's element type to indicate it contains elements of multiple types.
   *
   * <p>This tag type cannot be read or written. List tag serialization will auto-box lists with this element type.</p>
   *
   * @since 4.21.0
   */
  public static final BinaryTagType<BinaryTag> LIST_WILDCARD = new BinaryTagTypeImpl<>(BinaryTag.class, Byte.MAX_VALUE, input -> {
    throw new IllegalArgumentException("Unable to read values of placeholder type. This tag type exists only to indicate heterogeneous lists");
  }, (tag, output) -> {
    throw new IllegalArgumentException("Unable to write values of placeholder type. This tag type exists only to indicate heterogeneous lists");
  }, false);

  private BinaryTagTypes() {
  }
}
