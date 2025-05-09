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
package net.kyori.adventure.text.serializer.ansi;

import java.util.function.Consumer;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.ComponentEncoder;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.ansi.ColorLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer which emits <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape sequences</a>.
 *
 * <p>Note that this serializer does NOT support deserialization.</p>
 *
 * @since 4.14.0
 */
public interface ANSIComponentSerializer extends ComponentEncoder<Component, String> {
  /**
   * Gets a component serializer for serialization to a string using ANSI escape codes.
   *
   * <p>Note that this serializer does NOT support deserialization.</p>
   *
   * @return a component serializer for serialization with ANSI escape sequences.
   * @since 4.14.0
   */
  static @NotNull ANSIComponentSerializer ansi() {
    return ANSIComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Create a new builder.
   *
   * @return a new ANSI serializer builder
   * @since 4.14.0
   */
  static ANSIComponentSerializer.@NotNull Builder builder() {
    return new ANSIComponentSerializerImpl.BuilderImpl();
  }

  /**
   * A builder for the ANSI component serializer.
   *
   * @since 4.14.0
   */
  interface Builder extends AbstractBuilder<ANSIComponentSerializer> {
    /**
     * Sets the default color level used when serializing.
     *
     * <p>By default, this serializer will use {@link ColorLevel#compute()} to try to detect the color level of the terminal being used.</p>
     *
     * @param colorLevel the color level
     * @return this builder
     * @see ColorLevel
     * @since 4.14.0
     */
    @NotNull Builder colorLevel(final @NotNull ColorLevel colorLevel);

    /**
     * Sets the component flattener instance to use when traversing the component for serialization.
     *
     * <p>By default, this serializer will use {@link ComponentFlattener#basic()}.</p>
     *
     * @param componentFlattener the flattener instance.
     * @return this builder
     * @since 4.14.0
     */
    @NotNull Builder flattener(final @NotNull ComponentFlattener componentFlattener);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NotNull ANSIComponentSerializer build();
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
     * @since 4.14.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull ANSIComponentSerializer ansi();

    /**
     * Completes the building process of {@link Builder}.
     *
     * @return a {@link Consumer}
     * @since 4.14.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull Consumer<Builder> builder();
  }
}
