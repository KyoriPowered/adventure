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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadWriteTest {
  @Test
  void testByteArray() throws IOException {
    this.testWriteRead(ByteArrayBinaryTag.of(Byte.MIN_VALUE, (byte) -100, (byte) 0, (byte) 100, Byte.MAX_VALUE), BinaryTagTypes.BYTE_ARRAY);
  }

  @Test
  void testByte() throws IOException {
    this.testWriteRead(ByteBinaryTag.of((byte) 2), BinaryTagTypes.BYTE);
  }

  @Test
  void testCompound() throws IOException {
    final CompoundBinaryTag a = CompoundBinaryTag.builder()
      .putByte("AByte", (byte) 0)
      .putInt("AnInt", 1)
      .putIntArray("AnIntArray", new int[]{0, 1, 4, 5, 8, 9})
      .build();
    this.testWriteRead(a, BinaryTagTypes.COMPOUND);
  }

  @Test
  void testDouble() throws IOException {
    this.testWriteRead(DoubleBinaryTag.of(4d), BinaryTagTypes.DOUBLE);
  }

  @Test
  void testEnd() throws IOException {
    this.testWriteRead(EndBinaryTag.get(), BinaryTagTypes.END);
  }

  @Test
  void testFloat() throws IOException {
    this.testWriteRead(FloatBinaryTag.of(6f), BinaryTagTypes.FLOAT);
  }

  @Test
  void testIntArray() throws IOException {
    this.testWriteRead(IntArrayBinaryTag.of(Integer.MIN_VALUE, -100, 0, 100, Integer.MAX_VALUE), BinaryTagTypes.INT_ARRAY);
  }

  @Test
  void testInt() throws IOException {
    this.testWriteRead(IntBinaryTag.of(8), BinaryTagTypes.INT);
  }

  @Test
  void testList() throws IOException {
    final ListBinaryTag a = ListBinaryTag.builder()
      .add(DoubleBinaryTag.of(32d))
      .add(DoubleBinaryTag.of(64d))
      .build();
    final ListBinaryTag b = this.testWriteRead(a, BinaryTagTypes.LIST);
    assertEquals(a.listType(), b.listType());
  }

  @Test
  void testLongArray() throws IOException {
    this.testWriteRead(LongArrayBinaryTag.of(Long.MIN_VALUE, -100, 0, 100, Long.MAX_VALUE), BinaryTagTypes.LONG_ARRAY);
  }

  @Test
  void testLong() throws IOException {
    this.testWriteRead(LongBinaryTag.of(10), BinaryTagTypes.LONG);
  }

  @Test
  void testShort() throws IOException {
    this.testWriteRead(ShortBinaryTag.of((short) 12), BinaryTagTypes.SHORT);
  }

  @Test
  void testString() throws IOException {
    this.testWriteRead(StringBinaryTag.of("Hello, world!"), BinaryTagTypes.STRING);
  }

  private <T extends BinaryTag> T testWriteRead(final T a, final BinaryTagType<T> type) throws IOException {
    final T b = this.writeRead(a, type);
    assertEquals(a, b);
    return b;
  }

  @SuppressWarnings("UnstableApiUsage")
  private <T extends BinaryTag> T writeRead(final T a, final BinaryTagType<T> type) throws IOException {
    final ByteArrayDataOutput output = ByteStreams.newDataOutput();
    type.write(a, output);
    final ByteArrayDataInput input = ByteStreams.newDataInput(output.toByteArray());
    return type.read(input);
  }
}
