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

import com.fasterxml.jackson.databind.JsonNode;
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
    json.put("name", "kashike");
    json.put("id", "eb121687-8b1a-4944-bd4d-e0a818d9dfe2");
  }).toString();

  @Override
  Stream<Map.Entry<TranslatableComponent, JsonNode>> tests() {
    return Stream.of(
      entry(
        TranslatableComponent.of(KEY),
        json -> json.put(ComponentSerializer.TRANSLATE, KEY)
      ),
      entry(
        TranslatableComponent.of(
          KEY,
          TextComponent.builder(WHO)
            .clickEvent(ClickEvent.suggestCommand(COMMAND))
            .hoverEvent(HoverEvent.showEntity(TextComponent.of(ENTITY)))
            .build()
        ).color(TextColor.YELLOW),
        json -> {
          json.put(ComponentSerializer.TRANSLATE, KEY);
          json.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.YELLOW));
          json.set(ComponentSerializer.TRANSLATE_WITH, array(with -> with.add(object(item -> {
            item.put(ComponentSerializer.TEXT, WHO);
            item.set(StyleSerializer.CLICK_EVENT, object(event -> {
              event.put(StyleSerializer.CLICK_EVENT_ACTION, ClickEvent.Action.NAMES.name(ClickEvent.Action.SUGGEST_COMMAND));
              event.put(StyleSerializer.CLICK_EVENT_VALUE, COMMAND);
            }));
            item.set(StyleSerializer.HOVER_EVENT, object(event -> {
              event.put(StyleSerializer.HOVER_EVENT_ACTION, HoverEvent.Action.NAMES.name(HoverEvent.Action.SHOW_ENTITY));
              event.set(StyleSerializer.HOVER_EVENT_VALUE, object(value -> value.put(ComponentSerializer.TEXT, ENTITY)));
            }));
          }))));
        }
      )
    );
  }
}
