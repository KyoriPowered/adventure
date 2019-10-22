/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.event;

import net.kyori.text.Component;
import net.kyori.text.util.NameMap;
import net.kyori.text.util.ShadyPines;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 */
public final class HoverEvent {
  /**
   * Creates a hover event.
   *
   * @param action the action
   * @param value the value
   * @return a click event
   */
  public static @NonNull HoverEvent of(final @NonNull Action action, final @NonNull Component value) {
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
  public static @NonNull HoverEvent showItem(final @NonNull Component item) {
    return of(Action.SHOW_ITEM, item);
  }

  /**
   * Creates a hover event that shows an entity on hover.
   *
   * @param entity the entity to show on hover
   * @return a hover event
   */
  public static @NonNull HoverEvent showEntity(final @NonNull Component entity) {
    return of(Action.SHOW_ENTITY, entity);
  }

  /**
   * The hover event action.
   */
  private final Action action;
  /**
   * The hover event value.
   */
  private final Component value;

  private HoverEvent(final @NonNull Action action, final @NonNull Component value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   */
  public @NonNull Action action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   */
  public @NonNull Component value() {
    return this.value;
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
  public String toString() {
    return ShadyPines.toString(this, map -> {
      map.put("action", this.action);
      map.put("value", this.value);
    });
  }

  /**
   * An enumeration of hover event actions.
   */
  public enum Action {
    /**
     * Shows a {@link Component} when hovered over.
     */
    SHOW_TEXT("show_text", true),
    /**
     * Shows an item instance when hovered over.
     */
    SHOW_ITEM("show_item", true),
    /**
     * Shows an entity when hovered over.
     */
    SHOW_ENTITY("show_entity", true);

    /**
     * The name map.
     */
    public static final NameMap<Action> NAMES = NameMap.create(values(), constant -> constant.name);
    /**
     * The name of this action.
     */
    private final String name;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;

    Action(final String name, final boolean readable) {
      this.name = name;
      this.readable = readable;
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
