/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2018 KyoriPowered
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

import com.google.common.base.Enums;
import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import net.kyori.blizzard.NonNull;
import net.kyori.text.Component;

import java.util.Objects;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 */
public final class HoverEvent {
  /**
   * The hover event action.
   */
  @NonNull private final Action action;
  /**
   * The hover event value.
   */
  @NonNull private final Component value;

  public HoverEvent(@NonNull final Action action, @NonNull final Component value) {
    this.action = action;
    this.value = value;
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   */
  @NonNull
  public Action action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   */
  @NonNull
  public Component value() {
    return this.value;
  }

  /**
   * Creates a copy of this hover event.
   *
   * @return a copy of this hover event
   */
  @NonNull
  public HoverEvent copy() {
    return new HoverEvent(this.action, this.value.copy());
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final HoverEvent that = (HoverEvent) other;
    return this.action == that.action && Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.action, this.value);
  }

  @NonNull
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("action", this.action)
      .add("value", this.value)
      .toString();
  }

  /**
   * An enumeration of hover event actions.
   */
  public enum Action {
    /**
     * Shows a {@link Component} when hovered over.
     */
    @SerializedName("show_text")
    SHOW_TEXT(true),
    /**
     * Shows an item instance when hovered over.
     */
    @SerializedName("show_item")
    SHOW_ITEM(true),
    /**
     * Shows an entity when hovered over.
     */
    @SerializedName("show_entity")
    SHOW_ENTITY(true);

    /**
     * The serialized name of this action.
     */
    @NonNull private final String toString = Enums.getField(this).getAnnotation(SerializedName.class).value();
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserailized.</p>
     */
    private final boolean readable;

    Action(final boolean readable) {
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

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     * @deprecated use {@link #readable()}
     */
    @Deprecated
    public boolean isReadable() {
      return this.readable;
    }

    @NonNull
    @Override
    public String toString() {
      return this.toString;
    }
  }
}
