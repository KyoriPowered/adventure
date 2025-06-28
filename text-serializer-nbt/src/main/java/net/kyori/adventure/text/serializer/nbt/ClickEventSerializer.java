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
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.nbt.TagStringIO;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_COMMAND;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAYLOAD;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_URL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.getRequiredTag;

final class ClickEventSerializer {

  private static final TagStringIO SNBT_IO = TagStringIO.tagStringIO();
  private static final Codec<BinaryTag, String, IOException, IOException> SNBT_CODEC = Codec.codec(SNBT_IO::asTag, SNBT_IO::asString);

  private ClickEventSerializer() {
  }

  static @Nullable ClickEvent deserialize(@NotNull CompoundBinaryTag compound, boolean snakeCase) {
    StringBinaryTag actionTag = getRequiredTag(compound, CLICK_EVENT_ACTION, BinaryTagTypes.STRING);
    ClickEvent.Action action = ClickEvent.Action.NAMES.valueOrThrow(actionTag.value());

    if (!action.readable()) {
      return null;
    }

    if (snakeCase) {
      switch (action) {
        case OPEN_URL:
          StringBinaryTag urlTag = getRequiredTag(compound, CLICK_EVENT_URL, BinaryTagTypes.STRING);
          return ClickEvent.openUrl(urlTag.value());
        case RUN_COMMAND:
        case SUGGEST_COMMAND:
          StringBinaryTag commandTag = getRequiredTag(compound, CLICK_EVENT_COMMAND, BinaryTagTypes.STRING);
          String command = commandTag.value();
          return action == ClickEvent.Action.RUN_COMMAND ? ClickEvent.runCommand(command) : ClickEvent.suggestCommand(command);
        case CHANGE_PAGE:
          IntBinaryTag pageTag = getRequiredTag(compound, CLICK_EVENT_PAGE, BinaryTagTypes.INT);
          return ClickEvent.changePage(pageTag.value());
        case COPY_TO_CLIPBOARD:
          StringBinaryTag valueTag = getRequiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING);
          return ClickEvent.copyToClipboard(valueTag.value());
        case CUSTOM:
          try {
            StringBinaryTag clickEventIdTag = getRequiredTag(compound, CLICK_EVENT_ID, BinaryTagTypes.STRING);
            BinaryTag payloadTag = getRequiredTag(compound, CLICK_EVENT_PAYLOAD);
            return ClickEvent.custom(Key.key(clickEventIdTag.value()), BinaryTagHolder.encode(payloadTag, SNBT_CODEC));
          } catch (IOException exception) {
            throw new RuntimeException("An error occurred while encoding payload tag", exception);
          }
        default:
          // Never called, but needed for proper compilation
          throw new IllegalArgumentException("Unknown click event action: " + action);
      }
    } else {
      StringBinaryTag valueTag = getRequiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING);
      return ClickEvent.clickEvent(action, valueTag.value());
    }
  }

  static @Nullable CompoundBinaryTag serialize(@NotNull ClickEvent event, boolean snakeCase) {
    CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(CLICK_EVENT_ACTION, ClickEvent.Action.NAMES.keyOrThrow(event.action()));

    if (snakeCase) {
      ClickEvent.Action action = event.action();
      if (!action.readable()) {
        return null;
      }

      ClickEvent.Payload payload = event.payload();
      if (payload instanceof ClickEvent.Payload.Text) {
        String payloadFieldName;
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
          ClickEvent.Payload.Custom castPayload = (ClickEvent.Payload.Custom) payload;
          builder.putString(CLICK_EVENT_ID, castPayload.key().asString());
          builder.put(CLICK_EVENT_PAYLOAD, castPayload.nbt().get(SNBT_CODEC));
        } catch (IOException exception) {
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
