/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A NBT component serializer.
 *
 * @since 4.18.0
 */
public interface NBTComponentSerializer extends ComponentSerializer<Component, Component, BinaryTag> {
  /**
   * Deserializes a {@linkplain Style style} from a {@linkplain BinaryTag binary tag}.
   *
   * @param tag the binary tag
   * @return the style
   * @since 4.18.0
   */
  @NotNull Style deserializeStyle(@NotNull CompoundBinaryTag tag);

  /**
   * Serializes a {@linkplain Style style} to a {@linkplain BinaryTag binary tag}.
   *
   * @param style the style
   * @return the binary tag
   * @since 4.18.0
   */
  @NotNull CompoundBinaryTag serializeStyle(@NotNull Style style);

  /**
   * Gets a component serializer for NBT serialization and deserialization.
   *
   * @return a NBT component serializer
   * @since 4.18.0
   */
  static @NotNull NBTComponentSerializer nbt() {
    return NBTComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Creates a new {@link NBTComponentSerializer.Builder}.
   *
   * @return a builder
   * @since 4.18.0
   */
  static @NotNull Builder builder() {
    return new NBTComponentSerializerImpl.BuilderImpl();
  }

  /**
   * A builder for {@link NBTComponentSerializer}.
   *
   * @since 4.18.0
   */
  interface Builder extends AbstractBuilder<NBTComponentSerializer> {
    /**
     * Set the option state to apply on this serializer.
     *
     * <p>This controls how the serializer emits and interprets components.</p>
     *
     * @param flags the flag set to use
     * @return this builder
     * @see NBTSerializerOptions
     * @since 4.18.0
     */
    @NotNull Builder options(final @NotNull OptionState flags);

    /**
     * Edit the active set of serializer options.
     *
     * @param optionEditor the consumer operating on the existing flag set
     * @return this builder
     * @see NBTSerializerOptions
     * @since 4.18.0
     */
    @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor);

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     * @since 4.18.0
     */
    @Override
    @NotNull NBTComponentSerializer build();
  }

  /**
   * A {@link NBTComponentSerializer} service provider.
   *
   * @since 4.18.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link NBTComponentSerializer}.
     *
     * @return a {@link NBTComponentSerializer}
     * @since 4.18.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull NBTComponentSerializer nbt();

    /**
     * Completes the building process of {@link Builder}.
     *
     * @return a {@link Consumer}
     * @since 4.18.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull Consumer<Builder> builder();
  }
}
