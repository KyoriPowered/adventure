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
package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A text component.
 */
public interface Component {
  /**
   * Gets the unmodifiable list of children.
   *
   * @return the unmodifiable list of children
   */
  @NonNull List<Component> children();

  /**
   * Sets the list of children.
   *
   * <p>The contents of {@code children} will be copied.</p>
   *
   * @param children the children
   * @return the unmodifiable list of children
   */
  @NonNull Component children(final @NonNull List<Component> children);

  /**
   * Checks if this component contains a component.
   *
   * @param that the other component
   * @return {@code true} if this component contains the provided
   *     component, {@code false} otherwise
   */
  default boolean contains(final @NonNull Component that) {
    if(this == that) return true;
    for(final Component child : this.children()) {
      if(child.contains(that)) return true;
    }
    if(this.hoverEvent() != null) {
      final Component hover = this.hoverEvent().value();
      if(that == hover) return true;
      for(final Component child : hover.children()) {
        if(child.contains(that)) return true;
      }
    }
    return false;
  }

  /**
   * Prevents a cycle between this component and the provided component.
   *
   * @param that the other component
   */
  default void detectCycle(final @NonNull Component that) {
    if(that.contains(this)) {
      throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
    }
  }

  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return a component with the component added
   */
  @NonNull Component append(final @NonNull Component component);

  /**
   * Appends a component to this component.
   *
   * @param builder the component to append
   * @return a component with the component added
   */
  default @NonNull Component append(final @NonNull ComponentBuilder<?, ?> builder) {
    return this.append(builder.build());
  }

  /**
   * Gets the style of this component.
   *
   * @return the style of this component
   */
  @NonNull Style style();

  /**
   * Sets the style of this component.
   *
   * @param style the style
   * @return a component
   */
  @NonNull Component style(final @NonNull Style style);

  /**
   * Merges from another style into this component's style.
   *
   * @param that the other style
   * @return a component
   */
  default @NonNull Component mergeStyle(final @NonNull Component that) {
    return this.mergeStyle(that, Style.Merge.all());
  }

  /**
   * Merges from another style into this component's style.
   *
   * @param that the other style
   * @param merges the style parts to merge
   * @return a component
   */
  default @NonNull Component mergeStyle(final @NonNull Component that, final Style.@NonNull Merge@NonNull... merges) {
    return this.mergeStyle(that, Style.Merge.of(merges));
  }

  /**
   * Merges from another style into this component's style.
   *
   * @param that the other style
   * @param merges the style parts to merge
   * @return a component
   */
  default @NonNull Component mergeStyle(final @NonNull Component that, final @NonNull Set<Style.Merge> merges) {
    return this.style(this.style().merge(that.style(), merges));
  }

  /**
   * Gets the color of this component.
   *
   * @return the color of this component
   */
  default @Nullable TextColor color() {
    return this.style().color();
  }

  /**
   * Sets the color of this component.
   *
   * @param color the color
   * @return a component
   */
  default @NonNull Component color(final @Nullable TextColor color) {
    return this.style(this.style().color(color));
  }

  /**
   * Tests if this component has a decoration.
   *
   * @param decoration the decoration
   * @return {@code true} if this component has the decoration, {@code false} if this
   *     component does not have the decoration
   */
  default boolean hasDecoration(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Gets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @return {@link TextDecoration.State#TRUE} if this component has the decoration,
   *     {@link TextDecoration.State#FALSE} if this component does not have the decoration,
   *     and {@link TextDecoration.State#NOT_SET} if not set
   */
  default TextDecoration.@NonNull State decoration(final @NonNull TextDecoration decoration) {
    return this.style().decoration(decoration);
  }

  /**
   * Sets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this component should have the decoration, {@code false} if
   *     this component should not have the decoration
   * @return a component
   */
  default @NonNull Component decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
  }

  /**
   * Sets the value of a decoration on this component.
   *
   * @param decoration the decoration
   * @param state {@link TextDecoration.State#TRUE} if this component should have the
   *     decoration, {@link TextDecoration.State#FALSE} if this component should not
   *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
   *     should not have a set value
   * @return a component
   */
  default @NonNull Component decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    return this.style(this.style().decoration(decoration, state));
  }

  /**
   * Gets a set of decorations this component has.
   *
   * @return a set of decorations this component has
   */
  default @NonNull Set<TextDecoration> decorations() {
    return this.decorations(Collections.emptySet());
  }

  /**
   * Gets a set of decorations this component has.
   *
   * @param defaultValues a set of default values
   * @return a set of decorations this component has
   */
  default @NonNull Set<TextDecoration> decorations(final @NonNull Set<TextDecoration> defaultValues) {
    return this.style().decorations(defaultValues);
  }

  /**
   * Gets the click event of this component.
   *
   * @return the click event
   */
  default @Nullable ClickEvent clickEvent() {
    return this.style().clickEvent();
  }

  /**
   * Sets the click event of this component.
   *
   * @param event the click event
   * @return a component
   */
  default @NonNull Component clickEvent(final @Nullable ClickEvent event) {
    return this.style(this.style().clickEvent(event));
  }

  /**
   * Gets the hover event of this component.
   *
   * @return the hover event
   */
  default @Nullable HoverEvent hoverEvent() {
    return this.style().hoverEvent();
  }

  /**
   * Sets the hover event of this component.
   *
   * @param event the hover event
   * @return a component
   */
  default @NonNull Component hoverEvent(final @Nullable HoverEvent event) {
    if(event != null) this.detectCycle(event.value()); // detect cycle before modifying
    return this.style(this.style().hoverEvent(event));
  }

  /**
   * Gets the string to be inserted when this component is shift-clicked.
   *
   * @return the insertion string
   */
  default @Nullable String insertion() {
    return this.style().insertion();
  }

  /**
   * Sets the string to be inserted when this component is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a component
   */
  default @NonNull Component insertion(final @Nullable String insertion) {
    return this.style(this.style().insertion(insertion));
  }

  /**
   * Merges the color from another component into this component.
   *
   * @param that the other component
   * @return a component
   * @deprecated use {@link #mergeStyle(Component, Set)}
   */
  @Deprecated
  default @NonNull Component mergeColor(final @NonNull Component that) {
    return this.style(this.style().mergeColor(that.style()));
  }

  /**
   * Merges the decorations from another component into this component.
   *
   * @param that the other component
   * @return a component
   * @deprecated use {@link #mergeStyle(Component, Set)}
   */
  @Deprecated
  default @NonNull Component mergeDecorations(final @NonNull Component that) {
    return this.style(this.style().mergeDecorations(that.style()));
  }

  /**
   * Merges the events from another component into this component.
   *
   * @param that the other component
   * @return a component
   * @deprecated use {@link #mergeStyle(Component, Set)}
   */
  @Deprecated
  default @NonNull Component mergeEvents(final @NonNull Component that) {
    return this.style(this.style().mergeEvents(that.style()));
  }

  /**
   * Tests if this component has any styling.
   *
   * @return {@code true} if this component has any styling, {@code false} if this
   *     component does not have any styling
   */
  default boolean hasStyling() {
    return !this.style().isEmpty();
  }
}
