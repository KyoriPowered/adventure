/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import org.jetbrains.annotations.Debug;

@SuppressWarnings("ArrayRecordComponent") // We override equals/hashCode/toString.
@Debug.Renderer(text = "\"int[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
record IntArrayBinaryTagImpl(int... value) implements IntArrayBinaryTag {

  IntArrayBinaryTagImpl {
    value = Arrays.copyOf(value, value.length);
  }

  @Override
  public int[] value() {
    return Arrays.copyOf(this.value, this.value.length);
  }

  @Override
  public int size() {
    return this.value.length;
  }

  @Override
  public int get(final int index) {
    ShadyPines.checkIndex(index, this.value.length);
    return this.value[index];
  }

  @Override
  public PrimitiveIterator.OfInt iterator() {
    return new PrimitiveIterator.OfInt() {
      private int index;

      @Override
      public boolean hasNext() {
        return this.index < (IntArrayBinaryTagImpl.this.value.length - 1);
      }

      @Override
      public int nextInt() {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        return IntArrayBinaryTagImpl.this.value[this.index++];
      }
    };
  }

  @Override
  public Spliterator.OfInt spliterator() {
    return Arrays.spliterator(this.value);
  }

  @Override
  public IntStream stream() {
    return Arrays.stream(this.value);
  }

  @Override
  public void forEachInt(final IntConsumer action) {
    for (final int j : this.value) {
      action.accept(j);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof IntArrayBinaryTagImpl(int[] value1))) return false;
    return Objects.deepEquals(this.value, value1);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.value);
  }

  @Override
  public String toString() {
    return "IntArrayBinaryTagImpl{" +
      "value=" + Arrays.toString(this.value) +
      '}';
  }

  // to avoid copying array internally
  static int[] value(final IntArrayBinaryTag tag) {
    return (tag instanceof IntArrayBinaryTagImpl(int[] value1)) ? value1 : tag.value();
  }
}
