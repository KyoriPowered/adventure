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
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.COLOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.FONT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_SNAKE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.INSERTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHADOW_COLOR;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getOptionalTag;

final class StyleSerializer {

  @SuppressWarnings("checkstyle:NoWhitespaceAfter")
  private static final TextDecoration[] DECORATIONS = {
    // The order here is important -- Minecraft does string comparisons of some
    // serialized components so we have to make sure our order matches Vanilla
    TextDecoration.BOLD,
    TextDecoration.ITALIC,
    TextDecoration.UNDERLINED,
    TextDecoration.STRIKETHROUGH,
    TextDecoration.OBFUSCATED
  };

  static {
    // Ensure coverage of decorations
    final Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
    for (final TextDecoration decoration : DECORATIONS) {
      knownDecorations.remove(decoration);
    }
    if (!knownDecorations.isEmpty()) {
      throw new IllegalStateException("NBT serializer is missing some text decorations: " + knownDecorations);
    }
  }

  private StyleSerializer() {
  }

  static @NotNull Style deserialize(@NotNull CompoundBinaryTag compound, @NotNull NBTComponentSerializerImpl serializer) {
    Style.Builder styleBuilder = Style.style();

    StringBinaryTag colorTag = NBTSerializerUtils.getOptionalTag(compound, COLOR, BinaryTagTypes.STRING);
    if (colorTag != null) {
      styleBuilder.color(TextColorSerializer.deserialize(colorTag));
    }

    for (TextDecoration decoration : DECORATIONS) {
      String name = TextDecoration.NAMES.keyOrThrow(decoration);
      ByteBinaryTag decorationTag = NBTSerializerUtils.getOptionalTag(compound, name, BinaryTagTypes.BYTE);
      if (decorationTag == null) continue;
      styleBuilder.decoration(decoration, NBTSerializerUtils.asBoolean(decorationTag));
    }

    StringBinaryTag fontTag = getOptionalTag(compound, FONT, BinaryTagTypes.STRING);
    if (fontTag != null) {
      styleBuilder.font(Key.key(fontTag.value()));
    }

    StringBinaryTag insertionTag = getOptionalTag(compound, INSERTION, BinaryTagTypes.STRING);
    if (insertionTag != null) {
      styleBuilder.insertion(insertionTag.value());
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
      builder.put(COLOR, TextColorSerializer.serialize(color));
    }

    for (TextDecoration decoration : DECORATIONS) {
      TextDecoration.State state = style.decoration(decoration);
      if (state == TextDecoration.State.NOT_SET) continue;
      String name = TextDecoration.NAMES.keyOrThrow(decoration);
      builder.putBoolean(name, state == TextDecoration.State.TRUE);
    }

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
