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

    default @NotNull Builder emitModernHoverEvent(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_MODERN_HOVER, emit));
    }

    default @NotNull Builder emitLegacyHoverEvent(final boolean emit) {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_LEGACY_HOVER, emit));
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
