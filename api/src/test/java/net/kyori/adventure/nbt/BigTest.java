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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ensure that we can read the bigtest.nbt file.
 */
class BigTest {
  private static final double DOUBLE_DELTA = 1e-15;
  private static final byte[] BYTE_ARRAY_TEST = new byte[1000];
  private static final ListBinaryTag LONG_LIST = ListBinaryTag.builder(BinaryTagTypes.LONG)
    .add(LongBinaryTag.of(11))
    .add(LongBinaryTag.of(12))
    .add(LongBinaryTag.of(13))
    .add(LongBinaryTag.of(14))
    .add(LongBinaryTag.of(15))
    .build();
  private static final ListBinaryTag COMPOUND_LIST = ListBinaryTag.builder(BinaryTagTypes.COMPOUND)
    .add(
      CompoundBinaryTag.builder()
        .putLong("created-on", 1264099775885L)
        .putString("name", "Compound tag #0")
        .build()
    )
    .add(
      CompoundBinaryTag.builder()
        .putLong("created-on", 1264099775885L)
        .putString("name", "Compound tag #1")
        .build()
    )
    .build();
  private static final CompoundBinaryTag NESTED_COMPOUND = CompoundBinaryTag.builder()
    .put("egg", CompoundBinaryTag.empty()
      .putString("name", "Eggbert")
      .putFloat("value", 0.5f))
    .put("ham", CompoundBinaryTag.empty()
      .putString("name", "Hampus")
      .putFloat("value", 0.75f))
    .build();
  private static CompoundBinaryTag compound;

  static {
    for(int i = 0; i < 1000; i++) {
      BYTE_ARRAY_TEST[i] = (byte) ((i * i * 255 + i * 7) % 100);
    }
  }

  @BeforeAll
  static void before() throws IOException, URISyntaxException {
    final URL url = BigTest.class.getResource("/bigtest.nbt");
    compound = BinaryTagIO.readCompressedPath(Paths.get(url.toURI()));
  }

  @Test
  void testCorrectValues() {
    assertEquals(Short.MAX_VALUE, compound.getShort("shortTest"));
    assertEquals(Long.MAX_VALUE, compound.getLong("longTest"));
    assertEquals(Byte.MAX_VALUE, compound.getByte("byteTest"));
    assertArrayEquals(BYTE_ARRAY_TEST, compound.getByteArray("byteArrayTest (the first 1000 values of (n*n*255+n*7)%100, starting with n=0 (0, 62, 34, 16, 8, ...))"));
    assertEquals(LONG_LIST, compound.getList("listTest (long)"));
    assertEquals(0.49823147f, compound.getFloat("floatTest"), DOUBLE_DELTA);
    assertEquals(0.4931287132182315d, compound.getDouble("doubleTest"), DOUBLE_DELTA);
    assertEquals(Integer.MAX_VALUE, compound.getInt("intTest"));
    assertEquals(COMPOUND_LIST, compound.getList("listTest (compound)"));
    assertEquals(NESTED_COMPOUND, compound.getCompound("nested compound test"));
    assertEquals("HELLO WORLD THIS IS A TEST STRING ÅÄÖ!", compound.getString("stringTest"));
  }
}
