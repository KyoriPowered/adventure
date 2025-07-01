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
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.optionalTag;

final class StyleSerializer {

  private StyleSerializer() {
  }

  static @NotNull Style deserialize(final @NotNull CompoundBinaryTag compound, final @NotNull NBTComponentSerializerImpl serializer) {
    final Style.Builder styleBuilder = Style.style();

    final StringBinaryTag colorTag = optionalTag(compound, COLOR, BinaryTagTypes.STRING);
    if (colorTag != null) {
      styleBuilder.color(TextColorSerializer.deserialize(colorTag));
    }

    for (final TextDecoration decoration : TextDecoration.values()) {
      final String name = TextDecoration.NAMES.keyOrThrow(decoration);
      final ByteBinaryTag decorationTag = optionalTag(compound, name, BinaryTagTypes.BYTE);
      if (decorationTag == null) continue;
      styleBuilder.decoration(decoration, NBTSerializerUtils.asBoolean(decorationTag));
    }

    final StringBinaryTag fontTag = optionalTag(compound, FONT, BinaryTagTypes.STRING);
    if (fontTag != null) {
      styleBuilder.font(KeySerializer.deserialize(fontTag));
    }

    final StringBinaryTag insertionTag = optionalTag(compound, INSERTION, BinaryTagTypes.STRING);
    if (insertionTag != null) {
      styleBuilder.insertion(insertionTag.value());
    }

    CompoundBinaryTag clickEventTag = optionalTag(compound, CLICK_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (clickEventTag == null) {
      clickEventTag = optionalTag(compound, CLICK_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (clickEventTag != null) {
        styleBuilder.clickEvent(ClickEventSerializer.deserialize(clickEventTag, false));
      }
    } else {
      styleBuilder.clickEvent(ClickEventSerializer.deserialize(clickEventTag, true));
    }

    CompoundBinaryTag hoverEventTag = optionalTag(compound, HOVER_EVENT_SNAKE, BinaryTagTypes.COMPOUND);
    if (hoverEventTag == null) {
      hoverEventTag = optionalTag(compound, HOVER_EVENT_CAMEL, BinaryTagTypes.COMPOUND);
      if (hoverEventTag != null) {
        styleBuilder.hoverEvent(HoverEventSerializer.deserialize(hoverEventTag, false, serializer));
      }
    } else {
      styleBuilder.hoverEvent(HoverEventSerializer.deserialize(hoverEventTag, true, serializer));
    }

    final BinaryTag shadowColorTag = compound.get(SHADOW_COLOR);
    if (shadowColorTag != null) {
      styleBuilder.shadowColor(ShadowColorSerializer.deserialize(shadowColorTag));
    }

    return styleBuilder.build();
  }

  static void serialize(final @NotNull Style style, final CompoundBinaryTag.@NotNull Builder builder,
                        final @NotNull NBTComponentSerializerImpl serializer) {
    final OptionState flags = serializer.options();

    final TextColor color = style.color();
    if (color != null) {
      builder.put(COLOR, TextColorSerializer.serialize(color));
    }

    final ShadowColor shadowColor = style.shadowColor();
    if (shadowColor != null) {
      final BinaryTag shadowColorTag = ShadowColorSerializer.serialize(shadowColor, serializer);
      if (shadowColorTag != null) {
        builder.put(SHADOW_COLOR, shadowColorTag);
      }
    }

    for (final TextDecoration decoration : TextDecoration.values()) {
      final TextDecoration.State state = style.decoration(decoration);
      if (state == TextDecoration.State.NOT_SET) continue;
      final String name = TextDecoration.NAMES.keyOrThrow(decoration);
      builder.putBoolean(name, state == TextDecoration.State.TRUE);
    }

    final Key font = style.font();
    if (font != null) {
      builder.put(FONT, KeySerializer.serialize(font));
    }

    final String insertion = style.insertion();
    if (insertion != null) {
      builder.putString(INSERTION, insertion);
    }

    final ClickEvent clickEvent = style.clickEvent();
    if (clickEvent != null) {
      final NBTSerializerOptions.ClickEventValueMode clickEventValueMode = flags.value(NBTSerializerOptions.EMIT_CLICK_EVENT_TYPE);

      final boolean emitBothClickEvents = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.BOTH;
      final boolean emitSnakeCaseClickEvent = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.SNAKE_CASE;
      final boolean emitCamelCaseClickEvent = clickEventValueMode == NBTSerializerOptions.ClickEventValueMode.CAMEL_CASE;

      if (emitBothClickEvents || emitSnakeCaseClickEvent) {
        final BinaryTag clickEventTag = ClickEventSerializer.serialize(clickEvent, true);
        if (clickEventTag != null) {
          builder.put(CLICK_EVENT_SNAKE, clickEventTag);
        }
      }

      if (emitBothClickEvents || emitCamelCaseClickEvent) {
        final BinaryTag clickEventTag = ClickEventSerializer.serialize(clickEvent, false);
        if (clickEventTag != null) {
          builder.put(CLICK_EVENT_CAMEL, clickEventTag);
        }
      }
    }

    final HoverEvent<?> hoverEvent = style.hoverEvent();
    if (hoverEvent != null) {
      final NBTSerializerOptions.HoverEventValueMode hoverEventValueMode = flags.value(NBTSerializerOptions.EMIT_HOVER_EVENT_TYPE);

      final boolean emitBothHoverEvents = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.BOTH;
      final boolean emitSnakeCaseHoverEvent = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.SNAKE_CASE;
      final boolean emitCamelCaseHoverEvent = hoverEventValueMode == NBTSerializerOptions.HoverEventValueMode.CAMEL_CASE;

      if (emitBothHoverEvents || emitSnakeCaseHoverEvent) {
        final BinaryTag hoverEventTag = HoverEventSerializer.serialize(hoverEvent, true, serializer);
        if (hoverEventTag != null) {
          builder.put(HOVER_EVENT_SNAKE, hoverEventTag);
        }
      }

      if (emitBothHoverEvents || emitCamelCaseHoverEvent) {
        final BinaryTag hoverEventTag = HoverEventSerializer.serialize(hoverEvent, false, serializer);
        if (hoverEventTag != null) {
          builder.put(HOVER_EVENT_CAMEL, hoverEventTag);
        }
      }
    }
  }
}
