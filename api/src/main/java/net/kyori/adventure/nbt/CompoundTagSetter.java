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

public interface CompoundTagSetter<R> {
  @NonNull R put(final @NonNull String key, @NonNull Tag tag);

  /**
   * Inserts a byte.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putByte(final @NonNull String key, final byte value);

  /**
   * Inserts a short.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putShort(final @NonNull String key, final short value);

  /**
   * Inserts an int.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putInt(final @NonNull String key, final int value);

  /**
   * Inserts a long.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putLong(final @NonNull String key, final long value);

  /**
   * Inserts a float.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putFloat(final @NonNull String key, final float value);

  /**
   * Inserts a double.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putDouble(final @NonNull String key, final double value);

  /**
   * Inserts an array of bytes.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putByteArray(final @NonNull String key, final byte@NonNull[] value);

  /**
   * Inserts a string.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putString(final @NonNull String key, final @NonNull String value);

  /**
   * Inserts an array of ints.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putIntArray(final @NonNull String key, final int@NonNull[] value);

  /**
   * Inserts an array of longs.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @NonNull R putLongArray(final @NonNull String key, final long@NonNull[] value);
}
