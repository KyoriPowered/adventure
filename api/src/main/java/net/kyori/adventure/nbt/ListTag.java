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

import java.util.List;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ListTag extends ListTagSetter<ListTag>, Tag {
  static @NonNull ListTag empty() {
    return ListTagImpl.EMPTY;
  }

  static @NonNull Builder builder() {
    return new ListTagBuilder();
  }

  static @NonNull ListTag of(final @NonNull TagType<? extends Tag> type, final @NonNull List<Tag> tags) {
    return new ListTagImpl(type, tags);
  }

  @Override
  default @NonNull TagType<ListTag> type() {
    return TagTypes.LIST;
  }

  /**
   * Gets the type of element stored in this list.
   *
   * @return the type
   */
  @NonNull TagType<? extends Tag> listType();

  int size();

  /**
   * Gets a tag.
   *
   * @param index the index
   * @return the tag
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  @NonNull Tag get(final @NonNegative int index);

  /**
   * Gets a byte.
   *
   * @param index the index
   * @return the byte value, or {@code 0}
   */
  default byte getByte(final @NonNegative int index) {
    return this.getByte(index, (byte) 0);
  }

  /**
   * Gets a byte.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the byte value, or {@code defaultValue}
   */
  default byte getByte(final @NonNegative int index, final byte defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).byteValue();
    }
    return defaultValue;
  }

  /**
   * Gets a short.
   *
   * @param index the index
   * @return the short value, or {@code 0}
   */
  default short getShort(final @NonNegative int index) {
    return this.getShort(index, (short) 0);
  }

  /**
   * Gets a short.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the short value, or {@code defaultValue}
   */
  default short getShort(final @NonNegative int index, final short defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).shortValue();
    }
    return defaultValue;
  }

  /**
   * Gets an int.
   *
   * @param index the index
   * @return the int value, or {@code 0}
   */
  default int getInt(final @NonNegative int index) {
    return this.getInt(index, 0);
  }

  /**
   * Gets an int.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the int value, or {@code defaultValue}
   */
  default int getInt(final @NonNegative int index, final int defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).intValue();
    }
    return defaultValue;
  }

  /**
   * Gets a long.
   *
   * @param index the index
   * @return the long value, or {@code 0}
   */
  default long getLong(final @NonNegative int index) {
    return this.getLong(index, 0L);
  }

  /**
   * Gets a long.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the long value, or {@code defaultValue}
   */
  default long getLong(final @NonNegative int index, final long defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).longValue();
    }
    return defaultValue;
  }

  /**
   * Gets a float.
   *
   * @param index the index
   * @return the float value, or {@code 0}
   */
  default float getFloat(final @NonNegative int index) {
    return this.getFloat(index, 0f);
  }

  /**
   * Gets a float.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the float value, or {@code defaultValue}
   */
  default float getFloat(final @NonNegative int index, final float defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).floatValue();
    }
    return defaultValue;
  }

  /**
   * Gets a double.
   *
   * @param index the index
   * @return the double value, or {@code 0}
   */
  default double getDouble(final @NonNegative int index) {
    return this.getDouble(index, 0d);
  }

  /**
   * Gets a double.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the double value, or {@code defaultValue}
   */
  default double getDouble(final @NonNegative int index, final double defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberTag) tag).doubleValue();
    }
    return defaultValue;
  }

  /**
   * Gets an array of bytes.
   *
   * @param index the index
   * @return the array of bytes, or a zero-length array
   */
  default @NonNull byte[] getByteArray(final @NonNegative int index) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.BYTE_ARRAY) {
      return ((ByteArrayTag) tag).value();
    }
    return new byte[0];
  }

  /**
   * Gets an array of bytes.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of bytes, or {@code defaultValue}
   */
  default @NonNull byte[] getByteArray(final @NonNegative int index, final @NonNull byte[] defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.BYTE_ARRAY) {
      return ((ByteArrayTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets a string.
   *
   * @param index the index
   * @return the string value, or {@code ""}
   */
  default @NonNull String getString(final @NonNegative int index) {
    return this.getString(index, "");
  }

  /**
   * Gets a string.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the string value, or {@code defaultValue}
   */
  default @NonNull String getString(final @NonNegative int index, final @NonNull String defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.STRING) {
      return ((StringTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets a compound.
   *
   * @param index the index
   * @return the compound, or a new compound
   */
  default @NonNull CompoundTag getCompound(final @NonNegative int index) {
    return this.getCompound(index, CompoundTag.empty());
  }

  /**
   * Gets a compound.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the compound, or {@code defaultValue}
   */
  default @NonNull CompoundTag getCompound(final @NonNegative int index, final @NonNull CompoundTag defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.COMPOUND) {
      return (CompoundTag) tag;
    }
    return defaultValue;
  }

  /**
   * Gets an array of ints.
   *
   * @param index the index
   * @return the array of ints, or a zero-length array
   */
  default int@NonNull[] getIntArray(final @NonNegative int index) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.INT_ARRAY) {
      return ((IntArrayTag) tag).value();
    }
    return new int[0];
  }

  /**
   * Gets an array of ints.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of ints, or {@code defaultValue}
   */
  default int@NonNull[] getIntArray(final @NonNegative int index, final @NonNull int[] defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.INT_ARRAY) {
      return ((IntArrayTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets an array of longs.
   *
   * @param index the index
   * @return the array of longs, or a zero-length array
   */
  default long@NonNull[] getLongArray(final @NonNegative int index) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.LONG_ARRAY) {
      return ((LongArrayTag) tag).value();
    }
    return new long[0];
  }

  /**
   * Gets an array of longs.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of longs, or {@code defaultValue}
   */
  default long@NonNull[] getLongArray(final @NonNegative int index, final @NonNull long[] defaultValue) {
    final Tag tag = this.get(index);
    if(tag.type() == TagTypes.LONG_ARRAY) {
      return ((LongArrayTag) tag).value();
    }
    return defaultValue;
  }

  interface Builder extends ListTagSetter<Builder> {
    @NonNull ListTag build();
  }
}
