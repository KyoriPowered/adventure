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
package net.kyori.adventure.text.serializer.json;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A JSON component serializer.
 *
 * <p>This serializer exposes a common superset of functionality across any number of JSON library implementations.
 * For more specialized operations, users may need to refer to specific implementations.</p>
 *
 * @since 4.14.0
 */
public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * @return a JSON component serializer
   * @since 4.14.0
   */
  static @NotNull JSONComponentSerializer json() {
    return JSONComponentSerializerAccessor.Instances.INSTANCE;
  }

  /**
   * Get a builder to create a customized JSON serializer instance.
   *
   * @return the new builder
   * @since 4.14.0
   */
  static JSONComponentSerializer.@NotNull Builder builder() {
    return JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
  }

  /**
   * A builder for {@link JSONComponentSerializer} instances that delegates to the active serializer.
   *
   * @since 4.14.0
   */
  interface Builder {
    /**
     * Set the option state to apply on this serializer.
     *
     * <p>This controls how the serializer emits and interprets components.</p>
     *
     * @param flags the flag set to use
     * @return this builder
     * @see JSONOptions
     * @since 4.15.0
     */
    @NotNull Builder options(final @NotNull OptionState flags);

    /**
     * Edit the active set of serializer options.
     *
     * @param optionEditor the consumer operating on the existing flag set
     * @return this builder
     * @see JSONOptions
     * @since  4.15.0
     */
    @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor);

    /**
     * Sets that the serializer should downsample hex colors to named colors.
     *
     * @return this builder
     * @since 4.14.0
     * @deprecated for removal since 4.15.0, change the {@link JSONOptions#EMIT_RGB} flag instead
     */
    @Deprecated
    @NotNull Builder downsampleColors();

    /**
     * Sets a serializer that will be used to interpret legacy hover event {@code value} payloads.
     * If the serializer is {@code null}, then only {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}
     * legacy hover events can be deserialized.
     *
     * @param serializer serializer
     * @return this builder
     * @since 4.14.0
     */
    @NotNull Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer);

    /**
     * Output a legacy hover event {@code value} in addition to the modern {@code contents}.
     *
     * <p>A {@link #legacyHoverEventSerializer(LegacyHoverEventSerializer) legacy hover serializer} must also be set
     * to serialize any hover events beyond those with action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}</p>
     *
     * @return this builder
     * @since 4.14.0
     * @deprecated for removal since 4.15.0, change the {@link JSONOptions#EMIT_HOVER_EVENT_TYPE} flag instead
     */
    @Deprecated
    @NotNull Builder emitLegacyHoverEvent();

    /**
     * Create a finished serializer instance.
     *
     * @return the new serializer
     * @since 4.14.0
     */
    @NotNull JSONComponentSerializer build();
  }

  /**
   * A {@link JSONComponentSerializer} service provider.
   *
   * @since 4.14.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link JSONComponentSerializer}.
     *
     * @return a {@link JSONComponentSerializer}
     * @since 4.14.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull JSONComponentSerializer instance();

    /**
     * Provide a supplier for builder builders of {@link JSONComponentSerializer} instances.
     *
     * @return a {@link JSONComponentSerializer}
     * @since 4.14.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull Supplier<@NotNull Builder> builder();
  }
}
