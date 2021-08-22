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
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JsonComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A moshi component serializer.
 *
 * <p>Use {@link Builder#downsampleColors()} to support platforms
 * that do not understand hex colors that were introduced in Minecraft 1.16.</p>
 *
 * @since 4.9.0
 */
public interface MoshiComponentSerializer extends JsonComponentSerializer {
  /**
   * Gets a component serializer for moshi serialization and deserialization.
   *
   * @return a moshi component serializer
   * @since 4.9.0
   */
  static @NotNull MoshiComponentSerializer moshi() {
    return MoshiComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Gets a component serializer for moshi serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a moshi component serializer
   * @since 4.9.0
   */
  static @NotNull MoshiComponentSerializer colorDownsamplingMoshi() {
    return MoshiComponentSerializerImpl.Instances.LEGACY_INSTANCE;
  }

  /**
   * Creates a new {@link MoshiComponentSerializer.Builder}.
   *
   * @return a builder
   * @since 4.9.0
   */
  static Builder builder() {
    return new MoshiComponentSerializerImpl.BuilderImpl();
  }

  /**
   * Gets the underlying moshi serializer.
   *
   * @return a moshi serializer
   * @since 4.9.0
   */
  @NotNull Moshi serializer();

  /**
   * Gets the underlying moshi populator.
   *
   * @return a moshi populator
   * @since 4.9.0
   */
  @NotNull UnaryOperator<Moshi.Builder> populator();

  /**
   * Deserialize a component from input of type {@link Object}.
   *
   * @param input the input
   * @return the component
   * @since 4.7.0
   */
  @NotNull Component deserializeFromTree(final @NotNull Object input);

  /**
   * Deserialize a component to output of type {@link Object}.
   *
   * @param component the component
   * @return the json element
   * @since 4.7.0
   */
  @NotNull Object serializeToTree(final @NotNull Component component);

  /**
   * A builder for {@link MoshiComponentSerializer}.
   *
   * @since 4.9.0
   */
  interface Builder extends JsonComponentSerializer.Builder {
    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NotNull MoshiComponentSerializer build();
  }

  /**
   * A {@link MoshiComponentSerializer} service provider.
   *
   * @since 4.9.0
   */
  @ApiStatus.Internal
  interface Provider {
    /**
     * Provides a standard {@link MoshiComponentSerializer}.
     *
     * @return a {@link MoshiComponentSerializer}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull MoshiComponentSerializer moshi();

    /**
     * Provides a legacy {@link MoshiComponentSerializer}.
     *
     * @return a {@link MoshiComponentSerializer}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull MoshiComponentSerializer moshiLegacy();

    /**
     * Completes the building process of {@link Builder}.
     *
     * @return a {@link Consumer}
     * @since 4.9.0
     */
    @ApiStatus.Internal
    @NotNull Consumer<Builder> builder();
  }
}
