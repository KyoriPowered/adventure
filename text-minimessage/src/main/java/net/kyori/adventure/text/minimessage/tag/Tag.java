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
package net.kyori.adventure.text.minimessage.tag;

import java.util.Arrays;
import java.util.Locale;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A tag definition for the MiniMessage language.
 *
 * <p>All implementations of {@code Tag} must implement one of {@link Inserting}, {@link Modifying},
 * {@link ParserDirective} or {@link PreProcess}.</p>
 *
 * @since 4.10.0
 */
public /* sealed */ interface Tag /* permits Inserting, Modifying, ParserDirective, PreProcess, /internal/ AbstractTag */ {

  /**
   * Create a tag that inserts the content literally into the parse string.
   *
   * @param content content to insert
   * @return a new tag
   * @since 4.10.0
   */
  static @NotNull PreProcess preProcessParsed(final @NotNull String content) {
    return new PreProcessTagImpl(requireNonNull(content, "content"));
  }

  /**
   * Create a tag that will insert a certain component into the output.
   *
   * <p>Any content between this tag and an explicit closing tag will become a child of {@code content}.</p>
   *
   * @param content the content to insert.
   * @return a tag that will insert this component
   * @since 4.10.0
   */
  static @NotNull Tag inserting(final @NotNull Component content) {
    return new InsertingImpl(true, requireNonNull(content, "content must not be null"));
  }

  /**
   * Create a tag that will insert a certain component into the output.
   *
   * <p>Any content between this tag and an explicit closing tag will become a child of {@code content}.</p>
   *
   * @param value the content to insert, eagerly converted to a component
   * @return a tag that will insert this component
   * @since 4.10.0
   */
  static @NotNull Tag inserting(final @NotNull ComponentLike value) {
    return inserting(requireNonNull(value, "value").asComponent());
  }

  /**
   * Create a tag that will insert a certain component into the output.
   *
   * <p>This tag is self-closing, so its contents will not influence the style of following content.</p>
   *
   * @param content the content to insert.
   * @return a tag that will insert this component
   * @since 4.10.0
   */
  static @NotNull Tag selfClosingInserting(final @NotNull Component content) {
    return new InsertingImpl(false, requireNonNull(content, "content must not be null"));
  }

  /**
   * Create a tag that will insert a certain component into the output.
   *
   * <p>This tag is self-closing, so its contents will not influence the style of following content.</p>
   *
   * @param value the content to insert, eagerly converted to a component
   * @return a tag that will insert this component
   * @since 4.10.0
   */
  static @NotNull Tag selfClosingInserting(final @NotNull ComponentLike value) {
    return selfClosingInserting(requireNonNull(value, "value").asComponent());
  }

  /**
   * Create a tag that will apply a certain style to components.
   *
   * @param styles the action applied to the component style
   * @return a tag for this action
   * @since 4.10.0
   */
  static @NotNull Tag styling(final Consumer<Style.Builder> styles) {
    return new CallbackStylingTagImpl(styles);
  }

  /**
   * Create a tag that will apply certain styles to components.
   *
   * @param actions the style builder actions
   * @return a tag for these actions
   * @since 4.10.0
   */
  static @NotNull Tag styling(final @NotNull StyleBuilderApplicable@NotNull... actions) {
    requireNonNull(actions, "actions");
    for (int i = 0, length = actions.length; i < length; i++) {
      if (actions[i] == null) {
        throw new NullPointerException("actions[" + i + "]");
      }
    }
    return new StylingTagImpl(Arrays.copyOf(actions, actions.length));
  }

  /**
   * An argument that can be passed to a tag, after the first {@code :}.
   *
   * @since 4.10.0
   */
  @ApiStatus.NonExtendable
  interface Argument {
    /**
     * Returns the value of this argument.
     *
     * @return the value
     * @since 4.10.0
     */
    @NotNull String value();

    /**
     * Returns the value of this argument, lower-cased in the root locale.
     *
     * <p>This value should be used for comparisons against literals, to help ensure MiniMessage tags are case-insensitive.</p>
     *
     * @return the lower-cased value of this argument
     * @since 4.10.0
     */
    default @NotNull String lowerValue() {
      return this.value().toLowerCase(Locale.ROOT);
    }

    /**
     * Checks if this argument represents {@code true}.
     *
     * @return if this argument represents {@code true}
     * @since 4.10.0
     */
    default boolean isTrue() {
      return "true".equals(this.value()) || "on".equals(this.value());
    }

    /**
     * Checks if this argument represents {@code false}.
     *
     * @return if this argument represents {@code false}
     * @since 4.10.0
     */
    default boolean isFalse() {
      return "false".equals(this.value()) || "off".equals(this.value());
    }

    /**
     * Try and parse this argument as an {@code int}.
     *
     * <p>The optional will only be present if the value is a valid integer.</p>
     *
     * @return an optional providing the value of this argument as an integer
     * @since 4.10.0
     */
    default @NotNull OptionalInt asInt() {
      try {
        return OptionalInt.of(Integer.parseInt(this.value()));
      } catch (final NumberFormatException ex) {
        return OptionalInt.empty();
      }
    }

    /**
     * Try and parse this argument as a {@code double}.
     *
     * <p>The optional will only be present if the value is a valid double.</p>
     *
     * @return an optional providing the value of this argument as an integer
     * @since 4.10.0
     */
    default @NotNull OptionalDouble asDouble() {
      try {
        return OptionalDouble.of(Double.parseDouble(this.value()));
      } catch (final NumberFormatException ex) {
        return OptionalDouble.empty();
      }
    }
  }
}
