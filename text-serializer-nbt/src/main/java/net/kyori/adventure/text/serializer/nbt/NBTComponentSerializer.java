package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface NBTComponentSerializer extends ComponentSerializer<Component, Component, BinaryTag> {

  static @NotNull Builder builder() {
    return new NBTComponentSerializerImpl.BuilderImpl();
  }

  interface Builder extends AbstractBuilder<NBTComponentSerializer> {
    @NotNull Builder options(final @NotNull OptionState flags);
    @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor);

    default @NotNull Builder downsampleColors() {
      return this.editOptions(builder -> builder.value(NBTSerializerOptions.EMIT_RGB, false));
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

    @Override
    @NotNull NBTComponentSerializer build();
  }
}
