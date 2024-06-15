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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A NBT component serializer.
 *
 * <p>Use {@link Builder#emitRgb(boolean)} to support platforms
 * that do not understand hex colors that were introduced in Minecraft 1.16.</p>
 *
 * @since 4.18.0
 */
public interface NBTComponentSerializer extends ComponentSerializer<Component, Component, BinaryTag> {
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
     * Sets whether the serializer should downsample hex colors to named colors.
     *
     * @param emit true if the serializer should downsample hex colors to named colors, false otherwise
     * @return this builder
     * @since 4.18.0
     */
    default @NotNull Builder emitRgb(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_RGB, emit));
    }

    /**
     * Sets a {@linkplain NBTSerializerOptions.HoverEventValueMode hover event value mode} of the serializer.
     *
     * @param mode the mode
     * @return this builder
     * @since 4.18.0
     */
    default @NotNull Builder emitHoverEventValueMode(final @NotNull NBTSerializerOptions.HoverEventValueMode mode) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE, mode));
    }

    /**
     * Sets whether the serializer should serialize types of the components.
     *
     * @param serialize true if the serializer should serialize types of the components, false otherwise
     * @return this builder
     * @since 4.18.0
     */
    default @NotNull Builder serializeComponentTypes(final boolean serialize) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.SERIALIZE_COMPONENT_TYPES, serialize));
    }

    /**
     * Sets a {@linkplain NBTSerializerOptions.ShowItemHoverDataMode show item hover data mode} of the serializer.
     *
     * @param mode the mode
     * @return this builder
     * @since 4.18.0
     */
    default @NotNull Builder showItemHoverDataMode(final @NotNull NBTSerializerOptions.ShowItemHoverDataMode mode) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.SHOW_ITEM_HOVER_DATA_MODE, mode));
    }

    /**
     * Sets whether the serializer should serialize text components without styling as
     * {@linkplain net.kyori.adventure.nbt.StringBinaryTag string binary tags} instead of
     * {@linkplain net.kyori.adventure.nbt.CompoundBinaryTag compound binary tags}.
     *
     * @param emit true if the serializer should serialize text components without styling
     *             as string binary tags, false otherwise
     * @return this builder
     * @since 4.18.0
     */
    default @NotNull Builder emitCompactTextComponent(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_COMPACT_TEXT_COMPONENT, emit));
    }

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
