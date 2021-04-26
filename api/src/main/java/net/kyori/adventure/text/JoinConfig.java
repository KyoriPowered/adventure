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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The configuration for how a series of components can be joined.
 *
 * <p>A join configuration consists of the following parts, each of which is optional:</p>
 * <dl>
 *   <dt>a prefix to be prepended to the resulting component</dt>
 *   <dt>a separator to be placed between each component</dt>
 *   <dt>a final separator to be placed between the last two components</dt>
 *   <dt>a suffix to be appended to the resulting component</dt>
 * </dl>
 *
 * <p>In addition, a join configuration contains a unary operator to change each component to be joined.
 * This defaults to an identity operator.</p>
 *
 * <p>Note that if the final separator is omitted, the normal separator will be used instead.
 * To omit the final separator, but still include normal separators, use {@link Component#empty()}.</p>
 *
 * @see Component#join(JoinConfig, Iterable)
 * @see Component#join(JoinConfig, ComponentLike...)
 * @since 4.8.0
 */
public interface JoinConfig extends Buildable<JoinConfig, JoinConfig.Builder> {
  /**
   * Creates a new builder.
   *
   * @return a new builder
   * @since 4.8.0
   */
  static @NonNull Builder builder() {
    return new JoinConfigImpl.BuilderImpl();
  }

  /**
   * Gets a join configuration with no separators and no prefix or suffix.
   *
   * @return the join config
   * @since 4.8.0
   */
  static @NonNull JoinConfig noSeparator() {
    return JoinConfigImpl.NULL;
  }

  /**
   * Creates a join configuration with a separator and no prefix or suffix.
   *
   * @param separator the separator
   * @return the join config
   * @since 4.8.0
   */
  static @NonNull JoinConfig separator(final @Nullable ComponentLike separator) {
    if(separator == null) return JoinConfigImpl.NULL;
    return new JoinConfigImpl(separator, null, null, null, UnaryOperator.identity());
  }

  /**
   * Creates a join configuration with a separator and last separator but no prefix or suffix.
   *
   * @param separator the separator
   * @param lastSeparator the last separator
   * @return the join config
   * @since 4.8.0
   */
  static @NonNull JoinConfig separators(final @Nullable ComponentLike separator, final @Nullable ComponentLike lastSeparator) {
    if(separator == null && lastSeparator == null) return JoinConfigImpl.NULL;
    return new JoinConfigImpl(separator, lastSeparator, null, null, UnaryOperator.identity());
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
   * Joins {@code components} using this join configuration.
   *
   * @param components the components
   * @return a text component
   * @see Component#join(JoinConfig, ComponentLike...)
   * @since 4.8.0
   */
  @Contract(value = "_ -> new", pure = true)
  default @NonNull TextComponent join(final @NonNull ComponentLike@NonNull... components) {
    return Component.join(this, components);
  }

  /**
   * Joins {@code components} using this join configuration.
   *
   * @param components the components
   * @return a text component
   * @see Component#join(JoinConfig, Iterable)
   * @since 4.8.0
   */
  @Contract(value = "_ -> new", pure = true)
  default @NonNull TextComponent join(final @NonNull Iterable<? extends ComponentLike> components) {
    return Component.join(this, components);
  }

  /**
   * A builder for join configurations.
   *
   * @since 4.8.0
   */
  interface Builder extends Buildable.Builder<JoinConfig> {
    /**
     * Gets the prefix of this join configuration builder.
     *
     * @return the prefix
     * @since 4.8.0
     */
    @Nullable ComponentLike prefix();

    /**
     * Sets the prefix of this join configuration builder.
     *
     * @param prefix the prefix
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder prefix(final @Nullable ComponentLike prefix);

    /**
     * Gets the suffix of this join configuration builder.
     *
     * @return the suffix
     * @since 4.8.0
     */
    @Nullable ComponentLike suffix();

    /**
     * Sets the suffix of this join configuration builder.
     *
     * @param suffix the suffix
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder suffix(final @Nullable ComponentLike suffix);

    /**
     * Gets the separator of this join configuration builder.
     *
     * @return the separator
     * @since 4.8.0
     */
    @Nullable ComponentLike separator();

    /**
     * Sets the separator of this join configuration builder.
     *
     * @param separator the separator
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder separator(final @Nullable ComponentLike separator);

    /**
     * Gets the last separator of this join configuration builder.
     *
     * @return the last separator
     * @since 4.8.0
     */
    @Nullable ComponentLike lastSeparator();

    /**
     * Sets the last separator of this join configuration builder.
     *
     * @param lastSeparator the last separator
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NonNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator);

    /**
     * Gets the operator of this join configuration builder.
     *
     * @return the operator
     * @since 4.8.0
     */
    @NotNull UnaryOperator<ComponentLike> operator();

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
