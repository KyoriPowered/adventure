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

import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A keybind component.
 *
 * @since 4.0.0
 */
public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent> {
  /**
   * Creates a keybind component builder.
   *
   * @return a builder
   * @since 4.0.0
   * @deprecated use {@link Component#keybind()}
   */
  @Deprecated
  static @NonNull Builder builder() {
    return Component.keybind();
  }

  /**
   * Creates a keybind component builder with a keybind.
   *
   * @param keybind the keybind
   * @return a builder
   * @since 4.0.0
   * @deprecated no replacement
   */
  @Deprecated
  static @NonNull Builder builder(final @NonNull String keybind) {
    return builder().keybind(keybind);
  }

  /**
   * Creates a keybind component with a keybind.
   *
   * @param keybind the keybind
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(String)}
   */
  @Deprecated
  static @NonNull KeybindComponent of(final @NonNull String keybind) {
    return Component.keybind(keybind);
  }

  /**
   * Creates a keybind component with a keybind and styling.
   *
   * @param keybind the keybind
   * @param style the style
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(String, Style)}
   */
  @Deprecated
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @NonNull Style style) {
    return Component.keybind(keybind, style);
  }

  /**
   * Creates a keybind component with a keybind, and optional color.
   *
   * @param keybind the keybind
   * @param color the color
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(String, TextColor)}
   */
  @Deprecated
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @Nullable TextColor color) {
    return Component.keybind(keybind, color);
  }

  /**
   * Creates a keybind component with a keybind, and optional color and decorations.
   *
   * @param keybind the keybind
   * @param color the color
   * @param decorations the decorations
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(String, TextColor, TextDecoration...)}
   */
  @Deprecated
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @Nullable TextColor color, final TextDecoration@NonNull... decorations) {
    return Component.keybind(keybind, color, decorations);
  }

  /**
   * Creates a keybind component with a keybind, and optional color and decorations.
   *
   * @param keybind the keybind
   * @param color the color
   * @param decorations the decorations
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(String, TextColor, Set)}
   */
  @Deprecated
  static @NonNull KeybindComponent of(final @NonNull String keybind, final @Nullable TextColor color, final @NonNull Set<TextDecoration> decorations) {
    return Component.keybind(keybind, color, decorations);
  }

  /**
   * Creates a keybind component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the keybind component
   * @since 4.0.0
   * @deprecated use {@link Component#keybind(Consumer)}
   */
  @Deprecated
  static @NonNull KeybindComponent make(final @NonNull Consumer<? super Builder> consumer) {
    return Component.keybind(consumer);
  }

  /**
   * Creates a keybind component by applying configuration from {@code consumer}.
   *
   * @param keybind the keybind
   * @param consumer the builder configurator
   * @return the keybind component
   * @since 4.0.0
   * @deprecated no replacement
   */
  @Deprecated
  static @NonNull KeybindComponent make(final @NonNull String keybind, final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = Component.keybind().keybind(keybind);
    return Buildable.configureAndBuild(builder, consumer);
  }

  /**
   * Gets the keybind.
   *
   * @return the keybind
   * @since 4.0.0
   */
  @NonNull String keybind();

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   * @since 4.0.0
   */
  @NonNull KeybindComponent keybind(final @NonNull String keybind);

  /**
   * A keybind component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<KeybindComponent, Builder> {
    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     * @since 4.0.0
     */
    @NonNull Builder keybind(final @NonNull String keybind);
  }
}
