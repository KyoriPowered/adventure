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
import java.io.Writer;
import java.util.Map;

/**
 * An emmitter for the SNBT format.
 *
 * Details on the format are described in the package documentation.
 */
/* package */ class TagStringWriter implements AutoCloseable {
  private final Appendable out;
  private final String indent = "  "; // TODO: pretty-printing
  private int level;
  /**
   * Whether a {@link Tokens#VALUE_SEPARATOR} needs to be printed before the beginning of the next object.
   */
  private boolean needsSeparator;

  public TagStringWriter(final Appendable out) {
    this.out = out;
  }

  // NBT-specific

  public TagStringWriter writeTag(final BinaryTag tag) throws IOException {
    final BinaryTagType<?> type = tag.type();
    if(type == BinaryTagTypes.COMPOUND) {
      return writeCompound((CompoundBinaryTag) tag);
    } else if(type == BinaryTagTypes.LIST) {
      return writeList((ListBinaryTag) tag);
    } else if(type == BinaryTagTypes.BYTE_ARRAY) {
      return writeByteArray((ByteArrayBinaryTag) tag);
    } else if(type == BinaryTagTypes.INT_ARRAY) {
      return writeIntArray((IntArrayBinaryTag) tag);
    } else if(type == BinaryTagTypes.LONG_ARRAY) {
      return writeLongArray((LongArrayBinaryTag) tag);
    } else if(type == BinaryTagTypes.STRING) {
      return value(((StringBinaryTag) tag).value(), Tokens.EOF);
    } else if(type == BinaryTagTypes.BYTE) {
      return value(Byte.toString(((ByteBinaryTag) tag).value()), Tokens.TYPE_BYTE);
    } else if(type == BinaryTagTypes.SHORT) {
      return value(Short.toString(((ShortBinaryTag) tag).value()), Tokens.TYPE_SHORT);
    } else if(type == BinaryTagTypes.INT) {
      return value(Integer.toString(((IntBinaryTag) tag).value()), Tokens.TYPE_INT);
    } else if(type == BinaryTagTypes.LONG) {
      return value(Long.toString(((LongBinaryTag) tag).value()), Tokens.TYPE_LONG);
    } else if(type == BinaryTagTypes.FLOAT) {
      return value(Float.toString(((FloatBinaryTag) tag).value()), Tokens.TYPE_FLOAT);
    } else if(type == BinaryTagTypes.DOUBLE) {
      return value(Double.toString(((DoubleBinaryTag) tag).value()), Tokens.TYPE_DOUBLE);
    } else {
      throw new IOException("Unknown tag type: " + type);
      // unknown!
    }
  }

  private TagStringWriter writeCompound(final CompoundBinaryTag tag) throws IOException {
    beginCompound();
    for(final Map.Entry<String, ? extends BinaryTag> entry : tag) {
      key(entry.getKey());
      writeTag(entry.getValue());
    }
    endCompound();
    return this;
  }

  private TagStringWriter writeList(final ListBinaryTag tag) throws IOException {
    beginList();
    for(BinaryTag el : tag) {
      printAndResetSeparator();
      writeTag(el);
    }
    endList();
    return this;
  }

  private TagStringWriter writeByteArray(final ByteArrayBinaryTag tag) throws IOException {
    this.beginList()
    .out.append(Tokens.TYPE_BYTE)
    .append(Tokens.ARRAY_SIGNATURE_SEPARATOR);

    for (byte b : tag.value()) {
      printAndResetSeparator();
      value(Byte.toString(b), Tokens.TYPE_BYTE);
    }
    this.endList();
    return this;
  }

  private TagStringWriter writeIntArray(final IntArrayBinaryTag tag) throws IOException {
    this.beginList()
      .out.append(Tokens.TYPE_INT)
      .append(Tokens.ARRAY_SIGNATURE_SEPARATOR);

    for (int i : tag.value()) {
      printAndResetSeparator();
      value(Integer.toString(i), Tokens.TYPE_INT);
    }
    this.endList();
    return this;
  }

  private TagStringWriter writeLongArray(final LongArrayBinaryTag tag) throws IOException {
    this.beginList()
      .out.append(Tokens.TYPE_LONG)
      .append(Tokens.ARRAY_SIGNATURE_SEPARATOR);

    for (long l : tag.value()) {
      printAndResetSeparator();
      value(Long.toString(l), Tokens.TYPE_LONG);
    }
    this.endList();
    return this;
  }

  // Value types

  public TagStringWriter beginCompound() throws IOException {
    printAndResetSeparator();
    ++this.level;
    this.out.append(Tokens.COMPOUND_BEGIN);
    return this;
  }

  public TagStringWriter endCompound() throws IOException {
    out.append(Tokens.COMPOUND_END);
    --this.level;
    this.needsSeparator = true;
    return this;
  }

  public TagStringWriter key(final String key) throws IOException {
    printAndResetSeparator();
    writeMaybeQuoted(key, false);
    this.out.append(Tokens.COMPOUND_KEY_TERMINATOR); // TODO: spacing/pretty-printing
    return this;
  }

  public TagStringWriter value(final String value, final char valueType) throws IOException {
    if(valueType == Tokens.EOF) { // string doesn't have its type
      writeMaybeQuoted(value, true);
    } else {
      this.out.append(value);
      if(valueType != Tokens.TYPE_INT) {
        this.out.append(valueType);
      }
    }
    this.needsSeparator = true;
    return this;
  }

  public TagStringWriter beginList() throws IOException {
    printAndResetSeparator();
    ++this.level;
    this.out.append(Tokens.ARRAY_BEGIN);
    return this;
  }

  public TagStringWriter endList() throws IOException {
    this.out.append(Tokens.ARRAY_END);
    --this.level;
    this.needsSeparator = true;
    return this;
  }

  private void writeMaybeQuoted(final String content, boolean requireQuotes) throws IOException {
    if(!requireQuotes) {
      for(int i = 0; i < content.length(); ++i) {
        if (!Tokens.id(content.charAt(i))) {
          requireQuotes = true;
          break;
        }
      }
    }
    if(requireQuotes) { // TODO: single quotes
      this.out.append(Tokens.DOUBLE_QUOTE);
      this.out.append(escape(content, Tokens.DOUBLE_QUOTE));
      this.out.append(Tokens.DOUBLE_QUOTE);
    } else {
      this.out.append(content);
    }
  }

  private static String escape(final String content, final char quoteChar) {
    final StringBuilder output = new StringBuilder(content.length());
    for(int i = 0; i < content.length(); ++i) {
      final char c = content.charAt(i);
      if(c == quoteChar || c == '\\') {
        output.append(Tokens.ESCAPE_MARKER);
      }
      output.append(c);
    }
    return output.toString();
  }

  private void printAndResetSeparator() throws IOException {
    if(this.needsSeparator) {
      this.out.append(Tokens.VALUE_SEPARATOR);
      this.needsSeparator = false;
    }
  }


  @Override
  public void close() throws IOException {
    if(this.level != 0) {
      throw new IllegalStateException("Document finished with unbalanced start and end objects");
    }
    if (this.out instanceof Writer) {
      ((Writer) this.out).flush();
    }
  }
}
