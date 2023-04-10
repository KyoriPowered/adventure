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
package net.kyori.adventure.text.serializer.ansi;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.ansi.ColorLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer which emits <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape sequences</a>.
 * Note that this serializer does NOT support serialization.
 *
 * @since 4.14.0
 */
public interface ANSIComponentSerializer extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a component serializer for serialization to a string using ANSI escape codes.
   * Note that this serializer does NOT support serialization.
   *
   * @return a component serializer for serialization with ANSI escape sequences.
   * @since 4.14.0
   */
  static @NotNull ANSIComponentSerializer ansi() {
    return ANSIComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Serialize a {@link Component} to a {@link String} with ANSI escape sequences at the given {@link ColorLevel}.
   *
   * @param component the component to serialize
   * @param colorLevel the color level to use for escape sequences
   * @return string with escape sequences
   * @see ColorLevel
   * @since 4.14.0
   */
  @NotNull String serialize(@NotNull Component component, @NotNull ColorLevel colorLevel);

  @Override
  default @NotNull Component deserialize(@NotNull String input) {
    throw new UnsupportedOperationException("AnsiComponentSerializer does not support deserialization");
  }

  /**
   * A {@link ANSIComponentSerializer} service provider.
   *
   * @since 4.14.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a {@link ANSIComponentSerializer}.
     *
     * @return a {@link ANSIComponentSerializer}
     * @since 4.8.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull ANSIComponentSerializer ansi();
  }
}
