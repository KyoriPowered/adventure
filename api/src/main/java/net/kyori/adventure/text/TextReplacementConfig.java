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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.IntFunction2;
import net.kyori.examination.Examinable;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A configuration for how text can be replaced in a component.
 *
 * <p>The exact structure for a replacement specification is an implementation detail and therefore not exposed.
 * Custom implementations of {@code TextReplacementConfig} are not supported.</p>
 *
 * @since 4.2.0
 */
public interface TextReplacementConfig extends Buildable<TextReplacementConfig, TextReplacementConfig.Builder>, Examinable {
  /**
   * Create a new builder.
   *
   * @return a new builder
   * @since 4.2.0
   */
  static @NotNull Builder builder() {
    return new TextReplacementConfigImpl.Builder();
  }

  /**
   * Get the pattern that will be searched for.
   *
   * @return the match pattern
   * @since 4.2.0
   */
  @NotNull Pattern matchPattern();

  /**
   * A builder for replacement configurations.
   *
   * @since 4.2.0
   */
  interface Builder extends AbstractBuilder<TextReplacementConfig>, Buildable.Builder<TextReplacementConfig> {
    /*
     * -------------------
     * ---- Patterns -----
     * -------------------
     */

    /**
     * Set this builder to match only the literal string provided.
     *
     * <p>This will <b>NOT</b> be parsed as a regular expression.</p>
     *
     * @param literal the literal string to match
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default Builder matchLiteral(final String literal) {
      return this.match(Pattern.compile(literal, Pattern.LITERAL));
    }

    /**
     * Compile the provided input as a {@link Pattern} and set it as the match to test against.
     *
     * @param pattern the regex pattern to match
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder match(final @NotNull @RegExp String pattern) {
      return this.match(Pattern.compile(pattern));
    }

    /**
     * Match the provided {@link Pattern}.
     *
     * @param pattern pattern to find in any searched components
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    @NotNull Builder match(final @NotNull Pattern pattern);

    /*
     * ---------------------------
     * ---- Number of matches ----
     * ---------------------------
     */

    /**
     * Only replace the first occurrence of the matched pattern.
     *
     * @return this builder
     * @since 4.2.0
     */
    default @NotNull Builder once() {
      return this.times(1);
    }

    /**
     * Only replace the first {@code times} matches of the pattern.
     *
     * @param times maximum amount of matches to process
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder times(final int times) {
      return this.condition((index, replaced) -> replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
    }

    /**
     * Set the function to determine how an individual match should be processed.
     *
     * @param condition a function of {@code (matchCount, replaced)} used to determine if matches should be replaced, where "matchCount" is the number of matches
     *                  that have been found, including the current one, and "replaced" is the number of successful replacements.
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder condition(final @NotNull IntFunction2<PatternReplacementResult> condition) {
      return this.condition((result, matchCount, replaced) -> condition.apply(matchCount, replaced));
    }

    /**
     * Set the function to determine how an individual match should be processed.
     *
     * @param condition a function that determines whether a replacement should occur
     * @return this builder
     * @see Condition
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NotNull Builder condition(final @NotNull Condition condition);

    /*
     * -------------------------
     * ---- Action on match ----
     * -------------------------
     */

    /**
     * Supply a literal replacement for the matched pattern.
     *
     * @param replacement the replacement
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder replacement(final @NotNull String replacement) {
      requireNonNull(replacement, "replacement");
      return this.replacement(builder -> builder.content(replacement));
    }

    /**
     * Supply a literal replacement for the matched pattern.
     *
     * @param replacement the replacement
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder replacement(final @Nullable ComponentLike replacement) {
      final @Nullable Component baked = ComponentLike.unbox(replacement);
      return this.replacement((result, input) -> baked);
    }

    /**
     * Supply a function that provides replacements for each match.
     *
     * @param replacement the replacement function
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    default @NotNull Builder replacement(final @NotNull Function<TextComponent.Builder, @Nullable ComponentLike> replacement) {
      requireNonNull(replacement, "replacement");
      return this.replacement((result, input) -> replacement.apply(input));

    }

    /**
     * Supply a function that provides replacements for each match, with access to group information.
     *
     * @param replacement the replacement function, taking a match result and a text component pre-populated with
     * @return this builder
     * @since 4.2.0
     */
    @Contract("_ -> this")
    @NotNull Builder replacement(final @NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement);
  }

  /**
   * A function determining whether a certain match should be replaced.
   *
   * @since 4.8.0
   */
  @FunctionalInterface
  interface Condition {
    /**
     * Determine how a single match should be handled.
     *
     * @param result the current match result
     * @param matchCount the number of matches encountered, including this one and matches that were not replaced
     * @param replaced the number of matches that have already been replaced
     * @return whether a certain match should
     * @since 4.8.0
     */
    @NotNull PatternReplacementResult shouldReplace(final @NotNull MatchResult result, final int matchCount, final int replaced);
  }
}
