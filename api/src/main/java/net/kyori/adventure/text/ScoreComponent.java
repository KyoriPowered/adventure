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
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A scoreboard score component.
 */
public interface ScoreComponent extends BuildableComponent<ScoreComponent, ScoreComponent.Builder>, ScopedComponent<ScoreComponent> {
  /**
   * Creates a score component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new ScoreComponentImpl.BuilderImpl();
  }

  /**
   * Creates a score component builder with a name and objective.
   *
   * @param name the score name
   * @param objective the score objective
   * @return a builder
   */
  static @NonNull Builder builder(final @NonNull String name, final @NonNull String objective) {
    return builder().name(name).objective(objective);
  }

  /**
   * Creates a score component with a name and objective.
   *
   * @param name the score name
   * @param objective the score objective
   * @return the score component
   */
  static @NonNull ScoreComponent of(final @NonNull String name, final @NonNull String objective) {
    return of(name, objective, null);
  }

  /**
   * Creates a score component with a name, objective, and optional value.
   *
   * @param name the score name
   * @param objective the score objective
   * @param value the value
   * @return the score component
   */
  static @NonNull ScoreComponent of(final @NonNull String name, final @NonNull String objective, final @Nullable String value) {
    return builder()
      .name(name)
      .objective(objective)
      .value(value)
      .build();
  }

  /**
   * Creates a score component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the score component
   */
  static @NonNull ScoreComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Creates a score component by applying configuration from {@code consumer}.
   *
   * @param name the score name
   * @param objective the score objective
   * @param consumer the builder configurator
   * @return the score component
   */
  static @NonNull ScoreComponent make(final @NonNull String name, final @NonNull String objective, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder(name, objective);
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Gets the score name.
   *
   * @return the score name
   */
  @NonNull String name();

  /**
   * Sets the score name.
   *
   * @param name the score name
   * @return a copy of this component
   */
  @NonNull ScoreComponent name(final @NonNull String name);

  /**
   * Gets the objective name.
   *
   * @return the objective name
   */
  @NonNull String objective();

  /**
   * Sets the score objective.
   *
   * @param objective the score objective
   * @return a copy of this component
   */
  @NonNull ScoreComponent objective(final @NonNull String objective);

  /**
   * Gets the value.
   *
   * @return the value
   */
  @Nullable String value();

  /**
   * Sets the value.
   *
   * @param value the value
   * @return a copy of this component
   */
  @NonNull ScoreComponent value(final @Nullable String value);

  /**
   * A score component builder.
   */
  interface Builder extends ComponentBuilder<ScoreComponent, Builder> {
    /**
     * Sets the score name.
     *
     * @param name the score name
     * @return this builder
     */
    @NonNull Builder name(final @NonNull String name);

    /**
     * Sets the score objective.
     *
     * @param objective the score objective
     * @return this builder
     */
    @NonNull Builder objective(final @NonNull String objective);

    /**
     * Sets the value.
     *
     * @param value the value
     * @return this builder
     */
    @NonNull Builder value(final @Nullable String value);
  }
}
