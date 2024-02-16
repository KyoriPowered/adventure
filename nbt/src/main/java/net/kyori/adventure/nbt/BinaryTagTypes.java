/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
  public static final BinaryTagType<EndBinaryTag> END = BinaryTagType.register(EndBinaryTag.class, (byte) 0, input -> EndBinaryTag.endBinaryTag(), null); // nothing to write
  /**
   * {@link ByteBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagType.registerNumeric(ByteBinaryTag.class, (byte) 1, input -> ByteBinaryTag.byteBinaryTag(input.readByte()), (tag, output) -> output.writeByte(tag.value()));
  /**
   * {@link ShortBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagType.registerNumeric(ShortBinaryTag.class, (byte) 2, input -> ShortBinaryTag.shortBinaryTag(input.readShort()), (tag, output) -> output.writeShort(tag.value()));
  /**
   * {@link IntBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<IntBinaryTag> INT = BinaryTagType.registerNumeric(IntBinaryTag.class, (byte) 3, input -> IntBinaryTag.intBinaryTag(input.readInt()), (tag, output) -> output.writeInt(tag.value()));
  /**
   * {@link LongBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagType.registerNumeric(LongBinaryTag.class, (byte) 4, input -> LongBinaryTag.longBinaryTag(input.readLong()), (tag, output) -> output.writeLong(tag.value()));
  /**
   * {@link FloatBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagType.registerNumeric(FloatBinaryTag.class, (byte) 5, input -> FloatBinaryTag.floatBinaryTag(input.readFloat()), (tag, output) -> output.writeFloat(tag.value()));
  /**
   * {@link DoubleBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagType.registerNumeric(DoubleBinaryTag.class, (byte) 6, input -> DoubleBinaryTag.doubleBinaryTag(input.readDouble()), (tag, output) -> output.writeDouble(tag.value()));
  /**
   * {@link ByteArrayBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte) 7, input -> {
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
  public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagType.register(StringBinaryTag.class, (byte) 8, input -> StringBinaryTag.stringBinaryTag(input.readUTF()), (tag, output) -> output.writeUTF(tag.value()));
  /**
   * {@link ListBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagType.register(ListBinaryTag.class, (byte) 9, input -> {
    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
    final int length = input.readInt();
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input, length * 8L)) {
      final List<BinaryTag> tags = new ArrayList<>(length);
      for (int i = 0; i < length; i++) {
        tags.add(type.read(input));
      }
      return ListBinaryTag.listBinaryTag(type, tags);
    }
  }, (tag, output) -> {
    output.writeByte(tag.elementType().id());
    final int size = tag.size();
    output.writeInt(size);
    for (final BinaryTag item : tag) {
      BinaryTagType.writeUntyped(item.type(), item, output);
    }
  });
  /**
   * {@link CompoundBinaryTag}.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte) 10, input -> {
    try (final BinaryTagScope ignored = TrackingDataInput.enter(input)) {
      final Map<String, BinaryTag> tags = new HashMap<>();
      BinaryTagType<? extends BinaryTag> type;
      while ((type = BinaryTagType.binaryTagType(input.readByte())) != BinaryTagTypes.END) {
        final String key = input.readUTF();
        final BinaryTag tag = type.read(input);
        tags.put(key, tag);
      }
      return new CompoundBinaryTagImpl(tags);
    }
  }, (tag, output) -> {
    for (final Map.Entry<String, ? extends BinaryTag> entry : tag) {
      final BinaryTag value = entry.getValue();
      if (value != null) {
        final BinaryTagType<? extends BinaryTag> type = value.type();
        output.writeByte(type.id());
        if (type != BinaryTagTypes.END) {
          output.writeUTF(entry.getKey());
          BinaryTagType.writeUntyped(type, value, output);
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
  public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte) 11, input -> {
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
    for (int i = 0; i < length; i++) {
      output.writeInt(value[i]);
    }
  });
  /**
   * {@link LongArrayBinaryTag}.
   *
   * @since 4.0.0
   * @sinceMinecraft 1.12
   */
  @SuppressWarnings("try")
  public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte) 12, input -> {
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
    for (int i = 0; i < length; i++) {
      output.writeLong(value[i]);
    }
  });

  private BinaryTagTypes() {
  }
}
