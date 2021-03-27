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

import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Something that can provide x, y, and z components.
 *
 * @since 4.8.0
 */
public interface Vector3dLike extends Examinable {
  /**
   * Creates a new CoordinateLike.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a new CoordinateLike
   * @since 4.8.0
   */
  static Vector3dLike of(final double x, final double y, final double z) {
    return new Vector3dLikeImpl(x, y, z);
  }

  /**
   * Gets the x coordinate.
   *
   * @return the x coordinate
   * @since 4.8.0
   */
  double x();

  /**
   * Gets the y coordinate.
   *
   * @return the y coordinate
   * @since 4.8.0
   */
  double y();

  /**
   * Gets the y coordinate.
   *
   * @return the y coordinate
   * @since 4.8.0
   */
  double z();

  @Override
  default @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("x", this.x()),
      ExaminableProperty.of("y", this.y()),
      ExaminableProperty.of("z", this.z())
    );
  }
}
