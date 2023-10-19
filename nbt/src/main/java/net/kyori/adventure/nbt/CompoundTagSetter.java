/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.Map;
import java.util.function.Consumer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Common methods between {@link CompoundBinaryTag} and {@link CompoundBinaryTag.Builder}.
 *
 * @param <R> the return type
 * @since 4.0.0
 */
@NullMarked
public interface CompoundTagSetter<R> {
  /**
   * Inserts a tag.
   *
   * @param key the key
   * @param tag the tag
   * @return a compound tag
   * @since 4.0.0
   */
  R put(final String key, final BinaryTag tag);

  /**
   * Inserts the tags in {@code tag}, overwriting any that are in {@code this}.
   *
   * @param tag the tag
   * @return a compound tag
   * @since 4.6.0
   */
  R put(final CompoundBinaryTag tag);

  /**
   * Inserts some tags.
   *
   * @param tags the tags
   * @return a compound tag
   * @since 4.4.0
   */
  R put(final Map<String, ? extends BinaryTag> tags);

  /**
   * Removes a tag.
   *
   * @param key the key
   * @return a compound tag
   * @since 4.4.0
   */
  default R remove(final String key) {
    return this.remove(key, null);
  }

  /**
   * Removes a tag.
   *
   * @param key the key
   * @param removed a consumer that accepts the removed tag
   * @return a compound tag
   * @since 4.4.0
   */
  R remove(final String key, final @Nullable Consumer<? super BinaryTag> removed);

  /**
   * Inserts a boolean.
   *
   * <p>Booleans are stored as a {@link ByteBinaryTag} with a value of {@code 0} for {@code false} and {@code 1} for {@code true}.</p>
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putBoolean(final String key, final boolean value) {
    return this.put(key, value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO);
  }

  /**
   * Inserts a byte.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putByte(final String key, final byte value) {
    return this.put(key, ByteBinaryTag.byteBinaryTag(value));
  }

  /**
   * Inserts a short.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putShort(final String key, final short value) {
    return this.put(key, ShortBinaryTag.shortBinaryTag(value));
  }

  /**
   * Inserts an int.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putInt(final String key, final int value) {
    return this.put(key, IntBinaryTag.intBinaryTag(value));
  }

  /**
   * Inserts a long.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putLong(final String key, final long value) {
    return this.put(key, LongBinaryTag.longBinaryTag(value));
  }

  /**
   * Inserts a float.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putFloat(final String key, final float value) {
    return this.put(key, FloatBinaryTag.floatBinaryTag(value));
  }

  /**
   * Inserts a double.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putDouble(final String key, final double value) {
    return this.put(key, DoubleBinaryTag.doubleBinaryTag(value));
  }

  /**
   * Inserts an array of bytes.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putByteArray(final String key, final byte[] value) {
    return this.put(key, ByteArrayBinaryTag.byteArrayBinaryTag(value));
  }

  /**
   * Inserts a string.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putString(final String key, final String value) {
    return this.put(key, StringBinaryTag.stringBinaryTag(value));
  }

  /**
   * Inserts an array of ints.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putIntArray(final String key, final int[] value) {
    return this.put(key, IntArrayBinaryTag.intArrayBinaryTag(value));
  }

  /**
   * Inserts an array of longs.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default R putLongArray(final String key, final long[] value) {
    return this.put(key, LongArrayBinaryTag.longArrayBinaryTag(value));
  }
}
