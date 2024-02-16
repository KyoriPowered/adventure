/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
import java.io.Writer;
import java.util.Map;

/**
 * An emitter for the SNBT format.
 *
 * <p>Details on the format are described in the package documentation.</p>
 */
final class TagStringWriter implements AutoCloseable {
  private final Appendable out;
  private final String indent; // TODO: pretty-printing
  private int level;
  /**
   * Whether a {@link Tokens#VALUE_SEPARATOR} needs to be printed before the beginning of the next object.
   */
  private boolean needsSeparator;
  private boolean legacy;

  TagStringWriter(final Appendable out, final String indent) {
    this.out = out;
    this.indent = indent;
  }

  public TagStringWriter legacy(final boolean legacy) {
    this.legacy = legacy;
    return this;
  }

  // NBT-specific

  public TagStringWriter writeTag(final BinaryTag tag) throws IOException {
    final BinaryTagType<?> type = tag.type();
    if (type == BinaryTagTypes.COMPOUND) {
      return this.writeCompound((CompoundBinaryTag) tag);
    } else if (type == BinaryTagTypes.LIST) {
      return this.writeList((ListBinaryTag) tag);
    } else if (type == BinaryTagTypes.BYTE_ARRAY) {
      return this.writeByteArray((ByteArrayBinaryTag) tag);
    } else if (type == BinaryTagTypes.INT_ARRAY) {
      return this.writeIntArray((IntArrayBinaryTag) tag);
    } else if (type == BinaryTagTypes.LONG_ARRAY) {
      return this.writeLongArray((LongArrayBinaryTag) tag);
    } else if (type == BinaryTagTypes.STRING) {
      return this.value(((StringBinaryTag) tag).value(), Tokens.EOF);
    } else if (type == BinaryTagTypes.BYTE) {
      return this.value(Byte.toString(((ByteBinaryTag) tag).value()), Tokens.TYPE_BYTE);
    } else if (type == BinaryTagTypes.SHORT) {
      return this.value(Short.toString(((ShortBinaryTag) tag).value()), Tokens.TYPE_SHORT);
    } else if (type == BinaryTagTypes.INT) {
      return this.value(Integer.toString(((IntBinaryTag) tag).value()), Tokens.TYPE_INT);
    } else if (type == BinaryTagTypes.LONG) {
      return this.value(Long.toString(((LongBinaryTag) tag).value()), Character.toUpperCase(Tokens.TYPE_LONG)); // special-case
    } else if (type == BinaryTagTypes.FLOAT) {
      return this.value(Float.toString(((FloatBinaryTag) tag).value()), Tokens.TYPE_FLOAT);
    } else if (type == BinaryTagTypes.DOUBLE) {
      return this.value(Double.toString(((DoubleBinaryTag) tag).value()), Tokens.TYPE_DOUBLE);
    } else {
      throw new IOException("Unknown tag type: " + type);
      // unknown!
    }
  }

  private TagStringWriter writeCompound(final CompoundBinaryTag tag) throws IOException {
    this.beginCompound();
    for (final Map.Entry<String, ? extends BinaryTag> entry : tag) {
      this.key(entry.getKey());
      this.writeTag(entry.getValue());
    }
    this.endCompound();
    return this;
  }

  private TagStringWriter writeList(final ListBinaryTag tag) throws IOException {
    this.beginList();
    int idx = 0;
    final boolean lineBreaks = this.prettyPrinting() && this.breakListElement(tag.elementType());
    for (final BinaryTag el : tag) {
      this.printAndResetSeparator(!lineBreaks);
      if (lineBreaks) {
        this.newlineIndent();
      }
      if (this.legacy) {
        this.out.append(String.valueOf(idx++));
        this.appendSeparator(Tokens.COMPOUND_KEY_TERMINATOR);
      }

      this.writeTag(el);
    }
    this.endList(lineBreaks);
    return this;
  }

  private TagStringWriter writeByteArray(final ByteArrayBinaryTag tag) throws IOException {
    if (this.legacy) {
      throw new IOException("Legacy Mojangson only supports integer arrays!");
    }
    this.beginArray(Tokens.TYPE_BYTE);

    final char byteArrayType = Character.toUpperCase(Tokens.TYPE_BYTE); // special case to match vanilla format
    final byte[] value = ByteArrayBinaryTagImpl.value(tag);
    for (int i = 0, length = value.length; i < length; i++) {
      this.printAndResetSeparator(true);
      this.value(Byte.toString(value[i]), byteArrayType);
    }
    this.endArray();
    return this;
  }

  private TagStringWriter writeIntArray(final IntArrayBinaryTag tag) throws IOException {
    if (this.legacy) {
      this.beginList();
    } else {
      this.beginArray(Tokens.TYPE_INT);
    }

    final int[] value = IntArrayBinaryTagImpl.value(tag);
    for (int i = 0, length = value.length; i < length; i++) {
      this.printAndResetSeparator(true);
      this.value(Integer.toString(value[i]), Tokens.TYPE_INT);
    }
    this.endArray();
    return this;
  }

  private TagStringWriter writeLongArray(final LongArrayBinaryTag tag) throws IOException {
    if (this.legacy) {
      throw new IOException("Legacy Mojangson only supports integer arrays!");
    }
    this.beginArray(Tokens.TYPE_LONG);

    final long[] value = LongArrayBinaryTagImpl.value(tag);
    for (int i = 0, length = value.length; i < length; i++) {
      this.printAndResetSeparator(true);
      this.value(Long.toString(value[i]), Tokens.TYPE_LONG);
    }
    this.endArray();
    return this;
  }

  // Value types

  public TagStringWriter beginCompound() throws IOException {
    this.printAndResetSeparator(false);
    this.level++;
    this.out.append(Tokens.COMPOUND_BEGIN);
    return this;
  }

  public TagStringWriter endCompound() throws IOException {
    this.level--;
    this.newlineIndent();
    this.out.append(Tokens.COMPOUND_END);
    this.needsSeparator = true;
    return this;
  }

  public TagStringWriter key(final String key) throws IOException {
    this.printAndResetSeparator(false);
    this.newlineIndent();
    this.writeMaybeQuoted(key, false);
    this.appendSeparator(Tokens.COMPOUND_KEY_TERMINATOR);
    return this;
  }

  public TagStringWriter value(final String value, final char valueType) throws IOException {
    if (valueType == Tokens.EOF) { // string doesn't have its type
      this.writeMaybeQuoted(value, true);
    } else {
      this.out.append(value);
      if (valueType != Tokens.TYPE_INT) {
        this.out.append(valueType);
      }
    }
    this.needsSeparator = true;
    return this;
  }

  public TagStringWriter beginList() throws IOException {
    this.printAndResetSeparator(false);
    this.level++;
    this.out.append(Tokens.ARRAY_BEGIN);
    return this;
  }

  public TagStringWriter endList(final boolean lineBreak) throws IOException {
    this.level--;
    if (lineBreak) {
      this.newlineIndent();
    }
    this.out.append(Tokens.ARRAY_END);
    this.needsSeparator = true;
    return this;
  }

  private TagStringWriter beginArray(final char type) throws IOException {
    this.beginList()
      .out.append(Character.toUpperCase(type))
      .append(Tokens.ARRAY_SIGNATURE_SEPARATOR);

    if (this.prettyPrinting()) {
      this.out.append(' ');
    }

    return this;
  }

  private TagStringWriter endArray() throws IOException {
    return this.endList(false);
  }

  private void writeMaybeQuoted(final String content, boolean requireQuotes) throws IOException {
    if (!requireQuotes) {
      for (int i = 0; i < content.length(); ++i) {
        if (!Tokens.id(content.charAt(i))) {
          requireQuotes = true;
          break;
        }
      }
    }
    if (requireQuotes) { // TODO: single quotes
      this.out.append(Tokens.DOUBLE_QUOTE);
      this.out.append(escape(content, Tokens.DOUBLE_QUOTE));
      this.out.append(Tokens.DOUBLE_QUOTE);
    } else {
      this.out.append(content);
    }
  }

  private static String escape(final String content, final char quoteChar) {
    final StringBuilder output = new StringBuilder(content.length());
    for (int i = 0; i < content.length(); ++i) {
      final char c = content.charAt(i);
      if (c == quoteChar || c == '\\') {
        output.append(Tokens.ESCAPE_MARKER);
      }
      output.append(c);
    }
    return output.toString();
  }

  private void printAndResetSeparator(final boolean pad) throws IOException {
    if (this.needsSeparator) {
      this.out.append(Tokens.VALUE_SEPARATOR);
      if (pad && this.prettyPrinting()) {
        this.out.append(' ');
      }
      this.needsSeparator = false;
    }
  }

  // Pretty printing

  private boolean breakListElement(final BinaryTagType<?> type) {
    // lists should break between elements on any non-scalar element
    return type == BinaryTagTypes.COMPOUND
      || type == BinaryTagTypes.LIST
      || type == BinaryTagTypes.BYTE_ARRAY
      || type == BinaryTagTypes.INT_ARRAY
      || type == BinaryTagTypes.LONG_ARRAY;
  }

  private boolean prettyPrinting() {
    return this.indent.length() > 0;
  }

  private void newlineIndent() throws IOException {
    if (this.prettyPrinting()) {
      this.out.append(Tokens.NEWLINE);
      for (int i = 0; i < this.level; ++i) {
        this.out.append(this.indent);
      }
    }
  }

  private Appendable appendSeparator(final char separatorChar) throws IOException {
    this.out.append(separatorChar);
    if (this.prettyPrinting()) {
      this.out.append(' ');
    }
    return this.out;
  }

  @Override
  public void close() throws IOException {
    if (this.level != 0) {
      throw new IllegalStateException("Document finished with unbalanced start and end objects");
    }
    if (this.out instanceof Writer) {
      ((Writer) this.out).flush();
    }
  }
}
