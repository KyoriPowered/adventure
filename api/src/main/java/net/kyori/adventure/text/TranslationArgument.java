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

import static java.util.Objects.requireNonNull;

/**
 * An argument that can be part of a {@link TranslatableComponent}.
 *
 * @since 4.15.0
 */
public sealed interface TranslationArgument extends TranslationArgumentLike permits TranslationArgumentImpl {
  /**
   * Create a boolean argument.
   *
   * @param value the value
   * @return the argument
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  static TranslationArgument bool(final boolean value) {
    return new TranslationArgumentImpl(value);
  }

  /**
   * Create a numeric argument.
   *
   * @param value the value
   * @return the argument
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  static TranslationArgument numeric(final Number value) {
    return new TranslationArgumentImpl(requireNonNull(value, "value"));
  }

  /**
   * Create a component argument.
   *
   * @param value the value
   * @return the argument
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  static TranslationArgument component(final ComponentLike value) {
    if (value instanceof TranslationArgumentLike tal) return tal.asTranslationArgument();
    return new TranslationArgumentImpl(requireNonNull(requireNonNull(value, "value").asComponent(), "value.asComponent()"));
  }

  /**
   * The argument's value.
   *
   * @return the argument value
   * @since 4.15.0
   */
  Object value();

  @Override
  default TranslationArgument asTranslationArgument() {
    return this;
  }
}
