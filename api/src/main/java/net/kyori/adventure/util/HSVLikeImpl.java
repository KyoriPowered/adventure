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
package net.kyori.adventure.util;

import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;

final class HSVLikeImpl implements HSVLike {
  private final float h;
  private final float s;
  private final float v;

  HSVLikeImpl(final float h, final float s, final float v) {
    requireInsideRange(h, "h");
    requireInsideRange(s, "s");
    requireInsideRange(v, "v");
    this.h = h;
    this.s = s;
    this.v = v;
  }

  @Override
  public float h() {
    return this.h;
  }

  @Override
  public float s() {
    return this.s;
  }

  @Override
  public float v() {
    return this.v;
  }

  private static void requireInsideRange(final float number, final String name) throws IllegalArgumentException {
    if (number < 0 || 1 < number) {
      throw new IllegalArgumentException(
        name + " (" + number + ")" + " is not inside the required range: [0,1]");
    }
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof HSVLikeImpl)) return false;
    final HSVLikeImpl that = (HSVLikeImpl) other;
    return ShadyPines.equals(that.h, this.h) && ShadyPines.equals(that.s, this.s) && ShadyPines.equals(that.v, this.v);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.h, this.s, this.v);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }
}
