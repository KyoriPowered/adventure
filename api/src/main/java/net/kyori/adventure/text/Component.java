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
package net.kyori.adventure.text;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A text component.
 */
public interface Component extends ComponentBuilderApplicable, ComponentLike, HoverEventSource<Component> {
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
  @NonNull Component children(final @NonNull List<? extends ComponentLike> children);

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
    final /* @Nullable */ HoverEvent<?> hoverEvent = this.hoverEvent();
    if(hoverEvent != null) {
      if(hoverEvent.action().type().isAssignableFrom(Component.class)) {
        final Component hover = (Component) hoverEvent.value();
        if(that == hover) return true;
        for(final Component child : hover.children()) {
          if(child.contains(that)) return true;
        }
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
   * @param component the component to append
   * @return a component with the component added
   */
  default @NonNull Component append(final @NonNull ComponentLike component) {
    return this.append(component.asComponent());
  }

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
   * Sets the style of this component.
   *
   * @param consumer the style consumer
   * @return a component
   */
  default @NonNull Component style(final @NonNull Consumer<Style.Builder> consumer) {
    return this.style(this.style().edit(consumer));
  }

  /**
   * Sets the style of this component.
   *
   * @param consumer the style consumer
   * @param strategy the merge strategy
   * @return a component
   */
  default @NonNull Component style(final @NonNull Consumer<Style.Builder> consumer, final Style.Merge.@NonNull Strategy strategy) {
    return this.style(this.style().edit(consumer, strategy));
  }

  /**
   * Sets the style of this component.
   *
   * @param style the style
   * @return a component
   */
  default @NonNull Component style(final Style.@NonNull Builder style) {
    return this.style(style.build());
  }

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
   * Sets the color if there isn't one set already.
   *
   * @param color the color
   * @return this builder
   */
  default @NonNull Component colorIfAbsent(final @Nullable TextColor color) {
    if(this.color() == null) return this.color(color);
    return this;
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
   * Sets the state of {@code decoration} to {@link TextDecoration.State#TRUE} on this component.
   *
   * @param decoration the decoration
   * @return a component
   */
  default @NonNull Component decorate(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration, TextDecoration.State.TRUE);
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
  default @NonNull Map<TextDecoration, TextDecoration.State> decorations() {
    return this.style().decorations();
  }

  /**
   * Sets decorations for this component's style using the specified {@code decorations} map.
   *
   * <p>If a given decoration does not have a value explicitly set, the value of that particular decoration is not changed.</p>
   *
   * @param decorations a set of default values
   * @return a component
   */
  default @NonNull Component decorations(final @NonNull Map<TextDecoration, TextDecoration.State> decorations) {
    return this.style(this.style().decorations(decorations));
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
  default @Nullable HoverEvent<?> hoverEvent() {
    return this.style().hoverEvent();
  }

  /**
   * Sets the hover event of this component.
   *
   * @param source the hover event source
   * @return a component
   */
  default @NonNull Component hoverEvent(final @Nullable HoverEventSource<?> source) {
    final HoverEvent<?> event = HoverEventSource.unbox(source);
    if(event != null) {
      if(event.action().type().isAssignableFrom(Component.class)) {
        this.detectCycle((Component) event.value()); // detect cycle before modifying
      }
    }
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
   * Tests if this component has any styling.
   *
   * @return {@code true} if this component has any styling, {@code false} if this
   *     component does not have any styling
   */
  default boolean hasStyling() {
    return !this.style().isEmpty();
  }

  @Override
  default void componentBuilderApply(final @NonNull ComponentBuilder<?, ?> component) {
    component.append(this);
  }

  @Override
  default @NonNull Component asComponent() {
    return this;
  }

  @Override
  default @NonNull HoverEvent<Component> asHoverEvent(final @NonNull UnaryOperator<Component> op) {
    return HoverEvent.showText(op.apply(this));
  }
}
