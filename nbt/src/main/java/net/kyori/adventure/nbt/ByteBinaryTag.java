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

import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
   * @since 4.0.0
   */
  static @NonNull ByteBinaryTag of(final byte value) {
    if(value == 0) {
      return ZERO;
    } else if(value == 1) {
      return ONE;
    } else {
      return new ByteBinaryTagImpl(value);
    }
  }

  @Override
  default @NonNull BinaryTagType<ByteBinaryTag> type() {
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

final class ByteBinaryTagImpl implements ByteBinaryTag {
  private final byte value;

  ByteBinaryTagImpl(final byte value) {
    this.value = value;
  }

  @Override
  public byte value() {
    return this.value;
  }

  @Override
  public byte byteValue() {
    return this.value;
  }

  @Override
  public double doubleValue() {
    return this.value;
  }

  @Override
  public float floatValue() {
    return this.value;
  }

  @Override
  public int intValue() {
    return this.value;
  }

  @Override
  public long longValue() {
    return this.value;
  }

  @Override
  public short shortValue() {
    return this.value;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final ByteBinaryTagImpl that = (ByteBinaryTagImpl) other;
    return this.value == that.value;
  }

  @Override
  public int hashCode() {
    return Byte.hashCode(this.value);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.value));
  }
}
