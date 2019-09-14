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
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.util.NameMap;

import java.io.IOException;

public class NameMapSerializer<T> extends JsonSerializer<T> {
  static final NameMapSerializer<ClickEvent.Action> CLICK = new NameMapSerializer<>("click action", ClickEvent.Action.NAMES, ClickEvent.Action.class);
  static final NameMapSerializer<HoverEvent.Action> HOVER = new NameMapSerializer<>("hover action", HoverEvent.Action.NAMES, HoverEvent.Action.class);
  static final NameMapSerializer<TextColor> COLOR = new NameMapSerializer<>("text color", TextColor.NAMES, TextColor.class);
  static final NameMapSerializer<TextDecoration> DECORATION = new NameMapSerializer<>("text decoration", TextDecoration.NAMES, TextDecoration.class);

  private final String name;
  private final NameMap<T> map;
  private final Class<T> type;

  public NameMapSerializer(final String name, final NameMap<T> map, final Class<T> type) {
    this.name = name;
    this.map = map;
    this.type = type;
  }

  @Override
  public void serialize(final T value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    gen.writeString(this.map.name(value));
  }
}
