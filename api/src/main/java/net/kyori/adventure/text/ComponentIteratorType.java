/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.Deque;
import java.util.Set;

/**
 * The iterator types.
 *
 * @see Component#iterator(ComponentIteratorType, Set)
 * @see Component#iterable(ComponentIteratorType, Set)
 * @see Component#spliterator(ComponentIteratorType, Set)
 * @since 4.9.0
 */
public sealed interface ComponentIteratorType permits ComponentIteratorTypeImpl.BreadthFirst, ComponentIteratorTypeImpl.DepthFirst {
  /**
   * A depth-first iteration.
   *
   * @since 4.9.0
   */
  ComponentIteratorType DEPTH_FIRST = ComponentIteratorTypeImpl.DepthFirst.INSTANCE;
  /**
   * A breadth-first iteration.
   *
   * @since 4.9.0
   */
  ComponentIteratorType BREADTH_FIRST = ComponentIteratorTypeImpl.BreadthFirst.INSTANCE;

  /**
   * Populates a deque with the children of the provided component, based on the iterator type and flags.
   *
   * @param component the component
   * @param deque the deque
   * @param flags the flags
   * @since 4.9.0
   */
  void populate(final Component component, final Deque<Component> deque, final Set<ComponentIteratorFlag> flags);
}
