/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text.serializer;

import com.google.common.annotations.VisibleForTesting;
import net.kyori.text.Component;

import javax.annotation.Nonnull;

/**
 * A legacy component serializer.
 *
 * @param <C> the component type
 * @deprecated legacy
 */
@Deprecated
public interface LegacyComponentSerializer<C extends Component> extends ComponentSerializer<C, String> {

  /**
   * The legacy character.
   *
   * @deprecated legacy
   */
  @Deprecated
  @VisibleForTesting
  char CHARACTER = '\u00A7';

  /**
   * Deserialize a component from a {@link String} with the {@link #CHARACTER legacy character}.
   *
   * @param input the input
   * @return the component
   * @deprecated legacy
   */
  @Deprecated
  @Nonnull
  @Override
  default C deserialize(@Nonnull final String input) {
    return this.deserialize(input, CHARACTER);
  }

  /**
   * Deserialize a component from a {@link String} with the specified {@code character legacy character}.
   *
   * @param input the input
   * @param character the legacy character
   * @return the component
   * @deprecated legacy
   */
  @Deprecated
  @Nonnull
  C deserialize(@Nonnull final String input, final char character);

  /**
   * Serializes a component into a {@link String} with the specified {@link #CHARACTER legacy character}.
   *
   * @param component the component
   * @return the string
   * @deprecated legacy
   */
  @Deprecated
  @Nonnull
  @Override
  default String serialize(@Nonnull final C component) {
    return this.serialize(component, CHARACTER);
  }

  /**
   * Serializes a component into a {@link String} with the specified {@code character legacy character}.
   *
   * @param component the component
   * @param character the legacy character
   * @return the string
   * @deprecated legacy
   */
  @Deprecated
  @Nonnull
  String serialize(@Nonnull final C component, final char character);
}
