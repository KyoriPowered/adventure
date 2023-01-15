/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.function.Function;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.MutableStyleSetter;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component builder.
 *
 * @param <C> the component type
 * @param <B> the builder type
 * @since 4.0.0
 */
@ApiStatus.NonExtendable
public interface ComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends AbstractBuilder<C>, Buildable.Builder<C>, ComponentBuilderApplicable, ComponentLike, MutableStyleSetter<B> {
  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B append(final @NotNull Component component);

  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NotNull B append(final @NotNull ComponentLike component) {
    return this.append(component.asComponent());
  }

  /**
   * Appends a component to this component.
   *
   * @param builder the component to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NotNull B append(final @NotNull ComponentBuilder<?, ?> builder) {
    return this.append(builder.build());
  }

  /**
   * Appends components to this component.
   *
   * @param components the components to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B append(final @NotNull Component@NotNull... components);

  /**
   * Appends components to this component.
   *
   * @param components the components to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B append(final @NotNull ComponentLike@NotNull... components);

  /**
   * Appends components to this component.
   *
   * @param components the components to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B append(final @NotNull Iterable<? extends ComponentLike> components);

  /**
   * Appends a newline to this component.
   *
   * @return this builder
   * @since 4.12.0
   */
  default @NotNull B appendNewline() {
    return this.append(Component.newline());
  }

  /**
   * Appends a space to this component.
   *
   * @return this builder
   * @since 4.12.0
   */
  default @NotNull B appendSpace() {
    return this.append(Component.space());
  }

  /**
   * Applies an action to this builder.
   *
   * @param consumer the action
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NotNull B apply(final @NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
    consumer.accept(this);
    return (B) this;
  }

  /**
   * Applies an action to this component and all child components if they are
   * an instance of {@link BuildableComponent}.
   *
   * @param action the action
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B applyDeep(final @NotNull Consumer<? super ComponentBuilder<?, ?>> action);

  /**
   * Replaces each child of this component with the resultant component from the function.
   *
   * @param function the mapping function
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B mapChildren(final @NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

  /**
   * Replaces each child and sub-child of this component with the resultant
   * component of the function.
   *
   * @param function the mapping function
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B mapChildrenDeep(final @NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

  /**
   * Get an unmodifiable list containing all children currently in this builder.
   *
   * @return the list of children
   * @since 4.6.0
   */
  @NotNull List<Component> children();

  /**
   * Sets the style.
   *
   * @param style the style
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B style(final @NotNull Style style);

  /**
   * Configures the style.
   *
   * @param consumer the style consumer
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B style(final @NotNull Consumer<Style.Builder> consumer);

  /**
   * Sets the font of this component.
   *
   * @param font the font
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B font(final @Nullable Key font);

  /**
   * Sets the color of this component.
   *
   * @param color the color
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B color(final @Nullable TextColor color);

  /**
   * Sets the color of this component if there isn't one set already.
   *
   * @param color the color
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B colorIfAbsent(final @Nullable TextColor color);

  /**
   * Sets the state of a set of decorations to {@code flag} on this component.
   *
   * @param decorations the decorations
   * @param flag {@code true} if this component should have the decorations, {@code false} if
   *     this component should not have the decorations
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_, _ -> this")
  @Override
  default @NotNull B decorations(final @NotNull Set<TextDecoration> decorations, final boolean flag) {
    return MutableStyleSetter.super.decorations(decorations, flag);
  }

  /**
   * Sets the state of {@code decoration} to {@link TextDecoration.State#TRUE}.
   *
   * @param decoration the decoration
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  default @NotNull B decorate(final @NotNull TextDecoration decoration) {
    return this.decoration(decoration, TextDecoration.State.TRUE);
  }

  /**
   * Sets {@code decorations} to {@link TextDecoration.State#TRUE}.
   *
   * @param decorations the decorations
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  default @NotNull B decorate(final @NotNull TextDecoration@NotNull... decorations) {
    return MutableStyleSetter.super.decorate(decorations);
  }

  /**
   * Sets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this component should have the decoration, {@code false} if
   *     this component should not have the decoration
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_, _ -> this")
  @Override
  default @NotNull B decoration(final @NotNull TextDecoration decoration, final boolean flag) {
    return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
  }

  /**
   * Sets decorations for this component's style using the specified {@code decorations} map.
   *
   * <p>If a given decoration does not have a value explicitly set, the value of that particular decoration is not changed.</p>
   *
   * @param decorations a map containing text decorations and their respective state.
   * @return this builder
   * @since 4.10.0
   */
  @Contract("_ -> this")
  @Override
  default @NotNull B decorations(final @NotNull Map<TextDecoration, TextDecoration.State> decorations) {
    return MutableStyleSetter.super.decorations(decorations);
  }

  /**
   * Sets the value of a decoration on this component.
   *
   * @param decoration the decoration
   * @param state {@link TextDecoration.State#TRUE} if this component should have the
   *     decoration, {@link TextDecoration.State#FALSE} if this component should not
   *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
   *     should not have a set value
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_, _ -> this")
  @Override
  @NotNull B decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state);

  /**
   * Sets the state of a decoration on this component to {@code state} if the current state of
   * the decoration is {@link TextDecoration.State#NOT_SET}.
   *
   * @param decoration the decoration
   * @param state the state
   * @return this builder
   * @since 4.12.0
   */
  @Contract("_, _ -> this")
  @Override
  @NotNull B decorationIfAbsent(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state);

  /**
   * Sets the click event of this component.
   *
   * @param event the click event
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B clickEvent(final @Nullable ClickEvent event);

  /**
   * Sets the hover event of this component.
   *
   * @param source the hover event source
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B hoverEvent(final @Nullable HoverEventSource<?> source);

  /**
   * Sets the string to be inserted when this component is shift-clicked.
   *
   * @param insertion the insertion string
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @Override
  @NotNull B insertion(final @Nullable String insertion);

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NotNull B mergeStyle(final @NotNull Component that) {
    return this.mergeStyle(that, Style.Merge.all());
  }

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @param merges the parts to merge
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_, _ -> this")
  default @NotNull B mergeStyle(final @NotNull Component that, final Style.@NotNull Merge@NotNull... merges) {
    return this.mergeStyle(that, Style.Merge.merges(merges));
  }

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @param merges the parts to merge
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_, _ -> this")
  @NotNull B mergeStyle(final @NotNull Component that, final @NotNull Set<Style.Merge> merges);

  /**
   * Resets all styling on this component.
   *
   * @return this builder
   * @since 4.0.0
   */
  @NotNull B resetStyle();

  /**
   * Build a component.
   *
   * @return the component
   */
  @Override
  @NotNull C build();

  /**
   * Applies {@code applicable}.
   *
   * @param applicable the thing to apply
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NotNull B applicableApply(final @NotNull ComponentBuilderApplicable applicable) {
    applicable.componentBuilderApply(this);
    return (B) this;
  }

  @Override
  default void componentBuilderApply(final @NotNull ComponentBuilder<?, ?> component) {
    component.append(this);
  }

  @Override
  default @NotNull Component asComponent() {
    return this.build();
  }
}
