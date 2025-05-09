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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryTagIOTest {
  @Test
  void testWriteAndReadNoCompression() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("name", "test")
      .build();
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    BinaryTagIO.writer().write(tag, output);
    assertEquals(tag, BinaryTagIO.reader().read(new ByteArrayInputStream(output.toByteArray())));
  }

  @Test
  void testWriteAndReadGZIPCompression() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("name", "test")
      .build();
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    BinaryTagIO.writer().write(tag, output, BinaryTagIO.Compression.GZIP);
    assertEquals(tag, BinaryTagIO.reader().read(new ByteArrayInputStream(output.toByteArray()), BinaryTagIO.Compression.GZIP));
  }

  @Test
  void testWriteAndReadZLIBCompression() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("name", "test")
      .build();
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    BinaryTagIO.writer().write(tag, output, BinaryTagIO.Compression.ZLIB);
    assertEquals(tag, BinaryTagIO.reader().read(new ByteArrayInputStream(output.toByteArray()), BinaryTagIO.Compression.ZLIB));
  }

  @Test
  void testNamelessWriteAndReadNoCompression() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("name", "test")
      .build();
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    BinaryTagIO.writer().writeNameless(tag, output);
    assertEquals(tag, BinaryTagIO.reader().readNameless(new ByteArrayInputStream(output.toByteArray())));
  }
}
