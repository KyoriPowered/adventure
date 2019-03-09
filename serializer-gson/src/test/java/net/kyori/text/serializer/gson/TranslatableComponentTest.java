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

import com.google.gson.JsonElement;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

import java.util.Map;
import java.util.stream.Stream;

class TranslatableComponentTest extends AbstractComponentTest<TranslatableComponent> {
  private static final String KEY = "multiplayer.player.left";
  private static final String WHO = "kashike";
  private static final String COMMAND = "/msg kashike ";
  private static final String ENTITY = object(json -> {
    json.addProperty("name", "kashike");
    json.addProperty("id", "eb121687-8b1a-4944-bd4d-e0a818d9dfe2");
  }).toString();

  @Override
  Stream<Map.Entry<TranslatableComponent, JsonElement>> tests() {
    return Stream.of(
      entry(
        TranslatableComponent.of(KEY),
        json -> json.addProperty(GsonComponentSerializer.TRANSLATE, KEY)
      ),
      entry(
        TranslatableComponent.of(
          KEY,
          TextComponent.builder(WHO)
            .clickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, COMMAND))
            .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, TextComponent.of(ENTITY)))
            .build()
        ).color(TextColor.YELLOW),
        json -> {
          json.addProperty(GsonComponentSerializer.TRANSLATE, KEY);
          json.addProperty(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.YELLOW));
          json.add(GsonComponentSerializer.TRANSLATE_WITH, array(with -> with.add(object(item -> {
            item.addProperty(GsonComponentSerializer.TEXT, WHO);
            item.add(StyleSerializer.CLICK_EVENT, object(event -> {
              event.addProperty(StyleSerializer.CLICK_EVENT_ACTION, ClickEvent.Action.NAMES.name(ClickEvent.Action.SUGGEST_COMMAND));
              event.addProperty(StyleSerializer.CLICK_EVENT_VALUE, COMMAND);
            }));
            item.add(StyleSerializer.HOVER_EVENT, object(event -> {
              event.addProperty(StyleSerializer.HOVER_EVENT_ACTION, HoverEvent.Action.NAMES.name(HoverEvent.Action.SHOW_ENTITY));
              event.add(StyleSerializer.HOVER_EVENT_VALUE, object(value -> value.addProperty(GsonComponentSerializer.TEXT, ENTITY)));
            }));
          }))));
        }
      )
    );
  }
}
