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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A translatable component.
 *
 * @since 4.0.0
 */
public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent> {
  /**
   * Creates a translatable component builder.
   *
   * @return a builder
   * @since 4.0.0
   * @deprecated use {@link Component#translatable()}
   */
  @Deprecated
  static @NonNull Builder builder() {
    return Component.translatable();
  }

  /**
   * Creates a translatable component builder with a translation key.
   *
   * @param key the translation key
   * @return a builder
   * @since 4.0.0
   * @deprecated no replacement
   */
  @Deprecated
  static @NonNull Builder builder(final @NonNull String key) {
    return Component.translatable().key(key);
  }

  /**
   * Creates a translatable component with a translation key.
   *
   * @param key the translation key
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key) {
    return Component.translatable(key);
  }

  /**
   * Creates a translatable component with a translation key and styling.
   *
   * @param key the translation key
   * @param style the style
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, Style)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull Style style) {
    return Component.translatable(key, style);
  }

  /**
   * Creates a translatable component with a translation key, and optional color.
   *
   * @param key the translation key
   * @param color the color
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color) {
    return Component.translatable(key, color);
  }

  /**
   * Creates a translatable component with a translation key, and optional color and decorations.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, TextDecoration...)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return Component.translatable(key, color, decorations);
  }

  /**
   * Creates a translatable component with a translation key, and optional color and decorations.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, Set)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return Component.translatable(key, color, decorations);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, ComponentLike...)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull ComponentLike@NonNull... args) {
    return Component.translatable(key, args);
  }

  /**
   * Creates a translatable component with a translation key and styling.
   *
   * @param key the translation key
   * @param style the style
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, Style, ComponentLike...)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull Style style, final @NonNull ComponentLike@NonNull... args) {
    return Component.translatable(key, style, args);
  }

  /**
   * Creates a translatable component with a translation key, arguments, and optional color.
   *
   * @param key the translation key
   * @param color the color
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, ComponentLike...)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull ComponentLike@NonNull... args) {
    return Component.translatable(key, color, args);
  }

  /**
   * Creates a translatable component with a translation key, arguments, and optional color and decorations.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, Set, ComponentLike...)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations, final @NonNull ComponentLike@NonNull... args) {
    return Component.translatable(key, color, decorations, args);
  }

  /**
   * Creates a translatable component with a translation key and arguments.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, List)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull List<? extends ComponentLike> args) {
    return Component.translatable(key, args);
  }

  /**
   * Creates a translatable component with a translation key and styling.
   *
   * @param key the translation key
   * @param style the style
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, Style, List)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @NonNull Style style, final @NonNull List<? extends ComponentLike> args) {
    return Component.translatable(key, style, args);
  }

  /**
   * Creates a translatable component with a translation key, arguments, and optional color.
   *
   * @param key the translation key
   * @param color the color
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, List)}
   */
  @Deprecated
  static TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull List<? extends ComponentLike> args) {
    return Component.translatable(key, color, args);
  }

  /**
   * Creates a translatable component with a translation key, arguments, and optional color and decorations.
   *
   * @param key the translation key
   * @param color the color
   * @param decorations the decorations
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(String, TextColor, Set, List)}
   */
  @Deprecated
  static @NonNull TranslatableComponent of(final @NonNull String key, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations, final @NonNull List<? extends ComponentLike> args) {
    return Component.translatable(key, color, decorations, args);
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return a translatable component
   * @since 4.0.0
   * @deprecated use {@link Component#translatable(Consumer)}
   */
  @Deprecated
  static @NonNull TranslatableComponent make(final @NonNull Consumer<? super Builder> consumer) {
    return Component.translatable(consumer);
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param key the translation key
   * @param consumer the builder configurator
   * @return a translatable component
   * @since 4.0.0
   * @deprecated no replacement
   */
  @Deprecated
  static @NonNull TranslatableComponent make(final @NonNull String key, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = Component.translatable().key(key);
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Creates a translatable component by applying configuration from {@code consumer}.
   *
   * @param key the translation key
   * @param args the translation arguments
   * @param consumer the builder configurator
   * @return a translatable component
   * @since 4.0.0
   * @deprecated no replacement
   */
  @Deprecated
  static @NonNull TranslatableComponent make(final @NonNull String key, final @NonNull List<? extends ComponentLike> args, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = Component.translatable().key(key).args(args);
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Gets the translation key.
   *
   * @return the translation key
   * @since 4.0.0
   */
  @NonNull String key();

  /**
   * Sets the translation key.
   *
   * @param key the translation key
   * @return a translatable component
   * @since 4.0.0
   */
  @NonNull TranslatableComponent key(final @NonNull String key);

  /**
   * Gets the unmodifiable list of translation arguments.
   *
   * @return the unmodifiable list of translation arguments
   * @since 4.0.0
   */
  @NonNull List<Component> args();

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   */
  @NonNull TranslatableComponent args(final @NonNull ComponentLike@NonNull... args);

  /**
   * Sets the translation arguments for this component.
   *
   * @param args the translation arguments
   * @return a translatable component
   * @since 4.0.0
   */
  @NonNull TranslatableComponent args(final @NonNull List<? extends ComponentLike> args);

  /**
   * A text component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<TranslatableComponent, Builder> {
    /**
     * Sets the translation key.
     *
     * @param key the translation key
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder key(final @NonNull String key);

    /**
     * Sets the translation args.
     *
     * @param arg the translation arg
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder args(final @NonNull ComponentBuilder<?, ?> arg);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    // CHECKSTYLE:OFF
    @NonNull Builder args(final @NonNull ComponentBuilder<?, ?>@NonNull... args);
    // CHECKSTYLE:ON

    /**
     * Sets the translation args.
     *
     * @param arg the translation arg
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder args(final @NonNull Component arg);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder args(final @NonNull ComponentLike@NonNull... args);

    /**
     * Sets the translation args.
     *
     * @param args the translation args
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder args(final @NonNull List<? extends ComponentLike> args);
  }
}
