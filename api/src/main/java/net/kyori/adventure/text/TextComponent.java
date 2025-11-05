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
package net.kyori.adventure.text;

import org.jetbrains.annotations.Contract;

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
public sealed interface TextComponent extends ScopedComponent<TextComponent> permits TextComponentImpl, VirtualComponent {
  /**
   * Gets the plain text content.
   *
   * @return the plain text content
   * @since 4.0.0
   */
  String content();

  /**
   * Sets the plain text content.
   *
   * @param content the plain text content
   * @return a copy of this component
   * @since 4.0.0
   */
  @Contract(pure = true)
  TextComponent content(final String content);

  @Override
  Builder toBuilder();

  /**
   * A text component builder.
   *
   * @since 4.0.0
   */
  sealed interface Builder extends ComponentBuilder<TextComponent, Builder> permits TextComponentImpl.BuilderImpl {
    /**
     * Gets the plain text content.
     *
     * @return the plain text content
     * @since 4.0.0
     */
    String content();

    /**
     * Sets the plain text content.
     *
     * @param content the plain text content
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    Builder content(final String content);
  }
}
