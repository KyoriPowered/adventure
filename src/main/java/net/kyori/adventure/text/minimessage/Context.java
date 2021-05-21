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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.Token;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

/**
 * Carries needed context for minimessage around, ranging from debug info to the configured minimessage instance
 *
 * @since 4.1.0
 */
public class Context {

  private final boolean strict;
  private List<Token> tokens;
  private final String ogMessage;
  private String replacedMessage;
  private final MiniMessageImpl miniMessage;
  private final @NonNull Template @Nullable [] templates;

  Context(final boolean strict, final List<Token> tokens, final String ogMessage, final String replacedMessage, final MiniMessageImpl miniMessage, final @NonNull Template @Nullable [] templates) {
    this.strict = strict;
    this.tokens = tokens;
    this.ogMessage = ogMessage;
    this.replacedMessage = replacedMessage;
    this.miniMessage = miniMessage;
    this.templates = templates;
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @return the debug context
   * @since 4.1.0
   */
  public static Context of(final boolean strict, final String input, final MiniMessageImpl miniMessage) {
    return new Context(strict, null, input, null, miniMessage, null);
  }

  /**
   * Init.
   *
   * @param strict if strict mode is enabled
   * @param input the input message
   * @param miniMessage the minimessage instance
   * @param templates the templates passed to minimessage
   * @return the debug context
   * @since 4.1.0
   */
  public static Context of(final boolean strict, final String input, final MiniMessageImpl miniMessage, @NonNull final Template @ Nullable[] templates) {
    return new Context(strict, null, input, null, miniMessage, templates);
  }

  /**
   * Sets tokens.
   *
   * @param tokens the tokens.
   * @since 4.1.0
   */
  public void tokens(final List<Token> tokens) {
    this.tokens = tokens;
  }

  /**
   * sets the replaced message.
   *
   * @param replacedMessage the replaced message
   * @since 4.1.0
   */
  public void replacedMessage(final String replacedMessage) {
    this.replacedMessage = replacedMessage;
  }

  /**
   * Returns strict mode.
   *
   * @return if strict mode is enabled
   * @since 4.1.0
   */
  public boolean isStrict() {
    return this.strict;
  }

  /**
   * Returns tokens.
   *
   * @return tokens
   * @since 4.1.0
   */
  public List<Token> tokens() {
    return this.tokens;
  }

  /**
   * Returns og message.
   *
   * @return ogMessage
   * @since 4.1.0
   */
  public String ogMessage() {
    return this.ogMessage;
  }

  /**
   * Returns replaced message.
   *
   * @return replacedMessage
   * @since 4.1.0
   */
  public String replacedMessage() {
    return this.replacedMessage;
  }

  /**
   * Returns minimessage.
   *
   * @return minimessage
   * @since 4.1.0
   */
  public MiniMessageImpl miniMessage() {
    return this.miniMessage;
  }

  /**
   * Parses a MiniMessage using all the settings of this context, including templates
   *
   * @param message the message to parse
   * @return the parsed message
   */
  public Component parse(String message) {
    if (this.templates != null) {
      return this.miniMessage.parse(message, this.templates);
    } else {
      return this.miniMessage.parse(message);
    }
  }
}
