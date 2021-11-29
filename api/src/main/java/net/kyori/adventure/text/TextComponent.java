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
package net.kyori.adventure.text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A component that displays a string.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>content</dt>
 *   <dd>string to be displayed</dd>
 * </dl>
 *
 * @since 4.0.0
 */
public interface TextComponent extends BuildableComponent<TextComponent, TextComponent.Builder>, ScopedComponent<TextComponent> {
  /**
   * Creates a component with {@code components} as the children.
   *
   * @param components the children
   * @return a text component
   * @since 4.0.0
   * @deprecated for removal since 4.9.0, use {@link Component#textOfChildren(ComponentLike...)} instead
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  static @NotNull TextComponent ofChildren(final @NotNull ComponentLike@NotNull... components) {
    return Component.textOfChildren(components);
  }

  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   * @since 4.0.0
   */
  @NotNull String content();

  /**
   * Sets the plain text content.
   *
   * @param content the plain text content
   * @return a copy of this component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull TextComponent content(final @NotNull String content);

  /**
   * A text component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<TextComponent, Builder> {
    /**
     * Gets the plain text content.
     *
     * @return the plain text content
     * @since 4.0.0
     */
    @NotNull String content();

    /**
     * Sets the plain text content.
     *
     * @param content the plain text content
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder content(final @NotNull String content);
  }
}
