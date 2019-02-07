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
package net.kyori.text.format;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public interface Styled<R, M> {
  /**
   * Gets the color of this component.
   *
   * @return the color of this component
   */
  @Nullable TextColor color();

  /**
   * Sets the color of this component.
   *
   * @param color the color
   * @return a component
   */
  @NonNull R color(final @Nullable TextColor color);

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
  TextDecoration.@NonNull State decoration(final @NonNull TextDecoration decoration);

  /**
   * Sets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this component should have the decoration, {@code false} if
   *     this component should not have the decoration
   * @return a component
   */
  default @NonNull R decoration(final @NonNull TextDecoration decoration, final boolean flag) {
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
  @NonNull R decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state);

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
    final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
    for(final TextDecoration decoration : TextDecoration.values()) {
      final TextDecoration.State value = this.decoration(decoration);
      if(value == TextDecoration.State.TRUE || (value == TextDecoration.State.NOT_SET && defaultValues.contains(decoration))) {
        decorations.add(decoration);
      }
    }
    return decorations;
  }

  /**
   * Gets the click event of this component.
   *
   * @return the click event
   */
  @Nullable ClickEvent clickEvent();

  /**
   * Sets the click event of this component.
   *
   * @param event the click event
   * @return a component
   */
  @NonNull R clickEvent(final @Nullable ClickEvent event);

  /**
   * Gets the hover event of this component.
   *
   * @return the hover event
   */
  @Nullable HoverEvent hoverEvent();

  /**
   * Sets the hover event of this component.
   *
   * @param event the hover event
   * @return a component
   */
  @NonNull R hoverEvent(final @Nullable HoverEvent event);

  /**
   * Gets the string to be inserted when this component is shift-clicked.
   *
   * @return the insertion string
   */
  @Nullable String insertion();

  /**
   * Sets the string to be inserted when this component is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a component
   */
  @NonNull R insertion(final @Nullable String insertion);

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @NonNull R mergeStyle(final @NonNull M that);

  /**
   * Merges the color from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @NonNull R mergeColor(final @NonNull M that);

  /**
   * Merges the decorations from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @NonNull R mergeDecorations(final @NonNull M that);

  /**
   * Merges the events from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @NonNull R mergeEvents(final @NonNull M that);

  /**
   * Resets all styling on this component.
   *
   * @return a component
   */
  @NonNull R resetStyle();

  /**
   * Tests if this component has any styling.
   *
   * @return {@code true} if this component has any styling, {@code false} if this
   *     component does not have any styling
   */
  boolean hasStyling();
}
