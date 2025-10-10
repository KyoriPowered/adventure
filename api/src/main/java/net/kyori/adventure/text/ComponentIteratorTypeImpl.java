package net.kyori.adventure.text;

import java.util.Deque;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

class ComponentIteratorTypeImpl {
  private ComponentIteratorTypeImpl() {
  }

  static final class DepthFirst implements ComponentIteratorType {
    static final ComponentIteratorType INSTANCE = new DepthFirst();

    @Override
    public void populate(@NotNull Component component, @NotNull Deque<Component> deque, @NotNull Set<ComponentIteratorFlag> flags) {
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
    public void populate(@NotNull Component component, @NotNull Deque<Component> deque, @NotNull Set<ComponentIteratorFlag> flags) {
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
