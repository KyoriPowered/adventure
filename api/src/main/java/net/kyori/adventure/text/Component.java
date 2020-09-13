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
import java.util.regex.Pattern;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.IntFunction2;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A component.
 *
 * @see BlockNBTComponent
 * @see EntityNBTComponent
 * @see KeybindComponent
 * @see ScoreComponent
 * @see SelectorComponent
 * @see StorageNBTComponent
 * @see TextComponent
 * @see TranslatableComponent
 * @since 4.0.0
 */
public interface Component extends ComponentBuilderApplicable, ComponentLike, HoverEventSource<Component> {
  /**
   * Gets the unmodifiable list of children.
   *
   * @return the unmodifiable list of children
   * @since 4.0.0
   */
  @NonNull List<Component> children();

  /**
   * Sets the list of children.
   *
   * <p>The contents of {@code children} will be copied.</p>
   *
   * @param children the children
   * @return the unmodifiable list of children
   * @since 4.0.0
   */
  @NonNull Component children(final @NonNull List<? extends ComponentLike> children);

  /**
   * Checks if this component contains a component.
   *
   * @param that the other component
   * @return {@code true} if this component contains the provided
   *     component, {@code false} otherwise
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @since 4.0.0
   */
  @NonNull Component append(final @NonNull Component component);

  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return a component with the component added
   * @since 4.0.0
   */
  default @NonNull Component append(final @NonNull ComponentLike component) {
    return this.append(component.asComponent());
  }

  /**
   * Appends a component to this component.
   *
   * @param builder the component to append
   * @return a component with the component added
   * @since 4.0.0
   */
  default @NonNull Component append(final @NonNull ComponentBuilder<?, ?> builder) {
    return this.append(builder.build());
  }

  /**
   * Gets the style of this component.
   *
   * @return the style of this component
   * @since 4.0.0
   */
  @NonNull Style style();

  /**
   * Sets the style of this component.
   *
   * @param style the style
   * @return a component
   * @since 4.0.0
   */
  @NonNull Component style(final @NonNull Style style);

  /**
   * Sets the style of this component.
   *
   * @param consumer the style consumer
   * @return a component
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @NonNull Component style(final @NonNull Consumer<Style.Builder> consumer, final Style.Merge.@NonNull Strategy strategy) {
    return this.style(this.style().edit(consumer, strategy));
  }

  /**
   * Sets the style of this component.
   *
   * @param style the style
   * @return a component
   * @since 4.0.0
   */
  default @NonNull Component style(final Style.@NonNull Builder style) {
    return this.style(style.build());
  }

  /**
   * Merges from another style into this component's style.
   *
   * @param that the other style
   * @return a component
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @NonNull Component mergeStyle(final @NonNull Component that, final @NonNull Set<Style.Merge> merges) {
    return this.style(this.style().merge(that.style(), merges));
  }

  /**
   * Gets the color of this component.
   *
   * @return the color of this component
   * @since 4.0.0
   */
  default @Nullable TextColor color() {
    return this.style().color();
  }

  /**
   * Sets the color of this component.
   *
   * @param color the color
   * @return a component
   * @since 4.0.0
   */
  default @NonNull Component color(final @Nullable TextColor color) {
    return this.style(this.style().color(color));
  }

  /**
   * Sets the color if there isn't one set already.
   *
   * @param color the color
   * @return this builder
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default boolean hasDecoration(final @NonNull TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Sets the state of {@code decoration} to {@link TextDecoration.State#TRUE} on this component.
   *
   * @param decoration the decoration
   * @return a component
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @NonNull Component decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    return this.style(this.style().decoration(decoration, state));
  }

  /**
   * Gets a set of decorations this component has.
   *
   * @return a set of decorations this component has
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @NonNull Component decorations(final @NonNull Map<TextDecoration, TextDecoration.State> decorations) {
    return this.style(this.style().decorations(decorations));
  }

  /**
   * Gets the click event of this component.
   *
   * @return the click event
   * @since 4.0.0
   */
  default @Nullable ClickEvent clickEvent() {
    return this.style().clickEvent();
  }

  /**
   * Sets the click event of this component.
   *
   * @param event the click event
   * @return a component
   * @since 4.0.0
   */
  default @NonNull Component clickEvent(final @Nullable ClickEvent event) {
    return this.style(this.style().clickEvent(event));
  }

  /**
   * Gets the hover event of this component.
   *
   * @return the hover event
   * @since 4.0.0
   */
  default @Nullable HoverEvent<?> hoverEvent() {
    return this.style().hoverEvent();
  }

  /**
   * Sets the hover event of this component.
   *
   * @param source the hover event source
   * @return a component
   * @since 4.0.0
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
   * @since 4.0.0
   */
  default @Nullable String insertion() {
    return this.style().insertion();
  }

  /**
   * Sets the string to be inserted when this component is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a component
   * @since 4.0.0
   */
  default @NonNull Component insertion(final @Nullable String insertion) {
    return this.style(this.style().insertion(insertion));
  }

  /**
   * Tests if this component has any styling.
   *
   * @return {@code true} if this component has any styling, {@code false} if this
   *     component does not have any styling
   * @since 4.0.0
   */
  default boolean hasStyling() {
    return !this.style().isEmpty();
  }

  /**
   * Finds and replaces text within any {@link TextComponent}s using a regex pattern.
   *
   * @param pattern a regex pattern
   * @param replacement a function to replace each match
   * @return a modified copy of this component
   * @since 4.0.0
   */
  default @NonNull Component replaceText(final @NonNull Pattern pattern, final @NonNull UnaryOperator<TextComponent.Builder> replacement) {
    return this.replaceText(pattern, replacement, (index, replaced) -> PatternReplacementResult.REPLACE);
  }

  /**
   * Finds and replaces the first occurrence of text within any {@link TextComponent}s using a regex pattern.
   *
   * @param pattern a regex pattern
   * @param replacement a function to replace the first match
   * @return a modified copy of this component
   * @since 4.0.0
   */
  default @NonNull Component replaceFirstText(final @NonNull Pattern pattern, final @NonNull UnaryOperator<TextComponent.Builder> replacement) {
    return this.replaceText(pattern, replacement, 1);
  }

  /**
   * Finds and replaces {@code n} instances of text within any {@link TextComponent}s using a regex pattern.
   *
   * @param pattern a regex pattern
   * @param replacement a function to replace each match
   * @param numberOfReplacements the amount of matches that should be replaced
   * @return a modified copy of this component
   * @since 4.0.0
   */
  default @NonNull Component replaceText(final @NonNull Pattern pattern, final @NonNull UnaryOperator<TextComponent.Builder> replacement, final int numberOfReplacements) {
    return this.replaceText(pattern, replacement, (index, replaced) -> replaced < numberOfReplacements ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
  }

  /**
   * Finds and replaces text using a regex pattern.
   *
   * <p>Utilises an {@link IntFunction2} to determine if each instance should be replaced.</p>
   *
   * @param pattern a regex pattern
   * @param replacement a function to replace the first match
   * @param fn a function of (index, replaced) used to determine if matches should be replaced, where "replaced" is the number of successful replacements
   * @return a modified copy of this component
   * @since 4.0.0
   */
  @NonNull Component replaceText(final @NonNull Pattern pattern, final @NonNull UnaryOperator<TextComponent.Builder> replacement, final @NonNull IntFunction2<PatternReplacementResult> fn);

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
