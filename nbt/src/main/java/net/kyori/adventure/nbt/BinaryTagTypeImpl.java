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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

public record BinaryTagTypeImpl<T extends BinaryTag>(
  Class<T> type,
  byte id,
  Reader<T> reader,
  @Nullable Writer<T> writer,
  boolean numeric
) implements BinaryTagType<T> {
  static final List<BinaryTagType<? extends BinaryTag>> TYPES = new ArrayList<>();

  @SuppressWarnings("unchecked") // HACK: generics suck
  static <T extends BinaryTag> void writeUntyped(final BinaryTagType<? extends BinaryTag> type, final T tag, final DataOutput output) throws IOException {
    ((BinaryTagType<T>) type).write(tag, output);
  }

  static <T extends BinaryTag> BinaryTagType<T> register(final Class<T> type, final byte id, final Reader<T> reader, final @Nullable Writer<T> writer) {
    return register(new BinaryTagTypeImpl<>(type, id, reader, writer, false));
  }

  static <T extends NumberBinaryTag> BinaryTagType<T> registerNumeric(final Class<T> type, final byte id, final Reader<T> reader, final Writer<T> writer) {
    return register(new BinaryTagTypeImpl<>(type, id, reader, writer, true));
  }

  private static <T extends BinaryTag, Y extends BinaryTagType<T>> Y register(final Y type) {
    TYPES.add(type);
    return type;
  }

  @Override
  public T read(final DataInput input) throws IOException {
    return this.reader.read(input);
  }

  @Override
  public void write(final T tag, final DataOutput output) throws IOException {
    if (this.writer != null) this.writer.write(tag, output);
  }

  @Override
  public boolean test(final BinaryTagType<? extends BinaryTag> that) {
    return this == that || (this.numeric() && that.numeric());
  }

  @Override
  public String toString() {
    return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + (this.numeric() ? " (numeric)" : "") + "]";
  }
}
