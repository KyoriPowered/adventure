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

import java.io.IOException;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_COMMAND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAYLOAD;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_URL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.SNBT_CODEC;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.requiredTag;

final class ClickEventSerializer {

  private ClickEventSerializer() {
  }

  static @Nullable ClickEvent deserialize(final @NotNull CompoundBinaryTag compound, final boolean snakeCase) {
    final StringBinaryTag actionTag = requiredTag(compound, CLICK_EVENT_ACTION, BinaryTagTypes.STRING);
    final ClickEvent.Action action = ClickEvent.Action.NAMES.valueOrThrow(actionTag.value());

    if (!action.readable()) {
      return null;
    }

    if (snakeCase) {
      switch (action) {
        case OPEN_URL:
          final StringBinaryTag urlTag = requiredTag(compound, CLICK_EVENT_URL, BinaryTagTypes.STRING);
          return ClickEvent.openUrl(urlTag.value());
        case RUN_COMMAND:
        case SUGGEST_COMMAND:
          final StringBinaryTag commandTag = requiredTag(compound, CLICK_EVENT_COMMAND, BinaryTagTypes.STRING);
          final String command = commandTag.value();
          return action == ClickEvent.Action.RUN_COMMAND ? ClickEvent.runCommand(command) : ClickEvent.suggestCommand(command);
        case CHANGE_PAGE:
          final IntBinaryTag pageTag = requiredTag(compound, CLICK_EVENT_PAGE, BinaryTagTypes.INT);
          return ClickEvent.changePage(pageTag.value());
        case COPY_TO_CLIPBOARD:
          final StringBinaryTag valueTag = requiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING);
          return ClickEvent.copyToClipboard(valueTag.value());
        case CUSTOM:
          try {
            final StringBinaryTag clickEventIdTag = requiredTag(compound, CLICK_EVENT_ID, BinaryTagTypes.STRING);
            final BinaryTag payloadTag = requiredTag(compound, CLICK_EVENT_PAYLOAD);
            return ClickEvent.custom(KeySerializer.deserialize(clickEventIdTag), BinaryTagHolder.encode(payloadTag, SNBT_CODEC));
          } catch (final IOException exception) {
            throw new RuntimeException("An error occurred while encoding payload tag", exception);
          }
        default:
          // Never called, but needed for proper compilation
          throw new IllegalArgumentException("Unknown click event action: " + action);
      }
    } else {
      final StringBinaryTag valueTag = requiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING);
      return ClickEvent.clickEvent(action, valueTag.value());
    }
  }

  static @Nullable CompoundBinaryTag serialize(final @NotNull ClickEvent event, final boolean snakeCase) {
    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(CLICK_EVENT_ACTION, ClickEvent.Action.NAMES.keyOrThrow(event.action()));

    if (snakeCase) {
      final ClickEvent.Action action = event.action();
      if (!action.readable()) {
        return null;
      }

      final ClickEvent.Payload payload = event.payload();
      if (payload instanceof ClickEvent.Payload.Text) {
        final String payloadFieldName;
        switch (action) {
          case OPEN_URL:
            payloadFieldName = CLICK_EVENT_URL;
            break;
          case RUN_COMMAND:
          case SUGGEST_COMMAND:
            payloadFieldName = CLICK_EVENT_COMMAND;
            break;
          case COPY_TO_CLIPBOARD:
            payloadFieldName = CLICK_EVENT_VALUE;
            break;
          default:
            // Never called, but needed for proper compilation
            throw new IllegalArgumentException("Unknown click event action: " + action);
        }
        builder.putString(payloadFieldName, ((ClickEvent.Payload.Text) payload).value());
      } else if (payload instanceof ClickEvent.Payload.Custom) {
        try {
          final ClickEvent.Payload.Custom castPayload = (ClickEvent.Payload.Custom) payload;
          builder.put(CLICK_EVENT_ID, KeySerializer.serialize(castPayload.key()));
          builder.put(CLICK_EVENT_PAYLOAD, castPayload.nbt().get(SNBT_CODEC));
        } catch (final IOException exception) {
          throw new RuntimeException("An error occurred while decoding a payload tag", exception);
        }
      } else if (payload instanceof ClickEvent.Payload.Int) {
        builder.putInt(CLICK_EVENT_PAGE, ((ClickEvent.Payload.Int) payload).integer());
      }
    } else {
      builder.putString(CLICK_EVENT_VALUE, event.value());
    }

    return builder.build();
  }
}
