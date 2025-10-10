/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
public sealed interface ScoreComponent extends ScopedComponent<ScoreComponent> permits ScoreComponentImpl {
  /**
   * Gets the score name.
   *
   * @return the score name
   * @since 4.0.0
   */
  String name();

  /**
   * Sets the score name.
   *
   * @param name the score name
   * @return a score component
   * @since 4.0.0
   */
  @Contract(pure = true)
  ScoreComponent name(final String name);

  /**
   * Gets the objective name.
   *
   * @return the objective name
   * @since 4.0.0
   */
  String objective();

  /**
   * Sets the score objective.
   *
   * @param objective the score objective
   * @return a score component
   * @since 4.0.0
   */
  @Contract(pure = true)
  ScoreComponent objective(final String objective);

  @Override
  Builder toBuilder();

  /**
   * A score component builder.
   *
   * @since 4.0.0
   */
  sealed interface Builder extends ComponentBuilder<ScoreComponent, Builder> permits ScoreComponentImpl.BuilderImpl {
    /**
     * Sets the score name.
     *
     * @param name the score name
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    Builder name(final String name);

    /**
     * Sets the score objective.
     *
     * @param objective the score objective
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    Builder objective(final String objective);
  }
}
