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

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CONTENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class HoverEventSerializer {

  private HoverEventSerializer() {
  }

  static @Nullable HoverEvent<?> deserialize(@NotNull CompoundBinaryTag compound, boolean snakeCase,
                                             @NotNull NBTComponentSerializerImpl serializer) {
    String actionString = compound.getString(HOVER_EVENT_ACTION);
    HoverEvent.Action<?> action = HoverEvent.Action.NAMES.valueOrThrow(actionString);

    if (!action.readable()) {
      return null;
    }

    CompoundBinaryTag contentsTag = snakeCase ? compound : getRequiredTag(compound, HOVER_EVENT_CONTENTS, BinaryTagTypes.COMPOUND);
    if (action == HoverEvent.Action.SHOW_TEXT) {
      BinaryTag textTag;
      if (snakeCase) {
        textTag = compound.get(HOVER_EVENT_VALUE);
        if (textTag == null)
          throw new IllegalArgumentException("The show text hover event action tag does not contain a text field");
      } else {
        textTag = contentsTag;
      }
      return HoverEvent.showText(serializer.deserialize(textTag));
    } else if (action == HoverEvent.Action.SHOW_ITEM) {
      return HoverEvent.showItem(ShowItemSerializer.deserialize(contentsTag, snakeCase));
    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
      return HoverEvent.showEntity(ShowEntitySerializer.deserialize(contentsTag, snakeCase, serializer));
    } else {
      throw new IllegalArgumentException("Don't know how to deserialize a hoverEvent with action of " + actionString + " from a binary tag");
    }
  }

  static <V> @Nullable CompoundBinaryTag serialize(@NotNull HoverEvent<V> event, boolean snakeCase,
                                                   @NotNull NBTComponentSerializerImpl serializer) {
    HoverEvent.Action<V> action = event.action();
    if (!action.readable()) {
      return null;
    }

    BinaryTag contentsTag;
    if (action == HoverEvent.Action.SHOW_TEXT) {
      BinaryTag serializedComponent = serializer.serialize((Component) event.value());
      if (snakeCase) {
        contentsTag = CompoundBinaryTag.builder()
          .put(HOVER_EVENT_VALUE, serializedComponent)
          .build();
      } else {
        contentsTag = serializedComponent;
      }
    } else if (action == HoverEvent.Action.SHOW_ITEM) {
      contentsTag = ShowItemSerializer.serialize((HoverEvent.ShowItem) event.value(), snakeCase, serializer);
    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
      contentsTag = ShowEntitySerializer.serialize((HoverEvent.ShowEntity) event.value(), snakeCase, serializer);
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + event + " as a binary tag");
    }

    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(HOVER_EVENT_ACTION, HoverEvent.Action.NAMES.keyOrThrow(action));

    if (snakeCase) {
      CompoundBinaryTag castContentsTag = (CompoundBinaryTag) contentsTag;
      castContentsTag.forEach(entry -> builder.put(entry.getKey(), entry.getValue()));
    } else {
      builder.put(HOVER_EVENT_CONTENTS, contentsTag);
    }

    return builder.build();
  }
}
