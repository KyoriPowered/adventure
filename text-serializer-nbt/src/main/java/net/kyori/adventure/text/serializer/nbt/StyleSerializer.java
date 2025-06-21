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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHADOW_COLOR;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getOptionalTag;

final class StyleSerializer {

  private static final String COLOR = "color";
  private static final String BOLD = "bold";
  private static final String ITALIC = "italic";
  private static final String UNDERLINED = "underlined";
  private static final String STRIKETHROUGH = "strikethrough";
  private static final String OBFUSCATED = "obfuscated";
  private static final String FONT = "font";
  private static final String INSERTION = "insertion";

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

    CompoundBinaryTag binaryClickEvent = getOptionalTag(compound, CLICK_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (binaryClickEvent == null) {
      binaryClickEvent = getOptionalTag(compound, CLICK_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (binaryClickEvent != null) {
        styleBuilder.clickEvent(ClickEventSerializer.deserializeCamel(binaryClickEvent));
      }
    } else {
      styleBuilder.clickEvent(ClickEventSerializer.deserializeSnake(binaryClickEvent));
    }

    CompoundBinaryTag binaryHoverEvent = getOptionalTag(compound, HOVER_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (binaryHoverEvent == null) {
      binaryHoverEvent = getOptionalTag(compound, HOVER_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (binaryHoverEvent != null) {
        styleBuilder.hoverEvent(HoverEventSerializer.deserialize(binaryHoverEvent, false, serializer));
      }
    } else {
      styleBuilder.hoverEvent(HoverEventSerializer.deserialize(binaryHoverEvent, true, serializer));
    }

    BinaryTag shadowColorTag = compound.get(SHADOW_COLOR);
    if (shadowColorTag != null) {
      styleBuilder.shadowColor(ShadowColorSerializer.deserialize(shadowColorTag));
    }

    return styleBuilder.build();
  }

  static void serialize(@NotNull Style style, CompoundBinaryTag.@NotNull Builder builder,
                        @NotNull NBTComponentSerializerImpl serializer) {
    OptionState flags = serializer.flags();

    TextColor color = style.color();
    if (color != null) {
      builder.putString(COLOR, color.toString());
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
      NBTSerializerOptions.ClickEventValueMode clickEventValueMode = flags.value(NBTSerializerOptions.EMIT_CLICK_EVENT_TYPE);

      boolean emitBothClickEvents = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.BOTH;
      boolean emitSnakeCaseClickEvent = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.SNAKE_CASE;
      boolean emitCamelCaseClickEvent = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.CAMEL_CASE;

      if (emitBothClickEvents || emitSnakeCaseClickEvent) {
        BinaryTag serializedClickEvent = ClickEventSerializer.serialize(clickEvent, true);
        if (serializedClickEvent != null) {
          builder.put(CLICK_EVENT_SNAKE, serializedClickEvent);
        }
      }

      if (emitBothClickEvents || emitCamelCaseClickEvent) {
        BinaryTag serializedClickEvent = ClickEventSerializer.serialize(clickEvent, false);
        if (serializedClickEvent != null) {
          builder.put(CLICK_EVENT_CAMEL, serializedClickEvent);
        }
      }
    }

    HoverEvent<?> hoverEvent = style.hoverEvent();
    if (hoverEvent != null) {
      NBTSerializerOptions.HoverEventValueMode hoverEventValueMode = flags.value(NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE);

      boolean emitBothHoverEvents = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.BOTH;
      boolean emitSnakeCaseHoverEvent = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.SNAKE_CASE;
      boolean emitCamelCaseHoverEvent = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.CAMEL_CASE;

      if (emitBothHoverEvents || emitSnakeCaseHoverEvent) {
        BinaryTag serializedHoverEvent = HoverEventSerializer.serialize(hoverEvent, true, serializer);
        if (serializedHoverEvent != null) {
          builder.put(HOVER_EVENT_SNAKE, serializedHoverEvent);
        }
      }

      if (emitBothHoverEvents || emitCamelCaseHoverEvent) {
        BinaryTag serializedHoverEvent = HoverEventSerializer.serialize(hoverEvent, false, serializer);
        if (serializedHoverEvent != null) {
          builder.put(HOVER_EVENT_CAMEL, serializedHoverEvent);
        }
      }
    }

    ShadowColor shadowColor = style.shadowColor();
    if (shadowColor != null) {
      BinaryTag serializedShadowColor = ShadowColorSerializer.serialize(shadowColor, serializer);
      if (serializedShadowColor != null) {
        builder.put(SHADOW_COLOR, serializedShadowColor);
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
