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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * A binary tag type.
 *
 * @param <T> the tag type
 * @since 4.0.0
 */
public sealed interface BinaryTagType<T extends BinaryTag> extends Predicate<BinaryTagType<? extends BinaryTag>> permits BinaryTagTypeImpl {
  /**
   * Returns the binary tag type for the given id.
   *
   * @param id the id
   * @return the binary tag type
   * @since 4.0.0
   */
  static BinaryTagType<? extends BinaryTag> binaryTagType(final byte id) {
    for (final BinaryTagType<? extends BinaryTag> type : BinaryTagTypeImpl.TYPES) {
      if (type.id() == id) {
        return type;
      }
    }
    throw new IllegalArgumentException(String.valueOf(id));
  }

  /**
   * Gets the id.
   *
   * @return the id
   * @since 4.0.0
   */
  byte id();

  /**
   * If this tag type is numeric.
   *
   * @return if this tag type numeric
   * @since 4.0.0
   */
  boolean numeric();

  /**
   * Reads a tag.
   *
   * @param input the input
   * @return the tag
   * @throws IOException if an exception was encountered while reading
   * @since 4.0.0
   */
  T read(final DataInput input) throws IOException;

  /**
   * Writes a tag.
   *
   * @param tag the tag
   * @param output the output
   * @throws IOException if an exception was encountered while writing
   * @since 4.0.0
   */
  void write(final T tag, final DataOutput output) throws IOException;

  /**
   * A binary tag reader.
   *
   * @param <T> the tag type
   * @since 4.0.0
   */
  interface Reader<T extends BinaryTag> {
    /**
     * Reads a tag.
     *
     * @param input the input
     * @return the tag
     * @throws IOException if an exception was encountered while reading
     * @since 4.0.0
     */
    T read(final DataInput input) throws IOException;
  }

  /**
   * A binary tag writer.
   *
   * @param <T> the tag type
   * @since 4.0.0
   */
  interface Writer<T extends BinaryTag> {
    /**
     * Writes a tag.
     *
     * @param tag the tag to write
     * @param output the output to write to
     * @throws IOException if an exception was encountered while writing
     * @since 4.0.0
     */
    void write(final T tag, final DataOutput output) throws IOException;
  }
}
