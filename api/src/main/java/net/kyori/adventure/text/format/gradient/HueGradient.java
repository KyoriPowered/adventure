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

import org.jetbrains.annotations.NotNull;

/**
 * A HueGradient is a gradient whose {@code ColorSpace} involves some form of hue coordinate, which needs
 * to be specially interpolated.
 *
 * @see <a href="https://www.w3.org/TR/css-color-4/#hue-interpolation">W3C CSS Color Level 4 spec on hue interpolation</a>
 *
 * @since 4.15.0
 */
public abstract class HueGradient implements Gradient {

  private final HueGradient.@NotNull HueInterpolationMethod hueInterpolationMethod;

  protected HueGradient(final HueGradient.@NotNull HueInterpolationMethod hueInterpolationMethod) {
    this.hueInterpolationMethod = hueInterpolationMethod;
  }

  /**
   * Interpolates the hue, using the given method.
   *
   * <p>{@code b} need not be greater than or equal to @{code a}.</p>
   *
   * @param t the interpolation value, between {@code 0.0} and {@code 1.0} (both inclusive)
   * @param method the hue interpolation method
   * @param a the start hue
   * @param b the end hue
   *
   * @return the hue interpolated as specified, constrained to 0.0 <= {@code hue} < 1.0.
   *
   * @since 4.15.0
   */
  protected float interpolateHue(final float t,
                                 final HueGradient.@NotNull HueInterpolationMethod method,
                                 final float a,
                                 final float b) {
    float aClamped = ((a % 1.0f) + 1.0f) % 1.0f;
    float bClamped = ((b % 1.0f) + 1.0f) % 1.0f;

    switch (method) {
      case INCREASING: {
        if (bClamped < aClamped) {
          bClamped += 1.0f;
        }
        break;
      }
      case DECREASING: {
        if (aClamped < bClamped) {
          aClamped += 1.0f;
        }
        break;
      }
      case LONGER: {
        if ((bClamped - aClamped) < 0.5f) {
          bClamped += 1.0f;
        } else if ((bClamped - aClamped) < -0.5f) {
          aClamped += 1.0f;
        }
        break;
      }
      default:
      case SHORTER: {
        if ((bClamped - aClamped) > 0.5f) {
          aClamped += 1.0f;
        } else if ((bClamped - aClamped) < 0.5f) {
          bClamped += 1.0f;
        }
        break;
      }
    }

    final float hueInterpolated = aClamped + t * (bClamped - aClamped);

    return ((hueInterpolated % 1.0f) + 1.0f) % 1.0f;
  }

  /**
   * Gets the hue interpolation method.
   *
   * @return the hue interpolation method
   *
   * @since 4.15.0
   */
  public HueGradient.@NotNull HueInterpolationMethod hueInterpolationMethod() {
    return this.hueInterpolationMethod;
  }

  /**
   * Represents the methods in which the hue component of a color may be interpolated.
   *
   * @since 4.15.0
   */
  public enum HueInterpolationMethod {
    /**
     * Interpolates between the shorter of the two possible angles around a circle between hues.
     */
    SHORTER,
    /**
     * Interpolates between the longer of the two possible angles around a circle between hues.
     */
    LONGER,
    /**
     * Always interpolates in the direction of increasing hue ( 0.5 -> 0.75 -> 1.0 -> 0.25 )
     */
    INCREASING,
    /**
     * Always interpolates in the direction of decreasing hue ( 0.5 -> 0.25 -> 0.0 -> 0.75 )
     */
    DECREASING
  }
}
