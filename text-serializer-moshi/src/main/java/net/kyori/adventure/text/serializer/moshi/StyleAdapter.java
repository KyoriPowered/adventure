/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.Nullable;

final class StyleAdapter extends JsonAdapter<Style> {
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
    final Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
    for (final TextDecoration decoration : DECORATIONS) {
      knownDecorations.remove(decoration);
    }
    if (!knownDecorations.isEmpty()) {
      throw new IllegalStateException("Moshi serializer is missing some text decorations: " + knownDecorations);
    }
  }

  static final String FONT = "font";
  static final String COLOR = "color";
  static final String INSERTION = "insertion";
  static final String CLICK_EVENT = "clickEvent";
  static final String CLICK_EVENT_ACTION = "action";
  static final String CLICK_EVENT_VALUE = "value";
  static final String HOVER_EVENT = "hoverEvent";
  static final String HOVER_EVENT_ACTION = "action";
  static final String HOVER_EVENT_CONTENTS = "contents";
  static final @Deprecated String HOVER_EVENT_VALUE = "value";
  private static final JsonReader.Options CLICK_EVENT_OPTIONS = JsonReader.Options.of(CLICK_EVENT_ACTION, CLICK_EVENT_VALUE);
  private static final JsonReader.Options HOVER_EVENT_OPTIONS = JsonReader.Options.of(HOVER_EVENT_ACTION, HOVER_EVENT_CONTENTS, HOVER_EVENT_VALUE);

  static JsonAdapter<Style> create(final Moshi moshi, final @Nullable LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover) {
    return new StyleAdapter(moshi, legacyHover, emitLegacyHover).nullSafe();
  }

  private final Moshi moshi;
  private final LegacyHoverEventSerializer legacyHover;
  private final boolean emitLegacyHover;

  private StyleAdapter(final Moshi moshi, final @Nullable LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover) {
    this.moshi = moshi;
    this.legacyHover = legacyHover;
    this.emitLegacyHover = emitLegacyHover;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Style fromJson(final JsonReader reader) throws IOException {
    reader.beginObject();
    final Style.Builder style = Style.style();

    while (reader.hasNext()) {
      final String fieldName = reader.nextName();
      if (fieldName.equals(FONT)) {
        style.font(this.moshi.adapter(SerializerFactory.KEY_TYPE).fromJson(reader));
      } else if (fieldName.equals(COLOR)) {
        final TextColorWrapper color = this.moshi.adapter(SerializerFactory.COLOR_WRAPPER_TYPE).fromJson(reader);
        if (color.color != null) {
          style.color(color.color);
        } else if (color.decoration != null) {
          style.decoration(color.decoration, TextDecoration.State.TRUE);
        }
      } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
        style.decoration(TextDecoration.NAMES.value(fieldName), reader.nextBoolean());
      } else if (fieldName.equals(INSERTION)) {
        style.insertion(reader.nextString());
      } else if (fieldName.equals(CLICK_EVENT)) {
        reader.beginObject();
        ClickEvent.Action action = null;
        String value = null;
        while (reader.hasNext()) {
          switch (reader.selectName(CLICK_EVENT_OPTIONS)) {
            case 0:
              action = this.moshi.adapter(SerializerFactory.CLICK_ACTION_TYPE).fromJson(reader);
              break;
            case 1:
              value = reader.peek() == JsonReader.Token.NULL ? null : reader.nextString();
              break;
            default:
              reader.skipValue();
              break;
          }
        }
        if (action != null && action.readable() && value != null) {
          style.clickEvent(ClickEvent.clickEvent(action, value));
        }
        reader.endObject();
      } else if (fieldName.equals(HOVER_EVENT)) {
        reader.beginObject();
        HoverEvent.@Nullable Action action = null;
        @Nullable Object value = null;
        while (reader.hasNext()) {
          switch (reader.selectName(HOVER_EVENT_OPTIONS)) {
            case 0:
              action = this.moshi.adapter(SerializerFactory.HOVER_ACTION_TYPE).fromJson(reader);
              break;
            case 1:
              if (action != null && action.readable()) {
                final Class<?> actionType = action.type();
                if (Component.class.isAssignableFrom(actionType)) {
                  value = this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).fromJson(reader);
                } else if (HoverEvent.ShowItem.class.isAssignableFrom(actionType)) {
                  value = this.moshi.adapter(SerializerFactory.SHOW_ITEM_TYPE).fromJson(reader);
                } else if (HoverEvent.ShowEntity.class.isAssignableFrom(actionType)) {
                  value = this.moshi.adapter(SerializerFactory.SHOW_ENTITY_TYPE).fromJson(reader);
                } else {
                  value = null;
                }
              }
              break;
            case 2:
              if (action != null && action.readable()) {
                final Component rawValue = this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).fromJson(reader);
                value = this.legacyHoverEventContents(action, rawValue);
              }
              break;
            default:
              value = null;
              break;
          }
        }

        if (action != null && value != null) {
          style.hoverEvent(HoverEvent.hoverEvent(action, value));
        }
      } else {
        reader.skipValue();
      }
    }

    reader.endObject();
    return style.build();
  }

  @Override
  public void toJson(final JsonWriter writer, final Style value) throws IOException {
    writer.beginObject();

    for (int i = 0, length = DECORATIONS.length; i < length; i++) {
      final TextDecoration decoration = DECORATIONS[i];
      final TextDecoration.State state = value.decoration(decoration);
      if (state != TextDecoration.State.NOT_SET) {
        final String name = TextDecoration.NAMES.key(decoration);
        assert name != null; // should never be null
        writer.name(name);
        writer.value(state == TextDecoration.State.TRUE);
      }
    }

    final @Nullable TextColor color = value.color();
    if (color != null) {
      writer.name(COLOR);
      this.moshi.adapter(SerializerFactory.COLOR_TYPE).toJson(writer, color);
    }

    final @Nullable String insertion = value.insertion();
    if (insertion != null) {
      writer.name(INSERTION);
      writer.value(insertion);
    }

    final @Nullable ClickEvent clickEvent = value.clickEvent();
    if (clickEvent != null) {
      writer.name(CLICK_EVENT);
      writer.beginObject();
      writer.name(CLICK_EVENT_ACTION);
      this.moshi.adapter(SerializerFactory.CLICK_ACTION_TYPE).toJson(writer, clickEvent.action());
      writer.name(CLICK_EVENT_VALUE);
      writer.value(clickEvent.value());
      writer.endObject();
    }

    final @Nullable HoverEvent hoverEvent = value.hoverEvent();
    if (hoverEvent != null) {
      writer.name(HOVER_EVENT);
      writer.beginObject();
      writer.name(HOVER_EVENT_ACTION);
      final HoverEvent.Action<?> action = hoverEvent.action();
      this.moshi.adapter(SerializerFactory.HOVER_ACTION_TYPE).toJson(writer, action);
      writer.name(HOVER_EVENT_CONTENTS);
      if (action == HoverEvent.Action.SHOW_ITEM) {
        this.moshi.adapter(SerializerFactory.SHOW_ITEM_TYPE).toJson(writer, (HoverEvent.ShowItem) hoverEvent.value());
      } else if (action == HoverEvent.Action.SHOW_ENTITY) {
        this.moshi.adapter(SerializerFactory.SHOW_ENTITY_TYPE).toJson(writer, (HoverEvent.ShowEntity) hoverEvent.value());
      } else if (action == HoverEvent.Action.SHOW_TEXT) {
        this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).toJson(writer, (Component) hoverEvent.value());
      } else {
        throw new JsonDataException("Don't know how to serialize " + hoverEvent.value());
      }
      if (this.emitLegacyHover) {
        writer.name(HOVER_EVENT_VALUE);
        this.serializeLegacyHoverEvent(hoverEvent, writer);
      }

      writer.endObject();
    }

    final @Nullable Key font = value.font();
    if (font != null) {
      writer.name(FONT);
      this.moshi.adapter(SerializerFactory.KEY_TYPE).toJson(writer, font);
    }

    writer.endObject();
  }

  private Object legacyHoverEventContents(final HoverEvent.Action<?> action, final Component rawValue) {
    if (action == HoverEvent.Action.SHOW_TEXT) {
      return rawValue; // Passthrough -- no serialization needed
    } else if (this.legacyHover != null) {
      try {
        if (action == HoverEvent.Action.SHOW_ENTITY) {
          return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
          return this.legacyHover.deserializeShowItem(rawValue);
        }
      } catch (final IOException exception) {
        throw new JsonDataException(exception);
      }
    }
    // if we can't handle
    throw new UnsupportedOperationException();
  }

  private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter writer) throws IOException {
    if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) { // serialization is the same
      this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).toJson(writer, (Component) hoverEvent.value());
    } else if (this.legacyHover != null) { // for data formats that require knowledge of SNBT
      Component serialized = null;
      try {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
          serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.moshi.adapter(SerializerFactory.COMPONENT_TYPE)::toJson);
        } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
          serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
        }
      } catch (final IOException exception) {
        throw new JsonDataException(exception);
      }
      if (serialized != null) {
        this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).toJson(writer, serialized);
      } else {
        writer.nullValue();
      }
    } else {
      writer.nullValue();
    }
  }

  private Codec.Decoder<Component, String, JsonDataException> decoder() {
    return string -> {
      try {
        return Objects.requireNonNull(this.moshi.adapter(SerializerFactory.COMPONENT_TYPE).fromJson(string));
      } catch (final IOException exception) {
        throw new JsonDataException(exception);
      }
    };
  }
}
