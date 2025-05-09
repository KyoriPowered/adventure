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
package net.kyori.adventure.text.minimessage.internal.parser;

import java.util.Arrays;
import net.kyori.adventure.text.minimessage.ParsingException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An exception that happens while parsing.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public class ParsingExceptionImpl extends ParsingException {
  private static final long serialVersionUID = 2507190809441787202L;

  private final String originalText;
  private Token @NotNull [] tokens;

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the original text which was parsed
   * @param tokens the token which caused the error
   * @since 4.10.0
   */
  public ParsingExceptionImpl(
    final String message,
    final @Nullable String originalText,
    final @NotNull Token @NotNull ... tokens
  ) {
    super(message, null, true, false);
    this.tokens = tokens;
    this.originalText = originalText;
  }

  /**
   * Create a new parsing exception.
   *
   * @param message the detail message
   * @param originalText the original text which was parsed
   * @param cause the cause
   * @param withStackTrace whether to generate a stacktrace
   * @param tokens the token which caused the error
   * @since 4.10.0
   */
  public ParsingExceptionImpl(
    final String message,
    final @Nullable String originalText,
    final @Nullable Throwable cause,
    final boolean withStackTrace,
    final @NotNull Token @NotNull ... tokens
  ) {
    super(message, cause, true, withStackTrace);
    this.tokens = tokens;
    this.originalText = originalText;
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

  @Override
  public @Nullable String detailMessage() {
    return super.getMessage();
  }

  /**
   * Get the message which caused this exception.
   *
   * @return the original message
   * @since 4.10.0
   */
  @Override
  public @Nullable String originalText() {
    return this.originalText;
  }

  /**
   * Gets the tokens associated with this parsing error.
   *
   * @return the tokens for this error
   * @since 4.10.0
   */
  public @NotNull Token @NotNull [] tokens() {
    return this.tokens;
  }

  /**
   * Sets the tokens associated with this parsing error.
   *
   * @param tokens the tokens for this error
   * @since 4.10.0
   */
  public void tokens(final @NotNull Token @NotNull [] tokens) {
    this.tokens = tokens;
  }

  private String arrow() {
    final @NotNull Token[] ts = this.tokens();
    final char[] chars = new char[ts[ts.length - 1].endIndex()];

    int i = 0;
    for (final Token t : ts) {
      Arrays.fill(chars, i, t.startIndex(), ' ');
      chars[t.startIndex()] = '^';
      if (Math.abs(t.startIndex() - t.endIndex()) > 1) {
        Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
      }
      chars[t.endIndex() - 1] = '^';
      i = t.endIndex();
    }
    return new String(chars);
  }

  @Override
  public int startIndex() {
    if (this.tokens.length == 0) return LOCATION_UNKNOWN;
    return this.tokens[0].startIndex();
  }

  @Override
  public int endIndex() {
    if (this.tokens.length == 0) return LOCATION_UNKNOWN;
    return this.tokens[this.tokens.length - 1].endIndex();
  }
}
