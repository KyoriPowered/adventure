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

import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A configuration for how a series of components can be joined.
 *
 * <p>A join configuration consists of the following parts:</p>
 * <ul>
 *   <li>
 *    <b>a prefix</b> (optional)
 *    <p>a component to be prepended to the resulting component</p>
 *   </li>
 *  <li>
 *   <b>a separator</b> (optional)
 *   <p>a component to be placed between each component</p>
 *  </li>
 *  <li>
 *   <b>a last separator</b> (optional)
 *   <p>a component to be placed between the last two components</p>
 *  </li>
 *  <li>
 *   <b>a suffix</b> (optional)
 *   <p>a component to be appended to the resulting component</p>
 *  </li>
 *  <li>
 *   <b>a convertor</b> (required, defaults to {@link ComponentLike#asComponent()})
 *   <p>a function to change each {@link ComponentLike} that is being joined into a {@link Component}</p>
 *  </li>
 *  <li>
 *    <b>a predicate</b> (required, defaults to {@code true})
 *    <p>a predicate that specifies if a given component should be included in the join process</p>
 *  </li>
 *  <li>
 *    <b>a root {@link Style style} (required, defaults to {@link Style#empty()})</b>
 *    <p>the style of the parent component that contains the joined components.</p>
 *  </li>
 * </ul>
 *
 * <p>Note that the last separator only acts as an override for the normal separator.
 * This means that if you do not specify a last separator, the normal separator will be placed between the last two components.
 * To omit the final separator, but still include normal separators, use {@link Component#empty()} as the last separator.</p>
 *
 * <p>If specified, the join method can use a different last separator in the case where the amount of components
 * being joined together is more than two. This can be used to insert a serial (or Oxford) comma if needed.</p>
 *
 * <p>Null elements are not allowed in the input of the join methods or as output from the convertor. If you would like to
 * exclude elements from being joined, use the predicate.</p>
 *
 * @see Component#join(JoinConfiguration, Iterable)
 * @see Component#join(JoinConfiguration, ComponentLike...)
 * @since 4.9.0
 */
@ApiStatus.NonExtendable
public interface JoinConfiguration extends Buildable<JoinConfiguration, JoinConfiguration.Builder>, Examinable {
  /**
   * Creates a new builder.
   *
   * @return a new builder
   * @since 4.9.0
   */
  static @NotNull Builder builder() {
    return new JoinConfigurationImpl.BuilderImpl();
  }

  /**
   * Gets a join configuration with no separators, prefix or suffix.
   *
   * @return the join configuration
   * @since 4.9.0
   */
  static @NotNull JoinConfiguration noSeparators() {
    return JoinConfigurationImpl.NULL;
  }

  /**
   * Provides a join configuration with no prefix or suffix that simply joins the components together using the {@link Component#newline()} component.
   *
   * <p>A purely text based example of this syntax, without introducing the concepts of components, would join the two strings 'hello' and 'there' together,
   * creating the following output: 'hello\nthere'.</p>
   *
   * @return the join configuration
   * @since 4.10.0
   */
  static @NotNull JoinConfiguration newlines() {
    return JoinConfigurationImpl.STANDARD_NEW_LINES;
  }

  /**
   * Provides a join configuration with no prefix or suffix that simply joins the components together using the {@link Component#space()} component.
   *
   * <p>A purely text based example of this syntax, without introducing the concepts of components, would join the two strings 'hello' and 'there' together,
   * creating the following output: 'hello there'.</p>
   *
   * @return the join configuration
   * @since 4.15.0
   */
  static @NotNull JoinConfiguration spaces() {
    return JoinConfigurationImpl.STANDARD_SPACES;
  }

  /**
   * Provides a join configuration with no prefix or suffix that simply joins the components together using a single comma, matching a CSV like layout.
   *
   * <p>A purely text based example of this syntax, without introducing the concepts of components, would join the two strings 'hello' and 'there' together,
   * creating either the output 'hello,there' or 'hello, there' depending on whether the passed boolean flag was {@code false} or {@code true} respectively.</p>
   *
   * @param spaces a plain boolean flag indicating whether the returned comma-based join configuration should append a single space after each comma or not
   * @return the join configuration
   * @since 4.10.0
   */
  static @NotNull JoinConfiguration commas(final boolean spaces) {
    return spaces ? JoinConfigurationImpl.STANDARD_COMMA_SPACE_SEPARATED : JoinConfigurationImpl.STANDARD_COMMA_SEPARATED;
  }

  /**
   * Provides a join configuration that joins components together in the same manner {@link java.util.Arrays#toString(Object[])} stringifies an array.
   * Specifically, the join configuration prefixes and suffixes the components with an open or closed square bracket respectively.
   * Components themselves are joined together using a comma and a space.
   *
   * <p>A purely text based example of this syntax, without introducing the concepts of components, would join the two strings 'hello' and 'there' together,
   * creating the following output: '[hello, there]'.</p>
   *
   * @return the join configuration
   * @since 4.10.0
   */
  static @NotNull JoinConfiguration arrayLike() {
    return JoinConfigurationImpl.STANDARD_ARRAY_LIKE;
  }

  /**
   * Creates a join configuration with a separator and no prefix or suffix.
   *
   * @param separator the separator
   * @return the join configuration
   * @since 4.9.0
   */
  static @NotNull JoinConfiguration separator(final @Nullable ComponentLike separator) {
    if (separator == null) return JoinConfigurationImpl.NULL;
    return builder().separator(separator).build();
  }

  /**
   * Creates a join configuration with a separator and last separator but no prefix or suffix.
   *
   * @param separator the separator
   * @param lastSeparator the last separator
   * @return the join configuration
   * @since 4.9.0
   */
  static @NotNull JoinConfiguration separators(final @Nullable ComponentLike separator, final @Nullable ComponentLike lastSeparator) {
    if (separator == null && lastSeparator == null) return JoinConfigurationImpl.NULL;
    return builder().separator(separator).lastSeparator(lastSeparator).build();
  }

  /**
   * Gets the prefix of this join configuration.
   *
   * @return the prefix
   * @since 4.9.0
   */
  @Nullable Component prefix();

  /**
   * Gets the suffix of this join configuration.
   *
   * @return the suffix
   * @since 4.9.0
   */
  @Nullable Component suffix();

  /**
   * Gets the separator of this join configuration.
   *
   * @return the separator
   * @since 4.9.0
   */
  @Nullable Component separator();

  /**
   * Gets the last separator of this join configuration.
   *
   * @return the last separator
   * @since 4.9.0
   */
  @Nullable Component lastSeparator();

  /**
   * Gets the last separator that will be used instead of the normal last separator in the case where there
   * are more than two components being joined. This can be used to mimic a serial (or Oxford) comma.
   *
   * @return the separator
   * @since 4.9.0
   */
  @Nullable Component lastSeparatorIfSerial();

  /**
   * Gets the convertor of this join configuration.
   *
   * <p>This is used to change the components that are going to be joined. It does not touch the prefix, suffix or any of the separators.</p>
   *
   * @return the operator
   * @since 4.9.0
   */
  @NotNull Function<ComponentLike, Component> convertor();

  /**
   * Gets the predicate of this join configuration.
   *
   * <p>This is used to determine if a component is to be included in the join process. It does not touch the prefix, suffix or any of the separators.</p>
   *
   * @return the predicate
   * @since 4.9.0
   */
  @NotNull Predicate<ComponentLike> predicate();

  /**
   * Gets the style of the parent component that contains the joined components.
   *
   * @return the style
   * @since 4.11.0
   */
  @NotNull Style parentStyle();

  /**
   * A builder for join configurations.
   *
   * @since 4.9.0
   */
  interface Builder extends AbstractBuilder<JoinConfiguration>, Buildable.Builder<JoinConfiguration> {
    /**
     * Sets the prefix of this join configuration builder.
     *
     * @param prefix the prefix
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder prefix(final @Nullable ComponentLike prefix);

    /**
     * Sets the suffix of this join configuration builder.
     *
     * @param suffix the suffix
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder suffix(final @Nullable ComponentLike suffix);

    /**
     * Sets the separator of this join configuration builder.
     *
     * @param separator the separator
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder separator(final @Nullable ComponentLike separator);

    /**
     * Sets the last separator of this join configuration builder.
     *
     * @param lastSeparator the last separator
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator);

    /**
     * Sets the last separator that will be used instead of the normal last separator in the case where there
     * are more than two components being joined.
     *
     * <p>This can be used to mimic a serial (or Oxford) comma.</p>
     *
     * @param lastSeparatorIfSerial the last separator
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder lastSeparatorIfSerial(final @Nullable ComponentLike lastSeparatorIfSerial);

    /**
     * Sets the convertor of this join configuration builder.
     *
     * <p>This is used to mutate the components that are going to be joined. It does not touch the prefix, suffix or any of the separators.</p>
     *
     * @param convertor the convertor
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder convertor(final @NotNull Function<ComponentLike, Component> convertor);

    /**
     * Sets the predicate of this join configuration builder.
     *
     * <p>This is used to determine if a component is to be included in the join process. It does not touch the prefix, suffix or any of the separators.</p>
     *
     * @param predicate the predicate
     * @return this builder
     * @since 4.9.0
     */
    @Contract("_ -> this")
    @NotNull Builder predicate(final @NotNull Predicate<ComponentLike> predicate);

    /**
     * Sets the style of the parent component that contains the joined components.
     *
     * @param parentStyle the style
     * @return this builder
     * @since 4.11.0
     */
    @Contract("_ -> this")
    @NotNull Builder parentStyle(final @NotNull Style parentStyle);
  }
}
