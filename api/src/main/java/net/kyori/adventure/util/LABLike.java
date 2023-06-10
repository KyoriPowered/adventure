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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Something that can provide lightness (L*), green/magenta (a*), and blue/yellow (b*) color components.
 * (CIE LAB colorspace)
 *
 * <p>Lightness must be in the inclusive range of 0 to 1.0.</p>
 *
 * @since 4.6.0
 */
public interface LABLike extends Examinable {

  /**
   * Creates a new LABLike.
   *
   * @param lightness lightness (L*) component
   * @param a the a (a*) color component
   * @param b the b (b*) color component
   * @return a new HSVLike
   * @since 4.10.0
   */
  static @NotNull LABLike labLike(final double lightness, final double a, final double b) {
    return new LabLikeImpl(lightness, a, b);
  }

  /**
   * Gets the lightness (L*) component.
   *
   * @return the lightness (L*) component
   * @since 4.15.0
   */
  double lightness();

  /**
   * Gets the green/magenta (a*) component.
   *
   * @return the green/magenta (a*) component
   * @since 4.15.0
   */
  double a();

  /**
   * Gets the green/magenta (a*) component.
   *
   * @return the green/magenta (a*) component
   * @since 4.15.0
   */
  double b();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("lightness", this.lightness()),
      ExaminableProperty.of("a", this.a()),
      ExaminableProperty.of("b", this.b())
    );
  }
}
