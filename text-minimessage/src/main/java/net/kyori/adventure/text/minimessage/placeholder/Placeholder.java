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
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A placeholder in a message, which can replace a tag with a component.
 *
 * @since 4.10.0
 */
public interface Placeholder extends Examinable {

  /**
   * Constructs a placeholder that gets replaced with a string.
   *
   * @param key the placeholder
   * @param value the value to replace the key with
   * @return the constructed placeholder
   * @since 4.10.0
   */
  static @NotNull Placeholder placeholder(final @NotNull String key, final @NotNull String value) {
    return new StringPlaceholder(
        requireNonNull(key, "key"),
        requireNonNull(value, "value")
    );
  }

  /**
   * Constructs a placeholder that gets replaced with a component.
   *
   * @param key the placeholder
   * @param value the component to replace the key with
   * @return the constructed placeholder
   * @since 4.10.0
   */
  static @NotNull Placeholder placeholder(final @NotNull String key, final @NotNull ComponentLike value) {
    return new ComponentPlaceholder(
        requireNonNull(key, "key"),
        requireNonNull(requireNonNull(value, "value").asComponent(), "value cannot resolve to null")
    );
  }

  /**
   * Get the key for this placeholder.
   *
   * @return the key
   * @since 4.10.0
   */
  @NotNull String key();

  /**
   * Get the value for this placeholder.
   *
   * @return the value
   * @since 4.10.0
   */
  @NotNull Object value();

  /**
   * A placeholder with a value that will be parsed as a MiniMessage string.
   *
   * @since 4.10.0
   */
  @ApiStatus.Internal
  class StringPlaceholder implements Placeholder {
    private final String key;
    private final String value;

    StringPlaceholder(final @NotNull String key, final @NotNull String value) {
      if (!key.toLowerCase(Locale.ROOT).equals(key))
        throw new IllegalArgumentException("Placeholder key '" + key + "' must be lowercase");
      this.key = key;
      this.value = value;
    }

    @Override
    public @NotNull String key() {
      return this.key;
    }

    @Override
    public @NotNull String value() {
      return this.value;
    }

    @Override
    public final String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("value", this.value)
      );
    }
  }

  /**
   * A placeholder with a {@link Component} value that will be inserted directly.
   *
   * @since 4.10.0
   */
  @ApiStatus.Internal
  class ComponentPlaceholder implements Placeholder {
    private final @NotNull String key;
    private final @NotNull Component value;

    public ComponentPlaceholder(final @NotNull String key, final @NotNull Component value) {
      if (!key.toLowerCase(Locale.ROOT).equals(key))
        throw new IllegalArgumentException("Placeholder key '" + key + "' must be lowercase");
      this.key = key;
      this.value = value;
    }

    @Override
    public @NotNull String key() {
      return this.key;
    }

    @Override
    public @NotNull Component value() {
      return this.value;
    }

    @Override
    public final String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("value", this.value)
      );
    }
  }
}
