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
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.event.HoverEvent;

class ComponentIteratorTypeImpl {
  private ComponentIteratorTypeImpl() {
  }

  static final class DepthFirst implements ComponentIteratorType {
    static final ComponentIteratorType INSTANCE = new DepthFirst();

    @Override
    public void populate(Component component, Deque<Component> deque, Set<ComponentIteratorFlag> flags) {
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent translatable) {
        final List<? extends ComponentLike> args = translatable.arguments();

        for (int i = args.size() - 1; i >= 0; i--) {
          deque.addFirst(args.get(i).asComponent());
        }
      }

      final HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
        final HoverEvent.Action<?> action = hoverEvent.action();

        if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
          deque.addFirst(((HoverEvent.ShowEntity) hoverEvent.value()).name());
        } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
          deque.addFirst((Component) hoverEvent.value());
        }
      }

      final List<Component> children = component.children();
      for (int i = children.size() - 1; i >= 0; i--) {
        deque.addFirst(children.get(i));
      }
    }
  }

  static final class BreadthFirst implements ComponentIteratorType {
    static final ComponentIteratorType INSTANCE = new BreadthFirst();

    @Override
    public void populate(Component component, Deque<Component> deque, Set<ComponentIteratorFlag> flags) {
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
        for (final TranslationArgument argument : ((TranslatableComponent) component).arguments()) {
          deque.add(argument.asComponent());
        }
      }

      final HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
        final HoverEvent.Action<?> action = hoverEvent.action();

        if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
          deque.addLast(((HoverEvent.ShowEntity) hoverEvent.value()).name());
        } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
          deque.addLast((Component) hoverEvent.value());
        }
      }

      deque.addAll(component.children());
    }
  }
}
