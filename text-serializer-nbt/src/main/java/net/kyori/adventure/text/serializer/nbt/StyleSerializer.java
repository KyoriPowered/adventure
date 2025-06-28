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

  private StyleSerializer() {
  }

  static @NotNull Style deserialize(@NotNull CompoundBinaryTag compound, @NotNull NBTComponentSerializerImpl serializer) {
    Style.Builder styleBuilder = Style.style();

    StringBinaryTag colorTag = NBTSerializerUtils.getOptionalTag(compound, COLOR, BinaryTagTypes.STRING);
    if (colorTag != null) {
      styleBuilder.color(TextColorSerializer.deserialize(colorTag));
    }

    for (TextDecoration decoration : TextDecoration.values()) {
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

    CompoundBinaryTag clickEventTag = getOptionalTag(compound, CLICK_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (clickEventTag == null) {
      clickEventTag = getOptionalTag(compound, CLICK_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (clickEventTag != null) {
        styleBuilder.clickEvent(ClickEventSerializer.deserialize(clickEventTag, false));
      }
    } else {
      styleBuilder.clickEvent(ClickEventSerializer.deserialize(clickEventTag, true));
    }

    CompoundBinaryTag hoverEventTag = getOptionalTag(compound, HOVER_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (hoverEventTag == null) {
      hoverEventTag = getOptionalTag(compound, HOVER_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (hoverEventTag != null) {
        styleBuilder.hoverEvent(HoverEventSerializer.deserialize(hoverEventTag, false, serializer));
      }
    } else {
      styleBuilder.hoverEvent(HoverEventSerializer.deserialize(hoverEventTag, true, serializer));
    }

    BinaryTag shadowColorTag = compound.get(SHADOW_COLOR);
    if (shadowColorTag != null) {
      styleBuilder.shadowColor(ShadowColorSerializer.deserialize(shadowColorTag));
    }

    return styleBuilder.build();
  }

  static void serialize(@NotNull Style style, CompoundBinaryTag.@NotNull Builder builder,
                        @NotNull NBTComponentSerializerImpl serializer) {
    OptionState flags = serializer.options();

    TextColor color = style.color();
    if (color != null) {
      builder.put(COLOR, TextColorSerializer.serialize(color));
    }

    for (TextDecoration decoration : TextDecoration.values()) {
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
        BinaryTag clickEventTag = ClickEventSerializer.serialize(clickEvent, true);
        if (clickEventTag != null) {
          builder.put(CLICK_EVENT_SNAKE, clickEventTag);
        }
      }

      if (emitBothClickEvents || emitCamelCaseClickEvent) {
        BinaryTag clickEventTag = ClickEventSerializer.serialize(clickEvent, false);
        if (clickEventTag != null) {
          builder.put(CLICK_EVENT_CAMEL, clickEventTag);
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
        BinaryTag hoverEventTag = HoverEventSerializer.serialize(hoverEvent, true, serializer);
        if (hoverEventTag != null) {
          builder.put(HOVER_EVENT_SNAKE, hoverEventTag);
        }
      }

      if (emitBothHoverEvents || emitCamelCaseHoverEvent) {
        BinaryTag hoverEventTag = HoverEventSerializer.serialize(hoverEvent, false, serializer);
        if (hoverEventTag != null) {
          builder.put(HOVER_EVENT_CAMEL, hoverEventTag);
        }
      }
    }

    ShadowColor shadowColor = style.shadowColor();
    if (shadowColor != null) {
      BinaryTag shadowColorTag = ShadowColorSerializer.serialize(shadowColor, serializer);
      if (shadowColorTag != null) {
        builder.put(SHADOW_COLOR, shadowColorTag);
      }
    }
  }
}
