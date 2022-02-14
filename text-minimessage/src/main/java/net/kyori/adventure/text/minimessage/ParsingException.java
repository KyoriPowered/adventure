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
package net.kyori.adventure.text.minimessage;

/**
 * An exception thrown when an error occurs while parsing a MiniMessage string.
 *
 * @since 4.10.0
 */
public abstract class ParsingException extends RuntimeException {
  private static final long serialVersionUID = 4502774670340827070L;

  /**
   * Create a new parsing exception with only a message.
   *
   * @param message a detail message describing the error
   * @since 4.10.0
   */
  protected ParsingException(final String message) {
    super(message);
  }

  /**
   * Create a new parsing exception with a message and an optional cause.
   *
   * @param message a detail message describing the error
   * @param cause the cause
   * @since 4.10.0
   */
  protected ParsingException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Get the input message which caused this exception.
   *
   * @return the original input message
   * @since 4.10.0
   */
  public abstract String originalText();
}
