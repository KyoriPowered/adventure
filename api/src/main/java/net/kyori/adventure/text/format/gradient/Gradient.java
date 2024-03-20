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
package net.kyori.adventure.text.format.gradient;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

/**
 * A Gradient is capable of interpolating between two colors.
 *
 * @since 4.15.0
 */

@FunctionalInterface
public interface Gradient {

  /**
   * Returns a {@link net.kyori.adventure.text.Component} with a gradient applied in
   * the given colorspace to the {@code string}.
   *
   * @param string the string to be colorized. May be empty.
   * @param a the start color
   * @param b the end color
   * @param gradient the gradient to use to apply the color
   *
   * @return the component with the specified gradient
   *
   * @since 4.15.0
   */
  static @NotNull Component applyGradient(
    final @NotNull String string,
    final @NotNull TextColor a,
    final @NotNull TextColor b,
    final @NotNull Gradient gradient
  ) {
    final TextComponent.@NotNull Builder builder = Component.text();
    for (int i = 0; i < string.length(); i++) {
      builder.append(Component.text(
        string.charAt(i),
        gradient.interpolate((float) i / string.length(), a, b)
      ));
    }
    return builder.build();
  }



  /**
   * Interpolates in the given colorspace, between {@code a} and {@code b} by {@code t}.
   *
   * <p>This returns a color blended between color {@code a}, at {@code t=0.0}, and color {@code b}, at {@code t=1.0}.</p>
   * <p>Linear interpolation is not guaranteed.</p>
   * <p>
   *   At {@code t=0.0}, the returned text color may be equivalent to {@code a} but not necessarily the same instance.
   *   The same goes for {@code b} when {@code t=1.0}.
   * </p>
   *
   * @param t the interpolation value, between {@code 0.0} and {@code 1.0} (both inclusive)
   * @param a the lower bound ({@code t=0.0})
   * @param b the upper bound ({@code t=1.0})
   * @return the interpolated value, a color between the two input colors {@code a} and {@code b}
   *
   * @since 4.15.0
   */
  @NotNull TextColor interpolate(final float t, final @NotNull TextColor a, final @NotNull TextColor b);
}
