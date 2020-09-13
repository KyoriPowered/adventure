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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.checkerframework.checker.nullness.qual.NonNull;

import static net.kyori.adventure.nbt.IOStreamUtil.closeShield;

/**
 * Serialization operations for binary tags.
 *
 * @since 4.0.0
 */
public final class BinaryTagIO {
  private BinaryTagIO() {
  }

  static {
    BinaryTagTypes.COMPOUND.id(); // initialize tag types
  }

  /**
   * Reads a compound tag from {@code path}.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   */
  public static @NonNull CompoundBinaryTag readPath(final @NonNull Path path) throws IOException {
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path))) {
      return readInputStream(is);
    }
  }

  /**
   * Reads a compound tag from an input stream. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   */
  public static @NonNull CompoundBinaryTag readInputStream(final @NonNull InputStream input) throws IOException {
    return readDataInput(new DataInputStream(closeShield(input)));
  }

  /**
   * Reads a compound tag from {@code path} using GZIP decompression.
   *
   * @param path the path
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   */
  public static @NonNull CompoundBinaryTag readCompressedPath(final @NonNull Path path) throws IOException {
    try(final InputStream is = Files.newInputStream(path)) {
      return readCompressedInputStream(is);
    }
  }

  /**
   * Reads a compound tag from an input stream using GZIP decompression. The stream is not closed afterwards.
   *
   * @param input the input stream
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   */
  public static @NonNull CompoundBinaryTag readCompressedInputStream(final @NonNull InputStream input) throws IOException {
    try(final DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(closeShield(input))))) {
      return readDataInput(dis);
    }
  }

  /**
   * Reads a compound tag from {@code input}.
   *
   * @param input the input
   * @return the compound tag
   * @throws IOException if an exception was encountered while reading a compound tag
   * @since 4.0.0
   */
  public static @NonNull CompoundBinaryTag readDataInput(final @NonNull DataInput input) throws IOException {
    final BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
    if(type != BinaryTagTypes.COMPOUND) {
      throw new IOException(String.format("Expected root tag to be a %s, was %s", BinaryTagTypes.COMPOUND, type));
    }
    input.skipBytes(input.readUnsignedShort()); // read empty name
    return BinaryTagTypes.COMPOUND.read(input);
  }

  /**
   * Writes a compound tag to {@code path}.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   */
  public static void writePath(final @NonNull CompoundBinaryTag tag, final @NonNull Path path) throws IOException {
    try(final OutputStream os = new BufferedOutputStream(Files.newOutputStream(path))) {
      writeOutputStream(tag, os);
    }
  }

  /**
   * Writes a compound tag to an output stream. The output stream will not be closed.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   */
  public static void writeOutputStream(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output) throws IOException {
    writeDataOutput(tag, new DataOutputStream(output));
  }

  /**
   * Writes a compound tag to {@code path} using GZIP compression.
   *
   * @param tag the compound tag
   * @param path the path
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   */
  public static void writeCompressedPath(final @NonNull CompoundBinaryTag tag, final @NonNull Path path) throws IOException {
    writeCompressedOutputStream(tag, Files.newOutputStream(path));
  }

  /**
   * Writes a compound tag to an output stream using GZIP compression. The output stream is not closed afterwards.
   *
   * @param tag the compound tag
   * @param output the output stream
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   */
  public static void writeCompressedOutputStream(final @NonNull CompoundBinaryTag tag, final @NonNull OutputStream output) throws IOException {
    try(final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(closeShield(output))))) {
      writeDataOutput(tag, dos);
    }
  }

  /**
   * Writes a compound tag to {@code output}.
   *
   * @param tag the compound tag
   * @param output the output
   * @throws IOException if an exception was encountered while writing the compound tag
   * @since 4.0.0
   */
  public static void writeDataOutput(final @NonNull CompoundBinaryTag tag, final @NonNull DataOutput output) throws IOException {
    output.writeByte(BinaryTagTypes.COMPOUND.id());
    output.writeUTF(""); // write empty name
    BinaryTagTypes.COMPOUND.write(tag, output);
  }
}
