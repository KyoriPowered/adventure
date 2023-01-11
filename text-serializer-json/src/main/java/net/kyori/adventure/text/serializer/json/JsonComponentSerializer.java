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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A generic JSON component serializer.
 *
 * @since 4.13.0
 */
public interface JsonComponentSerializer extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * @return a JSON component serializer
   * @since 4.13.0
   */
  static @NotNull JsonComponentSerializer json() {
    return JsonComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Gets a component serializer for legacy JSON serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a JSON component serializer
   * @since 4.13.0
   */
  static @NotNull JsonComponentSerializer jsonLegacy() {
    return JsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
  }

  /**
   * A {@link JsonComponentSerializer} service provider.
   *
   * @since 4.13.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.13.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull JsonComponentSerializer json();

    /**
     * Provides a legacy {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.13.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull JsonComponentSerializer jsonLegacy();
  }
}
