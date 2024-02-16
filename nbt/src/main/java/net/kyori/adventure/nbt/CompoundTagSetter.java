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

import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  @NotNull R put(final @NotNull String key, final @NotNull BinaryTag tag);

  /**
   * Inserts the tags in {@code tag}, overwriting any that are in {@code this}.
   *
   * @param tag the tag
   * @return a compound tag
   * @since 4.6.0
   */
  @NotNull R put(final @NotNull CompoundBinaryTag tag);

  /**
   * Inserts some tags.
   *
   * @param tags the tags
   * @return a compound tag
   * @since 4.4.0
   */
  @NotNull R put(final @NotNull Map<String, ? extends BinaryTag> tags);

  /**
   * Removes a tag.
   *
   * @param key the key
   * @return a compound tag
   * @since 4.4.0
   */
  default @NotNull R remove(final @NotNull String key) {
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
  @NotNull R remove(final @NotNull String key, final @Nullable Consumer<? super BinaryTag> removed);

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
  default @NotNull R putBoolean(final @NotNull String key, final boolean value) {
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
  default @NotNull R putByte(final @NotNull String key, final byte value) {
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
  default @NotNull R putShort(final @NotNull String key, final short value) {
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
  default @NotNull R putInt(final @NotNull String key, final int value) {
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
  default @NotNull R putLong(final @NotNull String key, final long value) {
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
  default @NotNull R putFloat(final @NotNull String key, final float value) {
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
  default @NotNull R putDouble(final @NotNull String key, final double value) {
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
  default @NotNull R putByteArray(final @NotNull String key, final byte@NotNull[] value) {
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
  default @NotNull R putString(final @NotNull String key, final @NotNull String value) {
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
  default @NotNull R putIntArray(final @NotNull String key, final int@NotNull[] value) {
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
  default @NotNull R putLongArray(final @NotNull String key, final long@NotNull[] value) {
    return this.put(key, LongArrayBinaryTag.longArrayBinaryTag(value));
  }
}
