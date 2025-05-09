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
package net.kyori.adventure.text.minimessage.internal.serializer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A claim of a single style element.
 *
 * @param <V> the element type
 * @since 4.10.0
 */
public interface StyleClaim<V> {
  /**
   * Create a new style claim that will emit content for any non-null value.
   *
   * @param <T> the value type
   * @param claimKey claim key for de-duplication
   * @param lens value extractor from a {@link Style} instance
   * @param emitable the function that handles emitting
   * @return a new claim
   * @since 4.10.0
   */
  static <T> @NotNull StyleClaim<T> claim(final @NotNull String claimKey, final @NotNull Function<Style, @Nullable T> lens, final @NotNull BiConsumer<T, TokenEmitter> emitable) {
    return claim(claimKey, lens, $ -> true, emitable);
  }

  /**
   * Create a new style claim that will emit content for any non-null value that passes the filter.
   *
   * @param <T> the value type
   * @param claimKey claim key for de-duplication
   * @param lens value extractor from a {@link Style} instance
   * @param filter a filter for values, will only receive non-null values
   * @param emitable the function that handles emitting
   * @return a new claim
   * @since 4.10.0
   */
  static <T> @NotNull StyleClaim<T> claim(final @NotNull String claimKey, final @NotNull Function<Style, @Nullable T> lens, final @NotNull Predicate<T> filter, final @NotNull BiConsumer<T, TokenEmitter> emitable) {
    return new StyleClaimImpl<>(
      requireNonNull(claimKey, "claimKey"),
      requireNonNull(lens, "lens"),
      requireNonNull(filter, "filter"),
      requireNonNull(emitable, "emitable")
    );
  }

  /**
   * The key identifying this style element.
   *
   * @return the key to claim
   * @since 4.10.0
   */
  @NotNull String claimKey(); // TODO: multiple claim keys? for custom styling tags?

  /**
   * Prepare an emitable to apply this claim based on the style.
   *
   * @param style the style to test
   * @return an emitable for this style claim, if it is applicable to the provided style
   * @since 4.10.0
   */
  @Nullable Emitable apply(final @NotNull Style style);
}
