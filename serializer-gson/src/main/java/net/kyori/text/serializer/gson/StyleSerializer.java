/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.kyori.text.Component;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

public class StyleSerializer implements JsonDeserializer<Style>, JsonSerializer<Style> {
  public static final StyleSerializer INSTANCE = new StyleSerializer();
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  static final String COLOR = "color";
  static final String INSERTION = "insertion";
  static final String CLICK_EVENT = "clickEvent";
  static final String CLICK_EVENT_ACTION = "action";
  static final String CLICK_EVENT_VALUE = "value";
  static final String HOVER_EVENT = "hoverEvent";
  static final String HOVER_EVENT_ACTION = "action";
  static final String HOVER_EVENT_VALUE = "value";

  @Override
  public Style deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final JsonObject object = json.getAsJsonObject();
    return this.deserialize(object, typeOfT, context);
  }

  private Style deserialize(final JsonObject json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    final Style.Builder style = Style.builder();

    if(json.has(COLOR)) {
      final TextColorWrapper color = context.deserialize(json.get(COLOR), TextColorWrapper.class);
      if(color.color != null) {
        style.color(color.color);
      } else if(color.decoration != null) {
        style.decoration(color.decoration, true);
      }
    }

    for(final TextDecoration decoration : DECORATIONS) {
      final String name = TextDecoration.NAMES.name(decoration);
      if(json.has(name)) {
        style.decoration(decoration, json.get(name).getAsBoolean());
      }
    }

    if(json.has(INSERTION)) {
      style.insertion(json.get(INSERTION).getAsString());
    }

    if(json.has(CLICK_EVENT)) {
      final JsonObject clickEvent = json.getAsJsonObject(CLICK_EVENT);
      if(clickEvent != null) {
        final /* @Nullable */ JsonPrimitive rawAction = clickEvent.getAsJsonPrimitive(CLICK_EVENT_ACTION);
        final ClickEvent./*@Nullable*/ Action action = rawAction == null ? null : context.deserialize(rawAction, ClickEvent.Action.class);
        if(action != null && action.readable()) {
          final /* @Nullable */ JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive(CLICK_EVENT_VALUE);
          final /* @Nullable */ String value = rawValue == null ? null : rawValue.getAsString();
          if(value != null) {
            style.clickEvent(ClickEvent.of(action, value));
          }
        }
      }
    }

    if(json.has(HOVER_EVENT)) {
      final JsonObject hoverEvent = json.getAsJsonObject(HOVER_EVENT);
      if(hoverEvent != null) {
        final /* @Nullable */ JsonPrimitive rawAction = hoverEvent.getAsJsonPrimitive(HOVER_EVENT_ACTION);
        final HoverEvent./*@Nullable*/ Action action = rawAction == null ? null : context.deserialize(rawAction, HoverEvent.Action.class);
        if(action != null && action.readable()) {
          final /* @Nullable */ JsonElement rawValue = hoverEvent.get(HOVER_EVENT_VALUE);
          final /* @Nullable */ Component value = rawValue == null ? null : context.deserialize(rawValue, Component.class);
          if(value != null) {
            style.hoverEvent(HoverEvent.of(action, value));
          }
        }
      }
    }

    return style.build();
  }

  @Override
  public JsonElement serialize(final Style src, final Type typeOfSrc, final JsonSerializationContext context) {
    final JsonObject json = new JsonObject();

    for(final TextDecoration decoration : DECORATIONS) {
      final TextDecoration.State state = src.decoration(decoration);
      if(state != TextDecoration.State.NOT_SET) {
        final String name = TextDecoration.NAMES.name(decoration);
        json.addProperty(name, state == TextDecoration.State.TRUE);
      }
    }

    final /* @Nullable */ TextColor color = src.color();
    if(color != null) {
      json.add(COLOR, context.serialize(color));
    }

    final /* @Nullable */ String insertion = src.insertion();
    if(insertion != null) {
      json.add(INSERTION, context.serialize(insertion));
    }

    final /* @Nullable */ ClickEvent clickEvent = src.clickEvent();
    if(clickEvent != null) {
      final JsonObject eventJson = new JsonObject();
      eventJson.add(CLICK_EVENT_ACTION, context.serialize(clickEvent.action()));
      eventJson.addProperty(CLICK_EVENT_VALUE, clickEvent.value());
      json.add(CLICK_EVENT, eventJson);
    }

    final /* @Nullable */ HoverEvent hoverEvent = src.hoverEvent();
    if(hoverEvent != null) {
      final JsonObject eventJson = new JsonObject();
      eventJson.add(HOVER_EVENT_ACTION, context.serialize(hoverEvent.action()));
      eventJson.add(HOVER_EVENT_VALUE, context.serialize(hoverEvent.value()));
      json.add(HOVER_EVENT, eventJson);
    }

    return json;
  }
}
