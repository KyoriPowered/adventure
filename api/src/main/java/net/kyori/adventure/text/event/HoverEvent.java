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

import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.NameMap;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 */
public final class HoverEvent implements Examinable {
  /**
   * Creates a hover event.
   *
   * @param action the action
   * @param value the value
   * @return a click event
   */
  public static <V> @NonNull HoverEvent of(final @NonNull Action<V> action, final @NonNull V value) {
    return new HoverEvent(action, value);
  }

  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent showText(final @NonNull Component text) {
    return of(Action.SHOW_TEXT, text);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent showItem(final @NonNull ShowItem item) {
    return of(Action.SHOW_ITEM, item);
  }

  /**
   * Creates a hover event that shows an entity on hover.
   *
   * @param entity the entity to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent showEntity(final @NonNull ShowEntity entity) {
    return of(Action.SHOW_ENTITY, entity);
  }

  /**
   * The hover event action.
   */
  private final Action<?> action;
  /**
   * The hover event value.
   */
  private final Object value;

  private HoverEvent(final @NonNull Action<?> action, final @NonNull Object value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   */
  public @NonNull Action<?> action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   */
  public <V> @NonNull V value(final Action<V> action) {
    return action == this.action ? (V) this.value : null; // TODO
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final HoverEvent that = (HoverEvent) other;
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

  public static class ShowItem {
  }

  public static class ShowEntity {
  }

  /**
   * An enumeration of hover event actions.
   */
  public static final class Action<V> {
    /**
     * Shows a {@link Component} when hovered over.
     */
    public static final Action<Component> SHOW_TEXT = new Action<>("show_text", Component.class, true);
    /**
     * Shows an item instance when hovered over.
     */
    public static final Action<ShowItem> SHOW_ITEM = new Action<>("show_item", ShowItem.class, true);
    /**
     * Shows an entity when hovered over.
     */
    public static final Action<ShowEntity> SHOW_ENTITY = new Action<>("show_entity", ShowEntity.class, true);

    /**
     * The name map.
     */
    public static final NameMap<Action<?>> NAMES = NameMap.create(constant -> constant.name, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY);
    /**
     * The name of this action.
     */
    private final String name;
    private final Class<V> type;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;

    Action(final String name, final Class<V> type, final boolean readable) {
      this.name = name;
      this.type = type;
      this.readable = readable;
    }

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
  }
}
