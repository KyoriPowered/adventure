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

import java.util.function.Consumer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A selector component.
 *
 * @since 4.0.0
 */
public interface SelectorComponent extends BuildableComponent<SelectorComponent, SelectorComponent.Builder>, ScopedComponent<SelectorComponent> {
  /**
   * Creates a selector component builder.
   *
   * @return a builder
   * @since 4.0.0
   */
  static @NonNull Builder builder() {
    return new SelectorComponentImpl.BuilderImpl();
  }

  /**
   * Creates a selector component builder with a pattern.
   *
   * @param pattern the selector pattern
   * @return a builder
   * @since 4.0.0
   */
  static @NonNull Builder builder(final @NonNull String pattern) {
    return builder().pattern(pattern);
  }

  /**
   * Creates a selector component with a pattern.
   *
   * @param pattern the selector pattern
   * @return a selector component
   * @since 4.0.0
   */
  static @NonNull SelectorComponent of(final @NonNull String pattern) {
    return builder(pattern).build();
  }

  /**
   * Creates a selector component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return a selector component
   * @since 4.0.0
   */
  static @NonNull SelectorComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Creates a selector component by applying configuration from {@code consumer}.
   *
   * @param pattern the selector pattern
   * @param consumer the builder configurator
   * @return a selector component
   * @since 4.0.0
   */
  static @NonNull SelectorComponent make(final @NonNull String pattern, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder(pattern);
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Gets the selector pattern.
   *
   * @return the selector pattern
   * @since 4.0.0
   */
  @NonNull String pattern();

  /**
   * Sets the selector pattern.
   *
   * @param pattern the selector pattern
   * @return a selector component
   * @since 4.0.0
   */
  @NonNull SelectorComponent pattern(final @NonNull String pattern);

  /**
   * A selector component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<SelectorComponent, Builder> {
    /**
     * Sets the selector pattern.
     *
     * @param pattern the selector pattern
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder pattern(final @NonNull String pattern);
  }
}
