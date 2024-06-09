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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class NBTComponentSerializerImpl implements NBTComponentSerializer {

  private static final String TYPE = "type";

  private static final String TYPE_TEXT = "text";
  private static final String TYPE_TRANSLATABLE = "translatable";
  private static final String TYPE_KEYBIND = "keybind";
  private static final String TYPE_SCORE = "score";
  private static final String TYPE_SELECTOR = "selector";
  private static final String TYPE_NBT = "nbt";

  private static final String EXTRA = "extra";

  private static final String COLOR = "color";
  private static final String BOLD = "bold";
  private static final String ITALIC = "italic";
  private static final String UNDERLINED = "underlined";
  private static final String STRIKETHROUGH = "strikethrough";
  private static final String OBFUSCATED = "obfuscated";
  private static final String FONT = "font";
  private static final String INSERTION = "insertion";
  private static final String CLICK_EVENT = "clickEvent";
  private static final String HOVER_EVENT = "hoverEvent";

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
    return null;
  }

  @Override
  public @NotNull BinaryTag serialize(@NotNull Component component) {
    if (this.flags.value(NBTSerializerOptions.EMIT_COMPACT_TEXT_COMPONENT) && component instanceof TextComponent
      && !component.hasStyling()) {
      return StringBinaryTag.stringBinaryTag(((TextComponent) component).content());
    }
    return writeCompoundComponent(component);
  }

  private @NotNull CompoundBinaryTag writeCompoundComponent(@NotNull Component component) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder();

    TextColor color = component.color();

    if (color != null) {
      if (color instanceof NamedTextColor) {
        builder.putString(COLOR, color.toString());
      } else {
        builder.putString(COLOR, color.asHexString());
      }
    }

    component.decorations().forEach((decoration, state) -> {
      if (state != TextDecoration.State.NOT_SET) {
        String decorationName;

        switch (decoration) {
          case OBFUSCATED:
            decorationName = OBFUSCATED;
            break;
          case BOLD:
            decorationName = BOLD;
            break;
          case STRIKETHROUGH:
            decorationName = STRIKETHROUGH;
            break;
          case UNDERLINED:
            decorationName = UNDERLINED;
            break;
          case ITALIC:
            decorationName = ITALIC;
            break;
          default:
            // Never called, but needed for proper compilation
            throw new IllegalStateException("Unknown text decoration: " + decoration);
        }

        builder.putBoolean(decorationName, state == TextDecoration.State.TRUE);
      }
    });

    Key font = component.font();

    if (font != null) {
      builder.putString(FONT, font.asString());
    }

    String insertion = component.insertion();

    if (insertion != null) {
      builder.putString(INSERTION, insertion);
    }

    ClickEvent clickEvent = component.clickEvent();

    if (clickEvent != null) {
      builder.put(CLICK_EVENT, ClickEventSerializer.serialize(clickEvent));
    }

    HoverEvent<?> hoverEvent = component.hoverEvent();

    if (hoverEvent != null) {
      builder.put(HOVER_EVENT, HoverEventSerializer.serialize(hoverEvent, this));
    }

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
        serializedChildren.add(this.serialize(child));
      }

      builder.put(EXTRA, ListBinaryTag.from(serializedChildren));
    }

    return builder.build();
  }

  private void writeComponentType(final String componentType, final CompoundBinaryTag.Builder builder) {
    if (this.flags.value(NBTSerializerOptions.SERIALIZE_COMPONENT_TYPES)) {
      builder.putString(TYPE, componentType);
    }
  }

  private static IllegalArgumentException notSureHowToSerialize(final Component component) {
    return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
  }
}
