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
  public static final BinaryTagType<EndBinaryTag> END = BinaryTagType.register(EndBinaryTag.class, (byte) 0, EndBinaryTagImpl.READER, EndBinaryTagImpl.WRITER);
  /**
   * {@link ByteBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagType.registerNumeric(ByteBinaryTag.class, (byte) 1, ByteBinaryTagImpl.READER, ByteBinaryTagImpl.WRITER);
  /**
   * {@link ShortBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagType.registerNumeric(ShortBinaryTag.class, (byte) 2, ShortBinaryTagImpl.READER, ShortBinaryTagImpl.WRITER);
  /**
   * {@link IntBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<IntBinaryTag> INT = BinaryTagType.registerNumeric(IntBinaryTag.class, (byte) 3, IntBinaryTagImpl.READER, IntBinaryTagImpl.WRITER);
  /**
   * {@link LongBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagType.registerNumeric(LongBinaryTag.class, (byte) 4, LongBinaryTagImpl.READER, LongBinaryTagImpl.WRITER);
  /**
   * {@link FloatBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagType.registerNumeric(FloatBinaryTag.class, (byte) 5, FloatBinaryTagImpl.READER, FloatBinaryTagImpl.WRITER);
  /**
   * {@link DoubleBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagType.registerNumeric(DoubleBinaryTag.class, (byte) 6, DoubleBinaryTagImpl.READER, DoubleBinaryTagImpl.WRITER);
  /**
   * {@link ByteArrayBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte) 7, ByteArrayBinaryTagImpl.READER, ByteArrayBinaryTagImpl.WRITER);
  /**
   * {@link StringBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagType.register(StringBinaryTag.class, (byte) 8, StringBinaryTagImpl.READER, StringBinaryTagImpl.WRITER);
  /**
   * {@link ListBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagType.register(ListBinaryTag.class, (byte) 9, ListBinaryTagImpl.READER, ListBinaryTagImpl.WRITER);
  /**
   * {@link CompoundBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte) 10, CompoundBinaryTagImpl.READER, CompoundBinaryTagImpl.WRITER);
  /**
   * {@link IntArrayBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte) 11, IntArrayBinaryTagImpl.READER, IntArrayBinaryTagImpl.WRITER);
  /**
   * {@link LongArrayBinaryTag}.
   *
   * @since 4.0.0
   */
  public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte) 12, LongArrayBinaryTagImpl.READER, LongArrayBinaryTagImpl.WRITER);

  private BinaryTagTypes() {
  }
}
