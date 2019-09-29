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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.io.IOException;

public class StyleSerializer extends JsonSerializer<Style> {
  static final String COLOR = "color";
  static final String INSERTION = "insertion";
  static final String CLICK_EVENT = "clickEvent";
  static final String CLICK_EVENT_ACTION = "action";
  static final String CLICK_EVENT_VALUE = "value";
  static final String HOVER_EVENT = "hoverEvent";
  static final String HOVER_EVENT_ACTION = "action";
  static final String HOVER_EVENT_VALUE = "value";
  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  static StyleSerializer INSTANCE = new StyleSerializer();

  @Override
  public void serialize(final Style value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    gen.writeStartObject();

    for(final TextDecoration decoration : DECORATIONS) {
      final TextDecoration.State state = value.decoration(decoration);
      if(state != TextDecoration.State.NOT_SET) {
        final String name = TextDecoration.NAMES.name(decoration);
        gen.writeBooleanField(name, state == TextDecoration.State.TRUE);
      }
    }

    final /* @Nullable */ TextColor color = value.color();
    if(color != null) {
      gen.writeObjectField(COLOR, color);
    }

    final /* @Nullable */ String insertion = value.insertion();
    if(insertion != null) {
      gen.writeObjectField(INSERTION, insertion);
    }

    final /* @Nullable */ ClickEvent clickEvent = value.clickEvent();
    if(clickEvent != null) {
      gen.writeObjectFieldStart(CLICK_EVENT);
      gen.writeObjectField(CLICK_EVENT_ACTION, clickEvent.action());
      gen.writeObjectField(CLICK_EVENT_VALUE, clickEvent.value());
      gen.writeEndObject();
    }

    final /* @Nullable */ HoverEvent hoverEvent = value.hoverEvent();
    if(hoverEvent != null) {
      gen.writeObjectFieldStart(HOVER_EVENT);
      gen.writeObjectField(HOVER_EVENT_ACTION, hoverEvent.action());
      gen.writeObjectField(HOVER_EVENT_VALUE, hoverEvent.value());
      gen.writeEndObject();
    }

    gen.writeEndObject();
  }
}
