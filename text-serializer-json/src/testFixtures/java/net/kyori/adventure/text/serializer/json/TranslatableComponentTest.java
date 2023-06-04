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
package net.kyori.adventure.text.serializer.json;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

final class TranslatableComponentTest extends SerializerTest {
  private static final String KEY = "multiplayer.player.left";

  @Test
  void testNoArgs() {
    this.testObject(
      Component.translatable(KEY),
      json -> json.addProperty(JSONComponentConstants.TRANSLATE, KEY)
    );
  }

  @Test
  void testFallback() {
    this.testObject(
      Component.translatable()
        .key("thisIsA")
        .fallback("This is a test.")
        .build(),
      json -> {
        json.addProperty(JSONComponentConstants.TRANSLATE, "thisIsA");
        json.addProperty(JSONComponentConstants.TRANSLATE_FALLBACK, "This is a test.");
      }
    );
  }

  @Test
  void testSingleArgWithEvents() {
    final UUID id = UUID.fromString("eb121687-8b1a-4944-bd4d-e0a818d9dfe2");
    final String name = "kashike";
    final String command = String.format("/msg %s ", name);

    this.testObject(
      Component.translatable(
        KEY,
        Component.text().content(name)
          .clickEvent(ClickEvent.suggestCommand(command))
          .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.showEntity(
            Key.key("minecraft", "player"),
            id,
            Component.text(name)
          )))
          .build()
      ).color(NamedTextColor.YELLOW),
      json -> {
        json.addProperty(JSONComponentConstants.TRANSLATE, KEY);
        json.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.YELLOW));
        json.add(JSONComponentConstants.TRANSLATE_WITH, array(with -> with.add(object(item -> {
          item.addProperty(JSONComponentConstants.TEXT, name);
          item.add(JSONComponentConstants.CLICK_EVENT, object(event -> {
            event.addProperty(JSONComponentConstants.CLICK_EVENT_ACTION, name(ClickEvent.Action.SUGGEST_COMMAND));
            event.addProperty(JSONComponentConstants.CLICK_EVENT_VALUE, command);
          }));
          item.add(JSONComponentConstants.HOVER_EVENT, object(event -> {
            event.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY));
            event.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(value -> {
              value.addProperty(JSONComponentConstants.SHOW_ENTITY_TYPE, "minecraft:player");
              value.addProperty(JSONComponentConstants.SHOW_ENTITY_ID, id.toString());
              value.add(JSONComponentConstants.SHOW_ENTITY_NAME, object(namej -> {
                namej.addProperty(JSONComponentConstants.TEXT, name);
              }));
            }));
          }));
        }))));
      }
    );
  }
}
