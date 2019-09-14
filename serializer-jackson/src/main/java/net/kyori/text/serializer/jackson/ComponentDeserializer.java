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
import net.kyori.text.BlockNbtComponent;
import net.kyori.text.BuildableComponent;
import net.kyori.text.Component;
import net.kyori.text.ComponentBuilder;
import net.kyori.text.EntityNbtComponent;
import net.kyori.text.KeybindComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.format.Style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.kyori.text.serializer.jackson.ComponentSerializer.EXTRA;
import static net.kyori.text.serializer.jackson.ComponentSerializer.KEYBIND;
import static net.kyori.text.serializer.jackson.ComponentSerializer.NBT;
import static net.kyori.text.serializer.jackson.ComponentSerializer.NBT_BLOCK;
import static net.kyori.text.serializer.jackson.ComponentSerializer.NBT_ENTITY;
import static net.kyori.text.serializer.jackson.ComponentSerializer.NBT_INTERPRET;
import static net.kyori.text.serializer.jackson.ComponentSerializer.SCORE;
import static net.kyori.text.serializer.jackson.ComponentSerializer.SCORE_NAME;
import static net.kyori.text.serializer.jackson.ComponentSerializer.SCORE_OBJECTIVE;
import static net.kyori.text.serializer.jackson.ComponentSerializer.SCORE_VALUE;
import static net.kyori.text.serializer.jackson.ComponentSerializer.SELECTOR;
import static net.kyori.text.serializer.jackson.ComponentSerializer.TEXT;
import static net.kyori.text.serializer.jackson.ComponentSerializer.TRANSLATE;
import static net.kyori.text.serializer.jackson.ComponentSerializer.TRANSLATE_WITH;

public class ComponentDeserializer extends JsonDeserializer<Component> {
  static final ComponentDeserializer INSTANCE = new ComponentDeserializer();

  @Override
  public Component deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    return this.deserialize0(p, ctxt);
  }

  private BuildableComponent<?, ?> deserialize0(final JsonParser p, final DeserializationContext ctxt) throws IOException {
    final JsonNode json = p.readValueAsTree();
    if(json.isTextual()) {
      return TextComponent.of(json.asText());
    } else if(json.isArray()) {
      ComponentBuilder<?, ?> parent = null;
      for(final JsonNode childElement : json) {
        final BuildableComponent<?, ?> child = this.deserialize0(childElement.traverse(p.getCodec()), ctxt);
        if(parent == null) {
          parent = child.toBuilder();
        } else {
          parent.append(child);
        }
      }
      if(parent == null) {
        return ctxt.reportInputMismatch(Component.class, "Don't know how to turn " + json + " into a Component");
      }
      return parent.build();
    } else if(!json.isObject()) {
      return ctxt.reportInputMismatch(Component.class, "Don't know how to turn " + json + " into a Component");
    }

    final ComponentBuilder<?, ?> component;
    if(json.has(TEXT)) {
      component = TextComponent.builder(json.get(TEXT).asText());
    } else if(json.has(TRANSLATE)) {
      final String key = json.get(TRANSLATE).asText();
      if(!json.has(TRANSLATE_WITH)) {
        component = TranslatableComponent.builder(key);
      } else {
        final JsonNode with = json.get(TRANSLATE_WITH);
        final List<Component> args = new ArrayList<>(with.size());
        for(int i = 0, size = with.size(); i < size; i++) {
          final JsonNode argElement = with.get(i);
          args.add(argElement.traverse(p.getCodec()).readValueAs(Component.class));
        }
        component = TranslatableComponent.builder(key).args(args);
      }
    } else if(json.has(SCORE)) {
      final JsonNode score = json.get(SCORE);
      if(!score.has(SCORE_NAME) || !score.has(SCORE_OBJECTIVE)) {
        return ctxt.reportInputMismatch(Component.class, "A score component requires a " + SCORE_NAME + " and " + SCORE_OBJECTIVE);
      }
      // score components can have a value sometimes, let's grab it
      if(score.has(SCORE_VALUE)) {
        component = ScoreComponent.builder()
          .name(score.get(SCORE_NAME).asText())
          .objective(score.get(SCORE_OBJECTIVE).asText())
          .value(score.get(SCORE_VALUE).asText());
      } else {
        component = ScoreComponent.builder()
          .name(score.get(SCORE_NAME).asText())
          .objective(score.get(SCORE_OBJECTIVE).asText());
      }
    } else if(json.has(SELECTOR)) {
      component = SelectorComponent.builder().pattern(json.get(SELECTOR).asText());
    } else if(json.has(KEYBIND)) {
      component = KeybindComponent.builder().keybind(json.get(KEYBIND).asText());
    } else if(json.has(NBT)) {
      final String nbt = json.get(NBT).asText();
      final boolean interpret = json.has(NBT_INTERPRET) && json.get(NBT_INTERPRET).asBoolean();
      if(json.has(NBT_BLOCK)) {
        final BlockNbtComponent.Pos position = json.get(NBT_BLOCK)
          .traverse(p.getCodec())
          .readValueAs(BlockNbtComponent.Pos.class);
        component = BlockNbtComponent.builder()
          .nbtPath(nbt)
          .interpret(interpret)
          .pos(position);
      } else if(json.has(NBT_ENTITY)) {
        component = EntityNbtComponent.builder()
          .nbtPath(nbt)
          .interpret(interpret)
          .selector(json.get(NBT_ENTITY).asText());
      } else {
        return ctxt.reportInputMismatch(Component.class, "Don't know how to turn " + json + " into a Component");
      }
    } else {
      return ctxt.reportInputMismatch(Component.class, "Don't know how to turn " + json + " into a Component");
    }

    if(json.has(EXTRA)) {
      final JsonNode extra = json.get(EXTRA);
      for(int i = 0, size = extra.size(); i < size; i++) {
        final JsonNode extraElement = extra.get(i);
        component.append(extraElement.traverse(p.getCodec()).readValueAs(Component.class));
      }
    }

    final Style style = json.traverse(p.getCodec()).readValueAs(Style.class);
    if(!style.isEmpty()) {
      component.style(style);
    }

    return component.build();
  }
}
