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

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

import static net.kyori.adventure.nbt.IOStreamUtil.closeShield;

final class BinaryTagWriterImpl implements BinaryTagIO.Writer {
  static final BinaryTagIO.Writer INSTANCE = new BinaryTagWriterImpl();

  @Override
  public void write(final @NonNull CompoundBinaryTag tag, final @NonNull Path path, final BinaryTagIO.@NonNull Compression compression) throws IOException {
    try(final OutputStream os = Files.newOutputStream(path)) {
      this.write(tag, os, compression);
    }
  }

  @Override
  public void write(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output, final BinaryTagIO.@NonNull Compression compression) throws IOException {
    try(final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(closeShield(output))))) {
      this.write(tag, (DataOutput) dos);
    }
  }

  @Override
  public void write(final @NonNull CompoundBinaryTag tag, final @NonNull DataOutput output) throws IOException {
    output.writeByte(BinaryTagTypes.COMPOUND.id());
    output.writeUTF(""); // write empty name
    BinaryTagTypes.COMPOUND.write(tag, output);
  }

  @Override
  public void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull Path path, final BinaryTagIO.@NonNull Compression compression) throws IOException {
    try(final OutputStream os = Files.newOutputStream(path)) {
      this.writeNamed(tag, os, compression);
    }
  }

  @Override
  public void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull OutputStream output, final BinaryTagIO.@NonNull Compression compression) throws IOException {
    try(final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(compression.compress(closeShield(output))))) {
      this.writeNamed(tag, (DataOutput) dos);
    }
  }

  @Override
  public void writeNamed(final Map.@NonNull Entry<String, CompoundBinaryTag> tag, final @NonNull DataOutput output) throws IOException {
    output.writeByte(BinaryTagTypes.COMPOUND.id());
    output.writeUTF(tag.getKey());
    BinaryTagTypes.COMPOUND.write(tag.getValue(), output);
  }
}
