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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a token for the lexer.
 *
 * @since 4.10.0
 */
public final class Token implements Examinable {
  private final int startIndex;
  private final int endIndex;
  private final TokenType type;

  private List<Token> childTokens = null;

  /**
   * Creates a new token.
   *
   * @param startIndex the start index of the token
   * @param endIndex the end index of the token
   * @param type the type of the token
   * @since 4.10.0
   */
  public Token(final int startIndex, final int endIndex, final TokenType type) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.type = type;
  }

  /**
   * Returns the start index of this token.
   *
   * @return the start index
   * @since 4.10.0
   */
  public int startIndex() {
    return this.startIndex;
  }

  /**
   * Returns the end index of this token.
   *
   * @return the end index
   * @since 4.10.0
   */
  public int endIndex() {
    return this.endIndex;
  }

  /**
   * Returns the type of this token.
   *
   * @return the type
   * @since 4.10.0
   */
  public TokenType type() {
    return this.type;
  }

  /**
   * Returns the children of this token.
   *
   * @return the child tokens
   * @since 4.10.0
   */
  public List<Token> childTokens() {
    return this.childTokens;
  }

  /**
   * Sets the children of this token.
   *
   * @param childTokens the new children
   * @since 4.10.0
   */
  public void childTokens(final List<Token> childTokens) {
    this.childTokens = childTokens;
  }

  /**
   * Get the value of this token from the complete message.
   *
   * @param message the message to read
   * @return the value of this token
   * @since 4.10.0
   */
  public CharSequence get(final CharSequence message) {
    return message.subSequence(this.startIndex, this.endIndex);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("startIndex", this.startIndex),
      ExaminableProperty.of("endIndex", this.endIndex),
      ExaminableProperty.of("type", this.type)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof Token)) return false;
    final Token that = (Token) other;
    return this.startIndex == that.startIndex && this.endIndex == that.endIndex && this.type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.startIndex, this.endIndex, this.type);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }
}
