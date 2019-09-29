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
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.util.NameMap;

import java.io.IOException;
import java.util.Optional;

public class NameMapDeserializer<T> extends JsonDeserializer<T> {
  static final NameMapDeserializer<ClickEvent.Action> CLICK = new NameMapDeserializer<>("click action", ClickEvent.Action.NAMES, ClickEvent.Action.class);
  static final NameMapDeserializer<HoverEvent.Action> HOVER = new NameMapDeserializer<>("hover action", HoverEvent.Action.NAMES, HoverEvent.Action.class);
  static final NameMapDeserializer<TextColor> COLOR = new NameMapDeserializer<>("text color", TextColor.NAMES, TextColor.class);
  static final NameMapDeserializer<TextDecoration> DECORATION = new NameMapDeserializer<>("text decoration", TextDecoration.NAMES, TextDecoration.class);

  private final String name;
  private final NameMap<T> map;
  private final Class<T> type;

  public NameMapDeserializer(final String name, final NameMap<T> map, final Class<T> type) {
    this.name = name;
    this.map = map;
    this.type = type;
  }

  @Override
  public T deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    final String name = p.getValueAsString();
    final Optional<T> ret = this.map.get(name);
    if(ret.isPresent()) {
      return ret.get();
    }

    return ctxt.reportInputMismatch(this.type, "invalid " + this.name + ":  " + name);
  }
}
