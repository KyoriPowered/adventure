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
package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.kyori.text.Component;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextDecoration;

import java.io.IOException;

import static net.kyori.text.serializer.jackson.StyleSerializer.CLICK_EVENT;
import static net.kyori.text.serializer.jackson.StyleSerializer.CLICK_EVENT_ACTION;
import static net.kyori.text.serializer.jackson.StyleSerializer.CLICK_EVENT_VALUE;
import static net.kyori.text.serializer.jackson.StyleSerializer.COLOR;
import static net.kyori.text.serializer.jackson.StyleSerializer.HOVER_EVENT;
import static net.kyori.text.serializer.jackson.StyleSerializer.HOVER_EVENT_ACTION;
import static net.kyori.text.serializer.jackson.StyleSerializer.HOVER_EVENT_VALUE;
import static net.kyori.text.serializer.jackson.StyleSerializer.INSERTION;

public class StyleDeserializer extends JsonDeserializer<Style> {
  static final StyleDeserializer INSTANCE = new StyleDeserializer();

  private static final TextDecoration[] DECORATIONS = TextDecoration.values();

  @Override
  public Style deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    final Style.Builder style = Style.builder();
    final JsonNode json = p.readValueAsTree();

    if(json.has(COLOR)) {
      final TextColorWrapper color = json.get(COLOR).traverse(p.getCodec()).readValueAs(TextColorWrapper.class);
      if(color.color != null) {
        style.color(color.color);
      } else if(color.decoration != null) {
        style.decoration(color.decoration, true);
      }
    }

    for(final TextDecoration decoration : DECORATIONS) {
      final String name = TextDecoration.NAMES.name(decoration);
      if(json.has(name)) {
        style.decoration(decoration, json.get(name).asBoolean());
      }
    }

    if(json.has(INSERTION)) {
      style.insertion(json.get(INSERTION).asText());
    }

    if(json.has(CLICK_EVENT)) {
      final JsonNode clickEvent = json.get(CLICK_EVENT);
      if(clickEvent != null) {
        final /* @Nullable */ JsonNode rawAction = clickEvent.get(CLICK_EVENT_ACTION);
        final ClickEvent./*@Nullable*/ Action action = rawAction == null ? null : rawAction.traverse(p.getCodec()).readValueAs(ClickEvent.Action.class);
        if(action != null && action.readable()) {
          final /* @Nullable */ JsonNode rawValue = clickEvent.get(CLICK_EVENT_VALUE);
          final /* @Nullable */ String value = rawValue == null ? null : rawValue.asText();
          if(value != null) {
            style.clickEvent(ClickEvent.of(action, value));
          }
        }
      }
    }

    if(json.has(HOVER_EVENT)) {
      final JsonNode hoverEvent = json.get(HOVER_EVENT);
      if(hoverEvent != null) {
        final /* @Nullable */ JsonNode rawAction = hoverEvent.get(HOVER_EVENT_ACTION);
        final HoverEvent./*@Nullable*/ Action action = rawAction == null ? null : rawAction.traverse(p.getCodec()).readValueAs(HoverEvent.Action.class);
        if(action != null && action.readable()) {
          final /* @Nullable */ JsonNode rawValue = hoverEvent.get(HOVER_EVENT_VALUE);
          final /* @Nullable */ Component value = rawValue == null ? null : rawValue.traverse(p.getCodec()).readValueAs(Component.class);
          if(value != null) {
            style.hoverEvent(HoverEvent.of(action, value));
          }
        }
      }
    }

    return style.build();
  }
}
