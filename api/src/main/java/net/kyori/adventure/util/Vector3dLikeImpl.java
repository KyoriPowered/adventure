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

import java.util.Objects;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.Nullable;

final class Vector3dLikeImpl implements Vector3dLike {
  private final double x;
  private final double y;
  private final double z;

  Vector3dLikeImpl(final double x, final double y, final double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public double x() {
    return this.x;
  }

  @Override
  public double y() {
    return this.y;
  }

  @Override
  public double z() {
    return this.z;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof Vector3dLikeImpl)) return false;
    final Vector3dLikeImpl that = (Vector3dLikeImpl) other;
    return ShadyPines.equals(that.z, this.z) && ShadyPines.equals(that.y, this.y) && ShadyPines.equals(that.z, this.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y, this.z);
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
