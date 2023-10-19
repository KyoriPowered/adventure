/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.key;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This exception is thrown when an invalid namespace and/or value has been detected while creating a {@link Key}.
 *
 * @since 4.0.0
 */
@NullMarked
public final class InvalidKeyException extends RuntimeException {
  private static final long serialVersionUID = -5413304087321449434L;
  private final String keyNamespace;
  private final String keyValue;

  InvalidKeyException(final String keyNamespace, final String keyValue, final @Nullable String message) {
    super(message);
    this.keyNamespace = keyNamespace;
    this.keyValue = keyValue;
  }

  /**
   * Gets the invalid key, as a string.
   *
   * @return a key
   * @since 4.0.0
   */
  public final String keyNamespace() {
    return this.keyNamespace;
  }

  /**
   * Gets the invalid key, as a string.
   *
   * @return a key
   * @since 4.0.0
   */
  public final String keyValue() {
    return this.keyValue;
  }
}
