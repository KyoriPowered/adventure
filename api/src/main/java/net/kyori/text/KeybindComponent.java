/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A keybind component.
 */
public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent> {
  /**
   * Creates a keybind component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new KeybindComponentImpl.BuilderImpl();
  }

  /**
   * Creates a keybind component builder with a keybind.
   *
   * @param keybind the keybind
   * @return a builder
   */
  static @NonNull Builder builder(final @NonNull String keybind) {
    return builder().keybind(keybind);
  }

  /**
   * Creates a keybind component with a keybind.
   *
   * @param keybind the keybind
   * @return the keybind component
   */
  static @NonNull KeybindComponent of(final @NonNull String keybind) {
    return builder(keybind).build();
  }

  /**
   * Creates a keybind component with content, and optional color.
   *
   * @param keybind the keybind
   * @param color the color
   * @return the keybind component
   */
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @Nullable TextColor color) {
    return of(keybind, color, Collections.emptySet());
  }

  /**
   * Creates a keybind component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the keybind component
   */
  static @NonNull KeybindComponent of(final @NonNull String content, final @Nullable TextColor color, final TextDecoration @NonNull ... decorations) {
    final Set<TextDecoration> activeDecorations = new HashSet<>(decorations.length);
    Collections.addAll(activeDecorations, decorations);
    return of(content, color, activeDecorations);
  }

  /**
   * Creates a keybind component with content, and optional color and decorations.
   *
   * @param keybind the keybind
   * @param color the color
   * @param decorations the decorations
   * @return the keybind component
   */
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(keybind).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a keybind component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the keybind component
   */
  static @NonNull KeybindComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a keybind component by applying configuration from {@code consumer}.
   *
   * @param keybind the keybind
   * @param consumer the builder configurator
   * @return the keybind component
   */
  static @NonNull KeybindComponent make(final @NonNull String keybind, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder(keybind);
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Gets the keybind.
   *
   * @return the keybind
   */
  @NonNull String keybind();

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   */
  @NonNull KeybindComponent keybind(final @NonNull String keybind);

  /**
   * A keybind component builder.
   */
  interface Builder extends ComponentBuilder<KeybindComponent, Builder> {
    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     */
    @NonNull Builder keybind(final @NonNull String keybind);
  }
}
