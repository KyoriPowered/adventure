/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

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
 * @since 4.0.0
 */
public interface Template extends Examinable {
  /**
   * Constructs a template that gets replaced with a string.
   *
   * @param key the placeholder
   * @param value the value to replace the key with
   * @return the constructed template
   * @since 4.0.0
   * @deprecated Use {@link #template(String, String)}
   */
  @ApiStatus.ScheduledForRemoval
  @Deprecated
  static @NotNull Template of(final @NotNull String key, final @NotNull String value) {
    return new StringTemplate(
      requireNonNull(key, "key"),
      requireNonNull(value, "value")
    );
  }

  /**
   * Constructs a template that gets replaced with a component.
   *
   * @param key the placeholder
   * @param value the component to replace the key with
   * @return the constructed template
   * @since 4.0.0
   * @deprecated Use {@link #template(String, ComponentLike)}
   */
  @ApiStatus.ScheduledForRemoval
  @Deprecated
  static @NotNull Template of(final @NotNull String key, final @NotNull Component value) {
    return new ComponentTemplate(
      requireNonNull(key, "key"),
      requireNonNull(value, "value")
    );
  }

  /**
   * Constructs a template that gets replaced with a component lazily.
   *
   * @param key the placeholder
   * @param value the supplier that supplies the component to replace the key with
   * @return the constructed template
   * @since 4.2.0
   * @deprecated Use {@link #template(String, Supplier)}
   */
  @ApiStatus.ScheduledForRemoval
  @Deprecated
  static @NotNull Template of(final @NotNull String key, final @NotNull Supplier<Component> value) {
    return new LazyComponentTemplate(
      requireNonNull(key, "key"),
      requireNonNull(value, "value")
    );
  }

  /**
   * Constructs a template that gets replaced with a string.
   *
   * @param key the placeholder
   * @param value the value to replace the key with
   * @return the constructed template
   * @since 4.2.0
   */
  static @NotNull Template template(final @NotNull String key, final @NotNull String value) {
    return new StringTemplate(
        requireNonNull(key, "key"),
        requireNonNull(value, "value")
    );
  }

  /**
   * Constructs a template that gets replaced with a component.
   *
   * @param key the placeholder
   * @param value the component to replace the key with
   * @return the constructed template
   * @since 4.2.0
   */
  static @NotNull Template template(final @NotNull String key, final @NotNull ComponentLike value) {
    return new ComponentTemplate(
        requireNonNull(key, "key"),
        requireNonNull(requireNonNull(value, "value").asComponent(), "value cannot resolve to null")
    );
  }

  /**
   * Constructs a template that gets replaced with a component lazily.
   *
   * @param key the placeholder
   * @param value the supplier that supplies the component to replace the key with
   * @return the constructed template
   * @since 4.2.0
   */
  static @NotNull Template template(final @NotNull String key, final @NotNull Supplier<? extends ComponentLike> value) {
    return new LazyComponentTemplate(
        requireNonNull(key, "key"),
        requireNonNull(value, "value")
    );
  }

  /**
   * Get the key for this template.
   *
   * @return the key
   * @since 4.2.0
   */
  @NotNull String key();

  /**
   * Get the value for this template.
   *
   * @return the value
   * @since 4.2.0
   */
  @NotNull Object value();

  /**
   * A template with a value that will be parsed as a MiniMessage string.
   *
   * @since 4.0.0
   */
  @ApiStatus.Internal
  class StringTemplate implements Template {
    private final String key;
    private final String value;

    StringTemplate(final @NotNull String key, final @NotNull String value) {
      if (!key.toLowerCase(Locale.ROOT).equals(key))
        throw new IllegalArgumentException("Template key '" + key + "' must be lowercase");

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
   * A template with a {@link Component} value that will be inserted directly.
   *
   * @since 4.0.0
   */
  @ApiStatus.Internal
  class ComponentTemplate implements Template {
    private final @NotNull String key;
    private final @NotNull Component value;

    public ComponentTemplate(final @NotNull String key, final @NotNull Component value) {
      if (!key.toLowerCase(Locale.ROOT).equals(key))
        throw new IllegalArgumentException("Template key '" + key + "' must be lowercase");

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

  /**
   * A template with a lazily provided {@link Component} value that will be inserted directly.
   *
   * @since 4.2.0
   */
  @ApiStatus.Internal
  class LazyComponentTemplate extends ComponentTemplate {
    private final @NotNull Supplier<? extends ComponentLike> value;

    public LazyComponentTemplate(final @NotNull String key, final @NotNull Supplier<? extends ComponentLike> value) {
      super(key, Component.empty());
      this.value = value;
    }

    @Override
    public @NotNull Component value() {
      return requireNonNull(requireNonNull(
          this.value.get(), () -> "get() value of " + this.value)
          .asComponent(), () -> "asComponent() on value of " + this.value);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key()),
        ExaminableProperty.of("value", this.value)
      );
    }
  }
}
