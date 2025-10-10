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
package net.kyori.adventure.text.minimessage;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueueImpl;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Carries needed context for minimessage around, ranging from debug info to the
 * configured minimessage instance.
 *
 * @since 4.10.0
 */
final class ContextImpl implements Context {
  private static final Token[] EMPTY_TOKEN_ARRAY = new Token[0];

  private final boolean strict;
  private final boolean emitVirtuals;
  private final Consumer<String> debugOutput;
  private String message;
  private final MiniMessage miniMessage;
  private final @Nullable Pointered target;
  private final TagResolver tagResolver;
  private final UnaryOperator<String> preProcessor;
  private final UnaryOperator<Component> postProcessor;

  ContextImpl(
    final boolean strict,
    final boolean emitVirtuals,
    final Consumer<String> debugOutput,
    final String message,
    final MiniMessage miniMessage,
    final @Nullable Pointered target,
    final @Nullable TagResolver extraTags,
    final @Nullable UnaryOperator<String> preProcessor,
    final @Nullable UnaryOperator<Component> postProcessor
  ) {
    this.strict = strict;
    this.emitVirtuals = emitVirtuals;
    this.debugOutput = debugOutput;
    this.message = message;
    this.miniMessage = miniMessage;
    this.target = target;
    this.tagResolver = extraTags == null ? TagResolver.empty() : extraTags;
    this.preProcessor = preProcessor == null ? UnaryOperator.identity() : preProcessor;
    this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
  }

  public boolean strict() {
    return this.strict;
  }

  @Override
  public boolean emitVirtuals() {
    return this.emitVirtuals;
  }

  public Consumer<String> debugOutput() {
    return this.debugOutput;
  }

  public String message() {
    return this.message;
  }

  void message(final String message) {
    this.message = message;
  }

  public TagResolver extraTags() {
    return this.tagResolver;
  }

  public UnaryOperator<Component> postProcessor() {
    return this.postProcessor;
  }

  public UnaryOperator<String> preProcessor() {
    return this.preProcessor;
  }

  @Override
  public @Nullable Pointered target() {
    return this.target;
  }

  @Override
  public Pointered targetOrThrow() {
    if (this.target == null) {
      throw this.newException("A target is required for this deserialization attempt");
    } else {
      return this.target;
    }
  }

  @Override
  public <T extends Pointered> T targetAsType(final Class<T> targetClass) {
    if (requireNonNull(targetClass, "targetClass").isInstance(this.target)) {
      return targetClass.cast(this.target);
    } else {
      throw this.newException("A target with type " + targetClass.getSimpleName() + " is required for this deserialization attempt");
    }
  }

  @Override
  public Component deserialize(final String message) {
    return this.deserializeWithOptionalTarget(requireNonNull(message, "message"), this.tagResolver);
  }

  @Override
  public Component deserialize(final String message, final TagResolver resolver) {
    requireNonNull(message, "message");
    final TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolver(resolver).build();
    return this.deserializeWithOptionalTarget(message, combinedResolver);
  }

  @Override
  public Component deserialize(final String message, final TagResolver... resolvers) {
    requireNonNull(message, "message");
    final TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolvers(resolvers).build();
    return this.deserializeWithOptionalTarget(message, combinedResolver);
  }

  @Override
  public ParsingException newException(final String message) {
    return new ParsingExceptionImpl(message, this.message, null, false, EMPTY_TOKEN_ARRAY);
  }

  @Override
  public ParsingException newException(final String message, final ArgumentQueue tags) {
    return new ParsingExceptionImpl(message, this.message, null, false, tagsToTokens(((ArgumentQueueImpl<?>) tags).args));
  }

  @Override
  public ParsingException newException(final String message, final @Nullable Throwable cause, final ArgumentQueue tags) {
    return new ParsingExceptionImpl(message, this.message, cause, false, tagsToTokens(((ArgumentQueueImpl<?>) tags).args));
  }

  private Component deserializeWithOptionalTarget(final String message, final TagResolver tagResolver) {
    if (this.target != null) {
      return this.miniMessage.deserialize(message, this.target, tagResolver);
    } else {
      return this.miniMessage.deserialize(message, tagResolver);
    }
  }

  private static Token[] tagsToTokens(final List<? extends Tag.Argument> tags) {
    final Token[] tokens = new Token[tags.size()];
    for (int i = 0, length = tokens.length; i < length; i++) {
      tokens[i] = ((TagPart) tags.get(i)).token();
    }
    return tokens;
  }
}
