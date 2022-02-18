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
package net.kyori.adventure.animation.util;

/**
 * Greatest common divisor calculation utility class.
 *
 * @since 1.10.0
 */
public interface GCDCalc {

  /**
   * Calculates the Greatest Common Divisor of two longs.
   *
   * @param n1 first long
   * @param n2 second long
   * @return GCD
   *
   * @since 1.10.0
   */
  static long gcd(final long n1, final long n2) {
    long a = n1;
    long b = n2;

    while (a != 0) {
      final long a2 = a;
      a = b % a;
      b = a2;
    }

    if (b == 0)
      throw new ArithmeticException("No GCD for " + n1 + " and " + n2 + ".");

    return Math.abs(b);
  }

}
