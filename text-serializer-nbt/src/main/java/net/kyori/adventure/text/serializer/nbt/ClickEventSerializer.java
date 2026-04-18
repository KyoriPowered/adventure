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

  private static final String FALLBACK_URL_PROTOCOL = "https://";

  private ClickEventSerializer() {
  }

  static @Nullable ClickEvent<?> deserialize(final @NotNull CompoundBinaryTag compound, final boolean snakeCase) {
    final StringBinaryTag actionTag = requiredTag(compound, CLICK_EVENT_ACTION, BinaryTagTypes.STRING);
    final ClickEvent.Action<?> action = ClickEvent.Action.NAMES.valueOrThrow(actionTag.value());

    if (!action.readable()) {
      return null;
    }

    if (snakeCase) {
      return switch (action) {
        case ClickEvent.Action.OpenUrl ignored ->
          ClickEvent.openUrl(requiredTag(compound, CLICK_EVENT_URL, BinaryTagTypes.STRING).value());
        case ClickEvent.Action.RunCommand ignored ->
          ClickEvent.runCommand(requiredTag(compound, CLICK_EVENT_COMMAND, BinaryTagTypes.STRING).value());
        case ClickEvent.Action.SuggestCommand ignored ->
          ClickEvent.suggestCommand(requiredTag(compound, CLICK_EVENT_COMMAND, BinaryTagTypes.STRING).value());
        case ClickEvent.Action.ChangePage ignored ->
          ClickEvent.changePage(requiredTag(compound, CLICK_EVENT_PAGE, BinaryTagTypes.INT).value());
        case ClickEvent.Action.CopyToClipboard ignored ->
          ClickEvent.copyToClipboard(requiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING).value());
        case ClickEvent.Action.Custom ignored -> {
          try {
            final StringBinaryTag clickEventIdTag = requiredTag(compound, CLICK_EVENT_ID, BinaryTagTypes.STRING);
            final BinaryTag payloadTag = requiredTag(compound, CLICK_EVENT_PAYLOAD);
            yield ClickEvent.custom(KeySerializer.deserialize(clickEventIdTag), BinaryTagHolder.encode(payloadTag, SNBT_CODEC));
          } catch (final IOException exception) {
            throw new RuntimeException("An error occurred while encoding payload tag", exception);
          }
        }
        // Non-readable actions are filtered out above.
        case ClickEvent.Action.OpenFile ignored -> null;
        case ClickEvent.Action.ShowDialog ignored -> null;
      };
    } else {
      final String value = requiredTag(compound, CLICK_EVENT_VALUE, BinaryTagTypes.STRING).value();
      if (action instanceof ClickEvent.Action.TextCarrier) {
        @SuppressWarnings("unchecked")
        final ClickEvent.Action<ClickEvent.Payload.Text> textAction = (ClickEvent.Action<ClickEvent.Payload.Text>) action;
        return ClickEvent.clickEvent(textAction, ClickEvent.Payload.string(value));
      } else if (action instanceof ClickEvent.Action.ChangePage) {
        return ClickEvent.changePage(Integer.parseInt(value));
      }
      return null;
    }
  }

  static @Nullable CompoundBinaryTag serialize(final @NotNull ClickEvent<?> event, final boolean snakeCase,
                                               final @NotNull NBTComponentSerializerImpl serializer) {
    final ClickEvent.Action<?> action = event.action();
    if (!action.readable()) {
      return null;
    }

    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(CLICK_EVENT_ACTION, ClickEvent.Action.NAMES.keyOrThrow(action));

    final boolean emitHttps = serializer.options().value(NBTSerializerOptions.EMIT_CLICK_URL_HTTPS);

    if (snakeCase) {
      final ClickEvent.Payload payload = event.payload();
      switch (payload) {
        case ClickEvent.Payload.Text text -> {
          final String payloadFieldName = switch (action) {
            case ClickEvent.Action.OpenUrl ignored -> CLICK_EVENT_URL;
            case ClickEvent.Action.RunCommand ignored -> CLICK_EVENT_COMMAND;
            case ClickEvent.Action.SuggestCommand ignored -> CLICK_EVENT_COMMAND;
            case ClickEvent.Action.CopyToClipboard ignored -> CLICK_EVENT_VALUE;
            default -> throw new IllegalArgumentException("Unexpected text-payload click event action: " + action);
          };
          builder.putString(payloadFieldName, textPayloadValue(action, text.value(), emitHttps));
        }
        case ClickEvent.Payload.Int intPayload -> builder.putInt(CLICK_EVENT_PAGE, intPayload.integer());
        case ClickEvent.Payload.Custom customPayload -> {
          try {
            builder.put(CLICK_EVENT_ID, KeySerializer.serialize(customPayload.key()));
            final BinaryTagHolder nbt = customPayload.nbt();
            if (nbt != null) {
              builder.put(CLICK_EVENT_PAYLOAD, nbt.get(SNBT_CODEC));
            }
          } catch (final IOException exception) {
            throw new RuntimeException("An error occurred while decoding a payload tag", exception);
          }
        }
        case ClickEvent.Payload.Dialog ignored -> {
        }
      }
    } else {
      final ClickEvent.Payload payload = event.payload();
      final String value = switch (payload) {
        case ClickEvent.Payload.Text text -> textPayloadValue(action, text.value(), emitHttps);
        case ClickEvent.Payload.Int intPayload -> String.valueOf(intPayload.integer());
        case ClickEvent.Payload.Custom ignored -> null;
        case ClickEvent.Payload.Dialog ignored -> null;
      };
      if (value == null) {
        return null;
      }
      builder.putString(CLICK_EVENT_VALUE, value);
    }

    return builder.build();
  }

  private static @NotNull String textPayloadValue(final ClickEvent.@NotNull Action<?> action, final @NotNull String value, final boolean emitHttps) {
    if (emitHttps && action == ClickEvent.Action.OPEN_URL && !hasUrlScheme(value)) {
      return FALLBACK_URL_PROTOCOL + value;
    }
    return value;
  }

  @SuppressWarnings("HttpUrlsUsage")
  private static boolean hasUrlScheme(final @NotNull String url) {
    return url.startsWith("http://") || url.startsWith("https://");
  }
}
