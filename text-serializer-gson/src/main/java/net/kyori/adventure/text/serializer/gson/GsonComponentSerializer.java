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
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A gson component serializer.
 *
 * <p>Use {@link Builder#downsampleColors()} to support platforms
 * that do not understand hex colors that were introduced in Minecraft 1.16.</p>
 *
 * @since 4.0.0
 */
public interface GsonComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder> {
  /**
   * Gets a component serializer for gson serialization and deserialization.
   *
   * @return a gson component serializer
   * @since 4.0.0
   */
  static @NotNull GsonComponentSerializer gson() {
    return GsonComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Gets a component serializer for gson serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a gson component serializer
   * @since 4.0.0
   */
  static @NotNull GsonComponentSerializer colorDownsamplingGson() {
    return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
  }

  /**
   * Creates a new {@link GsonComponentSerializer.Builder}.
   *
   * @return a builder
   * @since 4.0.0
   */
  static Builder builder() {
    return new GsonComponentSerializerImpl.BuilderImpl();
  }

  /**
   * Gets the underlying gson serializer.
   *
   * @return a gson serializer
   * @since 4.0.0
   */
  @NotNull Gson serializer();

  /**
   * Gets the underlying gson populator.
   *
   * @return a gson populator
   * @since 4.0.0
   */
  @NotNull UnaryOperator<GsonBuilder> populator();

  /**
   * Deserialize a component from input of type {@link JsonElement}.
   *
   * @param input the input
   * @return the component
   * @since 4.7.0
   */
  @NotNull Component deserializeFromTree(final @NotNull JsonElement input);

  /**
   * Deserialize a component to output of type {@link JsonElement}.
   *
   * @param component the component
   * @return the json element
   * @since 4.7.0
   */
  @NotNull JsonElement serializeToTree(final @NotNull Component component);

  /**
   * A builder for {@link GsonComponentSerializer}.
   *
   * @since 4.0.0
   */
  interface Builder extends AbstractBuilder<GsonComponentSerializer>, Buildable.Builder<GsonComponentSerializer>, ComponentSerializer.Builder<Component, Component, String> {
    /**
     * Sets that the serializer should downsample hex colors to named colors.
     *
     * @return this builder
     * @since 4.0.0
     */
    @NotNull Builder downsampleColors();

    /**
     * Sets a serializer that will be used to interpret legacy hover event {@code value} payloads.
     * If the serializer is {@code null}, then only {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}
     * legacy hover events can be deserialized.
     *
     * @param serializer serializer
     * @return this builder
     * @since 4.0.0
     */
    @NotNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer);

    /**
     * Output a legacy hover event {@code value} in addition to the modern {@code contents}.
     *
     * <p>A {@link #legacyHoverEventSerializer(LegacyHoverEventSerializer) legacy hover serializer} must also be set
     * to serialize any hover events beyond those with action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}</p>
     *
     * @return this builder
     * @since 4.0.0
     */
    @NotNull Builder emitLegacyHoverEvent();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull Builder postProcessor(final @NotNull UnaryOperator<Component> postProcessor);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull Builder preProcessor(final @NotNull UnaryOperator<String> preProcessor);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NotNull GsonComponentSerializer build();
  }

  /**
   * A {@link GsonComponentSerializer} service provider.
   *
   * @since 4.8.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link GsonComponentSerializer}.
     *
     * @return a {@link GsonComponentSerializer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull GsonComponentSerializer gson();

    /**
     * Provides a legacy {@link GsonComponentSerializer}.
     *
     * @return a {@link GsonComponentSerializer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull GsonComponentSerializer gsonLegacy();

    /**
     * Completes the building process of {@link Builder}.
     *
     * @return a {@link Consumer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull Consumer<Builder> builder();
  }
}
