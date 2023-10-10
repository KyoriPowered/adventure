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
package net.kyori.adventure.text.serializer.json;

import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A JSON component serializer.
 *
 * <p>This serializer exposes a common superset of functionality across any number of JSON library implementations.
 * For more specialized operations, users may need to refer to specific implementations.</p>
 *
 * @since 4.14.0
 */
@NullMarked
public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * @return a JSON component serializer
   * @since 4.14.0
   */
  static JSONComponentSerializer json() {
    return JSONComponentSerializerAccessor.Instances.INSTANCE;
  }

  /**
   * Get a builder to create a customized JSON serializer instance.
   *
   * @return the new builder
   * @since 4.14.0
   */
  static JSONComponentSerializer.Builder builder() {
    return JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
  }

  /**
   * A builder for {@link JSONComponentSerializer} instances that delegates to the active serializer.
   *
   * @since 4.14.0
   */
  interface Builder {
    /**
     * Sets that the serializer should downsample hex colors to named colors.
     *
     * @return this builder
     * @since 4.14.0
     */
    Builder downsampleColors();

    /**
     * Sets a serializer that will be used to interpret legacy hover event {@code value} payloads.
     * If the serializer is {@code null}, then only {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}
     * legacy hover events can be deserialized.
     *
     * @param serializer serializer
     * @return this builder
     * @since 4.14.0
     */
    Builder legacyHoverEventSerializer(final @Nullable LegacyHoverEventSerializer serializer);

    /**
     * Output a legacy hover event {@code value} in addition to the modern {@code contents}.
     *
     * <p>A {@link #legacyHoverEventSerializer(LegacyHoverEventSerializer) legacy hover serializer} must also be set
     * to serialize any hover events beyond those with action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}</p>
     *
     * @return this builder
     * @since 4.14.0
     */
    Builder emitLegacyHoverEvent();

    /**
     * Create a finished serializer instance.
     *
     * @return the new serializer
     * @since 4.14.0
     */
    JSONComponentSerializer build();
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
    JSONComponentSerializer instance();

    /**
     * Provide a supplier for builder builders of {@link JSONComponentSerializer} instances.
     *
     * @return a {@link JSONComponentSerializer}
     * @since 4.14.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    Supplier<Builder> builder();
  }
}
