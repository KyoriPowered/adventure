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
 * A binary tag holding a {@code double} value.
 *
 * @since 4.0.0
 */
public interface DoubleBinaryTag extends NumberBinaryTag {
  /**
   * Creates a binary tag holding a {@code double} value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.14.0
   */
  static @NotNull DoubleBinaryTag doubleBinaryTag(final double value) {
    return new DoubleBinaryTagImpl(value);
  }

  /**
   * Creates a binary tag holding a {@code double} value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.0.0
   * @deprecated for removal since 4.14.0, use {@link #doubleBinaryTag(double)} instead.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  static @NotNull DoubleBinaryTag of(final double value) {
    return new DoubleBinaryTagImpl(value);
  }

  @Override
  default @NotNull BinaryTagType<DoubleBinaryTag> type() {
    return BinaryTagTypes.DOUBLE;
  }

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   */
  double value();
}
