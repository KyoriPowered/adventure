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
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BinaryTagType<T extends BinaryTag> implements Predicate<BinaryTagType<? extends BinaryTag>> {
  private static final List<BinaryTagType<? extends BinaryTag>> TYPES = new ArrayList<>();

  static <T extends BinaryTag> @NonNull BinaryTagType<T> register(final @NonNull Class<T> type, final byte id, final @NonNull Reader<T> reader, final @Nullable Writer<T> writer) {
    return register(new BinaryTagType<T>() {
      @Override
      public @NonNull T read(final @NonNull DataInput input) throws IOException {
        return reader.read(input);
      }

      @Override
      public void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException {
        if(writer != null) writer.write(tag, output);
      }

      @Override
      public byte id() {
        return id;
      }

      @Override
      boolean numeric() {
        return false;
      }

      @Override
      public String toString() {
        return this.getClass().getSimpleName() + '{' + type.getSimpleName() + " (" + id + ")}";
      }
    });
  }

  static <T extends NumberBinaryTag> @NonNull BinaryTagType<T> registerNumeric(final @NonNull Class<T> type, final byte id, final @NonNull Reader<T> reader, final @NonNull Writer<T> writer) {
    return register(new BinaryTagType<T>() {
      @Override
      public @NonNull T read(final @NonNull DataInput input) throws IOException {
        return reader.read(input);
      }

      @Override
      public void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException {
        writer.write(tag, output);
      }

      @Override
      public byte id() {
        return id;
      }

      @Override
      boolean numeric() {
        return true;
      }

      @Override
      public String toString() {
        return this.getClass().getSimpleName() + '{' + type.getSimpleName() + " (" + id + ")}";
      }
    });
  }

  private static <T extends BinaryTag, Y extends BinaryTagType<T>> Y register(final Y type) {
    TYPES.add(type);
    return type;
  }

  static @NonNull BinaryTagType<? extends BinaryTag> of(final byte id) {
    for(int i = 0; i < TYPES.size(); i++) {
      final BinaryTagType<? extends BinaryTag> type = TYPES.get(i);
      if(type.id() == id) {
        return type;
      }
    }
    throw new IllegalArgumentException(String.valueOf(id));
  }

  public abstract @NonNull T read(final @NonNull DataInput input) throws IOException;
  public abstract void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException;

  interface Reader<T extends BinaryTag> {
    @NonNull T read(final @NonNull DataInput input) throws IOException;
  }

  interface Writer<T extends BinaryTag> {
    void write(final @NonNull T tag, final @NonNull DataOutput output) throws IOException;
  }

  interface ReaderAndWriter<T extends BinaryTag> extends Reader<T>, Writer<T> {
  }

  public abstract byte id();

  abstract boolean numeric();

  @Override
  public boolean test(final BinaryTagType<? extends BinaryTag> that) {
    return this == that || (this.numeric() && that.numeric());
  }
}
