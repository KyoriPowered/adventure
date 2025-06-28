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

import net.kyori.adventure.key.Key;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getOptionalTag;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class NBTComponentSerializerImpl implements NBTComponentSerializer {

  private static final Optional<Provider> SERVICE = Services.service(Provider.class);
  private static final Consumer<Builder> BUILDER = SERVICE
    .map(Provider::builder)
    .orElse(builder -> {
      // NOOP
    });

  @Override
  public @NotNull Style deserializeStyle(@NotNull CompoundBinaryTag tag) {
    return StyleSerializer.deserialize(tag, this);
  }

  @Override
  public @NotNull CompoundBinaryTag serializeStyle(@NotNull Style style) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
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

  NBTComponentSerializerImpl(@NotNull OptionState options) {
    this.options = requireNonNull(options, "options");
    if (options.schema() != NBTSerializerOptions.schema()) {
      throw new IllegalArgumentException("The specified option state does not use the NBT serializer option schema");
    }
  }

  @Override
  public @NotNull Component deserialize(@NotNull BinaryTag input) {
    if (input instanceof StringBinaryTag) {
      return Component.text(((StringBinaryTag) input).value());
    } else if (input instanceof ListBinaryTag) {
      ListBinaryTag castInput = (ListBinaryTag) input;
      if (castInput.isEmpty()) {
        throw new IllegalArgumentException("The list binary tag must not be empty");
      }

      Component rootTag = this.deserialize(castInput.get(0));
      for (int index = 1; index < castInput.size(); index++) {
        rootTag = rootTag.append(this.deserialize(castInput.get(index)));
      }

      return rootTag;
    } else if (!(input instanceof CompoundBinaryTag)) {
      throw new IllegalArgumentException("The input isn't a compound, string or list binary tag");
    }

    CompoundBinaryTag compound = (CompoundBinaryTag) input;
    Style style = StyleSerializer.deserialize(compound, this);

    ListBinaryTag extraTag = getOptionalTag(compound, EXTRA, BinaryTagTypes.LIST);
    List<Component> children;

    if (extraTag == null) {
      children = Collections.emptyList();
    } else {
      children = new ArrayList<>();
      extraTag.unwrapHeterogeneity().forEach(child -> children.add(this.deserialize(child)));
    }

    if (compound.get(TEXT) != null) {
      return Component.text()
        .content(getRequiredTag(compound, TEXT, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(TRANSLATE) != null) {
      StringBinaryTag translateTag = getRequiredTag(compound, TRANSLATE, BinaryTagTypes.STRING);
      ListBinaryTag translateWithTag = compound.getList(TRANSLATE_WITH).unwrapHeterogeneity();
      StringBinaryTag fallbackTag = getOptionalTag(compound, TRANSLATE_FALLBACK, BinaryTagTypes.STRING);

      List<TranslationArgument> arguments = new ArrayList<>();
      translateWithTag.forEach(argumentTag -> arguments.add(TranslationArgumentSerializer.deserialize(argumentTag, this)));

      return Component.translatable()
        .key(translateTag.value())
        .fallback(fallbackTag == null ? null : fallbackTag.value())
        .arguments(arguments)
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(KEYBIND) != null) {
      return Component.keybind()
        .keybind(getRequiredTag(compound, KEYBIND, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(SCORE) != null) {
      CompoundBinaryTag scoreTag = getRequiredTag(compound, SCORE, BinaryTagTypes.COMPOUND);
      return Component.score()
        .name(getRequiredTag(scoreTag, SCORE_NAME, BinaryTagTypes.STRING).value())
        .objective(getRequiredTag(scoreTag, SCORE_OBJECTIVE, BinaryTagTypes.STRING).value())
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(SELECTOR) != null) {
      StringBinaryTag selectorTag = getRequiredTag(compound, SELECTOR, BinaryTagTypes.STRING);
      BinaryTag selectorSeparatorTag = compound.get(SEPARATOR);
      return Component.selector()
        .pattern(selectorTag.value())
        .separator(selectorSeparatorTag == null ? null : this.deserialize(selectorSeparatorTag))
        .style(style)
        .append(children)
        .build();
    } else if (compound.get(NBT) != null) {
      String nbtPath = getRequiredTag(compound, NBT, BinaryTagTypes.STRING).value();

      ByteBinaryTag nbtInterpretTag = getOptionalTag(compound, NBT_INTERPRET, BinaryTagTypes.BYTE);
      boolean nbtInterpret = nbtInterpretTag != null && asBoolean(nbtInterpretTag);

      BinaryTag nbtSeparatorTag = compound.get(SEPARATOR);
      Component nbtSeparator = null;

      if (nbtSeparatorTag != null) {
        nbtSeparator = this.deserialize(nbtSeparatorTag);
      }

      StringBinaryTag nbtBlockTag = getOptionalTag(compound, NBT_BLOCK, BinaryTagTypes.STRING);
      StringBinaryTag nbtEntityTag = getOptionalTag(compound, NBT_ENTITY, BinaryTagTypes.STRING);
      StringBinaryTag nbtStorageTag = getOptionalTag(compound, NBT_STORAGE, BinaryTagTypes.STRING);

      if (nbtBlockTag != null) {
        return Component.blockNBT()
          .nbtPath(nbtPath)
          .interpret(nbtInterpret)
          .separator(nbtSeparator)
          .pos(BlockNBTComponent.Pos.fromString(nbtBlockTag.value()))
          .style(style)
          .append(children)
          .build();
      } else if (nbtEntityTag != null) {
        return Component.entityNBT()
          .nbtPath(nbtPath)
          .interpret(nbtInterpret)
          .separator(nbtSeparator)
          .selector(nbtEntityTag.value())
          .style(style)
          .append(children)
          .build();
      } else if (nbtStorageTag != null) {
        return Component.storageNBT()
          .nbtPath(nbtPath)
          .interpret(nbtInterpret)
          .separator(nbtSeparator)
          .storage(Key.key(nbtStorageTag.value()))
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
  public @NotNull BinaryTag serialize(@NotNull Component component) {
    if (component instanceof TextComponent && !component.hasStyling() && component.children().isEmpty()) {
      return StringBinaryTag.stringBinaryTag(((TextComponent) component).content());
    }

    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

    if (component instanceof TextComponent) {
      builder.putString(TEXT, ((TextComponent) component).content());
    } else if (component instanceof TranslatableComponent) {
      TranslatableComponent translatable = (TranslatableComponent) component;
      builder.putString(TRANSLATE, translatable.key());

      String fallback = translatable.fallback();
      if (fallback != null) {
        builder.putString(TRANSLATE_FALLBACK, fallback);
      }

      List<TranslationArgument> arguments = translatable.arguments();
      if (!arguments.isEmpty()) {
        ListBinaryTag.Builder<BinaryTag> translateWithTagBuilder = ListBinaryTag.heterogeneousListBinaryTag();
        arguments.forEach(argument -> translateWithTagBuilder.add(TranslationArgumentSerializer.serialize(argument, this)));
        builder.put(TRANSLATE_WITH, translateWithTagBuilder.build().wrapHeterogeneity());
      }
    } else if (component instanceof KeybindComponent) {
      builder.putString(KEYBIND, ((KeybindComponent) component).keybind());
    } else if (component instanceof ScoreComponent) {
      ScoreComponent score = (ScoreComponent) component;

      CompoundBinaryTag.Builder scoreTagBuilder = CompoundBinaryTag.builder()
        .putString(SCORE_NAME, score.name())
        .putString(SCORE_OBJECTIVE, score.objective());

      builder.put(SCORE, scoreTagBuilder.build());
    } else if (component instanceof SelectorComponent) {
      SelectorComponent selector = (SelectorComponent) component;
      builder.putString(SELECTOR, selector.pattern());

      Component separator = selector.separator();
      if (separator != null) {
        builder.put(SEPARATOR, this.serialize(separator));
      }
    } else if (component instanceof NBTComponent) {
      NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) component;
      builder.putString(NBT, nbt.nbtPath());

      if (nbt.interpret()) {
        builder.putBoolean(NBT_INTERPRET, true);
      }

      Component separator = nbt.separator();
      if (separator != null) {
        builder.put(SEPARATOR, this.serialize(separator));
      }

      if (nbt instanceof BlockNBTComponent) {
        builder.putString(NBT_BLOCK, ((BlockNBTComponent) nbt).pos().asString());
      } else if (nbt instanceof EntityNBTComponent) {
        builder.putString(NBT_ENTITY, ((EntityNBTComponent) nbt).selector());
      } else if (nbt instanceof StorageNBTComponent) {
        builder.putString(NBT_STORAGE, ((StorageNBTComponent) nbt).storage().asString());
      } else {
        throw notSureHowToSerialize(component);
      }
    } else {
      throw notSureHowToSerialize(component);
    }

    List<Component> children = component.children();
    if (!children.isEmpty()) {
      ListBinaryTag.Builder<BinaryTag> extraTagBuilder = ListBinaryTag.heterogeneousListBinaryTag();
      children.forEach(child -> extraTagBuilder.add(this.serialize(child)));
      builder.put(EXTRA, extraTagBuilder.build().wrapHeterogeneity());
    }

    StyleSerializer.serialize(component.style(), builder, this);
    return builder.build();
  }

  @NotNull OptionState options() {
    return this.options;
  }

  private static @NotNull IllegalArgumentException notSureHowToDeserialize(@NotNull BinaryTag tag) {
    return new IllegalArgumentException("Don't know how to turn " + tag + " into a Component");
  }

  private static @NotNull IllegalArgumentException notSureHowToSerialize(@NotNull Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }

  static final class BuilderImpl implements NBTComponentSerializer.Builder {

    private OptionState flags = NBTSerializerOptions.schema().emptyState();

    BuilderImpl() {
      BUILDER.accept(this); // let service provider touch the builder before anybody else touches it
    }

    @Override
    public @NotNull Builder options(@NotNull OptionState flags) {
      this.flags = requireNonNull(flags, "flags");
      return this;
    }

    @Override
    public @NotNull Builder editOptions(@NotNull Consumer<OptionState.Builder> optionEditor) {
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
