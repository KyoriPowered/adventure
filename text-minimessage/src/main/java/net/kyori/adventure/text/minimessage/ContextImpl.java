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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.Tag.Argument;
import net.kyori.adventure.text.minimessage.tag.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Carries needed context for minimessage around, ranging from debug info to the
 * configured minimessage instance.
 *
 * @since 4.10.0
 */
class ContextImpl implements Context {
  private final boolean strict;
  private final Consumer<String> debugOutput;
  private final String originalMessage;
  private final MiniMessage miniMessage;
  private final TagResolver tagResolver;
  private final UnaryOperator<Component> postProcessor;

  ContextImpl(
    final boolean strict,
    final Consumer<String> debugOutput,
    final String originalMessage,
    final MiniMessage miniMessage,
    final @NotNull TagResolver extraTags,
    final UnaryOperator<Component> postProcessor
  ) {
    this.strict = strict;
    this.debugOutput = debugOutput;
    this.originalMessage = originalMessage;
    this.miniMessage = miniMessage;
    this.tagResolver = extraTags;
    this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
  }

  static ContextImpl of(
    final boolean strict,
    final Consumer<String> debugOutput,
    final String input,
    final MiniMessageImpl miniMessage,
    final TagResolver extraTags,
    final UnaryOperator<Component> postProcessor
  ) {
    return new ContextImpl(strict, debugOutput, input, miniMessage, extraTags, postProcessor);
  }

  public boolean strict() {
    return this.strict;
  }

  public Consumer<String> debugOutput() {
    return this.debugOutput;
  }

  @Override
  public @NotNull String originalMessage() {
    return this.originalMessage;
  }

  public @NotNull TagResolver extraTags() {
    return this.tagResolver;
  }

  public UnaryOperator<Component> postProcessor() {
    return this.postProcessor;
  }

  @Override
  public @NotNull Component parse(final @NotNull String message) {
    return this.miniMessage.deserialize(requireNonNull(message, "message"), this.tagResolver);
  }

  @Override
  public ParsingException newError(final String message, final @NotNull List<? extends Argument> tags) {
    return new ParsingExceptionImpl(message, this.originalMessage, tagsToTokens(tags));
  }

  @Override
  public ParsingException newError(final String message, final @Nullable Throwable cause, final @NotNull List<? extends Argument> tags) {
    return new ParsingExceptionImpl(message, this.originalMessage, cause, tagsToTokens(tags));
  }

  private static Token[] tagsToTokens(final List<? extends Tag.Argument> tags) {
    final Token[] tokens = new Token[tags.size()];
    for (int i = 0, length = tokens.length; i < length; i++) {
      tokens[i] = ((TagPart) tags.get(i)).token();
    }
    return tokens;
  }
}
