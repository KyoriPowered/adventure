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
package net.kyori.adventure.nbt.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringIOTest {
  @Test
  public void testReadKeyValuePair() throws StringTagParseException {
    final TagStringReader keyRead = new TagStringReader(new CharBuffer("testKey: \"hello\""));
    assertEquals("testKey", keyRead.key());
    assertEquals(StringBinaryTag.of("hello"), keyRead.tag());
  }

  @Test
  public void testComplexStringCompound() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("standard", "value")
      .putString("complex", "weird, isn't it: huh")
      .putString("quoted", "quo\"ted")
      .putString("comp:lex \"key", "let's go")
      .put("listed", ListBinaryTag.builder(BinaryTagTypes.STRING)
        .add(StringBinaryTag.of("one"))
        .add(StringBinaryTag.of("two"))
        .add(StringBinaryTag.of("three"))
        .build())
      .build();

    final String serialized = BinaryTagIO.writeString(tag);
    final CompoundBinaryTag deserialized = BinaryTagIO.readString(serialized);
    assertEquals(tag, deserialized);
  }

  @Test
  public void testBigTestRoundtrip() throws IOException {
    // Read and write
    final CompoundBinaryTag bigTest;
    try(final InputStream is = this.getClass().getResourceAsStream("/bigtest.nbt")) {
      bigTest = BinaryTagIO.readCompressedInputStream(is);
      final String written = BinaryTagIO.writeString(bigTest);
      assertEquals(bigTest, BinaryTagIO.readString(written));
    }

    // Read snbt equivalent
    final String result;
    try(final InputStream is = this.getClass().getResourceAsStream("/bigtest.snbt"); final BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
      final StringBuilder build = new StringBuilder();
      final char[] buffer = new char[2048];
      int read;
      while((read = reader.read(buffer)) != -1) {
        build.append(buffer, 0, read);
      }
      result = build.toString();
    }

    final CompoundBinaryTag parsedSnbt = (CompoundBinaryTag) this.stringToTag(result);
    for(final String key : parsedSnbt.keySet()) {
      if(!parsedSnbt.get(key).equals(bigTest.get(key))) {
        System.out.println(key + " not equal");
      }
    }

    assertEquals(bigTest, parsedSnbt);
  }


  @Test
  public void testStringTag() throws IOException {
    final StringBinaryTag basic = StringBinaryTag.of("hello");
    final String basicStr = this.tagToString(basic);
    assertEquals("\"hello\"", basicStr);
    assertEquals(basic, this.stringToTag(basicStr));

    final StringBinaryTag withEscapes = StringBinaryTag.of("hello \\world");
    final String withEscapesStr = this.tagToString(withEscapes);
    assertEquals("\"hello \\\\world\"", withEscapesStr);
    assertEquals(withEscapes, this.stringToTag(withEscapesStr));


    // single quotes
    assertEquals(StringBinaryTag.of("something single-quoted"), this.stringToTag("'something single-quoted'"));
    // unquoted
    assertEquals(StringBinaryTag.of("whatever"), this.stringToTag("whatever"));

    // something vaguely like a number
    assertEquals(StringBinaryTag.of("1.33.28d"), this.stringToTag("1.33.28d"));
  }

  private static final String UNICODE_TEST = "test ä ö";

  @Test
  public void testUnicodeString() throws IOException {
    assertEquals("\"" + UNICODE_TEST + "\"", this.tagToString(StringBinaryTag.of(UNICODE_TEST)));
    assertEquals(StringBinaryTag.of(UNICODE_TEST), this.stringToTag("\"" + UNICODE_TEST + "\""));
  }

  @Test
  public void testByteTag() throws IOException {
    assertEquals("0B", this.tagToString(ByteBinaryTag.of((byte) 0)));
    assertEquals("112B", this.tagToString(ByteBinaryTag.of((byte) 112)));

    assertEquals(ByteBinaryTag.of((byte) 12), this.stringToTag("12b"));
    assertEquals(ByteBinaryTag.of((byte) 13), this.stringToTag("13B"));
  }

  @Test
  public void testShortTag() throws IOException {
    assertEquals("14883S", this.tagToString(ShortBinaryTag.of((short) 14883)));

    assertEquals(ShortBinaryTag.of((short) -28), this.stringToTag("-28S"));
    assertEquals(ShortBinaryTag.of((short) 2229), this.stringToTag("+2229S"));
    assertEquals(StringBinaryTag.of("12.88S"), this.stringToTag("12.88S"));
  }

  @Test
  public void testIntTag() throws IOException {
    assertEquals("448228", this.tagToString(IntBinaryTag.of(448228)));

    assertEquals(IntBinaryTag.of(4482828), this.stringToTag("4482828"));
    assertEquals(IntBinaryTag.of(-24), this.stringToTag("-24"));
  }

  @Test
  public void testLongTag() throws IOException {
    assertEquals("28292849L", this.tagToString(LongBinaryTag.of(28292849L)));
    assertEquals("-28292849L", this.tagToString(LongBinaryTag.of(-28292849L)));

    assertEquals(LongBinaryTag.of(42L), this.stringToTag("42l"));
    assertEquals(LongBinaryTag.of(938L), this.stringToTag("+938L"));
  }

  @Test
  public void testFloatTag() throws IOException {
    assertEquals("1.204F", this.tagToString(FloatBinaryTag.of(1.204f)));

    assertEquals(FloatBinaryTag.of(1.2e4f), this.stringToTag("1.2e4f"));
    assertEquals(FloatBinaryTag.of(4.3e-4f), this.stringToTag("4.3e-4f"));
    assertEquals(FloatBinaryTag.of(-4.3e-4f), this.stringToTag("-4.3e-4F"));
    assertEquals(FloatBinaryTag.of(4.3e-4f), this.stringToTag("+4.3e-4F"));
    assertEquals(FloatBinaryTag.of(0.3f), this.stringToTag(".3F"));
  }

  @Test
  public void testDoubleTag() throws IOException {
    assertEquals("1.204D", this.tagToString(DoubleBinaryTag.of(1.204d)));

    assertEquals(DoubleBinaryTag.of(1.2e4d), this.stringToTag("1.2e4d"));
    assertEquals(DoubleBinaryTag.of(4.3e-4d), this.stringToTag("4.3e-4d"));
    assertEquals(DoubleBinaryTag.of(-4.3e-4d), this.stringToTag("-4.3e-4D"));
    assertEquals(DoubleBinaryTag.of(4.3e-4d), this.stringToTag("+4.3e-4D"));
  }

  @Test
  public void testByteArrayTag() throws IOException {
    assertEquals("[B;1B,2B,3B]", this.tagToString(ByteArrayBinaryTag.of((byte) 1, (byte) 2, (byte) 3)));
    assertEquals(ByteArrayBinaryTag.of((byte) 1, (byte) 1, (byte) 2, (byte) 3, (byte) 5, (byte) 8), this.stringToTag("[B; 1b, 1b, 2b, 3b, 5b, 8b]"));
  }

  @Test
  public void testIntArrayTag() throws IOException {
    assertEquals("[I;1,2,3]", this.tagToString(IntArrayBinaryTag.of(1, 2, 3)));
    assertEquals(IntArrayBinaryTag.of(2, 4, 6, 8, 10, 12), this.stringToTag("[I; 2, 4, 6, 8, 10, 12]"));
  }

  @Test
  public void testLongArrayTag() throws IOException {
    assertEquals("[L;1L,2L,3L]", this.tagToString(LongArrayBinaryTag.of(1, 2, 3)));
    assertEquals(LongArrayBinaryTag.of(2, 4, 6, -8, 10, 12), this.stringToTag("[L; 2l, 4l, 6l, -8l, 10l, 12l]"));
  }

  private String tagToString(final BinaryTag tag) throws IOException {
    final StringWriter writer = new StringWriter();
    try(final TagStringWriter emitter = new TagStringWriter(writer)) {
      emitter.writeTag(tag);
    }
    return writer.toString();
  }

  private BinaryTag stringToTag(final String input) throws StringTagParseException {
    final CharBuffer buffer = new CharBuffer(input);
    final TagStringReader parser = new TagStringReader(buffer);
    final BinaryTag ret = parser.tag();
    if(buffer.skipWhitespace().hasMore()) {
      throw buffer.makeError("Trailing content after parse!");
    }
    return ret;
  }
}
