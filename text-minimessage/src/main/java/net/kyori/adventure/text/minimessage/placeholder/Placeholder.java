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
package net.kyori.adventure.text.minimessage.placeholder;

import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A replacement with an associated key.
 *
 * @param <T> the type of the replacement
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface Placeholder<T> extends Replacement<T> {

  /**
   * Creates a placeholder that inserts a MiniMessage string. The inserted string will impact
   * the rest of the parse process.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  static @NotNull Placeholder<String> miniMessage(final @NotNull String key, final @NotNull String value) {
    if (!Objects.requireNonNull(key, "key").equals(key.toLowerCase(Locale.ROOT)))
      throw new IllegalArgumentException("key must be lowercase, was " + key);

    return new PlaceholderImpl<>(key, Objects.requireNonNull(value, "value"));
  }

  /**
   * Creates a placeholder that inserts a raw string, ignoring any MiniMessage tags present.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  static @NotNull Placeholder<Component> raw(final @NotNull String key, final @NotNull String value) {
    return Placeholder.component(key, Component.text(value));
  }

  /**
   * Creates a replacement that inserts a component.
   *
   * @param key the key
   * @param value the replacement
   * @return the placeholder
   * @since 4.10.0
   */
  static @NotNull Placeholder<Component> component(final @NotNull String key, final @NotNull ComponentLike value) {
    if (!Objects.requireNonNull(key, "key").equals(key.toLowerCase(Locale.ROOT)))
      throw new IllegalArgumentException("key must be lowercase, was " + key);

    return new PlaceholderImpl<>(
      key,
      Objects.requireNonNull(
        Objects.requireNonNull(value, "value").asComponent(),
        "value must not resolve to null"
      )
    );
  }

  /**
   * Get the key for this placeholder.
   *
   * <p>The key will always be lowercase.</p>
   *
   * @return the key
   * @since 4.10.0
   */
  @NotNull String key();
}
