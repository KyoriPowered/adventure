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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * A component that can display a player's score from a scoreboard objective,
 * with an optional fallback value if the search for the score fails.
 * 
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>name</dt>
 *   <dd>a player username or a Minecraft selector that leads to a single player</dd>
 *   <dt>objective</dt>
 *   <dd>a scoreboard objective</dd>
 *   <dt>value(optional)</dt>
 *   <dd>a value to use that will override any queried scoreboard value
 *   <p>This field is no longer present in the game from 1.16,
 *   which means it will be ignored</p></dd>
 * </dl>
 *
 * <p>This component is rendered serverside and can therefore receive platform-defined
 * context. See the documentation for your respective
 * platform for more info</p>
 *
 * @since 4.0.0
 */
public interface ScoreComponent extends BuildableComponent<ScoreComponent, ScoreComponent.Builder>, ScopedComponent<ScoreComponent> {
  /**
   * Gets the score name.
   *
   * @return the score name
   * @since 4.0.0
   */
  @NonNull String name();

  /**
   * Sets the score name.
   *
   * @param name the score name
   * @return a score component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull ScoreComponent name(final @NonNull String name);

  /**
   * Gets the objective name.
   *
   * @return the objective name
   * @since 4.0.0
   */
  @NonNull String objective();

  /**
   * Sets the score objective.
   *
   * @param objective the score objective
   * @return a score component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull ScoreComponent objective(final @NonNull String objective);

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   * @deprecated since 4.7.0, not for removal, with no replacement. This field is no longer supported in 1.16.5.
   */
  @Deprecated
  @Nullable String value();

  /**
   * Sets the value.
   *
   * @param value the value
   * @return a score component
   * @since 4.0.0
   * @deprecated since 4.7.0, not for removal, with no replacement. This field is no longer supported in 1.16.5.
   */
  @Deprecated
  @Contract(pure = true)
  @NonNull ScoreComponent value(final @Nullable String value);

  /**
   * A score component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<ScoreComponent, Builder> {
    /**
     * Sets the score name.
     *
     * @param name the score name
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NonNull Builder name(final @NonNull String name);

    /**
     * Sets the score objective.
     *
     * @param objective the score objective
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NonNull Builder objective(final @NonNull String objective);

    /**
     * Sets the value.
     *
     * @param value the value
     * @return this builder
     * @since 4.0.0
     * @deprecated since 4.7.0, not for removal, with no replacement. This field is no longer supported in 1.16.5.
     */
    @Deprecated
    @Contract("_ -> this")
    @NonNull Builder value(final @Nullable String value);
  }
}
