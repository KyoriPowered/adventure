/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.COLOR;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.FONT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT_CONTENTS;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.HOVER_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.json.JSONComponentConstants.INSERTION;

final class StyleSerializer extends TypeAdapter<Style> {
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
      throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
    }
  }

  static TypeAdapter<Style> create(final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
    return new StyleSerializer(legacyHover, emitLegacyHover, gson).nullSafe();
  }

  private final net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHover;
  private final boolean emitLegacyHover;
  private final Gson gson;

  private StyleSerializer(final net.kyori.adventure.text.serializer.json.@Nullable LegacyHoverEventSerializer legacyHover, final boolean emitLegacyHover, final Gson gson) {
    this.legacyHover = legacyHover;
    this.emitLegacyHover = emitLegacyHover;
    this.gson = gson;
  }

  @Override
  public Style read(final JsonReader in) throws IOException {
    in.beginObject();
    final Style.Builder style = Style.style();

    while (in.hasNext()) {
      final String fieldName = in.nextName();
      if (fieldName.equals(FONT)) {
        style.font(this.gson.fromJson(in, SerializerFactory.KEY_TYPE));
      } else if (fieldName.equals(COLOR)) {
        final TextColorWrapper color = this.gson.fromJson(in, SerializerFactory.COLOR_WRAPPER_TYPE);
        if (color.color != null) {
          style.color(color.color);
        } else if (color.decoration != null) {
          style.decoration(color.decoration, TextDecoration.State.TRUE);
        }
      } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
        style.decoration(TextDecoration.NAMES.value(fieldName), this.readBoolean(in));
      } else if (fieldName.equals(INSERTION)) {
        style.insertion(in.nextString());
      } else if (fieldName.equals(CLICK_EVENT)) {
        in.beginObject();
        ClickEvent.Action action = null;
        String value = null;
        while (in.hasNext()) {
          final String clickEventField = in.nextName();
          if (clickEventField.equals(CLICK_EVENT_ACTION)) {
            action = this.gson.fromJson(in, SerializerFactory.CLICK_ACTION_TYPE);
          } else if (clickEventField.equals(CLICK_EVENT_VALUE)) {
            value = in.peek() == JsonToken.NULL ? null : in.nextString();
          } else {
            in.skipValue();
          }
        }
        if (action != null && action.readable() && value != null) {
          style.clickEvent(ClickEvent.clickEvent(action, value));
        }
        in.endObject();
      } else if (fieldName.equals(HOVER_EVENT)) {
        final JsonObject hoverEventObject = this.gson.fromJson(in, JsonObject.class);
        if (hoverEventObject != null) {
          final JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive(HOVER_EVENT_ACTION);
          if (serializedAction == null) {
            continue;
          }

          @SuppressWarnings("unchecked")
          final HoverEvent.Action<Object> action = this.gson.fromJson(serializedAction, SerializerFactory.HOVER_ACTION_TYPE);
          if (action.readable()) {
            final @Nullable Object value;
            final Class<?> actionType = action.type();
            if (hoverEventObject.has(HOVER_EVENT_CONTENTS)) {
              final @Nullable JsonElement rawValue = hoverEventObject.get(HOVER_EVENT_CONTENTS);
              if (isNullOrEmpty(rawValue)) {
                value = null;
              } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
              } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
              } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
              } else {
                value = null;
              }
            } else if (hoverEventObject.has(HOVER_EVENT_VALUE)) {
              final JsonElement element = hoverEventObject.get(HOVER_EVENT_VALUE);
              if (isNullOrEmpty(element)) {
                value = null;
              } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                final Component rawValue = this.gson.fromJson(element, SerializerFactory.COMPONENT_TYPE);
                value = this.legacyHoverEventContents(action, rawValue);
              } else if (SerializerFactory.STRING_TYPE.isAssignableFrom(actionType)) {
                value = this.gson.fromJson(element, SerializerFactory.STRING_TYPE);
              } else {
                value = null;
              }
            } else {
              value = null;
            }

            if (value != null) {
              style.hoverEvent(HoverEvent.hoverEvent(action, value));
            }
          }
        }
      } else {
        in.skipValue();
      }
    }

    in.endObject();
    return style.build();
  }

  private static boolean isNullOrEmpty(final @Nullable JsonElement element) {
    return element == null || element.isJsonNull() || (element.isJsonArray() && element.getAsJsonArray().size() == 0) || (element.isJsonObject() && element.getAsJsonObject().size() == 0);
  }

  private boolean readBoolean(final JsonReader in) throws IOException {
    final JsonToken peek = in.peek();
    if (peek == JsonToken.BOOLEAN) {
      return in.nextBoolean();
    } else if (peek == JsonToken.STRING || peek == JsonToken.NUMBER) {
      return Boolean.parseBoolean(in.nextString());
    } else {
      throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
    }
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
      } catch (final IOException ex) {
        throw new JsonParseException(ex);
      }
    }
    // if we can't handle
    throw new UnsupportedOperationException();
  }

  private Codec.Decoder<Component, String, JsonParseException> decoder() {
    return string -> this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
  }

  private Codec.Encoder<Component, String, JsonParseException> encoder() {
    return component -> this.gson.toJson(component, SerializerFactory.COMPONENT_TYPE);
  }

  @Override
  public void write(final JsonWriter out, final Style value) throws IOException {
    out.beginObject();

    for (int i = 0, length = DECORATIONS.length; i < length; i++) {
      final TextDecoration decoration = DECORATIONS[i];
      final TextDecoration.State state = value.decoration(decoration);
      if (state != TextDecoration.State.NOT_SET) {
        final String name = TextDecoration.NAMES.key(decoration);
        assert name != null; // should never be null
        out.name(name);
        out.value(state == TextDecoration.State.TRUE);
      }
    }

    final @Nullable TextColor color = value.color();
    if (color != null) {
      out.name(COLOR);
      this.gson.toJson(color, SerializerFactory.COLOR_TYPE, out);
    }

    final @Nullable String insertion = value.insertion();
    if (insertion != null) {
      out.name(INSERTION);
      out.value(insertion);
    }

    final @Nullable ClickEvent clickEvent = value.clickEvent();
    if (clickEvent != null) {
      out.name(CLICK_EVENT);
      out.beginObject();
      out.name(CLICK_EVENT_ACTION);
      this.gson.toJson(clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, out);
      out.name(CLICK_EVENT_VALUE);
      out.value(clickEvent.value());
      out.endObject();
    }

    final @Nullable HoverEvent<?> hoverEvent = value.hoverEvent();
    if (hoverEvent != null && (hoverEvent.action() != HoverEvent.Action.SHOW_ACHIEVEMENT || this.emitLegacyHover)) {
      out.name(HOVER_EVENT);
      out.beginObject();
      out.name(HOVER_EVENT_ACTION);
      final HoverEvent.Action<?> action = hoverEvent.action();
      this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, out);
      if (action != HoverEvent.Action.SHOW_ACHIEVEMENT) { // legacy action has no modern contents value
        out.name(HOVER_EVENT_CONTENTS);
        if (action == HoverEvent.Action.SHOW_ITEM) {
          this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, out);
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
          this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, out);
        } else if (action == HoverEvent.Action.SHOW_TEXT) {
          this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
        } else {
          throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
        }
      }
      if (this.emitLegacyHover) {
        out.name(HOVER_EVENT_VALUE);
        this.serializeLegacyHoverEvent(hoverEvent, out);
      }

      out.endObject();
    }

    final @Nullable Key font = value.font();
    if (font != null) {
      out.name(FONT);
      this.gson.toJson(font, SerializerFactory.KEY_TYPE, out);
    }

    out.endObject();
  }

  private void serializeLegacyHoverEvent(final HoverEvent<?> hoverEvent, final JsonWriter out) throws IOException {
    if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) { // serialization is the same
      this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, out);
    } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
      this.gson.toJson(hoverEvent.value(), String.class, out);
    } else if (this.legacyHover != null) { // for data formats that require knowledge of SNBT
      Component serialized = null;
      try {
        if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
          serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity) hoverEvent.value(), this.encoder());
        } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
          serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem) hoverEvent.value());
        }
      } catch (final IOException ex) {
        throw new JsonSyntaxException(ex);
      }
      if (serialized != null) {
        this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, out);
      } else {
        out.nullValue();
      }
    } else {
      out.nullValue();
    }
  }
}
