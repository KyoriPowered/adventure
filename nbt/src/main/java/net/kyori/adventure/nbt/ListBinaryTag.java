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
import java.util.function.Consumer;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A list of zero or more values of a single tag type.
 *
 * @since 4.0.0
 */
public interface ListBinaryTag extends ListTagSetter<ListBinaryTag, BinaryTag>, BinaryTag, Iterable<BinaryTag> {
  /**
   * Gets an empty list tag.
   *
   * @return an empty tag
   * @since 4.0.0
   */
  static @NonNull ListBinaryTag empty() {
    return ListBinaryTagImpl.EMPTY;
  }

  /**
   * Creates a builder.
   *
   * @return a new builder
   * @since 4.0.0
   */
  static @NonNull Builder<BinaryTag> builder() {
    return new ListTagBuilder<>();
  }

  /**
   * Creates a builder.
   *
   * @param type the element type
   * @param <T> the element type
   * @return a new builder
   * @since 4.0.0
   */
  static <T extends BinaryTag> @NonNull Builder<T> builder(final @NonNull BinaryTagType<T> type) {
    if(type == BinaryTagTypes.END) throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
    return new ListTagBuilder<>(type);
  }

  /**
   * Creates a tag.
   *
   * @param type the element type
   * @param tags the elements
   * @return a tag
   * @since 4.0.0
   */
  static @NonNull ListBinaryTag of(final @NonNull BinaryTagType<? extends BinaryTag> type, final @NonNull List<BinaryTag> tags) {
    if(type == BinaryTagTypes.END) throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
    return new ListBinaryTagImpl(type, tags);
  }

  @Override
  default @NonNull BinaryTagType<ListBinaryTag> type() {
    return BinaryTagTypes.LIST;
  }

  /**
   * Gets the type of element stored in this list.
   *
   * @return the type
   * @since 4.0.0
   */
  @NonNull BinaryTagType<? extends BinaryTag> listType();

  /**
   * Gets the size.
   *
   * @return the size
   * @since 4.0.0
   */
  int size();

  /**
   * Gets a tag.
   *
   * @param index the index
   * @return the tag
   * @throws IndexOutOfBoundsException if the index is out of range
   * @since 4.0.0
   */
  @NonNull BinaryTag get(final @NonNegative int index);

  /**
   * Sets the tag at index {@code index} to {@code tag}, optionally providing {@code removedConsumer} with the tag previously at index {@code index}.
   *
   * @param index the index
   * @param tag the tag
   * @param removedConsumer a consumer which receives the tag being removed at index {@code index}
   * @return a list tag
   * @since 4.0.0
   */
  @NonNull ListBinaryTag set(final int index, final @NonNull BinaryTag tag, final @Nullable Consumer<BinaryTag> removedConsumer);

  /**
   * Removes the tag at index {@code index}, optionally providing {@code removedConsumer} with the tag previously at index {@code index}.
   *
   * @param index the index
   * @param removedConsumer a consumer which receives the tag being removed at index {@code index}
   * @return a list tag
   * @since 4.0.0
   */
  @NonNull ListBinaryTag remove(final int index, final @Nullable Consumer<BinaryTag> removedConsumer);

  /**
   * Gets a byte.
   *
   * @param index the index
   * @return the byte value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default byte getByte(final @NonNegative int index, final byte defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).byteValue();
    }
    return defaultValue;
  }

  /**
   * Gets a short.
   *
   * @param index the index
   * @return the short value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default short getShort(final @NonNegative int index, final short defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).shortValue();
    }
    return defaultValue;
  }

  /**
   * Gets an int.
   *
   * @param index the index
   * @return the int value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default int getInt(final @NonNegative int index, final int defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).intValue();
    }
    return defaultValue;
  }

  /**
   * Gets a long.
   *
   * @param index the index
   * @return the long value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default long getLong(final @NonNegative int index, final long defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).longValue();
    }
    return defaultValue;
  }

  /**
   * Gets a float.
   *
   * @param index the index
   * @return the float value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default float getFloat(final @NonNegative int index, final float defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).floatValue();
    }
    return defaultValue;
  }

  /**
   * Gets a double.
   *
   * @param index the index
   * @return the double value, or {@code 0}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default double getDouble(final @NonNegative int index, final double defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type().numeric()) {
      return ((NumberBinaryTag) tag).doubleValue();
    }
    return defaultValue;
  }

  /**
   * Gets an array of bytes.
   *
   * @param index the index
   * @return the array of bytes, or a zero-length array
   * @since 4.0.0
   */
  default byte@NonNull[] getByteArray(final @NonNegative int index) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.BYTE_ARRAY) {
      return ((ByteArrayBinaryTag) tag).value();
    }
    return new byte[0];
  }

  /**
   * Gets an array of bytes.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of bytes, or {@code defaultValue}
   * @since 4.0.0
   */
  default byte@NonNull[] getByteArray(final @NonNegative int index, final byte@NonNull[] defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.BYTE_ARRAY) {
      return ((ByteArrayBinaryTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets a string.
   *
   * @param index the index
   * @return the string value, or {@code ""}
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @NonNull String getString(final @NonNegative int index, final @NonNull String defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.STRING) {
      return ((StringBinaryTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets a compound.
   *
   * @param index the index
   * @return the compound, or a new compound
   * @since 4.0.0
   */
  default @NonNull CompoundBinaryTag getCompound(final @NonNegative int index) {
    return this.getCompound(index, CompoundBinaryTag.empty());
  }

  /**
   * Gets a compound.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the compound, or {@code defaultValue}
   * @since 4.0.0
   */
  default @NonNull CompoundBinaryTag getCompound(final @NonNegative int index, final @NonNull CompoundBinaryTag defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.COMPOUND) {
      return (CompoundBinaryTag) tag;
    }
    return defaultValue;
  }

  /**
   * Gets an array of ints.
   *
   * @param index the index
   * @return the array of ints, or a zero-length array
   * @since 4.0.0
   */
  default int@NonNull[] getIntArray(final @NonNegative int index) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.INT_ARRAY) {
      return ((IntArrayBinaryTag) tag).value();
    }
    return new int[0];
  }

  /**
   * Gets an array of ints.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of ints, or {@code defaultValue}
   * @since 4.0.0
   */
  default int@NonNull[] getIntArray(final @NonNegative int index, final int@NonNull[] defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.INT_ARRAY) {
      return ((IntArrayBinaryTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * Gets an array of longs.
   *
   * @param index the index
   * @return the array of longs, or a zero-length array
   * @since 4.0.0
   */
  default long@NonNull[] getLongArray(final @NonNegative int index) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.LONG_ARRAY) {
      return ((LongArrayBinaryTag) tag).value();
    }
    return new long[0];
  }

  /**
   * Gets an array of longs.
   *
   * @param index the index
   * @param defaultValue the default value
   * @return the array of longs, or {@code defaultValue}
   * @since 4.0.0
   */
  default long@NonNull[] getLongArray(final @NonNegative int index, final long@NonNull[] defaultValue) {
    final BinaryTag tag = this.get(index);
    if(tag.type() == BinaryTagTypes.LONG_ARRAY) {
      return ((LongArrayBinaryTag) tag).value();
    }
    return defaultValue;
  }

  /**
   * A list tag builder.
   *
   * @param <T> the element type
   * @since 4.0.0
   */
  interface Builder<T extends BinaryTag> extends ListTagSetter<Builder<T>, T> {
    /**
     * Builds.
     *
     * @return a list tag
     * @since 4.0.0
     */
    @NonNull ListBinaryTag build();
  }
}
