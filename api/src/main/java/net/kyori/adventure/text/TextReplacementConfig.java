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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.IntFunction2;
import net.kyori.examination.Examinable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.regex.qual.Regex;

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
  static @NonNull Builder builder() {
    return new TextReplacementConfigImpl.Builder();
  }

  /**
   * Get the pattern that will be searched for.
   *
   * @return the match pattern
   * @since 4.2.0
   */
  @NonNull Pattern matchPattern();

  /**
   * A builder for replacement configurations.
   *
   * @since 4.2.0
   */
  interface Builder extends Buildable.Builder<TextReplacementConfig> {
    /*
     * -------------------
     * ---- Patterns -----
     * -------------------
     */

    /**
     * Match against the literal string provided.
     *
     * <p>This will <b>NOT</b> be parsed as a regular expression.</p>
     *
     * @param literal the literal string to match
     * @return this builder
     * @since 4.2.0
     */
    default Builder matchLiteral(final String literal) {
      return this.match(Pattern.compile(literal, Pattern.LITERAL));
    }

    /**
     * Compile the provided input as a {@link Pattern} and match against it.
     *
     * @param pattern the regex pattern to match
     * @return this builder
     * @since 4.2.0
     */
    default @NonNull Builder match(final @NonNull @Regex String pattern) {
      return this.match(Pattern.compile(pattern));
    }

    /**
     * Match the provided {@link Pattern}.
     *
     * @param pattern pattern to find in any searched components
     * @return this builder
     * @since 4.2.0
     */
    @NonNull Builder match(final @NonNull Pattern pattern);

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
    default @NonNull Builder once() {
      return this.times(1);
    }

    /**
     * Only replace the first {@code times} matches of the pattern.
     *
     * @param times maximum amount of matches to process
     * @return this builder
     * @since 4.2.0
     */
    default @NonNull Builder times(int times) {
      return this.condition((index, replaced) -> replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
    }

    /**
     * Set the function to determine how an individual match should be processed.
     *
     * @param condition a function of {@code (index, replaced)} used to determine if matches should be replaced, where "replaced" is the number of successful replacements.
     * @return this builder
     * @since 4.2.0
     */
    @NonNull Builder condition(final @NonNull IntFunction2<PatternReplacementResult> condition);

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
    default @NonNull Builder replacement(final @NonNull String replacement) {
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
    default @NonNull Builder replacement(final @NonNull ComponentLike replacement) {
      requireNonNull(replacement, "replacement");
      final Component baked = replacement.asComponent();
      return this.replacement((result, input) -> baked);
    }

    /**
     * Supply a function that provides replacements for each match.
     *
     * @param replacement the replacement function
     * @return this builder
     * @since 4.2.0
     */
    default @NonNull Builder replacement(final @NonNull Function<TextComponent.Builder, @Nullable ComponentLike> replacement) {
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
    @NonNull Builder replacement(final @NonNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement);
  }
}
