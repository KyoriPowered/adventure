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
package net.kyori.adventure.text;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An iterator that traverses a component and it's children.
 * <p>As components are immutable, this iterator does not support removal.</p>
 *
 * @see Component#iterator()
 * @since 4.8.0
 */
public final class ComponentIterator implements Iterator<Component> {
  private final Deque<Component> queue;
  private final boolean bfs;
  private Component component;

  /**
   * Creates an iterable for a component with a given type.
   *
   * @param component the component
   * @param type the type
   * @return the iterable
   * @since 4.8.0
   */
  public static Iterable<Component> iterable(final @NonNull Component component, final @NonNull Type type) {
    return () -> iterator(component, type);
  }

  /**
   * Creates an iterator on a component with a given type.
   *
   * @param component the component
   * @param type the type
   * @return the iterable
   * @since 4.8.0
   */
  public static Iterator<Component> iterator(final @NonNull Component component, final @NonNull Type type) {
    return new ComponentIterator(component, type);
  }

  private ComponentIterator(final @NonNull Component component, final @NonNull Type type) {
    this.component = Objects.requireNonNull(component, "component");
    this.queue = new ArrayDeque<>();
    this.bfs = Objects.requireNonNull(type, "type") == Type.BREADTH_FIRST;
  }

  @Override
  public boolean hasNext() {
    return this.component != null || !this.queue.isEmpty();
  }

  @Override
  public Component next() {
    if(this.component != null) {
      final Component next = this.component;
      this.component = null;

      final List<Component> children = next.children();
      if(!children.isEmpty()) {
        if(this.bfs) {
          this.queue.addAll(next.children());
        } else {
          for(int i = children.size() - 1; i >= 0; i--) {
            this.queue.addFirst(children.get(i));
          }
        }
      }

      return next;
    } else {
      if(this.queue.isEmpty()) throw new NoSuchElementException();
      this.component = this.queue.poll();
      return this.next();
    }
  }

  /**
   * The iterator types.
   *
   * @since 4.8.0
   */
  public enum Type {
    /**
     * A depth first search.
     *
     * @since 4.8.0
     */
    DEPTH_FIRST,

    /**
     * A breadth first search.
     *
     * @since 4.8.0
     */
    BREADTH_FIRST;
  }
}
