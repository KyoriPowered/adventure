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

import com.google.common.io.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringIOTest {
  @Test
  void testReadKeyValuePair() throws StringTagParseException {
    final TagStringReader keyRead = new TagStringReader(new CharBuffer("testKey: \"hello\""));
    assertEquals("testKey", keyRead.key());
    assertEquals(StringBinaryTag.stringBinaryTag("hello"), keyRead.tag());
  }

  @Test
  void testComplexStringCompound() throws IOException {
    final CompoundBinaryTag tag = CompoundBinaryTag.builder()
      .putString("standard", "value")
      .putString("complex", "weird, isn't it: huh")
      .putString("quoted", "quo\"ted")
      .putString("comp:lex \"key", "let's go")
      .put("listed", ListBinaryTag.builder(BinaryTagTypes.STRING)
        .add(StringBinaryTag.stringBinaryTag("one"))
        .add(StringBinaryTag.stringBinaryTag("two"))
        .add(StringBinaryTag.stringBinaryTag("three"))
        .build())
      .build();

    final String serialized = TagStringIO.get().asString(tag);
    final CompoundBinaryTag deserialized = TagStringIO.get().asCompound(serialized);
    assertEquals(tag, deserialized);
  }

  @Test
  void testBigTestRoundtrip() throws IOException {
    // Read and write
    final CompoundBinaryTag bigTest;
    try (final InputStream is = this.getClass().getResourceAsStream("/bigtest.nbt")) {
      bigTest = BinaryTagIO.reader().read(is, BinaryTagIO.Compression.GZIP);
      final String written = TagStringIO.get().asString(bigTest);
      assertEquals(bigTest, TagStringIO.get().asCompound(written));
    }

    // Read snbt equivalent
    final String result;
    try (final InputStream is = this.getClass().getResourceAsStream("/bigtest.snbt"); final BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
      final StringBuilder build = new StringBuilder();
      final char[] buffer = new char[2048];
      int read;
      while ((read = reader.read(buffer)) != -1) {
        build.append(buffer, 0, read);
      }
      result = build.toString();
    }

    final CompoundBinaryTag parsedSnbt = (CompoundBinaryTag) this.stringToTag(result);
    assertEquals(bigTest, parsedSnbt);
  }

  @Test
  void testBigTestPrettyPrinting() throws IOException {
    final CompoundBinaryTag bigTest;
    try (final InputStream is = this.getClass().getResourceAsStream("/bigtest.nbt")) {
      bigTest = BinaryTagIO.reader().read(is, BinaryTagIO.Compression.GZIP);
    }

    final String rawTest = Resources.toString(this.getClass().getResource("/bigtest.snbt"), StandardCharsets.UTF_8);
    assertEquals(rawTest.trim(), TagStringIO.builder().indent(4).build().asString(bigTest).trim());

  }

  @Test
  void testStringTag() throws IOException {
    final StringBinaryTag basic = StringBinaryTag.stringBinaryTag("hello");
    final String basicStr = this.tagToString(basic);
    assertEquals("\"hello\"", basicStr);
    assertEquals(basic, this.stringToTag(basicStr));

    final StringBinaryTag withEscapes = StringBinaryTag.stringBinaryTag("hello \\world");
    final String withEscapesStr = this.tagToString(withEscapes);
    assertEquals("\"hello \\\\world\"", withEscapesStr);
    assertEquals(withEscapes, this.stringToTag(withEscapesStr));

    // single quotes
    assertEquals(StringBinaryTag.stringBinaryTag("something single-quoted"), this.stringToTag("'something single-quoted'"));
    // unquoted
    assertEquals(StringBinaryTag.stringBinaryTag("whatever"), this.stringToTag("whatever"));

    // something vaguely like a number
    assertEquals(StringBinaryTag.stringBinaryTag("1.33.28d"), this.stringToTag("1.33.28d"));
    assertEquals(StringBinaryTag.stringBinaryTag("2147483649"), this.stringToTag("2147483649")); // larger than an int
  }

  private static final String UNICODE_TEST = "test ä ö";

  @Test
  void testUnicodeString() throws IOException {
    assertEquals("\"" + UNICODE_TEST + "\"", this.tagToString(StringBinaryTag.stringBinaryTag(UNICODE_TEST)));
    assertEquals(StringBinaryTag.stringBinaryTag(UNICODE_TEST), this.stringToTag("\"" + UNICODE_TEST + "\""));
  }

  @Test
  void testByteTag() throws IOException {
    assertEquals("0b", this.tagToString(ByteBinaryTag.byteBinaryTag((byte) 0)));
    assertEquals("112b", this.tagToString(ByteBinaryTag.byteBinaryTag((byte) 112)));

    assertEquals(ByteBinaryTag.byteBinaryTag((byte) 12), this.stringToTag("12b"));
    assertEquals(ByteBinaryTag.byteBinaryTag((byte) 13), this.stringToTag("13B"));
  }

  @Test
  void testShortTag() throws IOException {
    assertEquals("14883s", this.tagToString(ShortBinaryTag.shortBinaryTag((short) 14883)));

    assertEquals(ShortBinaryTag.shortBinaryTag((short) -28), this.stringToTag("-28S"));
    assertEquals(ShortBinaryTag.shortBinaryTag((short) 2229), this.stringToTag("+2229S"));
    assertEquals(StringBinaryTag.stringBinaryTag("12.88S"), this.stringToTag("12.88S"));
  }

  @Test
  void testIntTag() throws IOException {
    assertEquals("448228", this.tagToString(IntBinaryTag.intBinaryTag(448228)));

    assertEquals(IntBinaryTag.intBinaryTag(4482828), this.stringToTag("4482828"));
    assertEquals(IntBinaryTag.intBinaryTag(4482828), this.stringToTag("4_4_8______2_8_2_8"));
    assertEquals(IntBinaryTag.intBinaryTag(-24), this.stringToTag("-24"));
    assertEquals(IntBinaryTag.intBinaryTag(0xABC), this.stringToTag("0xABC"));
    assertEquals(IntBinaryTag.intBinaryTag(0b1001), this.stringToTag("0b1001"));
  }

  @Test
  void testNumberSign() throws IOException {
    assertEquals(ByteBinaryTag.byteBinaryTag((byte) -16), this.stringToTag("240ub"));
    assertEquals(ByteBinaryTag.byteBinaryTag((byte) -16), this.stringToTag("-16sb"));
    assertEquals(IntBinaryTag.intBinaryTag(-0xABC), this.stringToTag("-0xABCsI"));
  }

  @Test
  void testReadLiteralBoolean() throws IOException {
    assertEquals(ByteBinaryTag.byteBinaryTag((byte) 1), this.stringToTag("true"));
    assertEquals(ByteBinaryTag.byteBinaryTag((byte) 0), this.stringToTag("false"));
  }

  @Test
  void testLongTag() throws IOException {
    assertEquals("28292849L", this.tagToString(LongBinaryTag.longBinaryTag(28292849L)));
    assertEquals("-28292849L", this.tagToString(LongBinaryTag.longBinaryTag(-28292849L)));

    assertEquals(LongBinaryTag.longBinaryTag(42L), this.stringToTag("42l"));
    assertEquals(LongBinaryTag.longBinaryTag(938L), this.stringToTag("+938L"));
  }

  @Test
  void testFloatTag() throws IOException {
    assertEquals("1.204f", this.tagToString(FloatBinaryTag.floatBinaryTag(1.204f)));

    assertEquals(FloatBinaryTag.floatBinaryTag(1.2e4f), this.stringToTag("1.2e4f"));
    assertEquals(FloatBinaryTag.floatBinaryTag(4.3e-4f), this.stringToTag("4.3e-4f"));
    assertEquals(FloatBinaryTag.floatBinaryTag(-4.3e-4f), this.stringToTag("-4.3e-4F"));
    assertEquals(FloatBinaryTag.floatBinaryTag(4.3e-4f), this.stringToTag("+4.3e-4F"));
    assertEquals(FloatBinaryTag.floatBinaryTag(0.3f), this.stringToTag(".3F"));
    assertEquals(FloatBinaryTag.floatBinaryTag(3.0f), this.stringToTag("3.F"));
  }

  @Test
  void testDoubleTag() throws IOException {
    assertEquals("1.204d", this.tagToString(DoubleBinaryTag.doubleBinaryTag(1.204d)));

    assertEquals(DoubleBinaryTag.doubleBinaryTag(1.2e4d), this.stringToTag("1.2e4d"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(4.3e-4d), this.stringToTag("4.3e-4d"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(-4.3e-4d), this.stringToTag("-4.3e-4D"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(4.3e-4d), this.stringToTag("+4.3e-4D"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(3.0d), this.stringToTag("3."));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(0.3d), this.stringToTag(".3"));
  }

  @Test
  void testUnsuffixedDoubleTag() throws IOException {
    // we can read this, but will never write it
    assertEquals(DoubleBinaryTag.doubleBinaryTag(2.55e5), this.stringToTag("2.55e5"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(9.0), this.stringToTag("9."));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(-9.5), this.stringToTag("-9.5"));
    assertEquals(DoubleBinaryTag.doubleBinaryTag(0.5), this.stringToTag(".5"));
  }

  @Test
  void testSpecialFloatingPointNumbers() throws IOException {
    assertEquals(StringBinaryTag.stringBinaryTag("NaNd"), this.stringToTag("NaNd"));
    assertEquals(StringBinaryTag.stringBinaryTag("NaNf"), this.stringToTag("NaNf"));
    assertEquals(StringBinaryTag.stringBinaryTag("Infinityd"), this.stringToTag("Infinityd"));
    assertEquals(StringBinaryTag.stringBinaryTag("Infinityf"), this.stringToTag("Infinityf"));
  }

  @Test
  void testPrematureNumericParsing() throws IOException {
    assertEquals(StringBinaryTag.stringBinaryTag("0da"), this.stringToTag("0da"));
    assertEquals(StringBinaryTag.stringBinaryTag("00000faa"), this.stringToTag("00000faa"));
    assertEquals(StringBinaryTag.stringBinaryTag("1350diamonds_plz"), this.stringToTag("1350diamonds_plz"));
  }

  @Test
  void testByteArrayTag() throws IOException {
    assertEquals("[B;1B,2B,3B]", this.tagToString(ByteArrayBinaryTag.byteArrayBinaryTag((byte) 1, (byte) 2, (byte) 3)));
    assertEquals(ByteArrayBinaryTag.byteArrayBinaryTag((byte) 1, (byte) 1, (byte) 2, (byte) 3, (byte) 5, (byte) 8), this.stringToTag("[B; 1b, 1b, 2b, 3b, 5b, 8b]"));
  }

  @Test
  void testLegacyListTag() throws IOException {
    final String legacyInput = "[0:\"Tag #1\",1:\"Tag #2\"]";
    final BinaryTag tag = this.stringToTag(legacyInput);
    assertEquals("[\"Tag #1\",\"Tag #2\"]", this.tagToString(tag));
    final StringWriter output = new StringWriter();
    try (final TagStringWriter writer = new TagStringWriter(output, "").legacy(true)) {
      writer.writeTag(tag);
    }
    assertEquals(legacyInput, output.toString());

    final ListTagBuilder<BinaryTag> builder = new ListTagBuilder<>();
    builder.add(StringBinaryTag.stringBinaryTag("Tag #1"));
    builder.add(StringBinaryTag.stringBinaryTag("Tag #2"));
    assertEquals(builder.build(), tag);
  }

  @Test
  void testReadsLegacyCompoundKey() throws IOException {
    final String input = "{test*compound: \"hello world\"}";
    assertThrows(IOException.class, () -> this.stringToTag(input, false));
    assertEquals(CompoundBinaryTag.builder().putString("test*compound", "hello world").build(), this.stringToTag(input));

  }

  @Test
  void testIntArrayTag() throws IOException {
    assertEquals("[I;1,2,3]", this.tagToString(IntArrayBinaryTag.intArrayBinaryTag(1, 2, 3)));
    assertEquals(IntArrayBinaryTag.intArrayBinaryTag(2, 4, 6, 8, 10, 12), this.stringToTag("[I; 2, 4, 6, 8, 10, 12]"));
  }

  @Test
  void testLongArrayTag() throws IOException {
    assertEquals("[L;1l,2l,3l]", this.tagToString(LongArrayBinaryTag.longArrayBinaryTag(1, 2, 3)));
    assertEquals(LongArrayBinaryTag.longArrayBinaryTag(2, 4, 6, -8, 10, 12), this.stringToTag("[L; 2l, 4l, 6l, -8l, 10l, 12l]"));
  }

  @Test
  void testEmptyCompoundTag() throws StringTagParseException {
    assertEquals(CompoundBinaryTag.empty(), this.stringToTag("{}"));
    assertEquals(CompoundBinaryTag.empty(), this.stringToTag("{  }"));
  }

  @Test
  void testEmptyListTag() throws IOException {
    assertEquals("[]", this.tagToString(ListBinaryTag.empty()));
    assertEquals(ListBinaryTag.empty(), this.stringToTag("[]"));
    assertEquals(ListBinaryTag.empty(), this.stringToTag("[ ]"));
  }

  @Test
  void testEmptyByteArray() throws IOException {
    assertEquals("[B;]", this.tagToString(ByteArrayBinaryTag.byteArrayBinaryTag()));
    assertEquals(ByteArrayBinaryTag.byteArrayBinaryTag(), this.stringToTag("[B;]"));
    assertEquals(ByteArrayBinaryTag.byteArrayBinaryTag(), this.stringToTag("[B; ]"));
  }

  @Test
  void testEmptyIntArray() throws IOException {
    assertEquals("[I;]", this.tagToString(IntArrayBinaryTag.intArrayBinaryTag()));
    assertEquals(IntArrayBinaryTag.intArrayBinaryTag(), this.stringToTag("[I;]"));
    assertEquals(IntArrayBinaryTag.intArrayBinaryTag(), this.stringToTag("[I; ]"));
  }

  @Test
  void testEmptyLongArray() throws IOException {
    assertEquals("[L;]", this.tagToString(LongArrayBinaryTag.longArrayBinaryTag()));
    assertEquals(LongArrayBinaryTag.longArrayBinaryTag(), this.stringToTag("[L;]"));
    assertEquals(LongArrayBinaryTag.longArrayBinaryTag(), this.stringToTag("[L; ]"));
  }

  @Test
  void testTrailingComma() throws IOException {
    assertEquals(CompoundBinaryTag.builder().putString("test", "hello").build(), this.stringToTag("{test: \"hello\",}"));
    assertEquals(IntArrayBinaryTag.intArrayBinaryTag(1), this.stringToTag("[I;1,]"));
    assertEquals(ListBinaryTag.builder().add(StringBinaryTag.stringBinaryTag("hello")).build(), this.stringToTag("[\"hello\",]"));
  }

  private String tagToString(final BinaryTag tag) throws IOException {
    final StringWriter writer = new StringWriter();
    try (final TagStringWriter emitter = new TagStringWriter(writer, "")) {
      emitter.writeTag(tag);
    }
    return writer.toString();
  }

  private BinaryTag stringToTag(final String input) throws StringTagParseException {
    return this.stringToTag(input, true);
  }

  private BinaryTag stringToTag(final String input, final boolean acceptLegacy) throws StringTagParseException {
    final CharBuffer buffer = new CharBuffer(input);
    final TagStringReader parser = new TagStringReader(buffer);
    parser.legacy(acceptLegacy);
    final BinaryTag ret = parser.tag();
    if (buffer.skipWhitespace().hasMore()) {
      throw buffer.makeError("Trailing content after parse!");
    }
    return ret;
  }
}
