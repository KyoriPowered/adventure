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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Collection} related utilities.
 *
 * @since 4.8.0
 */
public final class MonkeyBars {
  private MonkeyBars() {
  }

  /**
   * Creates a set from an array of enum constants.
   *
   * @param type the enum type
   * @param constants the enum constants
   * @param <E> the enum type
   * @return the set
   * @since 4.0.0
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <E extends Enum<E>> @NotNull Set<E> enumSet(final Class<E> type, final E@NotNull... constants) {
    final Set<E> set = EnumSet.noneOf(type);
    Collections.addAll(set, constants);
    return Collections.unmodifiableSet(set);
  }

  /**
   * Adds an element to the end of the list, or returns a new list.
   *
   * <p>The returned list is unmodifiable.</p>
   *
   * @param oldList the old list
   * @param newElement the element to add
   * @param <T> the element type
   * @return a list
   * @since 4.8.0
   */
  public static <T> @NotNull List<T> addOne(final @NotNull List<T> oldList, final T newElement) {
    if (oldList.isEmpty()) return Collections.singletonList(newElement);
    final List<T> newList = new ArrayList<>(oldList.size() + 1);
    newList.addAll(oldList);
    newList.add(newElement);
    return Collections.unmodifiableList(newList);
  }
}
