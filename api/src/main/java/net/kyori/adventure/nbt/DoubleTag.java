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

import net.kyori.adventure.util.ShadyPines;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DoubleTag extends NumberTag {
  static @NonNull DoubleTag of(final double value) {
    return new DoubleTagImpl(value);
  }

  @Override
  default @NonNull TagType<DoubleTag> type() {
    return TagTypes.DOUBLE;
  }

  double value();
}

/* package */ final class DoubleTagImpl implements DoubleTag {
  private final double value;

  /* package */ DoubleTagImpl(final double value) {
    this.value = value;
  }

  @Override
  public double value() {
    return this.value;
  }

  @Override
  public byte byteValue() {
    return (byte) (ShadyPines.floor(this.value) & 0xff);
  }

  @Override
  public double doubleValue() {
    return this.value;
  }

  @Override
  public float floatValue() {
    return (float) this.value;
  }

  @Override
  public int intValue() {
    return ShadyPines.floor(this.value);
  }

  @Override
  public long longValue() {
    return (long) Math.floor(this.value);
  }

  @Override
  public short shortValue() {
    return (short) (ShadyPines.floor(this.value) & 0xffff);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final DoubleTagImpl that = (DoubleTagImpl) other;
    return ShadyPines.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Double.hashCode(this.value);
  }
}
