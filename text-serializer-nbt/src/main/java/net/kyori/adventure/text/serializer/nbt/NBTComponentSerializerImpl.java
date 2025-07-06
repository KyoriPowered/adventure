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
package net.kyori.adventure.text.serializer.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Services;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.EXTRA;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.KEYBIND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_BLOCK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_ENTITY;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_INTERPRET;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.NBT_STORAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SCORE_OBJECTIVE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SELECTOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SEPARATOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TEXT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_FALLBACK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.TRANSLATE_WITH;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.asBoolean;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.forEach;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.optionalTag;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.requiredTag;

final class NBTComponentSerializerImpl implements NBTComponentSerializer {

  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  private static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElse(builder -> {
      // NOOP
    });

  @Override
  public @NotNull Style deserializeStyle(final @NotNull CompoundBinaryTag tag) {
    return StyleSerializer.deserialize(tag, this);
  }

  @Override
  public @NotNull CompoundBinaryTag serializeStyle(final @NotNull Style style) {
    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
    StyleSerializer.serialize(style, builder, this);
    return builder.build();
  }

  // We cannot store these fields in NBTComponentSerializerImpl directly due to class initialisation issues.
  static final class Instances {
    static final NBTComponentSerializer INSTANCE = SERVICE
      .map(Provider::nbt)
      .orElseGet(() -> new NBTComponentSerializerImpl(NBTSerializerOptions.schema().emptyState()));
  }

  private final OptionState options;

  NBTComponentSerializerImpl(final @NotNull OptionState options) {
    this.options = requireNonNull(options, "options");
    if (options.schema() != NBTSerializerOptions.schema()) {
      throw new IllegalArgumentException("The specified option state does not use the NBT serializer option schema");
    }
  }

  @Override
  public @NotNull Component deserialize(final @NotNull BinaryTag input) {
    if (input instanceof StringBinaryTag) {
      return Component.text(((StringBinaryTag) input).value());
    } else if (input instanceof ListBinaryTag) {
      final ListBinaryTag castInput = ((ListBinaryTag) input).unwrapHeterogeneity();
      if (castInput.isEmpty()) {
        throw new IllegalArgumentException("The list binary tag representing a component must not be empty");
      }

      Component rootTag = this.deserialize(castInput.get(0));
      for (int index = 1; index < castInput.size(); index++) {
        rootTag = rootTag.append(this.deserialize(castInput.get(index)));
      }

      return rootTag;
    } else if (!(input instanceof CompoundBinaryTag)) {
      throw new IllegalArgumentException("The input isn't a compound, string or list binary tag");
    }

    final CompoundBinaryTag compound = (CompoundBinaryTag) input;
    final Style style = StyleSerializer.deserialize(compound, this);

    final BinaryTag extraTag = compound.get(EXTRA);
    final List<Component> children = new ArrayList<>();

    if (extraTag != null) {
      forEach(extraTag, child -> children.add(this.deserialize(child)));
    }

    if (compound.get(TEXT) != null) {
      return Component.text()
        .content(requiredTag(compound, TEXT, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(TRANSLATE) != null) {
      final StringBinaryTag translateTag = requiredTag(compound, TRANSLATE, BinaryTagTypes.STRING);
      final BinaryTag translateWithTag = compound.get(TRANSLATE_WITH);
      final StringBinaryTag fallbackTag = optionalTag(compound, TRANSLATE_FALLBACK, BinaryTagTypes.STRING);

      final List<TranslationArgument> arguments = new ArrayList<>();
      if (translateWithTag != null) {
        forEach(translateWithTag, argumentTag -> arguments.add(TranslationArgumentSerializer.deserialize(argumentTag, this)));
      }

      return Component.translatable()
        .key(translateTag.value())
        .fallback(fallbackTag == null ? null : fallbackTag.value())
        .arguments(arguments)
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(KEYBIND) != null) {
      return Component.keybind()
        .keybind(requiredTag(compound, KEYBIND, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(SCORE) != null) {
      final CompoundBinaryTag scoreTag = requiredTag(compound, SCORE, BinaryTagTypes.COMPOUND);
      return Component.score()
        .name(requiredTag(scoreTag, SCORE_NAME, BinaryTagTypes.STRING).value())
        .objective(requiredTag(scoreTag, SCORE_OBJECTIVE, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(SELECTOR) != null) {
      final StringBinaryTag selectorTag = requiredTag(compound, SELECTOR, BinaryTagTypes.STRING);
      final BinaryTag separatorTag = compound.get(SEPARATOR);
      return Component.selector()
        .pattern(selectorTag.value())
        .separator(separatorTag == null ? null : this.deserialize(separatorTag))
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(NBT) != null) {
      final String nbtPath = requiredTag(compound, NBT, BinaryTagTypes.STRING).value();

      final ByteBinaryTag interpretTag = optionalTag(compound, NBT_INTERPRET, BinaryTagTypes.BYTE);
      final boolean interpret = interpretTag != null && asBoolean(interpretTag);

      final BinaryTag separatorTag = compound.get(SEPARATOR);
      Component separator = null;

      if (separatorTag != null) {
        separator = this.deserialize(separatorTag);
      }

      final StringBinaryTag blockTag = optionalTag(compound, NBT_BLOCK, BinaryTagTypes.STRING);
      final StringBinaryTag entityTag = optionalTag(compound, NBT_ENTITY, BinaryTagTypes.STRING);
      final StringBinaryTag storageTag = optionalTag(compound, NBT_STORAGE, BinaryTagTypes.STRING);

      if (blockTag != null) {
        return Component.blockNBT()
          .nbtPath(nbtPath)
          .interpret(interpret)
          .separator(separator)
          .pos(BlockNBTComponent.Pos.fromString(blockTag.value()))
          .style(style)
          .append(children)
          .build();
      } else if (entityTag != null) {
        return Component.entityNBT()
          .nbtPath(nbtPath)
          .interpret(interpret)
          .separator(separator)
          .selector(entityTag.value())
          .style(style)
          .append(children)
          .build();
      } else if (storageTag != null) {
        return Component.storageNBT()
          .nbtPath(nbtPath)
          .interpret(interpret)
          .separator(separator)
          .storage(KeySerializer.deserialize(storageTag))
          .style(style)
          .append(children)
          .build();
      } else {
        throw notSureHowToDeserialize(input);
      }
    } else {
      throw notSureHowToDeserialize(input);
    }
  }

  @Override
  public @NotNull BinaryTag serialize(final @NotNull Component component) {
    if (component instanceof TextComponent && !component.hasStyling() && component.children().isEmpty()) {
      return StringBinaryTag.stringBinaryTag(((TextComponent) component).content());
    }

    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

    if (component instanceof TextComponent) {
      builder.putString(TEXT, ((TextComponent) component).content());
    } else if (component instanceof TranslatableComponent) {
      final TranslatableComponent translatable = (TranslatableComponent) component;
      builder.putString(TRANSLATE, translatable.key());

      final String fallback = translatable.fallback();
      if (fallback != null) {
        builder.putString(TRANSLATE_FALLBACK, fallback);
      }

      final List<TranslationArgument> arguments = translatable.arguments();
      if (!arguments.isEmpty()) {
        final NBTAggregateCollector translateWithTagBuilder = NBTAggregateCollector.create(this);
        arguments.forEach(argument -> translateWithTagBuilder.add(TranslationArgumentSerializer.serialize(argument, this)));
        builder.put(TRANSLATE_WITH, translateWithTagBuilder.collect());
      }
    } else if (component instanceof KeybindComponent) {
      builder.putString(KEYBIND, ((KeybindComponent) component).keybind());
    } else if (component instanceof ScoreComponent) {
      final ScoreComponent score = (ScoreComponent) component;

      final CompoundBinaryTag.Builder scoreTagBuilder = CompoundBinaryTag.builder()
        .putString(SCORE_NAME, score.name())
        .putString(SCORE_OBJECTIVE, score.objective());

      builder.put(SCORE, scoreTagBuilder.build());
    } else if (component instanceof SelectorComponent) {
      final SelectorComponent selector = (SelectorComponent) component;
      builder.putString(SELECTOR, selector.pattern());

      final Component separator = selector.separator();
      if (separator != null) {
        builder.put(SEPARATOR, this.serialize(separator));
      }
    } else if (component instanceof NBTComponent) {
      final NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) component;
      builder.putString(NBT, nbt.nbtPath());

      if (nbt.interpret()) {
        builder.putBoolean(NBT_INTERPRET, true);
      }

      final Component separator = nbt.separator();
      if (separator != null) {
        builder.put(SEPARATOR, this.serialize(separator));
      }

      if (nbt instanceof BlockNBTComponent) {
        builder.putString(NBT_BLOCK, ((BlockNBTComponent) nbt).pos().asString());
      } else if (nbt instanceof EntityNBTComponent) {
        builder.putString(NBT_ENTITY, ((EntityNBTComponent) nbt).selector());
      } else if (nbt instanceof StorageNBTComponent) {
        builder.put(NBT_STORAGE, KeySerializer.serialize(((StorageNBTComponent) nbt).storage()));
      } else {
        throw notSureHowToSerialize(component);
      }
    } else {
      throw notSureHowToSerialize(component);
    }

    final List<Component> children = component.children();
    if (!children.isEmpty()) {
      final NBTAggregateCollector extraTagBuilder = NBTAggregateCollector.create(this);
      children.forEach(child -> extraTagBuilder.add(this.serialize(child)));
      builder.put(EXTRA, extraTagBuilder.collect());
    }

    StyleSerializer.serialize(component.style(), builder, this);
    return builder.build();
  }

  @NotNull OptionState options() {
    return this.options;
  }

  private static @NotNull IllegalArgumentException notSureHowToDeserialize(final @NotNull BinaryTag tag) {
    return new IllegalArgumentException("Don't know how to turn " + tag + " into a Component");
  }

  private static @NotNull IllegalArgumentException notSureHowToSerialize(final @NotNull Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }

  static final class BuilderImpl implements NBTComponentSerializer.Builder {

    private OptionState flags = NBTSerializerOptions.schema().emptyState();

    BuilderImpl() {
      BUILDER.accept(this); // let service provider touch the builder before anybody else touches it
    }

    @Override
    public @NotNull Builder options(final @NotNull OptionState flags) {
      this.flags = requireNonNull(flags, "flags");
      return this;
    }

    @Override
    public @NotNull Builder editOptions(final @NotNull Consumer<OptionState.Builder> optionEditor) {
      final OptionState.Builder builder = NBTSerializerOptions.schema().stateBuilder().values(this.flags);
      requireNonNull(optionEditor, "optionEditor").accept(builder);
      this.flags = builder.build();
      return this;
    }

    @Override
    public @NotNull NBTComponentSerializer build() {
      return new NBTComponentSerializerImpl(this.flags);
    }
  }
}
