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

import java.util.Iterator;
import java.util.List;

/**
 * A list iterator, that loops after specified index.
 *
 * @param <E> type of the iterated elements
 * @since 4.9.2
 */
public final class LoopedListIterator<E> implements Iterator<E> {

  /**
   * Creates new iterator with given properties.
   *
   * @param list a list to iterate over
   * @param loopIndex index after which the loop begins
   * @param <E> type of the iterated elements
   * @return new instance with given properties
   * @since 4.9.2
   */
  public static <E> Iterator<E> iterator(final List<E> list, final int loopIndex) {
    return new LoopedListIterator<>(list, loopIndex);
  }

  private final List<E> list;

  private final int loopIndex;

  private int index = -1;

  private LoopedListIterator(final List<E> list, final int loopIndex) {
    this.list = list;
    this.loopIndex = loopIndex;
    if (loopIndex >= list.size() || loopIndex < 0)
      throw new IllegalArgumentException("Loop index must be positive number less than list size");
  }

  /**
   * This iterator is looped, so it always has next element.
   *
   * @return always true
   * @since 4.9.2
   */
  @Override
  public boolean hasNext() {
    return true;
  }

  /**
   * Finds the next element.
   *
   * <p>If in the ends of list iterator jumps to loop index.</p>
   *
   * @return next element in list or element on loop index when nothing have been found.
   * @since 4.9.2
   */
  @Override
  public E next() {
    this.index++;
    if (this.index == this.list.size())
      this.index = this.loopIndex;

    return this.list.get(this.index);
  }

}
