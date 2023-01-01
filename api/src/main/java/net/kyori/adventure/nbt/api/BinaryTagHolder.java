/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.nbt.api;

import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Holds a compound binary tag.
 *
 * <p>Instead of including an entire NBT implementation in Adventure, it was decided to
 * use this "holder" interface instead. This opens the door for platform specific implementations.</p>
 *
 * <p>See {@code net.kyori.adventure.nbt.impl} for a platform agnostic implementation.</p>
 *
 * @since 4.0.0
 */
public interface BinaryTagHolder {
  /**
   * Encodes {@code nbt} using {@code codec}.
   *
   * @param nbt the binary tag
   * @param codec the codec
   * @param <T> the binary tag type
   * @param <EX> encode exception type
   * @return the encoded binary tag holder
   * @throws EX if an error occurred while encoding the binary tag
   * @since 4.0.0
   */
  static <T, EX extends Exception> @NotNull BinaryTagHolder encode(final @NotNull T nbt, final @NotNull Codec<? super T, String, ?, EX> codec) throws EX {
    return new BinaryTagHolderImpl(codec.encode(nbt));
  }

  /**
   * Creates an encoded binary tag holder.
   *
   * @param string the encoded binary tag value
   * @return the encoded binary tag
   * @since 4.10.0
   */
  static @NotNull BinaryTagHolder binaryTagHolder(final @NotNull String string) {
    return new BinaryTagHolderImpl(string);
  }

  /**
   * Creates an encoded binary tag holder.
   *
   * @param string the encoded binary tag value
   * @return the encoded binary tag
   * @since 4.0.0
   * @deprecated for removal since 4.10.0, use {@link #binaryTagHolder(String)} instead.
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  static @NotNull BinaryTagHolder of(final @NotNull String string) {
    return new BinaryTagHolderImpl(string);
  }

  /**
   * Gets the raw string value.
   *
   * @return the raw string value
   * @since 4.0.0
   */
  @NotNull String string();

  /**
   * Gets the held value as a binary tag.
   *
   * @param codec the codec
   * @param <T> the binary tag type
   * @param <DX> decode thrown exception type
   * @return the binary tag
   * @throws DX if an error occurred while retrieving the binary tag
   * @since 4.0.0
   */
  <T, DX extends Exception> @NotNull T get(final @NotNull Codec<T, String, DX, ?> codec) throws DX;
}
