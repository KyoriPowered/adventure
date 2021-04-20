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

final class ComponentIterator implements Iterator<Component> {
  private Component component;
  private final ComponentIteratorType type;
  private final Deque<Component> queue;

  ComponentIterator(final @NonNull Component component, final @NonNull ComponentIteratorType type) {
    this.component = Objects.requireNonNull(component, "component");
    this.type = Objects.requireNonNull(type, "type");
    this.queue = new ArrayDeque<>();
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
        this.addChildren(children);
      }

      return next;
    } else {
      if(this.queue.isEmpty()) throw new NoSuchElementException();
      this.component = this.queue.poll();
      return this.next();
    }
  }

  private void addChildren(final @NonNull List<Component> children) {
    switch(this.type) {
      case DEPTH_FIRST:
        for(int i = children.size() - 1; i >= 0; i--) {
          this.queue.addFirst(children.get(i));
        }
        break;
      case BREADTH_FIRST:
        this.queue.addAll(children);
        break;
    }
  }
}
