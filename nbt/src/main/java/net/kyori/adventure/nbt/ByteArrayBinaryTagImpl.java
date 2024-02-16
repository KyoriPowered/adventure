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

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text = "\"byte[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
final class ByteArrayBinaryTagImpl extends ArrayBinaryTagImpl implements ByteArrayBinaryTag {
  final byte[] value;

  ByteArrayBinaryTagImpl(final byte[] value) {
    this.value = Arrays.copyOf(value, value.length);
  }

  @Override
  public byte@NotNull[] value() {
    return Arrays.copyOf(this.value, this.value.length);
  }

  @Override
  public int size() {
    return this.value.length;
  }

  @Override
  public byte get(final int index) {
    checkIndex(index, this.value.length);
    return this.value[index];
  }

  // to avoid copying array internally
  static byte[] value(final ByteArrayBinaryTag tag) {
    return (tag instanceof ByteArrayBinaryTagImpl) ? ((ByteArrayBinaryTagImpl) tag).value : tag.value();
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final ByteArrayBinaryTagImpl that = (ByteArrayBinaryTagImpl) other;
    return Arrays.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.value);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.value));
  }

  @Override
  public Iterator<Byte> iterator() {
    return new Iterator<Byte>() {
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
