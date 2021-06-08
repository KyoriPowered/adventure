/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.parser;

import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

/**
 * An exception that happens while parsing.
 *
 * @since 4.1.0
 */
public class ParsingException extends RuntimeException {
  private static final long serialVersionUID = 2507190809441787201L;

  private @Nullable String originalText;
  private Token @NonNull [] tokens;

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the origina text which was parsed
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  public ParsingException(
    final String message,
    final @Nullable String originalText,
    final @NonNull Token @NonNull ... tokens
  ) {
    super(message);
    this.tokens = tokens;
    this.originalText = originalText;
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the origina text which was parsed
   * @param cause the cause
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  public ParsingException(
    final String message,
    final @Nullable String originalText,
    final @Nullable Throwable cause,
    final @NonNull Token @NonNull ... tokens
  ) {
    super(message, cause);
    this.tokens = tokens;
    this.originalText = originalText;
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  public ParsingException(final String message, final @NonNull Token @NonNull ... tokens) {
    this(message, null, null, tokens);
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param cause the cause
   * @param tokens the token which caused the error
   * @since 4.1.0
   */
  public ParsingException(
    final String message,
    final @Nullable Throwable cause,
    final @NonNull Token @NonNull ... tokens
  ) {
    this(message, null, cause, tokens);
  }

  @Override
  public String getMessage() {
    final String arrowInfo = this.tokens().length != 0
      ? "\n\t" + this.arrow()
      : "";
    final String messageInfo = this.originalText() != null
      ? "\n\t" + this.originalText() + arrowInfo
      : "";
    return super.getMessage() + messageInfo;
  }

  /**
   * Get the message which caused this exception.
   *
   * @return the original message
   * @since 4.2.0
   */
  public @Nullable String originalText() {
    return this.originalText;
  }

  /**
   * Set the message which caused this exception.
   *
   * @param originalText the original message
   * @since 4.2.0
   */
  public void originalText(final @NotNull String originalText) {
    this.originalText = originalText;
  }

  /**
   * Gets the tokens associated with this parsing error.
   *
   * @return the tokens for this error
   * @since 4.1.0
   */
  public @NonNull Token @NonNull [] tokens() {
    return this.tokens;
  }

  /**
   * Sets the tokens associated with this parsing error.
   *
   * @param tokens the tokens for this error
   * @since 4.2.0
   */
  public void tokens(final @NonNull Token @NonNull [] tokens) {
    this.tokens = tokens;
  }

  private String arrow() {
    final @NonNull Token[] ts = this.tokens();
    final char[] chars = new char[ts[ts.length - 1].endIndex()];

    int i = 0;
    for (final Token t : ts) {
      Arrays.fill(chars, i, t.startIndex(), ' ');
      chars[t.startIndex()] = '^';
      Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
      chars[t.endIndex() - 1] = '^';
      i = t.endIndex();
    }
    return new String(chars);
  }
}
