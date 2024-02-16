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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A binary tag holding a {@code byte}-array value.
 *
 * @since 4.0.0
 */
public interface ByteArrayBinaryTag extends ArrayBinaryTag, Iterable<Byte> {
  /**
   * Creates a binary tag holding a {@code byte}-array value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.14.0
   */
  static @NotNull ByteArrayBinaryTag byteArrayBinaryTag(final byte@NotNull... value) {
    return new ByteArrayBinaryTagImpl(value);
  }

  /**
   * Creates a binary tag holding a {@code byte}-array value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.0.0
   * @deprecated for removal since 4.14.0, use {@link #byteArrayBinaryTag(byte...)}  instead.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  static @NotNull ByteArrayBinaryTag of(final byte@NotNull... value) {
    return new ByteArrayBinaryTagImpl(value);
  }

  @Override
  default @NotNull BinaryTagType<ByteArrayBinaryTag> type() {
    return BinaryTagTypes.BYTE_ARRAY;
  }

  /**
   * Gets the value.
   *
   * <p>The returned array is a copy.</p>
   *
   * @return the value
   * @since 4.0.0
   */
  byte@NotNull[] value();

  /**
   * Get the size of the array.
   *
   * @return array size
   * @since 4.2.0
   */
  int size();

  /**
   * Gets the value at {@code index} in this tag.
   *
   * @param index the index in the array
   * @return the byte at the index in the array
   * @throws IndexOutOfBoundsException if index is &lt; 0 or &ge; {@link #size()}
   * @since 4.2.0
   */
  byte get(final int index);
}
