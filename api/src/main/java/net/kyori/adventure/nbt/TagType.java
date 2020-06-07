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

import java.io.DataInput;
import java.io.IOException;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;

// TODO: write!
public abstract class TagType<T extends Tag> implements Predicate<TagType<? extends Tag>> {
  static <T extends Tag> @NonNull TagType<T> create(final byte id, final @NonNull Reader<T> reader) {
    return new TagType<T>() {
      @Override
      public @NonNull T read(final @NonNull DataInput input) throws IOException {
        return reader.read(input);
      }

      @Override
      public byte id() {
        return id;
      }

      @Override
      boolean numeric() {
        return false;
      }
    };
  }

  static <T extends Tag> @NonNull TagType<T> createNumeric(final byte id, final @NonNull Reader<T> reader) {
    return new TagType<T>() {
      @Override
      public @NonNull T read(final @NonNull DataInput input) throws IOException {
        return reader.read(input);
      }

      @Override
      public byte id() {
        return id;
      }

      @Override
      boolean numeric() {
        return true;
      }
    };
  }

  public abstract @NonNull T read(final @NonNull DataInput input) throws IOException;

  interface Reader<T extends Tag> {
    @NonNull T read(final @NonNull DataInput input) throws IOException;
  }

  public abstract byte id();

  abstract boolean numeric();

  @Override
  public boolean test(final TagType<? extends Tag> that) {
    return this == that || (this.numeric() && that.numeric());
  }
}
