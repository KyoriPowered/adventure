/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class LongArrayBinaryTagImpl extends ArrayBinaryTagImpl implements LongArrayBinaryTag {
  final long[] value;

  LongArrayBinaryTagImpl(final long[] value) {
    this.value = Arrays.copyOf(value, value.length);
  }

  @Override
  public long@NonNull[] value() {
    return Arrays.copyOf(this.value, this.value.length);
  }
  
  @Override
  public int size() {
    return this.value.length;
  }

  @Override
  public long get(final int index) {
    checkIndex(index, this.value.length);
    return this.value[index];
  }

  @Override
  public PrimitiveIterator.@NonNull OfLong iterator() {
    return new PrimitiveIterator.OfLong() {
      private int index;

      @Override
      public boolean hasNext() {
        return this.index < (LongArrayBinaryTagImpl.this.value.length - 1);
      }

      @Override
      public long nextLong() {
        if(!this.hasNext()) {
          throw new NoSuchElementException();
        }
        return LongArrayBinaryTagImpl.this.value[this.index++];
      }
    };
  }

  @Override
  public Spliterator.@NonNull OfLong spliterator() {
    return Arrays.spliterator(this.value);
  }

  @Override
  public @NonNull LongStream stream() {
    return Arrays.stream(this.value);
  }

  @Override
  public void forEachLong(final @NonNull LongConsumer action) {
    for(int i = 0, length = this.value.length; i < length; i++) {
      action.accept(this.value[i]);
    }
  }

  // to avoid copying array internally
  static long[] value(final LongArrayBinaryTag tag) {
    return (tag instanceof LongArrayBinaryTagImpl) ? ((LongArrayBinaryTagImpl) tag).value : tag.value();
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final LongArrayBinaryTagImpl that = (LongArrayBinaryTagImpl) other;
    return Arrays.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.value);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.value));
  }
}
