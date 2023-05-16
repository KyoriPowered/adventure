/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.plain;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class PlainTextComponentSerializerImpl implements PlainTextComponentSerializer {
  private static final ComponentFlattener DEFAULT_FLATTENER = ComponentFlattener.basic().toBuilder()
    .unknownMapper(component -> {
      throw new UnsupportedOperationException("Don't know how to turn " + component.getClass().getSimpleName() + " into a string");
    })
    .build();
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::plainText)
    .orElseGet(() -> builder -> {
      // NOOP
    });
  final ComponentFlattener flattener;
  private final UnaryOperator<Component> postProcessor;
  private final UnaryOperator<String> preProcessor;

  // We cannot store these fields in PlainTextComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final PlainTextComponentSerializer INSTANCE = SERVICE
      .map(Provider::plainTextSimple)
      .orElseGet(() -> new PlainTextComponentSerializerImpl(DEFAULT_FLATTENER, ComponentSerializer.Builder.DEFAULT_NO_OP, ComponentSerializer.Builder.DEFAULT_COMPACTING_METHOD));
  }

  PlainTextComponentSerializerImpl(final ComponentFlattener flattener, final @NotNull UnaryOperator<String> preProcessor, final @NotNull UnaryOperator<Component> postProcessor) {
    this.flattener = flattener;
    this.preProcessor = preProcessor;
    this.postProcessor = postProcessor;
  }

  @Override
  public void serialize(final @NotNull StringBuilder sb, final @NotNull Component component) {
    this.flattener.flatten(this.postProcessor.apply(requireNonNull(component, "component")), sb::append);
  }

  @Override
  public @NotNull TextComponent deserialize(final @NotNull String input) {
    return Component.text(this.preProcessor.apply(input));
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements PlainTextComponentSerializer.Builder {
    private ComponentFlattener flattener = DEFAULT_FLATTENER;
    private UnaryOperator<Component> postProcessor = DEFAULT_COMPACTING_METHOD;
    private UnaryOperator<String> preProcessor = DEFAULT_NO_OP;

    BuilderImpl() {
      BUILDER.accept(this);
    }

    BuilderImpl(final PlainTextComponentSerializerImpl serializer) {
      this();
      this.flattener = serializer.flattener;
      this.postProcessor = serializer.postProcessor;
      this.preProcessor = serializer.preProcessor;
    }

    @Override
    public PlainTextComponentSerializer.@NotNull Builder flattener(final @NotNull ComponentFlattener flattener) {
      this.flattener = requireNonNull(flattener, "flattener");
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
    public @NotNull PlainTextComponentSerializer build() {
      return new PlainTextComponentSerializerImpl(this.flattener, this.preProcessor, this.postProcessor);
    }
  }
}
