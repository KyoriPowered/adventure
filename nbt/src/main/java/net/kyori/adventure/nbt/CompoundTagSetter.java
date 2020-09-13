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

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Common methods between {@link CompoundBinaryTag} and {@link CompoundBinaryTag.Builder}.
 *
 * @param <R> the return type
 * @since 4.0.0
 */
public interface CompoundTagSetter<R> {
  /**
   * Inserts a tag.
   *
   * @param key the key
   * @param tag the tag
   * @return a compound tag
   * @since 4.0.0
   */
  @NonNull R put(final @NonNull String key, final @NonNull BinaryTag tag);

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
  default @NonNull R putBoolean(final @NonNull String key, final boolean value) {
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
  default @NonNull R putByte(final @NonNull String key, final byte value) {
    return this.put(key, ByteBinaryTag.of(value));
  }

  /**
   * Inserts a short.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putShort(final @NonNull String key, final short value) {
    return this.put(key, ShortBinaryTag.of(value));
  }

  /**
   * Inserts an int.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putInt(final @NonNull String key, final int value) {
    return this.put(key, IntBinaryTag.of(value));
  }

  /**
   * Inserts a long.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putLong(final @NonNull String key, final long value) {
    return this.put(key, LongBinaryTag.of(value));
  }

  /**
   * Inserts a float.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putFloat(final @NonNull String key, final float value) {
    return this.put(key, FloatBinaryTag.of(value));
  }

  /**
   * Inserts a double.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putDouble(final @NonNull String key, final double value) {
    return this.put(key, DoubleBinaryTag.of(value));
  }

  /**
   * Inserts an array of bytes.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putByteArray(final @NonNull String key, final byte@NonNull[] value) {
    return this.put(key, ByteArrayBinaryTag.of(value));
  }

  /**
   * Inserts a string.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putString(final @NonNull String key, final @NonNull String value) {
    return this.put(key, StringBinaryTag.of(value));
  }

  /**
   * Inserts an array of ints.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putIntArray(final @NonNull String key, final int@NonNull[] value) {
    return this.put(key, IntArrayBinaryTag.of(value));
  }

  /**
   * Inserts an array of longs.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   * @since 4.0.0
   */
  default @NonNull R putLongArray(final @NonNull String key, final long@NonNull[] value) {
    return this.put(key, LongArrayBinaryTag.of(value));
  }
}
