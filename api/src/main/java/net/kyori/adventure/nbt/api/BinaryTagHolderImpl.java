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
package net.kyori.adventure.nbt.api;

import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class BinaryTagHolderImpl implements BinaryTagHolder {
  private final String string;

  BinaryTagHolderImpl(final String string) {
    this.string = requireNonNull(string, "string");
  }

  @Override
  public @NotNull String string() {
    return this.string;
  }

  @Override
  public <T, DX extends Exception> @NotNull T get(final @NotNull Codec<T, String, DX, ?> codec) throws DX {
    return codec.decode(this.string);
  }

  @Override
  public int hashCode() {
    return 31 * this.string.hashCode();
  }

  @Override
  public boolean equals(final Object that) {
    if (!(that instanceof BinaryTagHolderImpl)) {
      return false;
    }

    return this.string.equals(((BinaryTagHolderImpl) that).string);
  }

  @Override
  public String toString() {
    return this.string;
  }
}
