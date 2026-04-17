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

import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tree.Node;
import net.kyori.adventure.util.Services;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record MiniMessageImpl(
  MiniMessageParser parser,
  boolean strict,
  boolean emitVirtuals,
  @Nullable Consumer<String> debugOutput,
  UnaryOperator<String> preProcessor,
  UnaryOperator<Component> postProcessor
) implements MiniMessage {
  private static final Optional<Provider> SERVICE = Services.service(ServiceLoader.load(Provider.class, Provider.class.getClassLoader()), Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  // We cannot store these fields in MiniMessageImpl directly due to class initialisation issues.
  static final class Instances {
    static final MiniMessage INSTANCE = SERVICE
      .map(MiniMessage.Provider::miniMessage)
      .orElseGet(() -> new MiniMessageImpl(TagResolver.standard(), false, true, null, DEFAULT_NO_OP, DEFAULT_COMPACTING_METHOD));

    static final Map<MiniMessage.Preset, MiniMessage> PRESETS = MiniMessage.Preset
      .NAMES
      .values()
      .stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          preset -> {
            if (preset == Preset.DEFAULT) {
              return INSTANCE;
            } else {
              return MiniMessage.builder(preset).build();
            }
          }
        )
      );
  }

  static final UnaryOperator<String> DEFAULT_NO_OP = UnaryOperator.identity();
  static final UnaryOperator<Component> DEFAULT_COMPACTING_METHOD = Component::compact;

  MiniMessageImpl(final TagResolver parser, final boolean strict, final boolean emitVirtuals, final @Nullable Consumer<String> debugOutput, final UnaryOperator<String> preProcessor, final UnaryOperator<Component> postProcessor) {
    this(new MiniMessageParser(parser), strict, emitVirtuals, debugOutput, preProcessor, postProcessor);
  }

  @Override
  public Component deserialize(final String input) {
    return this.parser.parseFormat(this.newContext(input, null, null));
  }

  @Override
  public Component deserialize(final String input, final Pointered target) {
    return this.parser.parseFormat(this.newContext(input, requireNonNull(target, "target"), null));
  }

  @Override
  public Component deserialize(final String input, final TagResolver tagResolver) {
    return this.parser.parseFormat(this.newContext(input, null, requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public Component deserialize(final String input, final Pointered target, final TagResolver tagResolver) {
    return this.parser.parseFormat(this.newContext(input, requireNonNull(target, "target"), requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public Node.Root deserializeToTree(final String input) {
    return this.parser.parseToTree(this.newContext(input, null, null));
  }

  @Override
  public Node.Root deserializeToTree(final String input, final Pointered target) {
    return this.parser.parseToTree(this.newContext(input, requireNonNull(target, "target"), null));
  }

  @Override
  public Node.Root deserializeToTree(final String input, final TagResolver tagResolver) {
    return this.parser.parseToTree(this.newContext(input, null, requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public Node.Root deserializeToTree(final String input, final Pointered target, final TagResolver tagResolver) {
    return this.parser.parseToTree(this.newContext(input, requireNonNull(target, "target"), requireNonNull(tagResolver, "tagResolver")));
  }

  @Override
  public String serialize(final Component component) {
    return MiniMessageSerializer.serialize(component, this.serialResolver(null), this.strict);
  }

  private SerializableResolver serialResolver(final @Nullable TagResolver extraResolver) {
    if (extraResolver == null) {
      if (this.parser.tagResolver() instanceof SerializableResolver) {
        return (SerializableResolver) this.parser.tagResolver();
      }
    } else {
      final TagResolver combined = TagResolver.resolver(this.parser.tagResolver(), extraResolver);
      if (combined instanceof SerializableResolver serializableResolver) {
        return serializableResolver;
      }
    }

    return (SerializableResolver) TagResolver.empty();
  }

  @Override
  public String escapeTags(final String input) {
    return this.parser.escapeTokens(this.newContext(input, null, null));
  }

  @Override
  public String escapeTags(final String input, final TagResolver tagResolver) {
    return this.parser.escapeTokens(this.newContext(input, null, tagResolver));
  }

  @Override
  public String stripTags(final String input) {
    return this.parser.stripTokens(this.newContext(input, null, null));
  }

  @Override
  public String stripTags(final String input, final TagResolver tagResolver) {
    return this.parser.stripTokens(this.newContext(input, null, tagResolver));
  }

  @Override
  public TagResolver tags() {
    return this.parser.tagResolver();
  }

  private ContextImpl newContext(final String input, final @Nullable Pointered target, final @Nullable TagResolver resolver) {
    requireNonNull(input, "input");
    return new ContextImpl(this.strict, this.emitVirtuals, this.debugOutput, input, this, target, resolver, this.preProcessor, this.postProcessor);
  }

  static final class BuilderImpl implements Builder {
    private TagResolver tagResolver = TagResolver.standard();
    private boolean strict = false;
    private boolean emitVirtuals = true;
    private @Nullable Consumer<String> debug = null;
    private UnaryOperator<Component> postProcessor = DEFAULT_COMPACTING_METHOD;
    private UnaryOperator<String> preProcessor = DEFAULT_NO_OP;

    BuilderImpl() {
      BUILDER.accept(this);
    }

    BuilderImpl(final MiniMessageImpl serializer) {
      this();
      this.tagResolver = serializer.parser.tagResolver();
      this.strict = serializer.strict;
      this.debug = serializer.debugOutput;
      this.postProcessor = serializer.postProcessor;
      this.preProcessor = serializer.preProcessor;
    }

    @Override
    public Builder tags(final TagResolver tags) {
      this.tagResolver = requireNonNull(tags, "tags");
      return this;
    }

    @Override
    public Builder editTags(final Consumer<TagResolver.Builder> adder) {
      requireNonNull(adder, "adder");
      final TagResolver.Builder builder = TagResolver.builder().resolver(this.tagResolver);
      adder.accept(builder);
      this.tagResolver = builder.build();
      return this;
    }

    @Override
    public Builder strict(final boolean strict) {
      this.strict = strict;
      return this;
    }

    @Override
    public Builder emitVirtuals(final boolean emitVirtuals) {
      this.emitVirtuals = emitVirtuals;
      return this;
    }

    @Override
    public Builder debug(final @Nullable Consumer<String> debugOutput) {
      this.debug = debugOutput;
      return this;
    }

    @Override
    public Builder postProcessor(final UnaryOperator<Component> postProcessor) {
      this.postProcessor = requireNonNull(postProcessor, "postProcessor");
      return this;
    }

    @Override
    public Builder preProcessor(final UnaryOperator<String> preProcessor) {
      this.preProcessor = requireNonNull(preProcessor, "preProcessor");
      return this;
    }

    @Override
    public MiniMessage build() {
      return new MiniMessageImpl(this.tagResolver, this.strict, this.emitVirtuals, this.debug, this.preProcessor, this.postProcessor);
    }
  }
}
