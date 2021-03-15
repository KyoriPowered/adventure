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
  public static Iterable<Component> iterable(@NonNull Component component, @NonNull Type type) {
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
  public static Iterator<Component> iterator(@NonNull Component component, @NonNull Type type) {
    return new ComponentIterator(component, type);
  }

  private ComponentIterator(@NonNull Component component, @NonNull Type type) {
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
