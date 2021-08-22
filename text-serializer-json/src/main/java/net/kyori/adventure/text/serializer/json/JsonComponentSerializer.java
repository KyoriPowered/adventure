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
package net.kyori.adventure.text.serializer.json;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A generic JSON component serializer.
 *
 * <p>Use {@link Builder#downsampleColors()} to support platforms
 * that do not understand hex colors that were introduced in Minecraft 1.16.</p>
 *
 * @since 4.9.0
 */
public interface JsonComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<JsonComponentSerializer, JsonComponentSerializer.Builder> {
  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * @return a JSON component serializer
   * @since 4.9.0
   */
  static @NotNull JsonComponentSerializer json() {
    return Instances.INSTANCE.orElseThrow(() -> new IllegalStateException("A provider for JsonComponentSerializer was not found!"));
  }

  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a JSON component serializer
   * @since 4.9.0
   */
  static @NotNull JsonComponentSerializer colorDownsamplingJson() {
    return Instances.LEGACY_INSTANCE.orElseThrow(() -> new IllegalStateException("A provider for JsonComponentSerializer was not found!"));
  }

  /**
   * Creates a new {@link JsonComponentSerializer.Builder}.
   *
   * @return a builder
   * @since 4.9.0
   */
  static Builder builder() {
    return Instances.builder().orElseThrow(() -> new IllegalStateException("A provider for JsonComponentSerializer was not found!"));
  }

  /**
   * A builder for {@link JsonComponentSerializer}.
   *
   * @since 4.9.0
   */
  interface Builder extends Buildable.Builder<JsonComponentSerializer> {
    /**
     * Sets that the serializer should downsample hex colors to named colors.
     *
     * @return this builder
     * @since 4.9.0
     */
    @NotNull Builder downsampleColors();

    /**
     * Sets a serializer that will be used to interpret legacy hover event {@code value} payloads.
     * If the serializer is {@code null}, then only {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}
     * legacy hover events can be deserialized.
     *
     * @param serializer serializer
     * @return this builder
     * @since 4.9.0
     */
    @NotNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer);

    /**
     * Output a legacy hover event {@code value} in addition to the modern {@code contents}.
     *
     * <p>A {@link #legacyHoverEventSerializer(LegacyHoverEventSerializer) legacy hover serializer} must also be set
     * to serialize any hover events beyond those with action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}</p>
     *
     * @return this builder
     * @since 4.9.0
     */
    @NotNull Builder emitLegacyHoverEvent();

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NotNull JsonComponentSerializer build();
  }

  /**
   * A {@link JsonComponentSerializer} service provider.
   *
   * @since 4.9.0
   */
  @ApiStatus.Internal
  interface Provider {
    /**
     * Provides a standard {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull JsonComponentSerializer json();

    /**
     * Provides a legacy {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull JsonComponentSerializer jsonLegacy();

    /**
     * Provides a new {@link Builder}.
     *
     * @return a {@link Builder}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull Builder builder();
  }
}
