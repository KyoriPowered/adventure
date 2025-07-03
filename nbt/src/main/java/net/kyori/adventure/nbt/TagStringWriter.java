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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  private boolean heterogeneousLists;

  TagStringWriter(final Appendable out, final String indent) {
    this.out = out;
    this.indent = indent;
  }

  public TagStringWriter legacy(final boolean legacy) {
    this.legacy = legacy;
    return this;
  }

  public TagStringWriter heterogeneousLists(final boolean emitHeterogeneousLists) {
    this.heterogeneousLists = emitHeterogeneousLists;
    return this;
  }

  // NBT-specific

  public TagStringWriter writeTag(final BinaryTag tag) throws IOException {
    return switch (tag) {
      case CompoundBinaryTag compoundTag -> this.writeCompound(compoundTag);
      case ListBinaryTag listTag -> this.writeList(listTag);
      case ByteArrayBinaryTag byteArrayTag -> this.writeByteArray(byteArrayTag);
      case IntArrayBinaryTag intArrayTag -> this.writeIntArray(intArrayTag);
      case LongArrayBinaryTag longArrayTag -> this.writeLongArray(longArrayTag);
      case StringBinaryTag stringTag -> this.value(stringTag.value(), Tokens.EOF);
      case ByteBinaryTag byteTag -> this.value(Byte.toString(byteTag.value()), Tokens.TYPE_BYTE);
      case ShortBinaryTag shortTag -> this.value(Short.toString(shortTag.value()), Tokens.TYPE_SHORT);
      case IntBinaryTag intTag -> this.value(Integer.toString(intTag.value()), Tokens.TYPE_INT);
      case LongBinaryTag longTag -> this.value(Long.toString(longTag.value()), Character.toUpperCase(Tokens.TYPE_LONG)); // special-case
      case FloatBinaryTag floatTag -> this.value(Float.toString(floatTag.value()), Tokens.TYPE_FLOAT);
      case DoubleBinaryTag doubleTag -> this.value(Double.toString(doubleTag.value()), Tokens.TYPE_DOUBLE);
      default -> throw new IOException("Unknown tag type: " + tag.type());
    };
  }

  private TagStringWriter writeCompound(final CompoundBinaryTag tag) throws IOException {
    this.beginCompound();

    final List<String> keys = new ArrayList<>(tag.keySet());
    Collections.sort(keys);

    for (final String key : keys) {
      final BinaryTag value = tag.get(key);
      if (value == null) continue;
      this.key(key);
      this.writeTag(value);
    }

    this.endCompound();
    return this;
  }

  private TagStringWriter writeList(final ListBinaryTag rawTag) throws IOException {
    final ListBinaryTag tag = this.heterogeneousLists ? rawTag.unwrapHeterogeneity() : rawTag.wrapHeterogeneity();
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
    for (final byte b : value) {
      this.printAndResetSeparator(true);
      this.value(Byte.toString(b), byteArrayType);
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
    for (final int j : value) {
      this.printAndResetSeparator(true);
      this.value(Integer.toString(j), Tokens.TYPE_INT);
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
    for (final long l : value) {
      this.printAndResetSeparator(true);
      this.value(Long.toString(l), Tokens.TYPE_LONG);
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
    return !this.indent.isEmpty();
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
    if (this.out instanceof Writer writer) {
      writer.flush();
    }
  }
}
