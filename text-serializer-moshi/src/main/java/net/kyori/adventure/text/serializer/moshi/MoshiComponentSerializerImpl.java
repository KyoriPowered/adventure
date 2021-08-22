/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.moshi;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MoshiComponentSerializerImpl implements MoshiComponentSerializer {
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  // We cannot store these fields in MoshiComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final MoshiComponentSerializer INSTANCE = SERVICE
      .map(Provider::moshi)
      .orElseGet(() -> new MoshiComponentSerializerImpl(false, null, false));
    static final MoshiComponentSerializer LEGACY_INSTANCE = SERVICE
      .map(Provider::moshiLegacy)
      .orElseGet(() -> new MoshiComponentSerializerImpl(true, null, true));
  }

  private final Moshi serializer;
  private final UnaryOperator<Moshi.Builder> populator;
  private final boolean downsampleColor;
  private final @Nullable LegacyHoverEventSerializer legacyHoverSerializer;
  private final boolean emitLegacyHover;

  MoshiComponentSerializerImpl(final boolean downsampleColor, final @Nullable LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
    this.downsampleColor = downsampleColor;
    this.legacyHoverSerializer = legacyHoverSerializer;
    this.emitLegacyHover = emitLegacyHover;
    this.populator = builder -> {
      builder.add(new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
      return builder;
    };
    this.serializer = this.populator.apply(new Moshi.Builder()).build();
  }

  @Override
  public @NotNull Moshi serializer() {
    return this.serializer;
  }

  @Override
  public @NotNull UnaryOperator<Moshi.Builder> populator() {
    return this.populator;
  }

  @Override
  public @NotNull Component deserialize(final @NotNull String input) {
    try {
      final Component component = this.serializer().adapter(SerializerFactory.COMPONENT_TYPE).fromJson(input);
      if (component == null) throw ComponentAdapter.notSureHowToDeserialize(input);
      return component;
    } catch (final IOException exception) {
      throw ComponentAdapter.notSureHowToDeserialize(input);
    }
  }

  @Override
  public @NotNull String serialize(final @NotNull Component component) {
    return this.serializer().adapter(SerializerFactory.COMPONENT_TYPE).toJson(component);
  }

  @Override
  public @NotNull Component deserializeFromTree(final @NotNull Object input) {
    final Component component = this.serializer().adapter(SerializerFactory.COMPONENT_TYPE).fromJsonValue(input);
    if (component == null) throw ComponentAdapter.notSureHowToDeserialize(input);
    return component;
  }

  @Override
  public @NotNull Object serializeToTree(final @NotNull Component component) {
    return Objects.requireNonNull(this.serializer().adapter(SerializerFactory.COMPONENT_TYPE).toJsonValue(component));
  }

  @Override
  public @NotNull MoshiComponentSerializer.Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private boolean downsampleColor = false;
    private @Nullable LegacyHoverEventSerializer legacyHoverSerializer;
    private boolean emitLegacyHover = false;

    BuilderImpl() {
      BUILDER.accept(this);
    }

    BuilderImpl(final MoshiComponentSerializerImpl serializer) {
      this();
      this.downsampleColor = serializer.downsampleColor;
      this.emitLegacyHover = serializer.emitLegacyHover;
      this.legacyHoverSerializer = serializer.legacyHoverSerializer;
    }

    @Override
    public @NotNull Builder downsampleColors() {
      this.downsampleColor = true;
      return this;
    }

    @Override
    public @NotNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer) {
      this.legacyHoverSerializer = serializer;
      return this;
    }

    @Override
    public @NotNull Builder emitLegacyHoverEvent() {
      this.emitLegacyHover = true;
      return this;
    }

    @Override
    public @NotNull MoshiComponentSerializer build() {
      if (this.legacyHoverSerializer == null) {
        return this.downsampleColor ? Instances.LEGACY_INSTANCE : Instances.INSTANCE;
      } else {
        return new MoshiComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
      }
    }
  }
}
