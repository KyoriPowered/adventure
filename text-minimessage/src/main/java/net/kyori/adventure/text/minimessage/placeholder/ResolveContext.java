/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage.placeholder;

import net.kyori.adventure.text.minimessage.Context;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The context of a placeholder resolve attempt.
 *
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface ResolveContext {
  /**
   * Creates a new resolve context.
   *
   * <p>As there may be new properties added to the resolve context at any time, this constructor
   * method is not considered public API and should not be relied upon as a stable way of
   * constructing a resolve context instance (something which should not need to be done anyway).</p>
   *
   * @return a resolve context
   * @since 4.10.0
   */
  @ApiStatus.Internal
  static @NotNull ResolveContext resolveContext(final @NotNull String key, final @NotNull Context context) {
    return new ResolveContextImpl(key, context);
  }

  /**
   * The key of the placeholder.
   *
   * @return the key
   * @since 4.10.0
   */
  @NotNull String key();

  /**
   * The context of the parse.
   *
   * @return the parser context
   * @since 4.10.0
   */
  @NotNull Context parseContext();
}
