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
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * Holds a compound binary tag.
 */
public interface BinaryTagHolder {
  /*
   * Instead of including an entire NBT implementation in adventure-api, we have decided to
   * use this "holder" interface instead, allowing for either our own NBT API to be used, or
   * one from a specific platform (when possible).
   */

  /**
   * Encodes {@code nbt} using {@code codec}.
   *
   * @param nbt the binary tag
   * @param codec the codec
   * @param <T> the binary tag type
   * @return the encoded binary tag holder
   * @throws IOException if an error occurred while encoding the binary tag
   */
  static <T> @NonNull BinaryTagHolder encode(final @NonNull T nbt, final @NonNull Codec<T> codec) throws IOException {
    return new BinaryTagHolderImpl(codec.writeBinaryTag(nbt));
  }

  /**
   * Creates an encoded binary tag holder.
   *
   * @param string the encoded binary tag value
   * @return the encoded binary tag
   */
  static @NonNull BinaryTagHolder of(final @NonNull String string) {
    return new BinaryTagHolderImpl(string);
  }

  /**
   * Gets the raw string value.
   *
   * @return the raw string value
   */
  @NonNull String string();

  /**
   * Gets the held value as a binary tag.
   *
   * @param codec the codec
   * @param <T> the binary tag type
   * @return the binary tag
   * @throws IOException if an error occurred while retrieving the binary tag
   */
  <T> @NonNull T get(final @NonNull Codec<T> codec) throws IOException;

  /**
   * Something that can read and write a binary tag from a {@link String}.
   */
  interface Codec<T> {
    /**
     * Reads a binary tag from a {@link String}.
     *
     * @param string the string
     * @return the binary tag
     * @throws IOException if an error occurred while reading
     */
    @NonNull T readBinaryTag(final @NonNull String string) throws IOException;

    /**
     * Writes a binary tag to a {@link String}.
     *
     * @param nbt the binary tag
     * @return the string
     * @throws IOException if an error occurred while reading
     */
    @NonNull String writeBinaryTag(final @NonNull T nbt) throws IOException;
  }
}

/* package */ final class BinaryTagHolderImpl implements BinaryTagHolder {
  private final String string;

  /* package */ BinaryTagHolderImpl(final String string) {
    this.string = requireNonNull(string, "string");
  }

  @Override
  public @NonNull String string() {
    return this.string;
  }

  @Override
  public <T> @NonNull T get(final @NonNull Codec<T> codec) throws IOException {
    return codec.readBinaryTag(this.string);
  }
}
