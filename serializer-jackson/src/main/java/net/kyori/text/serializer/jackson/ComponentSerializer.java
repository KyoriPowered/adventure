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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.kyori.text.BlockNbtComponent;
import net.kyori.text.Component;
import net.kyori.text.EntityNbtComponent;
import net.kyori.text.KeybindComponent;
import net.kyori.text.NbtComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ComponentSerializer extends JsonSerializer<Component> {
  static final ComponentSerializer INSTANCE = new ComponentSerializer();

  static final String TEXT = "text";
  static final String TRANSLATE = "translate";
  static final String TRANSLATE_WITH = "with";
  static final String SCORE = "score";
  static final String SCORE_NAME = "name";
  static final String SCORE_OBJECTIVE = "objective";
  static final String SCORE_VALUE = "value";
  static final String SELECTOR = "selector";
  static final String KEYBIND = "keybind";
  static final String EXTRA = "extra";
  static final String NBT = "nbt";
  static final String NBT_INTERPRET = "interpret";
  static final String NBT_BLOCK = "block";
  static final String NBT_ENTITY = "entity";

  @Override
  public void serialize(final Component value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    gen.writeStartObject();

    if(value instanceof TextComponent) {
      gen.writeStringField(TEXT, ((TextComponent) value).content());
    } else if(value instanceof TranslatableComponent) {
      final TranslatableComponent tc = (TranslatableComponent) value;
      gen.writeStringField(TRANSLATE, tc.key());
      if(!tc.args().isEmpty()) {
        gen.writeArrayFieldStart(TRANSLATE_WITH);
        for(final Component arg : tc.args()) {
          gen.writeObject(arg);
        }
        gen.writeEndArray();
      }
    } else if(value instanceof ScoreComponent) {
      final ScoreComponent sc = (ScoreComponent) value;
      gen.writeObjectFieldStart(SCORE);
      gen.writeStringField(SCORE_NAME, sc.name());
      gen.writeStringField(SCORE_OBJECTIVE, sc.objective());
      // score component value is optional
      if(sc.value() != null) gen.writeStringField(SCORE_VALUE, sc.value());
      gen.writeEndObject();
    } else if(value instanceof SelectorComponent) {
      gen.writeStringField(SELECTOR, ((SelectorComponent) value).pattern());
    } else if(value instanceof KeybindComponent) {
      gen.writeStringField(KEYBIND, ((KeybindComponent) value).keybind());
    } else if(value instanceof NbtComponent) {
      final NbtComponent<?, ?> nc = (NbtComponent<?, ?>) value;
      gen.writeStringField(NBT, nc.nbtPath());
      gen.writeBooleanField(NBT_INTERPRET, nc.interpret());
      if(value instanceof BlockNbtComponent) {
        gen.writeObjectField(NBT_BLOCK, ((BlockNbtComponent) nc).pos());
      } else if(value instanceof EntityNbtComponent) {
        gen.writeObjectField(NBT_ENTITY, ((EntityNbtComponent) nc).selector());
      } else {
        serializers.reportMappingProblem("Don't know how to serialize " + value + " as a Component");
      }
    } else {
      serializers.reportMappingProblem("Don't know how to serialize " + value + " as a Component");
    }

    if(!value.children().isEmpty()) {
      gen.writeArrayFieldStart(EXTRA);
      for(final Component child : value.children()) {
        gen.writeObject(child);
      }
      gen.writeEndArray();
    }

    if(value.hasStyling()) {
      // TODO: remove static accessor
      final JsonNode jsonNode = JacksonComponentSerializer.MAPPER.valueToTree(value.style());
      if(jsonNode.isObject()) {
        final Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while(fields.hasNext()) {
          final Map.Entry<String, JsonNode> next = fields.next();
          gen.writeFieldName(next.getKey());
          gen.writeTree(next.getValue());
        }
      }
    }

    gen.writeEndObject();
  }
}
