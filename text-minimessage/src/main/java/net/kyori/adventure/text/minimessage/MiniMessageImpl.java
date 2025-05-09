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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * not public api.
 *
 * @since 4.10.0
 */
final class MiniMessageImpl implements MiniMessage {
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  // We cannot store these fields in MiniMessageImpl directly due to class initialisation issues.
  static final class Instances {
    static final MiniMessage INSTANCE = SERVICE
      .map(Provider::miniMessage)
      .orElseGet(() -> new MiniMessageImpl(TagResolver.standard(), false, true, null, DEFAULT_NO_OP, DEFAULT_COMPACTING_METHOD));
  }

  static final UnaryOperator<String> DEFAULT_NO_OP = UnaryOperator.identity();
  static final UnaryOperator<Component> DEFAULT_COMPACTING_METHOD = Component::compact;

  private final boolean strict;
  private final boolean emitVirtuals;
  private final @Nullable Consumer<String> debugOutput;
  private final UnaryOperator<Component> postProcessor;
  private final UnaryOperator<String> preProcessor;
  final MiniMessageParser parser;

  MiniMessageImpl(final @NotNull TagResolver resolver, final boolean strict, final boolean emitVirtuals, final @Nullable Consumer<String> debugOutput, final @NotNull UnaryOperator<String> preProcessor, final @NotNull UnaryOperator<Component> postProcessor) {
    this.parser = new MiniMessageParser(resolver);
    this.strict = strict;
    this.emitVirtuals = emitVirtuals;
    this.debugOutput = debugOutput;
    this.preProcessor = preProcessor;
    this.postProcessor = postProcessor;
  }

  @Override
  public @NotNull Component deserialize(final @NotNull String input) {
    return this.parser.parseFormat(this.newContext(input, null, null));
  }

  @Override
  public @NotNull Component deserialize(final @NotNull String input, final @NotNull Pointered target) {
    return this.parser.parseFormat(this.newContext(input, requireNonNull(target, "target"), null));
  }

  @Override
  public @NotNull Component deserialize(final @NotNull String input, final @NotNull TagResolver tagResolver) {
    return this.parser.parseFormat(this.newContext(input, null, requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public @NotNull Component deserialize(final @NotNull String input, final @NotNull Pointered target, final @NotNull TagResolver tagResolver) {
    return this.parser.parseFormat(this.newContext(input, requireNonNull(target, "target"), requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public Node.@NotNull Root deserializeToTree(final @NotNull String input) {
    return this.parser.parseToTree(this.newContext(input, null, null));
  }

  @Override
  public Node.@NotNull Root deserializeToTree(final @NotNull String input, final @NotNull Pointered target) {
    return this.parser.parseToTree(this.newContext(input, requireNonNull(target, "target"), null));
  }

  @Override
  public Node.@NotNull Root deserializeToTree(final @NotNull String input, final @NotNull TagResolver tagResolver) {
    return this.parser.parseToTree(this.newContext(input, null, requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public Node.@NotNull Root deserializeToTree(final @NotNull String input, final @NotNull Pointered target, final @NotNull TagResolver tagResolver) {
    return this.parser.parseToTree(this.newContext(input, requireNonNull(target, "target"), requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    return MiniMessageSerializer.serialize(component, this.serialResolver(null), this.strict);
  }

  private SerializableResolver serialResolver(final @Nullable TagResolver extraResolver) {
    if (extraResolver == null) {
      if (this.parser.tagResolver instanceof SerializableResolver) {
        return (SerializableResolver) this.parser.tagResolver;
      }
    } else {
      final TagResolver combined = TagResolver.resolver(this.parser.tagResolver, extraResolver);
      if (combined instanceof SerializableResolver) {
        return (SerializableResolver) combined;
      }
    }

    return (SerializableResolver) TagResolver.empty();
  }

  @Override
  public @NotNull String escapeTags(final @NotNull String input) {
    return this.parser.escapeTokens(this.newContext(input, null, null));
  }

  @Override
  public @NotNull String escapeTags(final @NotNull String input, final @NotNull TagResolver tagResolver) {
    return this.parser.escapeTokens(this.newContext(input, null, tagResolver));
  }

  @Override
  public @NotNull String stripTags(final @NotNull String input) {
    return this.parser.stripTokens(this.newContext(input, null, null));
  }

  @Override
  public @NotNull String stripTags(final @NotNull String input, final @NotNull TagResolver tagResolver) {
    return this.parser.stripTokens(this.newContext(input, null, tagResolver));
  }

  @Override
  public boolean strict() {
    return this.strict;
  }

  @Override
  public @NotNull TagResolver tags() {
    return this.parser.tagResolver;
  }

  private @NotNull ContextImpl newContext(final @NotNull String input, final @Nullable Pointered target, final @Nullable TagResolver resolver) {
    requireNonNull(input, "input");
    return new ContextImpl(this.strict, this.emitVirtuals, this.debugOutput, input, this, target, resolver, this.preProcessor, this.postProcessor);
  }

  static final class BuilderImpl implements Builder {
    private TagResolver tagResolver = TagResolver.standard();
    private boolean strict = false;
    private boolean emitVirtuals = true;
    private Consumer<String> debug = null;
    private UnaryOperator<Component> postProcessor = DEFAULT_COMPACTING_METHOD;
    private UnaryOperator<String> preProcessor = DEFAULT_NO_OP;

    BuilderImpl() {
      BUILDER.accept(this);
    }

    BuilderImpl(final MiniMessageImpl serializer) {
      this();
      this.tagResolver = serializer.parser.tagResolver;
      this.strict = serializer.strict;
      this.debug = serializer.debugOutput;
      this.postProcessor = serializer.postProcessor;
      this.preProcessor = serializer.preProcessor;
    }

    @Override
    public @NotNull Builder tags(final @NotNull TagResolver tags) {
      this.tagResolver = requireNonNull(tags, "tags");
      return this;
    }

    @Override
    public @NotNull Builder editTags(final @NotNull Consumer<TagResolver.Builder> adder) {
      requireNonNull(adder, "adder");
      final TagResolver.Builder builder = TagResolver.builder().resolver(this.tagResolver);
      adder.accept(builder);
      this.tagResolver = builder.build();
      return this;
    }

    @Override
    public @NotNull Builder strict(final boolean strict) {
      this.strict = strict;
      return this;
    }

    @Override
    public @NotNull Builder emitVirtuals(final boolean emitVirtuals) {
      this.emitVirtuals = emitVirtuals;
      return this;
    }

    @Override
    public @NotNull Builder debug(final @Nullable Consumer<String> debugOutput) {
      this.debug = debugOutput;
      return this;
    }

    @Override
    public @NotNull Builder postProcessor(final @NotNull UnaryOperator<Component> postProcessor) {
      this.postProcessor = Objects.requireNonNull(postProcessor, "postProcessor");
      return this;
    }

    @Override
    public @NotNull Builder preProcessor(final @NotNull UnaryOperator<String> preProcessor) {
      this.preProcessor = Objects.requireNonNull(preProcessor, "preProcessor");
      return this;
    }

    @Override
    public @NotNull MiniMessage build() {
      return new MiniMessageImpl(this.tagResolver, this.strict, this.emitVirtuals, this.debug, this.preProcessor, this.postProcessor);
    }
  }
}
