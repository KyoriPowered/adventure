package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
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
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

final class NBTComponentSerializerImpl implements NBTComponentSerializer {

  private static final String TYPE = "type";

  private static final String TYPE_TEXT = "text";
  private static final String TYPE_TRANSLATABLE = "translatable";
  private static final String TYPE_KEYBIND = "keybind";
  private static final String TYPE_SCORE = "score";
  private static final String TYPE_SELECTOR = "selector";
  private static final String TYPE_NBT = "nbt";

  private static final String EXTRA = "extra";

  private static final String TEXT = "text";

  private static final String TRANSLATE_KEY = "translate";
  private static final String TRANSLATE_WITH = "with";
  private static final String TRANSLATE_FALLBACK = "fallback";

  private static final String KEYBIND = "keybind";

  private static final String SCORE = "score";
  private static final String SCORE_NAME = "name";
  private static final String SCORE_OBJECTIVE = "objective";
  @Deprecated
  private static final String SCORE_VALUE = "value";

  private static final String SELECTOR = "selector";
  private static final String SELECTOR_SEPARATOR = "separator";

  private static final String NBT = "nbt";
  private static final String NBT_INTERPRET = "interpret";
  private static final String NBT_SEPARATOR = "separator";
  private static final String NBT_BLOCK = "block";
  private static final String NBT_ENTITY = "entity";
  private static final String NBT_STORAGE = "storage";

  private final OptionState flags;

  NBTComponentSerializerImpl(@NotNull OptionState flags) {
    this.flags = flags;
  }

  @Override
  public @NotNull Component deserialize(@NotNull BinaryTag input) {
    if (input instanceof StringBinaryTag) {
      return Component.text(((StringBinaryTag) input).value());
    }

    if (!(input instanceof CompoundBinaryTag)) {
      throw new IllegalArgumentException("The input isn't a compound or string binary tag");
    }

    CompoundBinaryTag compound = (CompoundBinaryTag) input;
    String type = compound.getString(TYPE);

    if (type.isEmpty()) {
      if (compound.get(TEXT) != null) {
        type = TYPE_TEXT;
      } else if (compound.get(TRANSLATE_KEY) != null) {
        type = TYPE_TRANSLATABLE;
      } else if (compound.get(KEYBIND) != null) {
        type = TYPE_KEYBIND;
      } else if (compound.get(SCORE) != null) {
        type = TYPE_SCORE;
      } else if (compound.get(SELECTOR) != null) {
        type = TYPE_SELECTOR;
      } else if (compound.get(NBT) != null && (compound.get(NBT_BLOCK) != null
        || compound.get(NBT_STORAGE) != null || compound.get(NBT_ENTITY) != null)) {
        type = TYPE_NBT;
      } else {
        throw new IllegalArgumentException("Could not guess type of the component");
      }
    }

    Style style = StyleSerializer.deserialize(compound, this);

    List<Component> children = new ArrayList<>();
    ListBinaryTag binaryChildren = compound.getList(EXTRA);
    binaryChildren.forEach(child -> children.add(this.deserialize(child)));

    switch (type) {
      case TYPE_TEXT:
        return Component.text()
          .content(compound.getString(TEXT))
          .style(style)
          .append(children)
          .build();
      case TYPE_TRANSLATABLE:
        ListBinaryTag binaryArguments = compound.getList(TRANSLATE_WITH);
        String fallback = compound.getString(TRANSLATE_FALLBACK);

        if (fallback.isEmpty()) {
          fallback = null;
        }

        List<Component> arguments = new ArrayList<>();
        for (BinaryTag argument : binaryArguments) {
          arguments.add(this.deserialize(argument));
        }

        return Component.translatable()
          .key(compound.getString(TRANSLATE_KEY))
          .fallback(fallback)
          .arguments(arguments)
          .style(style)
          .append(children)
          .build();
      case TYPE_KEYBIND:
        return Component.keybind()
          .keybind(compound.getString(KEYBIND))
          .style(style)
          .append(children)
          .build();
      case TYPE_SCORE:
        CompoundBinaryTag binaryScore = compound.getCompound(SCORE);

        String scoreName = binaryScore.getString(SCORE_NAME);
        String scoreObjective = binaryScore.getString(SCORE_OBJECTIVE);

        String scoreValue = null;
        BinaryTag binaryScoreValue = binaryScore.get(SCORE_VALUE);

        if (binaryScoreValue != null) {
          scoreValue = ((StringBinaryTag) binaryScoreValue).value();
        }

        return Component.score()
          .name(scoreName)
          .objective(scoreObjective)
          .value(scoreValue)
          .style(style)
          .append(children)
          .build();
      case TYPE_SELECTOR:
        String selector = compound.getString(SELECTOR);
        Component selectorSeparator = null;

        BinaryTag binarySelectorSeparator = compound.get(SELECTOR_SEPARATOR);
        if (binarySelectorSeparator != null) {
          selectorSeparator = this.deserialize(binarySelectorSeparator);
        }

        return Component.selector()
          .pattern(selector)
          .separator(selectorSeparator)
          .style(style)
          .append(children)
          .build();
      case TYPE_NBT:
        String nbtPath = compound.getString(NBT);
        boolean nbtInterpret = compound.getBoolean(NBT_INTERPRET);
        Component nbtSeparator = null;

        BinaryTag binaryNbtSeparator = compound.get(NBT_SEPARATOR);
        if (binaryNbtSeparator != null) {
          nbtSeparator = this.deserialize(binaryNbtSeparator);
        }

        BinaryTag binaryBlock = compound.get(NBT_BLOCK);
        BinaryTag binaryEntity = compound.get(NBT_ENTITY);
        BinaryTag binaryStorage = compound.get(NBT_STORAGE);

        if (binaryBlock != null) {
          BlockNBTComponent.Pos pos = BlockNBTComponent.Pos.fromString(((StringBinaryTag) binaryBlock).value());
          return Component.blockNBT()
            .nbtPath(nbtPath)
            .interpret(nbtInterpret)
            .separator(nbtSeparator)
            .pos(pos)
            .style(style)
            .append(children)
            .build();
        } else if (binaryEntity != null) {
          return Component.entityNBT()
            .nbtPath(nbtPath)
            .interpret(nbtInterpret)
            .separator(nbtSeparator)
            .selector(((StringBinaryTag) binaryEntity).value())
            .style(style)
            .append(children)
            .build();
        } else if (binaryStorage != null) {
          return Component.storageNBT()
            .nbtPath(nbtPath)
            .interpret(nbtInterpret)
            .separator(nbtSeparator)
            .storage(Key.key(((StringBinaryTag) binaryStorage).value()))
            .style(style)
            .append(children)
            .build();
        }
      default:
        throw new IllegalArgumentException("Unknown component type " + type);
    }
  }

  @Override
  public @NotNull BinaryTag serialize(@NotNull Component component) {
    if (this.flags.value(NBTSerializerOptions.EMIT_COMPACT_TEXT_COMPONENT) && component instanceof TextComponent
      && !component.hasStyling() && component.children().isEmpty()) {
      return StringBinaryTag.stringBinaryTag(((TextComponent) component).content());
    }
    return writeCompoundComponent(component);
  }

  private @NotNull CompoundBinaryTag writeCompoundComponent(@NotNull Component component) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();
    StyleSerializer.serialize(component.style(), builder, this);

    if (component instanceof TextComponent) {
      this.writeComponentType(TYPE_TEXT, builder);
      builder.putString(TEXT, ((TextComponent) component).content());
    } else if (component instanceof TranslatableComponent) {
      this.writeComponentType(TYPE_TRANSLATABLE, builder);

      TranslatableComponent translatable = (TranslatableComponent) component;
      builder.putString(TRANSLATE_KEY, translatable.key());

      List<TranslationArgument> arguments = translatable.arguments();

      if (!arguments.isEmpty()) {
        List<BinaryTag> argumentsTags = new ArrayList<>();

        for (TranslationArgument argument : arguments) {
          argumentsTags.add(this.writeCompoundComponent(argument.asComponent()));
        }

        builder.put(TRANSLATE_WITH, ListBinaryTag.from(argumentsTags));
      }

      String fallback = translatable.fallback();
      if (fallback != null) {
        builder.putString(TRANSLATE_FALLBACK, fallback);
      }
    } else if (component instanceof KeybindComponent) {
      this.writeComponentType(TYPE_KEYBIND, builder);
      builder.putString(KEYBIND, ((KeybindComponent) component).keybind());
    } else if (component instanceof ScoreComponent) {
      this.writeComponentType(TYPE_SCORE, builder);
      ScoreComponent score = (ScoreComponent) component;

      CompoundBinaryTag.Builder scoreBuilder = CompoundBinaryTag.builder()
        .putString(SCORE_NAME, score.name())
        .putString(SCORE_OBJECTIVE, score.objective());

      String value = score.value();
      if (value != null) {
        scoreBuilder.putString(SCORE_VALUE, value);
      }

      builder.put(SCORE, scoreBuilder.build());
    } else if (component instanceof SelectorComponent) {
      this.writeComponentType(TYPE_SELECTOR, builder);

      SelectorComponent selector = (SelectorComponent) component;
      builder.putString(SELECTOR, selector.pattern());

      Component separator = selector.separator();
      if (separator != null) {
        builder.put(SELECTOR_SEPARATOR, serialize(separator));
      }
    } else if (component instanceof NBTComponent) {
      this.writeComponentType(TYPE_NBT, builder);

      NBTComponent<?, ?> nbt = (NBTComponent<?, ?>) component;
      builder.putString(NBT, nbt.nbtPath());
      builder.putBoolean(NBT_INTERPRET, nbt.interpret());

      Component separator = nbt.separator();
      if (separator != null) {
        builder.put(NBT_SEPARATOR, serialize(separator));
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
      List<BinaryTag> serializedChildren = new ArrayList<>();

      for (Component child : children) {
        serializedChildren.add(this.writeCompoundComponent(child));
      }

      builder.put(EXTRA, ListBinaryTag.from(serializedChildren));
    }

    return builder.build();
  }

  @NotNull OptionState flags() {
    return this.flags;
  }

  private void writeComponentType(final String componentType, final CompoundBinaryTag.Builder builder) {
    if (this.flags.value(NBTSerializerOptions.SERIALIZE_COMPONENT_TYPES)) {
      builder.putString(TYPE, componentType);
    }
  }

  private static IllegalArgumentException notSureHowToSerialize(final Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }

  static final class BuilderImpl implements NBTComponentSerializer.Builder {

    private OptionState flags = OptionState.emptyOptionState();

    @Override
    public @NotNull Builder options(@NotNull OptionState flags) {
      this.flags = requireNonNull(flags, "flags");
      return this;
    }

    @Override
    public @NotNull Builder editOptions(@NotNull Consumer<OptionState.Builder> optionEditor) {
      final OptionState.Builder builder = OptionState.optionState()
        .values(this.flags);
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
