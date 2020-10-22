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
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Token implements Examinable {
  private final String value;
  private final TokenType type;

  public Token(final TokenType type) {
    this.type = type;
    this.value = type.value();
  }

  public Token(final String value) {
    this.type = TokenType.STRING;
    this.value = value;
  }

  public Token(final TokenType type, final String value) {
    this.type = type;
    this.value = value;
  }

  public TokenType type() {
    return this.type;
  }

  public String value() {
    return this.value;
  }

  public static boolean oneString(final List<Token> tokens) {
    return tokens.size() == 1 && tokens.get(0).type() == TokenType.STRING;
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
