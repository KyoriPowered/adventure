/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * A component builder.
 *
 * @param <C> the component type
 * @param <B> the builder type
 * @deprecated since 4.8.0 for removal, in favour of {@link Component.AbstractBuilder}
 * @since 4.0.0
 */
@Deprecated
public interface ComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends Buildable.Builder<C>, Component.AbstractBuilder<C, B>, ComponentBuilderApplicable, ComponentLike {
  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NonNull B append(final @NonNull Component component) {
    return this.append((ComponentLike) component);
  }

  /**
   * Appends a component to this component.
   *
   * @param builder the component to append
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NonNull B append(final @NonNull ComponentBuilder<?, ?> builder) {
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
  @NonNull B append(final @NonNull Component@NonNull... components);

  /**
   * Applies an action to this builder.
   *
   * @param consumer the action
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NonNull B apply(final @NonNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
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
  @NonNull B applyDeep(final @NonNull Consumer<? super ComponentBuilder<?, ?>> action);

  /**
   * Replaces each child of this component with the resultant component from the function.
   *
   * @param function the mapping function
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NonNull B mapChildren(final @NonNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

  /**
   * Replaces each child and sub-child of this component with the resultant
   * component of the function.
   *
   * @param function the mapping function
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NonNull B mapChildrenDeep(final @NonNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

  /**
   * Get an unmodifiable list containing all children currently in this builder.
   *
   * @return the list of children
   * @since 4.6.0
   */
  @NonNull List<Component> children();

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  default @NonNull B mergeStyle(final @NonNull Component that) {
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
  default @NonNull B mergeStyle(final @NonNull Component that, final Style.@NonNull Merge@NonNull... merges) {
    return this.mergeStyle(that, Style.Merge.of(merges));
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
  @NonNull B mergeStyle(final @NonNull Component that, final @NonNull Set<Style.Merge> merges);

  /**
   * Resets all styling on this component.
   *
   * @return this builder
   * @since 4.0.0
   */
  @Contract("-> this")
  @NonNull B resetStyle();

  /**
   * Build a component.
   *
   * @return the component
   */
  @Override
  @NonNull C build();

  /**
   * Applies {@code applicable}.
   *
   * @param applicable the thing to apply
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NonNull B applicableApply(final @NonNull ComponentBuilderApplicable applicable) {
    applicable.componentBuilderApply(this);
    return (B) this;
  }

  @Override
  default void componentBuilderApply(final @NonNull ComponentBuilder<?, ?> component) {
    component.append(this);
  }
}
