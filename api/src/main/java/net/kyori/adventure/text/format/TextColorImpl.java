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
package net.kyori.adventure.text.format;

import org.checkerframework.common.value.qual.IntRange;

public final class TextColorImpl implements TextColor {
  private final int value;

  TextColorImpl(final int value) {
    this.value = value;
  }

  TextColorImpl(final @IntRange(from = 0, to = 0xff) int r, final @IntRange(from = 0, to = 0xff) int g, final @IntRange(from = 0, to = 0xff) int b) {
    this((r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff));
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof TextColorImpl)) return false;
    final TextColorImpl that = (TextColorImpl) other;
    return this.value == that.value;
  }

  @Override
  public int hashCode() {
    return this.value;
  }

  @Override
  public String toString() {
    return "#" + Integer.toString(this.value, 16);
  }
}
