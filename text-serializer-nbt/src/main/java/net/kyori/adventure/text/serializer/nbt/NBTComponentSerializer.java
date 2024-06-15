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

public interface NBTComponentSerializer extends ComponentSerializer<Component, Component, BinaryTag> {

  static @NotNull NBTComponentSerializer nbt() {
    return NBTComponentSerializerImpl.Instances.INSTANCE;
  }

  static @NotNull Builder builder() {
    return new NBTComponentSerializerImpl.BuilderImpl();
  }

  interface Builder extends AbstractBuilder<NBTComponentSerializer> {
    @NotNull Builder options(final @NotNull OptionState flags);
    @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor);

    default @NotNull Builder emitRgb(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_RGB, emit));
    }

    default @NotNull Builder emitHoverEventValueMode(final NBTSerializerOptions.HoverEventValueMode mode) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE, mode));
    }

    default @NotNull Builder serializeComponentTypes(final boolean serialize) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.SERIALIZE_COMPONENT_TYPES, serialize));
    }

    default @NotNull Builder showItemHoverDataMode(final @NotNull NBTSerializerOptions.ShowItemHoverDataMode mode) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.SHOW_ITEM_HOVER_DATA_MODE, mode));
    }

    default @NotNull Builder emitCompactTextComponent(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_COMPACT_TEXT_COMPONENT, emit));
    }

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
