/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.util;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A combination encoder and decoder.
 *
 * @param <D> the decoded type
 * @param <E> the encoded type
 * @param <DX> the exception type
 * @since 4.0.0
 */
public interface Codec<D, E, DX extends Throwable, EX extends Throwable> {
  /**
   * Creates a codec.
   *
   * @param decoder the decoder
   * @param encoder the encoder
   * @param <D> the decoded type
   * @param <E> the encoded type
   * @param <DX> the decode exception type
   * @param <EX> the encode exception type
   * @return a codec
   * @since 4.0.0
   */
  static <D, E, DX extends Throwable, EX extends Throwable> @NonNull Codec<D, E, DX, EX> of(final @NonNull Decoder<D, E, DX> decoder, final @NonNull Encoder<D, E, EX> encoder) {
    return new Codec<D, E, DX, EX>() {
      @Override
      public @NonNull D decode(final @NonNull E encoded) throws DX {
        return decoder.decode(encoded);
      }

      @Override
      public @NonNull E encode(final @NonNull D decoded) throws EX {
        return encoder.encode(decoded);
      }
    };
  }

  /**
   * Decodes.
   *
   * @param encoded the encoded input
   * @return the decoded value
   * @throws DX if an exception is encountered while decoding
   * @since 4.0.0
   */
  @NonNull D decode(final @NonNull E encoded) throws DX;

  /**
   * A decoder.
   *
   * @param <D> the decoded type
   * @param <E> the encoded type
   * @param <X> the exception type
   * @since 4.0.0
   */
  interface Decoder<D, E, X extends Throwable> {
    /**
     * Decodes.
     *
     * @param encoded the encoded input
     * @return the decoded value
     * @throws X if an exception is encountered while decoding
     * @since 4.0.0
     */
    @NonNull D decode(final @NonNull E encoded) throws X;
  }

  /**
   * Encodes.
   *
   * @param decoded the decoded value
   * @return the encoded output
   * @throws EX if an exception is encountered while encoding
   * @since 4.0.0
   */
  @NonNull E encode(final @NonNull D decoded) throws EX;

  /**
   * An encoder.
   *
   * @param <D> the decoded type
   * @param <E> the encoded type
   * @param <X> the exception type
   * @since 4.0.0
   */
  interface Encoder<D, E, X extends Throwable> {
    /**
     * Encodes.
     *
     * @param decoded the decoded value
     * @return the encoded output
     * @throws X if an exception is encountered while encoding
     * @since 4.0.0
     */
    @NonNull E encode(final @NonNull D decoded) throws X;
  }
}
