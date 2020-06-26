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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.ShadyPines;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A plain text component.
 */
public interface TextComponent extends BuildableComponent<TextComponent, TextComponent.Builder>, ScopedComponent<TextComponent> {
  /**
   * Gets an empty component.
   *
   * @return an empty component
   */
  static @NonNull TextComponent empty() {
    return TextComponentImpl.EMPTY;
  }

  /**
   * Gets a text component with a new line character as the content.
   *
   * @return a text component with a new line character as the content
   */
  static @NonNull TextComponent newline() {
    return TextComponentImpl.NEWLINE;
  }

  /**
   * Gets a text immutable component with a single space as the content.
   *
   * @return a text component with a single space as the content
   */
  static @NonNull TextComponent space() {
    return TextComponentImpl.SPACE;
  }

  /**
   * Joins {@code components} using {@code separator}.
   *
   * @param separator the separator
   * @param components the components
   * @return a component
   */
  static @NonNull TextComponent join(final @NonNull Component separator, final Component... components) {
    return join(separator, Arrays.asList(components));
  }

  /**
   * Joins {@code components} using {@code separator}.
   *
   * @param separator the separator
   * @param components the components
   * @return a component
   */
  static @NonNull TextComponent join(final @NonNull Component separator, final Iterable<? extends Component> components) {
    final Iterator<? extends Component> it = components.iterator();
    if(!it.hasNext()) {
      return empty();
    }
    final Builder builder = builder();
    while(it.hasNext()) {
      builder.append(it.next());
      if(it.hasNext()) {
        builder.append(separator);
      }
    }
    return builder.build();
  }

  /**
   * Creates a text component with content.
   *
   * @param content the plain text content
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content) {
    if(content.isEmpty()) return empty();
    return builder(content).build();
  }

  /**
   * Creates a text component with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @Nullable TextColor color) {
    return builder(content).color(color).build();
  }

  /**
   * Creates a text component with content, and optional color and decorations.
   *
   * @param content the plain text content
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(content, color, ShadyPines.enumSet(TextDecoration.class, decorations));
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
   * Creates a text component with content and styling.
   *
   * @param content the plain text content
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final @NonNull String content, final @NonNull Style style) {
    return builder(content).style(style).build();
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
   * Creates a text component with the content of {@link String#valueOf(boolean)}, and optional color.
   *
   * @param value the boolean value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final boolean value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(boolean)}, and optional color and decorations.
   *
   * @param value the boolean value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final boolean value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(boolean)}, and optional color and decorations.
   *
   * @param value the boolean value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final boolean value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(boolean)} and styling.
   *
   * @param value the boolean value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final boolean value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}.
   *
   * @param value the char value
   * @return the component
   */
  static @NonNull TextComponent of(final char value) {
    if(value == '\n') return newline();
    if(value == ' ') return space();
    return of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}, and optional color.
   *
   * @param value the char value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final char value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}, and optional color and decorations.
   *
   * @param value the char value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final char value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}, and optional color and decorations.
   *
   * @param value the char value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final char value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)} and styling.
   *
   * @param value the char value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final char value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
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
   * Creates a text component with the content of {@link String#valueOf(double)}, and optional color.
   *
   * @param value the double value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final double value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(double)}, and optional color and decorations.
   *
   * @param value the double value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final double value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(double)}, and optional color and decorations.
   *
   * @param value the double value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final double value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(double)} and styling.
   *
   * @param value the double value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final double value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
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
   * Creates a text component with the content of {@link String#valueOf(float)}, and optional color.
   *
   * @param value the float value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final float value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(float)}, and optional color and decorations.
   *
   * @param value the float value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final float value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(float)}, and optional color and decorations.
   *
   * @param value the float value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final float value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(float)} and styling.
   *
   * @param value the float value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final float value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
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
   * Creates a text component with the content of {@link String#valueOf(int)}, and optional color.
   *
   * @param value the int value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final int value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(int)}, and optional color and decorations.
   *
   * @param value the int value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final int value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(int)}, and optional color and decorations.
   *
   * @param value the int value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final int value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(int)} and styling.
   *
   * @param value the int value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final int value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
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
   * Creates a text component with the content of {@link String#valueOf(long)}, and optional color.
   *
   * @param value the long value
   * @param color the color
   * @return the text component
   */
  static @NonNull TextComponent of(final long value, final @Nullable TextColor color) {
    return builder(String.valueOf(value)).color(color).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(long)}, and optional color and decorations.
   *
   * @param value the long value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final long value, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return of(String.valueOf(value), color, ShadyPines.enumSet(TextDecoration.class, decorations));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(long)}, and optional color and decorations.
   *
   * @param value the long value
   * @param color the color
   * @param decorations the decorations
   * @return the text component
   */
  static @NonNull TextComponent of(final long value, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return builder(String.valueOf(value)).color(color).decorations(decorations, true).build();
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(long)} and styling.
   *
   * @param value the long value
   * @param style the style
   * @return the text component
   */
  static @NonNull TextComponent of(final long value, final @NonNull Style style) {
    return builder(String.valueOf(value)).style(style).build();
  }

  /**
   * Creates a text component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new TextComponentImpl.BuilderImpl();
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
   * Creates a text component builder with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return a builder
   */
  static @NonNull Builder builder(final @NonNull String content, final @Nullable TextColor color) {
    return builder(content).color(color);
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the text component
   */
  static @NonNull TextComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Creates a text component by applying configuration from {@code consumer}.
   *
   * @param content the plain text content
   * @param consumer the builder configurator
   * @return the text component
   */
  static @NonNull TextComponent make(final @NonNull String content, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder(content);
    return Buildable.configureAndBuild(builder, consumer);
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
   * Finds and replaces text using a regex pattern.
   *
   * @param pattern a regex pattern
   * @param replacement a function to replace each match
   * @return a modified copy of this component
   */
  @NonNull TextComponent replace(final @NonNull Pattern pattern, final @NonNull UnaryOperator<Builder> replacement);

  /**
   * A text component builder.
   */
  interface Builder extends ComponentBuilder<TextComponent, Builder> {
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
     * @return this builder
     */
    @NonNull Builder content(final @NonNull String content);
  }
}
