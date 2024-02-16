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
 * A binary tag holding a {@code byte} value.
 *
 * @since 4.0.0
 */
public interface ByteBinaryTag extends NumberBinaryTag {
  /**
   * A tag with the value {@code 0}.
   *
   * @since 4.0.0
   */
  ByteBinaryTag ZERO = new ByteBinaryTagImpl((byte) 0);

  /**
   * A tag with the value {@code 1}.
   *
   * @since 4.0.0
   */
  ByteBinaryTag ONE = new ByteBinaryTagImpl((byte) 1);

  /**
   * Creates a binary tag holding a {@code byte} value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.14.0
   */
  static @NotNull ByteBinaryTag byteBinaryTag(final byte value) {
    if (value == 0) {
      return ZERO;
    } else if (value == 1) {
      return ONE;
    } else {
      return new ByteBinaryTagImpl(value);
    }
  }

  /**
   * Creates a binary tag holding a {@code byte} value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.0.0
   * @deprecated for removal since 4.14.0, use {@link #byteBinaryTag(byte)} instead.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  static @NotNull ByteBinaryTag of(final byte value) {
    return byteBinaryTag(value);
  }

  @Override
  default @NotNull BinaryTagType<ByteBinaryTag> type() {
    return BinaryTagTypes.BYTE;
  }

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   */
  byte value();
}
