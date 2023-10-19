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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Services;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class GsonComponentSerializerImpl implements GsonComponentSerializer {
  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElseGet(() -> builder -> {
      // NOOP
    });

  // We cannot store these fields in GsonComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final GsonComponentSerializer INSTANCE = SERVICE
      .map(Provider::gson)
      .orElseGet(() -> new GsonComponentSerializerImpl(false, null, false));
    static final GsonComponentSerializer LEGACY_INSTANCE = SERVICE
      .map(Provider::gsonLegacy)
      .orElseGet(() -> new GsonComponentSerializerImpl(true, null, true));
  }

  private final Gson serializer;
  private final UnaryOperator<GsonBuilder> populator;
  private final boolean downsampleColor;
  private final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer;
  private final boolean emitLegacyHover;

  GsonComponentSerializerImpl(final boolean downsampleColor, final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
    this.downsampleColor = downsampleColor;
    this.legacyHoverSerializer = legacyHoverSerializer;
    this.emitLegacyHover = emitLegacyHover;
    this.populator = builder -> {
      builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
      return builder;
    };
    this.serializer = this.populator.apply(
      new GsonBuilder()
        .disableHtmlEscaping() // to be consistent with vanilla
    ).create();
  }

  @Override
  public Gson serializer() {
    return this.serializer;
  }

  @Override
  public UnaryOperator<GsonBuilder> populator() {
    return this.populator;
  }

  @Override
  public Component deserialize(final String string) {
    final Component component = this.serializer().fromJson(string, Component.class);
    if (component == null) throw ComponentSerializerImpl.notSureHowToDeserialize(string);
    return component;
  }

  @Override
  public @Nullable Component deserializeOr(final @Nullable String input, final @Nullable Component fallback) {
    if (input == null) return fallback;
    final Component component = this.serializer().fromJson(input, Component.class);
    if (component == null) return fallback;
    return component;
  }

  @Override
  public String serialize(final Component component) {
    return this.serializer().toJson(component);
  }

  @Override
  public Component deserializeFromTree(final JsonElement input) {
    final Component component = this.serializer().fromJson(input, Component.class);
    if (component == null) throw ComponentSerializerImpl.notSureHowToDeserialize(input);
    return component;
  }

  @Override
  public JsonElement serializeToTree(final Component component) {
    return this.serializer().toJsonTree(component);
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private boolean downsampleColor = false;
    private net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHoverSerializer;
    private boolean emitLegacyHover = false;

    BuilderImpl() {
      BUILDER.accept(this); // let service provider touch the builder before anybody else touches it
    }

    BuilderImpl(final GsonComponentSerializerImpl serializer) {
      this();
      this.downsampleColor = serializer.downsampleColor;
      this.emitLegacyHover = serializer.emitLegacyHover;
      this.legacyHoverSerializer = serializer.legacyHoverSerializer;
    }

    @Override
    public Builder downsampleColors() {
      this.downsampleColor = true;
      return this;
    }

    @Override
    public Builder legacyHoverEventSerializer(final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer serializer) {
      this.legacyHoverSerializer = serializer;
      return this;
    }

    @Override
    public Builder emitLegacyHoverEvent() {
      this.emitLegacyHover = true;
      return this;
    }

    @Override
    public GsonComponentSerializer build() {
      if (this.legacyHoverSerializer == null) {
        return this.downsampleColor ? Instances.LEGACY_INSTANCE : Instances.INSTANCE;
      } else {
        return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
      }
    }
  }
}
