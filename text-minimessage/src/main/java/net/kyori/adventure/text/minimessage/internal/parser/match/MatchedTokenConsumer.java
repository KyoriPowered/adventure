/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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

import net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * A consumer of a region of a string that was identified as a token.
 *
 * @param <T> the return result
 * @since 4.10.0
 */
public abstract class MatchedTokenConsumer<T> {
  protected final String input;

  private int lastIndex = -1;

  /**
   * Creates a new matched token consumer.
   *
   * @param input the input
   * @since 4.10.0
   */
  public MatchedTokenConsumer(final @NotNull String input) {
    this.input = input;
  }

  /**
   * Accepts a matched token.
   *
   * @param start     the start of the token
   * @param end       the end of the token
   * @param tokenType the type of the token
   * @since 4.10.0
   */
  @MustBeInvokedByOverriders
  public void accept(final int start, final int end, final @NotNull TokenType tokenType) {
    this.lastIndex = end;
  }

  /**
   * Gets the result of this consumer, if any.
   *
   * @return the result
   * @since 4.10.0
   */
  public abstract @UnknownNullability T result();

  /**
   * The last accepted end index, or {@code -1} if no match has been accepted.
   *
   * @return the last accepted end index
   * @since 4.10.0
   */
  public final int lastEndIndex() {
    return this.lastIndex;
  }
}
