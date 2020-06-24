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
package net.kyori.adventure.util;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Various utilities.
 */
public final class ShadyPines {
  private ShadyPines() {
  }

  /**
   * Creates a set from an array of enum constants.
   *
   * @param type the enum type
   * @param constants the enum constants
   * @param <E> the enum type
   * @return the set
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <E extends Enum<E>> @NonNull Set<E> enumSet(final Class<E> type, final E@NonNull... constants) {
    final Set<E> set = EnumSet.noneOf(type);
    Collections.addAll(set, constants);
    return Collections.unmodifiableSet(set);
  }

  /**
   * Checks if {@code a} is equal to {@code b}.
   *
   * @param a a double
   * @param b a double
   * @return {@code true} if {@code a} is equal to {@code b}, otherwise {@code false}
   */
  public static boolean equals(final double a, final double b) {
    return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
  }

  /**
   * Checks if {@code a} is equal to {@code b}.
   *
   * @param a a float
   * @param b a float
   * @return {@code true} if {@code a} is equal to {@code b}, otherwise {@code false}
   */
  public static boolean equals(final float a, final float b) {
    return Float.floatToIntBits(a) == Float.floatToIntBits(b);
  }

  public static int floor(final double n) {
    final int i = (int) n;
    return n < (double) i ? i - 1 : i;
  }

  public static int floor(final float n) {
    final int i = (int) n;
    return n < (float) i ? i - 1 : i;
  }

  /**
   * Wrap an existing iterator so it cannot be modified
   *
   * @param orig The original iterator
   * @param <T> value type
   * @return wrapped iterator delegating all operations except {@link Iterator#remove()} to {@code orig}
   */
  public static <T> Iterator<T> unmodifiableIterator(final Iterator<? extends T> orig)  {
    return new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return orig.hasNext();
      }

      @Override
      public T next() {
        return  orig.next();
      }

      @Override
      public void forEachRemaining(final Consumer<? super T> action) {
        orig.forEachRemaining(action);
      }
    };
  }
}
