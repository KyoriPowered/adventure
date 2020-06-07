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
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IntTag extends NumberTag {
  static @NonNull IntTag of(final int value) {
    return new IntTagImpl(value);
  }

  @Override
  default @NonNull TagType<IntTag> type() {
    return TagTypes.INT;
  }

  int value();
}

/* package */ final class IntTagImpl implements IntTag {
  private final int value;

  /* package */ IntTagImpl(final int value) {
    this.value = value;
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public byte byteValue() {
    return (byte) (this.value & 0xff);
  }

  @Override
  public double doubleValue() {
    return (double) this.value;
  }

  @Override
  public float floatValue() {
    return (float) this.value;
  }

  @Override
  public int intValue() {
    return this.value;
  }

  @Override
  public long longValue() {
    return (long) this.value;
  }

  @Override
  public short shortValue() {
    return (short) (this.value & 0xffff);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final IntTagImpl that = (IntTagImpl) other;
    return this.value == that.value;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(this.value);
  }
}
