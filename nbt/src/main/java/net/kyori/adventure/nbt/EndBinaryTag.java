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
package net.kyori.adventure.nbt;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An end tag.
 *
 * @since 4.0.0
 */
public interface EndBinaryTag extends BinaryTag {
  /**
   * Gets the end tag.
   *
   * @return the end tag
   * @since 4.0.0
   */
  static @NonNull EndBinaryTag get() {
    return EndBinaryTagImpl.INSTANCE;
  }

  @Override
  default @NonNull BinaryTagType<EndBinaryTag> type() {
    return BinaryTagTypes.END;
  }
}

final class EndBinaryTagImpl extends AbstractBinaryTag implements EndBinaryTag {
  static final EndBinaryTagImpl INSTANCE = new EndBinaryTagImpl();

  @Override
  public boolean equals(final Object that) {
    return this == that;
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
