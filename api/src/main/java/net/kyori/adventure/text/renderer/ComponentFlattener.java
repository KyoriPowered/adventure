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
package net.kyori.adventure.text.renderer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A 'flattener' to convert a component tree to a linear string for display.
 *
 * @since 4.7.0
 */
public interface ComponentFlattener extends Buildable<ComponentFlattener, ComponentFlattener.Builder> {
  /**
   * Create a new builder for a flattener.
   *
   * @return a new builder
   * @since 4.7.0
   */
  static @NonNull Builder builder() {
    return new ComponentFlattenerImpl.Builder();
  }

  /**
   * A basic flattener that will only print information directly contained in components.
   *
   * @return a basic flattener
   * @since 4.7.0
   */
  static @NonNull ComponentFlattener basic() {
    return ComponentFlattenerImpl.BASIC;
  }

  /**
   * A component flattener that will only handle text components.
   *
   * <p>All other component types will not be included in the output.</p>
   *
   * @return a text-only flattener
   * @since 4.7.0
   */
  static @NonNull ComponentFlattener textOnly() {
    return ComponentFlattenerImpl.TEXT_ONLY;
  }

  /**
   * Perform a flattening on the component, providing output to the {@code listener}.
   *
   * @param input the component to be flattened
   * @param listener the listener that will receive flattened component state
   * @since 4.7.0
   */
  void flatten(final @NonNull Component input, final @NonNull FlattenerListener listener);

  /**
   * A builder for a component flattener.
   *
   * <p>A new builder will start out empty, providing empty strings for all component types.</p>
   *
   * @since 4.7.0
   */
  interface Builder extends Buildable.Builder<ComponentFlattener> {
    /**
     * Register a type of component to be handled.
     *
     * @param type the component type
     * @param converter the converter to map that component to a string
     * @param <T> component type
     * @return this builder
     * @since 4.7.0
     * @see #complexType(Class, BiConsumer) for component types that are too complex to be directly rendered to a string
     */
    <T extends Component> @NonNull Builder type(final @NonNull Class<T> type, final @NonNull Function<T, String> converter);

    /**
     * Register a type of component that needs to be flattened to an intermediate stage.
     *
     * @param type the component type
     * @param converter a provider of contained Components
     * @param <T> component type
     * @return this builder
     * @since 4.7.0
     */
    <T extends Component> @NonNull Builder complexType(final @NonNull Class<T> type, final @NonNull BiConsumer<T, Consumer<Component>> converter);

    /**
     * Register a handler for unknown component types.
     *
     * <p>This will be called if no other converter can be found.</p>
     *
     * @param converter the converter, may be null to ignore unknown components
     * @return this builder
     * @since 4.7.0
     */
    @NonNull Builder unknownHandler(final @Nullable Function<Component, String> converter);
  }
}
