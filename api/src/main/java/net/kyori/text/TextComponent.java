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
 * A plain text component.
 */
public interface TextComponent extends BuildableComponent<TextComponent, TextComponent.Builder>, ScopedComponent<TextComponent> {
  /**
   * Creates a text component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new TextComponentImpl.BuilderImpl();
  }

  /**
   * Creates a text component builder with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return a builder
   */
  static @NonNull Builder builder(final @NonNull String content, final @Nullable TextColor color) {
    return builder().content(content).color(color);
  }

  /**
   * Creates a text component builder with content.
   *
   * @param content the plain text content
   * @return a builder
   */
  static @NonNull Builder builder(final @NonNull String content) {
    return builder().content(content);
  }

  /**
   * Creates a text component with content.
   *
   * @param content the plain text content
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content) {
    return builder(content).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(boolean)}.
   *
   * @param value the boolean value
   * @return the component
   */
  static @NonNull TextComponent of(final boolean value) {
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}.
   *
   * @param value the char value
   * @return the component
   */
  static @NonNull TextComponent of(final char value) {
    if(value == '\n') return Component0.NEWLINE;
    if(value == ' ') return Component0.SPACE;
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(double)}.
   *
   * @param value the double value
   * @return the component
   */
  static @NonNull TextComponent of(final double value) {
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(float)}.
   *
   * @param value the float value
   * @return the component
   */
  static @NonNull TextComponent of(final float value) {
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(int)}.
   *
   * @param value the int value
   * @return the component
   */
  static @NonNull TextComponent of(final int value) {
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(long)}.
   *
   * @param value the long value
   * @return the component
   */
  static @NonNull TextComponent of(final long value) {
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @Nullable TextColor color) {
    return builder(content, color).build();
  }

  /**
   * Creates a text component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @Nullable TextColor color, final TextDecoration @NonNull ... decorations) {
    final Set<TextDecoration> activeDecorations = new HashSet<>(decorations.length);
    Collections.addAll(activeDecorations, decorations);
    return of(content, color, activeDecorations);
  }

  /**
   * Creates a text component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(content).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the text component
   */
  static @NonNull TextComponent make(final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder();
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param content the plain text content
   * @param consumer the builder configurator
   * @return the text component
   */
  static @NonNull TextComponent make(final @NonNull String content, final @NonNull Consumer<Builder> consumer) {
    final Builder builder = builder(content);
    consumer.accept(builder);
    return builder.build();
  }

  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   */
  @NonNull String content();

  /**
   * Sets the plain text content.
   *
   * @param content the plain text content
   * @return a copy of this component
   */
  @NonNull TextComponent content(final @NonNull String content);

  /**
   * A text component builder.
   */
  interface Builder extends ComponentBuilder<TextComponent, Builder> {
    /**
     * Sets the plain text content.
     *
     * @param content the plain text content
     * @return this builder
     */
    @NonNull Builder content(final @NonNull String content);
  }
}
