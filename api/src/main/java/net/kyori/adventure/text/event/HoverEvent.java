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
package net.kyori.adventure.text.event;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.NameMap;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 *
 * @param <V> the value type
 */
public final class HoverEvent<V> implements Examinable {
  /**
   * Creates a hover event.
   *
   * @param action the action
   * @param value the value
   * @param <V> the value type
   * @return a click event
   */
  public static <V> @NonNull HoverEvent<V> of(final @NonNull Action<V> action, final @NonNull V value) {
    return new HoverEvent<>(action, value);
  }

  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent<Component> showText(final @NonNull Component text) {
    return of(Action.SHOW_TEXT, text);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull ShowItem item) {
    return of(Action.SHOW_ITEM, item);
  }

  /**
   * Creates a hover event that shows an entity on hover.
   *
   * @param entity the entity to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull ShowEntity entity) {
    return of(Action.SHOW_ENTITY, entity);
  }

  /**
   * The hover event action.
   */
  private final Action<V> action;
  /**
   * The hover event value.
   */
  private final V value;

  private HoverEvent(final @NonNull Action<V> action, final @NonNull V value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   */
  public @NonNull Action<V> action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   */
  public @NonNull V value() {
    return this.value;
  }

  /**
   * Returns a hover event with the value rendered using {@code renderer} when possible.
   *
   * @param renderer the renderer
   * @param context the render context
   * @param <C> the context type
   * @return a hover event
   */
  public <C> @NonNull HoverEvent<V> withRenderedValue(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context) {
    final V oldValue = this.value;
    final V newValue = this.action.renderer.render(renderer, context, oldValue);
    if(newValue != oldValue) return new HoverEvent<>(this.action, newValue);
    return this;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final HoverEvent<?> that = (HoverEvent<?>) other;
    return this.action == that.action && this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    int result = this.action.hashCode();
    result = (31 * result) + this.value.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public String toString() {
    return StringExaminer.simpleEscaping().examine(this);
  }

  public static final class ShowItem implements Examinable {
    private final Key item;
    private final int count;
    // TODO: nbt

    public ShowItem(final @NonNull Key item, final @NonNegative int count) {
      this.item = item;
      this.count = count;
    }

    /**
     * Gets the item.
     *
     * @return the item
     */
    public @NonNull Key item() {
      return this.item;
    }

    /**
     * Gets the count.
     *
     * @return the count
     */
    public @NonNegative int count() {
      return this.count;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final ShowItem that = (ShowItem) other;
      return this.item.equals(that.item) && this.count == that.count;
    }

    @Override
    public int hashCode() {
      int result = this.item.hashCode();
      result = (31 * result) + Integer.hashCode(this.count);
      return result;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("item", this.item),
        ExaminableProperty.of("count", this.count)
      );
    }
  }

  public static final class ShowEntity implements Examinable {
    private final Key type;
    private final UUID id;
    private final Component name;

    public ShowEntity(final @NonNull Key type, final @NonNull UUID id, final @Nullable Component name) {
      this.type = type;
      this.id = id;
      this.name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public @NonNull Key type() {
      return this.type;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public @NonNull UUID id() {
      return this.id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public @Nullable Component name() {
      return this.name;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final ShowEntity that = (ShowEntity) other;
      return this.type.equals(that.type) && this.id.equals(that.id) && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
      int result = this.type.hashCode();
      result = (31 * result) + this.id.hashCode();
      result = (31 * result) + Objects.hashCode(this.name);
      return result;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("type", this.type),
        ExaminableProperty.of("id", this.id),
        ExaminableProperty.of("name", this.name)
      );
    }
  }

  /**
   * An enumeration of hover event actions.
   */
  public static final class Action<V> {
    /**
     * Shows a {@link Component} when hovered over.
     */
    public static final Action<Component> SHOW_TEXT = new Action<>("show_text", Component.class, true, new Renderer<Component>() {
      @Override
      public <C> @NonNull Component render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull Component value) {
        return renderer.render(value, context);
      }
    });
    /**
     * Shows an item instance when hovered over.
     */
    public static final Action<ShowItem> SHOW_ITEM = new Action<>("show_item", ShowItem.class, true, new Renderer<ShowItem>() {
      @Override
      public <C> @NonNull ShowItem render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull ShowItem value) {
        return value;
      }
    });
    /**
     * Shows an entity when hovered over.
     */
    public static final Action<ShowEntity> SHOW_ENTITY = new Action<>("show_entity", ShowEntity.class, true, new Renderer<ShowEntity>() {
      @Override
      public <C> @NonNull ShowEntity render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull ShowEntity value) {
        if(value.name == null) return value;
        return new ShowEntity(value.type, value.id, renderer.render(value.name, context));
      }
    });

    /**
     * The name map.
     */
    public static final NameMap<Action<?>> NAMES = NameMap.create(constant -> constant.name, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY);
    /**
     * The name of this action.
     */
    private final String name;
    /**
     * The value type.
     */
    private final Class<V> type;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;
    private final Renderer<V> renderer;

    Action(final String name, final Class<V> type, final boolean readable, final Renderer<V> renderer) {
      this.name = name;
      this.type = type;
      this.readable = readable;
      this.renderer = renderer;
    }

    /**
     * Gets the value type.
     *
     * @return the value type
     */
    public @NonNull Class<V> type() {
      return this.type;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     */
    public boolean readable() {
      return this.readable;
    }

    @Override
    public @NonNull String toString() {
      return this.name;
    }

    @FunctionalInterface
    interface Renderer<V> {
      <C> @NonNull V render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull V value);
    }
  }
}
