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

import org.jetbrains.annotations.NotNull;

/**
 * A numeric binary tag.
 *
 * @since 4.0.0
 */
public interface NumberBinaryTag extends BinaryTag {
  @Override
  @NotNull BinaryTagType<? extends NumberBinaryTag> type();

  /**
   * Gets the value as a {@code byte}.
   *
   * @return the value as a {@code byte}
   * @since 4.0.0
   */
  byte byteValue();

  /**
   * Gets the value as a {@code double}.
   *
   * @return the value as a {@code double}
   * @since 4.0.0
   */
  double doubleValue();

  /**
   * Gets the value as a {@code float}.
   *
   * @return the value as a {@code float}
   * @since 4.0.0
   */
  float floatValue();

  /**
   * Gets the value as a {@code int}.
   *
   * @return the value as a {@code int}
   * @since 4.0.0
   */
  int intValue();

  /**
   * Gets the value as a {@code long}.
   *
   * @return the value as a {@code long}
   * @since 4.0.0
   */
  long longValue();

  /**
   * Gets the value as a {@code short}.
   *
   * @return the value as a {@code short}
   * @since 4.0.0
   */
  short shortValue();
}
