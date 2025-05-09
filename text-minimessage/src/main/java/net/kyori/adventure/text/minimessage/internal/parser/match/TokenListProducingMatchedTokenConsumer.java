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
package net.kyori.adventure.text.minimessage.internal.parser.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.NotNull;

/**
 * A matched token consumer that produces a list of matched tokens.
 *
 * @since 4.10.0
 */
public final class TokenListProducingMatchedTokenConsumer extends MatchedTokenConsumer<List<Token>> {
  private List<Token> result = null;

  /**
   * Creates a new token list producing matched token consumer.
   *
   * @param input the input
   * @since 4.10.0
   */
  public TokenListProducingMatchedTokenConsumer(final @NotNull String input) {
    super(input);
  }

  @Override
  public void accept(final int start, final int end, final @NotNull TokenType tokenType) {
    super.accept(start, end, tokenType);

    if (this.result == null) {
      this.result = new ArrayList<>();
    }

    this.result.add(new Token(start, end, tokenType));
  }

  @Override
  public @NotNull List<Token> result() {
    return this.result == null ? Collections.emptyList() : this.result;
  }
}
