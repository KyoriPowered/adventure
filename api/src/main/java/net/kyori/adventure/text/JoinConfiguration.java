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

import java.util.function.UnaryOperator;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A configuration for how a series of components can be joined.
 *
 * <p>A join configuration consists of the following parts:</p>
 * <dl>
 *   <dt>a prefix (optional)</dt>
 *   <dd>a component to be prepended to the resulting component</dd>
 *   <dt>a separator (optional)</dt>
 *   <dd>a component to be placed between each component</dd>
 *   <dt>a last separator (optiona)</dt>
 *   <dd>a component to be placed between the last two components</dd>
 *   <dt>a suffix (optional)</dt>
 *   <dd>a component to be appended to the resulting component</dd>
 *   <dt>an operator (non-optional)</dt>
 *   <dd>a unary operator to change each component that is being joined, defaults to the identity operator</dd>
 * </dl>
 *
 * <p>Note that the last separator only acts as an override for the normal separator.
 * This means that if you do not specify a last separator, the normal separator will be placed between the last two components.
 * To omit the final separator, but still include normal separators, use {@link Component#empty()} as the last separator.</p>
 *
 * @see Component#join(JoinConfiguration, Iterable)
 * @see Component#join(JoinConfiguration, ComponentLike...)
 * @since 4.8.0
 */
public interface JoinConfiguration extends Buildable<JoinConfiguration, JoinConfiguration.Builder>, Examinable {
  /**
   * Creates a new builder.
   *
   * @return a new builder
   * @since 4.8.0
   */
  static @NonNull Builder builder() {
    return new JoinConfigurationImpl.BuilderImpl();
  }

  /**
   * Gets a join configuration with no separators, prefix or suffix.
   *
   * @return the join configuration
   * @since 4.8.0
   */
  static @NonNull JoinConfiguration noSeparators() {
    return JoinConfigurationImpl.NULL;
  }

  /**
   * Creates a join configuration with a separator and no prefix or suffix.
   *
   * @param separator the separator
   * @return the join configuration
   * @since 4.8.0
   */
  static @NonNull JoinConfiguration separator(final @Nullable ComponentLike separator) {
    if(separator == null) return JoinConfigurationImpl.NULL;
    return builder().separator(separator).build();
  }

  /**
   * Creates a join configuration with a separator and last separator but no prefix or suffix.
   *
   * @param separator the separator
   * @param lastSeparator the last separator
   * @return the join configuration
   * @since 4.8.0
   */
  static @NonNull JoinConfiguration separators(final @Nullable ComponentLike separator, final @Nullable ComponentLike lastSeparator) {
    if(separator == null && lastSeparator == null) return JoinConfigurationImpl.NULL;
    return builder().separator(separator).lastSeparator(lastSeparator).build();
  }

  /**
   * Gets the prefix of this join configuration.
   *
   * @return the prefix
   * @since 4.8.0
   */
  @Nullable ComponentLike prefix();

  /**
   * Gets the suffix of this join configuration.
   *
   * @return the suffix
   * @since 4.8.0
   */
  @Nullable ComponentLike suffix();

  /**
   * Gets the separator of this join configuration.
   *
   * @return the separator
   * @since 4.8.0
   */
  @Nullable ComponentLike separator();

  /**
   * Gets the last separator of this join configuration.
   *
   * @return the last separator
   * @since 4.8.0
   */
  @Nullable ComponentLike lastSeparator();

  /**
   * Gets the operator of this join configuration.
   *
   * @return the operator
   * @since 4.8.0
   */
  @NotNull UnaryOperator<ComponentLike> operator();

  /**
   * A builder for join configurations.
   *
   * @since 4.8.0
   */
  interface Builder extends Buildable.Builder<JoinConfiguration> {
    /**
     * Sets the prefix of this join configuration builder.
     *
     * @param prefix the prefix
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder prefix(final @Nullable ComponentLike prefix);

    /**
     * Sets the suffix of this join configuration builder.
     *
     * @param suffix the suffix
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder suffix(final @Nullable ComponentLike suffix);

    /**
     * Sets the separator of this join configuration builder.
     *
     * @param separator the separator
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder separator(final @Nullable ComponentLike separator);

    /**
     * Sets the last separator of this join configuration builder.
     *
     * @param lastSeparator the last separator
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator);

    /**
     * Sets the operator of this join configuration builder.
     *
     * @return the operator
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder operator(final @NotNull UnaryOperator<ComponentLike> operator);
  }
}
