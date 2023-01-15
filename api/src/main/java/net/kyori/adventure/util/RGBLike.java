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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Something that can provide red, green, and blue colour components.
 *
 * @since 4.0.0
 */
public interface RGBLike {
  /**
   * Gets the red component.
   *
   * @return the red component
   * @since 4.0.0
   */
  @Range(from = 0x0, to = 0xff) int red();

  /**
   * Gets the green component.
   *
   * @return the green component
   * @since 4.0.0
   */
  @Range(from = 0x0, to = 0xff) int green();

  /**
   * Gets the blue component.
   *
   * @return the blue component
   * @since 4.0.0
   */
  @Range(from = 0x0, to = 0xff) int blue();

  /**
   * Converts the color represented by this RGBLike to the HSV color space.
   *
   * @return an HSVLike representing this RGBLike in the HSV color space
   * @since 4.6.0
   */
  default @NotNull HSVLike asHSV() {
    return HSVLike.fromRGB(this.red(), this.green(), this.blue());
  }
}
