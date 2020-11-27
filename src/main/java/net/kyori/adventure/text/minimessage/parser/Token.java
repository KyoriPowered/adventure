/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A parsed token from the lexer.
 *
 * @since 4.1.0
 */
public final class Token implements Examinable {
  // TODO token should have a char pointer/counter to point to the index where it begins, for nice error messages
  private final String value;
  private final TokenType type;

  /**
   * Create a new value-less token of the provided type.
   *
   * @param type the token type
   * @since 4.1.0
   */
  public Token(final TokenType type) {
    this(type, type.value());
  }

  /**
   * Create a new plain text token of the provided value.
   *
   * @param value the content of the token
   * @since 4.1.0
   */
  public Token(final String value) {
    this(TokenType.STRING, value);
  }

  /**
   * Create a new token of the provided type with a customized value.
   *
   * @param type the type of token
   * @param value the contents of the token
   * @since 4.1.0
   */
  public Token(final TokenType type, final String value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Get the type of token matched.
   *
   * @return the token type
   * @since 4.1.0
   */
  public TokenType type() {
    return this.type;
  }

  /**
   * Get the literal matched value.
   *
   * <p>This may be different than the {@link TokenType#value() default value} for the token type</p>
   *
   * @return the token value
   * @since 4.1.0
   */
  public String value() {
    return this.value;
  }

  /**
   * Test if a token list contains one single string token.
   *
   * @param tokens tokens to test
   * @return if the token list contains only one single string
   * @since 4.1.0
   */
  public static boolean oneString(final List<Token> tokens) {
    return tokens.size() == 1 && tokens.get(0).type() == TokenType.STRING;
  }

  /**
   * Get the plain-text values of tokens joined together.
   *
   * @param args the tokens
   * @return a joined token string
   * @since 4.1.0
   */
  public static String asValueString(final List<Token> args) {
    return args.stream().map(Token::value).collect(Collectors.joining());
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("type", this.type),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
