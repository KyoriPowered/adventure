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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.jetbrains.annotations.Nullable;

final class TagStringReader {
  private static final int MAX_DEPTH = 512;
  private static final int HEX_RADIX = 16;
  private static final int BINARY_RADIX = 2;
  private static final int DECIMAL_RADIX = 10;
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  private static final int[] EMPTY_INT_ARRAY = new int[0];
  private static final long[] EMPTY_LONG_ARRAY = new long[0];

  private final CharBuffer buffer;
  private boolean acceptLegacy;
  private int depth;

  TagStringReader(final CharBuffer buffer) {
    this.buffer = buffer;
  }

  public CompoundBinaryTag compound() throws StringTagParseException {
    this.buffer.expect(Tokens.COMPOUND_BEGIN);
    if (this.buffer.takeIf(Tokens.COMPOUND_END)) {
      return CompoundBinaryTag.empty();
    }

    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
    while (this.buffer.hasMore()) {
      builder.put(this.key(), this.tag());
      if (this.separatorOrCompleteWith(Tokens.COMPOUND_END)) {
        return builder.build();
      }
    }
    throw this.buffer.makeError("Unterminated compound tag!");
  }

  public ListBinaryTag list() throws StringTagParseException {
    final ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.builder();
    this.buffer.expect(Tokens.ARRAY_BEGIN);
    final boolean prefixedIndex = this.acceptLegacy && this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
    if (!prefixedIndex && this.buffer.takeIf(Tokens.ARRAY_END)) {
      return ListBinaryTag.empty();
    }
    while (this.buffer.hasMore()) {
      if (prefixedIndex) {
        this.buffer.takeUntil(':');
      }

      final BinaryTag next = this.tag();
      // TODO: validate type
      builder.add(next);
      if (this.separatorOrCompleteWith(Tokens.ARRAY_END)) {
        return builder.build();
      }
    }
    throw this.buffer.makeError("Reached end of file without end of list tag!");
  }

  /**
   * Similar to a list tag in syntax, but returning a single array tag rather than a list of tags.
   *
   * @param elementType the indicator character for element type
   * @return array-typed tag
   * @throws StringTagParseException if the array was not properly terminated
   */
  public BinaryTag array(char elementType) throws StringTagParseException {
    this.buffer.expect(Tokens.ARRAY_BEGIN)
      .expect(elementType)
      .expect(Tokens.ARRAY_SIGNATURE_SEPARATOR);

    elementType = Character.toLowerCase(elementType);
    if (elementType == Tokens.TYPE_BYTE) {
      return ByteArrayBinaryTag.byteArrayBinaryTag(this.byteArray());
    } else if (elementType == Tokens.TYPE_INT) {
      return IntArrayBinaryTag.intArrayBinaryTag(this.intArray());
    } else if (elementType == Tokens.TYPE_LONG) {
      return LongArrayBinaryTag.longArrayBinaryTag(this.longArray());
    } else {
      throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
    }
  }

  private byte[] byteArray() throws StringTagParseException {
    if (this.buffer.takeIf(Tokens.ARRAY_END)) {
      return EMPTY_BYTE_ARRAY;
    }

    final List<Byte> bytes = new ArrayList<>();
    while (this.buffer.hasMore()) {
      final CharSequence value = this.buffer.skipWhitespace().takeUntil(Tokens.TYPE_BYTE);
      try {
        bytes.add(Byte.valueOf(value.toString()));
      } catch (final NumberFormatException ex) {
        throw this.buffer.makeError("All elements of a byte array must be bytes!");
      }

      if (this.separatorOrCompleteWith(Tokens.ARRAY_END)) {
        final byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i) { // todo yikes, let's do less boxing
          result[i] = bytes.get(i);
        }
        return result;
      }
    }
    throw this.buffer.makeError("Reached end of document without array close");
  }

  private int[] intArray() throws StringTagParseException {
    if (this.buffer.takeIf(Tokens.ARRAY_END)) {
      return EMPTY_INT_ARRAY;
    }

    final IntStream.Builder builder = IntStream.builder();
    while (this.buffer.hasMore()) {
      final BinaryTag value = this.tag();
      if (!(value instanceof IntBinaryTag)) {
        throw this.buffer.makeError("All elements of an int array must be ints!");
      }
      builder.add(((IntBinaryTag) value).intValue());
      if (this.separatorOrCompleteWith(Tokens.ARRAY_END)) {
        return builder.build().toArray();
      }
    }
    throw this.buffer.makeError("Reached end of document without array close");
  }

  private long[] longArray() throws StringTagParseException {
    if (this.buffer.takeIf(Tokens.ARRAY_END)) {
      return EMPTY_LONG_ARRAY;
    }

    final LongStream.Builder longs = LongStream.builder();
    while (this.buffer.hasMore()) {
      final CharSequence value = this.buffer.skipWhitespace().takeUntil(Tokens.TYPE_LONG);
      try {
        longs.add(Long.parseLong(value.toString()));
      } catch (final NumberFormatException ex) {
        throw this.buffer.makeError("All elements of a long array must be longs!");
      }

      if (this.separatorOrCompleteWith(Tokens.ARRAY_END)) {
        return longs.build().toArray();
      }
    }
    throw this.buffer.makeError("Reached end of document without array close");
  }

  public String key() throws StringTagParseException {
    this.buffer.skipWhitespace();
    final char starChar = this.buffer.peek();
    try {
      if (starChar == Tokens.SINGLE_QUOTE || starChar == Tokens.DOUBLE_QUOTE) {
        return unescape(this.buffer.takeUntil(this.buffer.take()).toString());
      }

      final StringBuilder builder = new StringBuilder();
      while (this.buffer.hasMore()) {
        final char peek = this.buffer.peek();
        if (!Tokens.id(peek)) {
          if (this.acceptLegacy) {
            // In legacy format, a key is any non-colon character, with escapes allowed
            if (peek == Tokens.ESCAPE_MARKER) {
              this.buffer.take(); // skip
              continue;
            } else if (peek != Tokens.COMPOUND_KEY_TERMINATOR) {
              builder.append(this.buffer.take());
              continue;
            }
          }
          break;
        }
        builder.append(this.buffer.take());
      }
      return builder.toString();
    } finally {
      this.buffer.expect(Tokens.COMPOUND_KEY_TERMINATOR);
    }
  }

  public BinaryTag tag() throws StringTagParseException {
    if (this.depth++ > MAX_DEPTH) {
      throw this.buffer.makeError("Exceeded maximum allowed depth of " + MAX_DEPTH + " when reading tag");
    }
    try {
      final char startToken = this.buffer.skipWhitespace().peek();
      switch (startToken) {
        case Tokens.COMPOUND_BEGIN:
          return this.compound();
        case Tokens.ARRAY_BEGIN:
          // TODO: legacy-format int arrays are ambiguous with new format int lists
          // Maybe add in a legacy-only mode to read those?
          if (this.buffer.hasMore(2) && this.buffer.peek(2) == ';') { // we know we're an array tag
            return this.array(this.buffer.peek(1));
          } else {
            return this.list();
          }
        case Tokens.SINGLE_QUOTE:
        case Tokens.DOUBLE_QUOTE:
          // definitely a string tag
          this.buffer.advance();
          return StringBinaryTag.stringBinaryTag(unescape(this.buffer.takeUntil(startToken).toString()));
        default: // scalar
          return this.scalar();
      }
    } finally {
      this.depth--;
    }
  }

  /**
   * A tag that is definitely some sort of scalar.
   *
   * <p>Does not detect quoted strings, so those should have been parsed already.</p>
   *
   * @return a parsed tag
   */
  private BinaryTag scalar() throws StringTagParseException {
    final StringBuilder builder = new StringBuilder();
    while (this.buffer.hasMore()) {
      char current = this.buffer.peek();
      if (current == '\\') { // escape -- we are significantly more lenient than original format at the moment
        this.buffer.advance();
        current = this.buffer.take();
      } else if (Tokens.id(current)) {
        this.buffer.advance();
      } else { // end of value
        break;
      }
      builder.append(current);
    }
    if (builder.length() == 0) {
      throw this.buffer.makeError("Expected a value but got nothing");
    }
    final String original = builder.toString(); // use unmodified string when number parsing fails

    // Start stripping down the string so we can use Java's number parsing instead of having to write our own.
    // Determine the radix and strip its prefix if present
    final int radix = this.extractRadix(builder, original);

    // Check for the sign before removing the type token because of hex number always needing a sign thanks to byte types
    final char last = builder.charAt(builder.length() - 1);
    boolean hasSignToken = false;
    boolean signed = radix != HEX_RADIX; // hex defaults to unsigned
    if (builder.length() > 2) {
      final char signChar = builder.charAt(builder.length() - 2);
      if (signChar == Tokens.TYPE_SIGNED || signChar == Tokens.TYPE_UNSIGNED) {
        hasSignToken = true;
        signed = signChar == Tokens.TYPE_SIGNED;
        builder.deleteCharAt(builder.length() - 2);
      }
    }

    // Check for the type token and make sure we didn't fall into the hex trap (e.g. 0xAB)
    boolean hasTypeToken = false;
    char typeToken = Tokens.TYPE_INT;
    if (Tokens.numericType(last) && (hasSignToken || radix != HEX_RADIX)) {
      hasTypeToken = true;
      typeToken = Character.toLowerCase(last);
      builder.deleteCharAt(builder.length() - 1);
    }

    if (!signed && (typeToken == Tokens.TYPE_FLOAT || typeToken == Tokens.TYPE_DOUBLE)) {
      throw this.buffer.makeError("Cannot create unsigned floating point numbers");
    }

    final String strippedString = builder.toString().replace("_", "");
    if (hasTypeToken) {
      try {
        final NumberBinaryTag tag = this.parseNumberTag(strippedString, typeToken, radix, signed);
        if (tag != null) {
          return tag;
        }
      } catch (final NumberFormatException ignored) {
        // not a numeric tag of the appropriate type
      }
    } else { // default to int or double parsing before falling back to string
      try {
        return IntBinaryTag.intBinaryTag(this.parseInt(strippedString, radix, signed));
      } catch (final NumberFormatException ex) {
        if (strippedString.indexOf('.') != -1) { // see if we have an unsuffixed double; always needs a dot
          try {
            return DoubleBinaryTag.doubleBinaryTag(Double.parseDouble(strippedString));
          } catch (final NumberFormatException ex2) {
            // ignore
          }
        }
      }
    }

    if (original.equalsIgnoreCase(Tokens.LITERAL_TRUE)) {
      return ByteBinaryTag.ONE;
    } else if (original.equalsIgnoreCase(Tokens.LITERAL_FALSE)) {
      return ByteBinaryTag.ZERO;
    }
    return StringBinaryTag.stringBinaryTag(original);
  }

  private int extractRadix(final StringBuilder builder, final String original) {
    int radixPrefixOffset = 0;
    final int radix;
    final char first = builder.charAt(0);
    if (first == '+' || first == '-') {
      radixPrefixOffset = 1;
    }
    if (original.startsWith("0b", radixPrefixOffset) || original.startsWith("0B", radixPrefixOffset)) {
      radix = BINARY_RADIX;
    } else if (original.startsWith("0x", radixPrefixOffset) || original.startsWith("0X", radixPrefixOffset)) {
      radix = HEX_RADIX;
    } else {
      radix = DECIMAL_RADIX;
    }
    if (radix != DECIMAL_RADIX) {
      builder.delete(radixPrefixOffset, 2 + radixPrefixOffset);
    }
    return radix;
  }

  private @Nullable NumberBinaryTag parseNumberTag(final String s, final char typeToken, final int radix, final boolean signed) {
    switch (typeToken) {
      case Tokens.TYPE_BYTE:
        return ByteBinaryTag.byteBinaryTag(this.parseByte(s, radix, signed));
      case Tokens.TYPE_SHORT:
        return ShortBinaryTag.shortBinaryTag(this.parseShort(s, radix, signed));
      case Tokens.TYPE_INT:
        return IntBinaryTag.intBinaryTag(this.parseInt(s, radix, signed));
      case Tokens.TYPE_LONG:
        return LongBinaryTag.longBinaryTag(this.parseLong(s, radix, signed));
      case Tokens.TYPE_FLOAT:
        final float floatValue = Float.parseFloat(s);
        if (Float.isFinite(floatValue)) { // don't accept NaN and Infinity
          return FloatBinaryTag.floatBinaryTag(floatValue);
        }
        break;
      case Tokens.TYPE_DOUBLE:
        final double doubleValue = Double.parseDouble(s);
        if (Double.isFinite(doubleValue)) { // don't accept NaN and Infinity
          return DoubleBinaryTag.doubleBinaryTag(doubleValue);
        }
        break;
    }
    return null;
  }

  private byte parseByte(final String s, final int radix, final boolean signed) {
    if (signed) {
      return Byte.parseByte(s, radix);
    }
    final int parsedInt = Integer.parseInt(s, radix);
    if (parsedInt >> Byte.SIZE == 0) {
      return (byte) parsedInt;
    }
    throw new NumberFormatException();
  }

  private short parseShort(final String s, final int radix, final boolean signed) {
    if (signed) {
      return Short.parseShort(s, radix);
    }
    final int parsedInt = Integer.parseInt(s, radix);
    if (parsedInt >> Short.SIZE == 0) {
      return (short) parsedInt;
    }
    throw new NumberFormatException();
  }

  private int parseInt(final String s, final int radix, final boolean signed) {
    return signed ? Integer.parseInt(s, radix) : Integer.parseUnsignedInt(s, radix);
  }

  private long parseLong(final String s, final int radix, final boolean signed) {
    return signed ? Long.parseLong(s, radix) : Long.parseUnsignedLong(s, radix);
  }

  private boolean separatorOrCompleteWith(final char endCharacter) throws StringTagParseException {
    if (this.buffer.takeIf(endCharacter)) {
      return true;
    }
    this.buffer.expect(Tokens.VALUE_SEPARATOR);
    return this.buffer.takeIf(endCharacter);
  }

  /**
   * Remove simple escape sequences from a string.
   *
   * @param withEscapes input string with escapes
   * @return string with escapes processed
   */
  private static String unescape(final String withEscapes) {
    int escapeIdx = withEscapes.indexOf(Tokens.ESCAPE_MARKER);
    if (escapeIdx == -1) { // nothing to unescape
      return withEscapes;
    }
    int lastEscape = 0;
    final StringBuilder output = new StringBuilder(withEscapes.length());
    do {
      output.append(withEscapes, lastEscape, escapeIdx);
      lastEscape = escapeIdx + 1;
    } while ((escapeIdx = withEscapes.indexOf(Tokens.ESCAPE_MARKER, lastEscape + 1)) != -1); // add one extra character to make sure we don't include escaped backslashes
    output.append(withEscapes.substring(lastEscape));
    return output.toString();
  }

  public void legacy(final boolean acceptLegacy) {
    this.acceptLegacy = acceptLegacy;
  }
}
