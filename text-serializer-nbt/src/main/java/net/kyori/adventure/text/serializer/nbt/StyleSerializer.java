package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

final class StyleSerializer {

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

  private StyleSerializer() {
  }

  static @NotNull Style deserialize(@NotNull CompoundBinaryTag compound, @NotNull NBTComponentSerializerImpl serializer) {
    Style.Builder styleBuilder = Style.style();

    String colorString = compound.getString(COLOR);
    if (!colorString.isEmpty()) {
      if (colorString.startsWith(TextColor.HEX_PREFIX)) {
        styleBuilder.color(TextColor.fromHexString(colorString));
      } else {
        styleBuilder.color(NamedTextColor.NAMES.value(colorString));
      }
    }

    styleBuilder.decoration(TextDecoration.BOLD, readOptionalState(BOLD, compound))
      .decoration(TextDecoration.ITALIC, readOptionalState(ITALIC, compound))
      .decoration(TextDecoration.UNDERLINED, readOptionalState(UNDERLINED, compound))
      .decoration(TextDecoration.STRIKETHROUGH, readOptionalState(STRIKETHROUGH, compound))
      .decoration(TextDecoration.OBFUSCATED, readOptionalState(OBFUSCATED, compound));

    String fontString = compound.getString(FONT);
    if (!fontString.isEmpty()) {
      styleBuilder.font(Key.key(fontString));
    }

    BinaryTag binaryInsertion = compound.get(INSERTION);
    if (binaryInsertion != null) {
      styleBuilder.insertion(((StringBinaryTag) binaryInsertion).value());
    }

    BinaryTag binaryClickEvent = compound.get(CLICK_EVENT);
    if (binaryClickEvent != null) {
      styleBuilder.clickEvent(ClickEventSerializer.deserialize((CompoundBinaryTag) binaryClickEvent));
    }

    BinaryTag binaryHoverEvent = compound.get(HOVER_EVENT);
    if (binaryHoverEvent != null) {
      styleBuilder.hoverEvent(HoverEventSerializer.deserialize((CompoundBinaryTag) binaryHoverEvent, serializer));
    }

    return styleBuilder.build();
  }

  static void serialize(@NotNull Style style, CompoundBinaryTag.@NotNull Builder builder,
                        @NotNull NBTComponentSerializerImpl serializer) {
    TextColor color = style.color();

    if (color != null) {
      if (color instanceof NamedTextColor) {
        builder.putString(COLOR, color.toString());
      } else {
        builder.putString(COLOR, color.asHexString());
      }
    }

    style.decorations().forEach((decoration, state) -> {
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

    Key font = style.font();

    if (font != null) {
      builder.putString(FONT, font.asString());
    }

    String insertion = style.insertion();

    if (insertion != null) {
      builder.putString(INSERTION, insertion);
    }

    ClickEvent clickEvent = style.clickEvent();

    if (clickEvent != null) {
      builder.put(CLICK_EVENT, ClickEventSerializer.serialize(clickEvent));
    }

    HoverEvent<?> hoverEvent = style.hoverEvent();

    if (hoverEvent != null) {
      CompoundBinaryTag binaryHoverEvent = HoverEventSerializer.serialize(hoverEvent, serializer);
      if (binaryHoverEvent != null) {
        builder.put(HOVER_EVENT, binaryHoverEvent);
      }
    }
  }

  private static TextDecoration.@NotNull State readOptionalState(@NotNull String key, @NotNull CompoundBinaryTag compound) {
    BinaryTag tag = compound.get(key);
    if (tag == null) {
      return TextDecoration.State.NOT_SET;
    }
    return TextDecoration.State.byBoolean(((ByteBinaryTag) tag).value() != 0);
  }
}
