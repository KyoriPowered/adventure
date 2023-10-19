/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.nbt.IOStreamUtil.closeShield;

@SuppressWarnings("DuplicatedCode")
@NullMarked
final class BinaryTagReaderImpl implements BinaryTagIO.Reader {
  private final long maxBytes;
  static final BinaryTagIO.Reader UNLIMITED = new BinaryTagReaderImpl(-1L);
  static final BinaryTagIO.Reader DEFAULT_LIMIT = new BinaryTagReaderImpl(0x20_00a);

  BinaryTagReaderImpl(final long maxBytes) {
    this.maxBytes = maxBytes;
  }

  @Override
  public CompoundBinaryTag read(final Path path, final BinaryTagIO.Compression compression) throws IOException {
    try (final InputStream is = Files.newInputStream(path)) {
      return this.read(is, compression);
    }
  }

  @Override
  public CompoundBinaryTag read(final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
    try (final DataInputStream dis = new DataInputStream(new BufferedInputStream(compression.decompress(closeShield(input))))) {
      return this.read((DataInput) dis);
    }
  }

  @Override
  public CompoundBinaryTag read(DataInput input) throws IOException {
    if (!(input instanceof TrackingDataInput)) {
      input = new TrackingDataInput(input, this.maxBytes);
    }

    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
    requireCompound(type);
    input.skipBytes(input.readUnsignedShort()); // read empty name
    return BinaryTagTypes.COMPOUND.read(input);
  }

  @Override
  public Map.Entry<String, CompoundBinaryTag> readNamed(final Path path, final BinaryTagIO.Compression compression) throws IOException {
    try (final InputStream is = Files.newInputStream(path)) {
      return this.readNamed(is, compression);
    }
  }

  @Override
  public Map.Entry<String, CompoundBinaryTag> readNamed(final InputStream input, final BinaryTagIO.Compression compression) throws IOException {
    try (final DataInputStream dis = new DataInputStream(new BufferedInputStream(compression.decompress(closeShield(input))))) {
      return this.readNamed((DataInput) dis);
    }
  }

  @Override
  public Map.Entry<String, CompoundBinaryTag> readNamed(final DataInput input) throws IOException {
    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.binaryTagType(input.readByte());
    requireCompound(type);
    final String name = input.readUTF();
    return new AbstractMap.SimpleImmutableEntry<>(name, BinaryTagTypes.COMPOUND.read(input));
  }

  private static void requireCompound(final BinaryTagType<? extends BinaryTag> type) throws IOException {
    if (type != BinaryTagTypes.COMPOUND) {
      throw new IOException(String.format("Expected root tag to be a %s, was %s", BinaryTagTypes.COMPOUND, type));
    }
  }
}
