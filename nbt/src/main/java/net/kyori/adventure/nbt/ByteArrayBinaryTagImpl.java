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
import java.util.Iterator;
import java.util.Objects;
import org.jetbrains.annotations.Debug;

@SuppressWarnings("ArrayRecordComponent") // We override equals/hashCode/toString.
@Debug.Renderer(text = "\"byte[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
record ByteArrayBinaryTagImpl(byte[] value) implements ByteArrayBinaryTag {
  ByteArrayBinaryTagImpl(final byte[] value) {
    this.value = Arrays.copyOf(value, value.length);
  }

  @Override
  public byte[] value() {
    return Arrays.copyOf(this.value, this.value.length);
  }

  @Override
  public int size() {
    return this.value.length;
  }

  @Override
  public byte get(final int index) {
    ShadyPines.checkIndex(index, this.value.length);
    return this.value[index];
  }

  // to avoid copying array internally
  static byte[] value(final ByteArrayBinaryTag tag) {
    return (tag instanceof ByteArrayBinaryTagImpl(byte[] value1)) ? value1 : tag.value();
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof ByteArrayBinaryTagImpl(byte[] value1))) return false;
    return Objects.deepEquals(this.value, value1);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.value);
  }

  @Override
  public String toString() {
    return "ByteArrayBinaryTagImpl{" +
      "value=" + Arrays.toString(this.value) +
      '}';
  }

  @Override
  public Iterator<Byte> iterator() {
    return new Iterator<>() {
      private int index;

      @Override
      public boolean hasNext() {
        return this.index < ByteArrayBinaryTagImpl.this.value.length - 1;
      }

      @Override
      public Byte next() {
        return ByteArrayBinaryTagImpl.this.value[this.index++];
      }
    };
  }
}
