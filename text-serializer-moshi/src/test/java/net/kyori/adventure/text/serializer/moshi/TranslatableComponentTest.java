/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.moshi;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.moshi.StyleTest.name;

class TranslatableComponentTest extends ComponentTest {
  private static final String KEY = "multiplayer.player.left";

  @Test
  void testNoArgs() {
    this.test(Component.translatable(KEY), object(json -> json.put(ComponentAdapter.TRANSLATE, KEY)));
  }

  @Test
  void testSingleArgWithEvents() {
    final UUID id = UUID.fromString("eb121687-8b1a-4944-bd4d-e0a818d9dfe2");
    final String name = "kashike";
    final String command = String.format("/msg %s ", name);

    this.test(
      Component.translatable(
        KEY,
        Component.text().content(name)
          .clickEvent(ClickEvent.suggestCommand(command))
          .hoverEvent(HoverEvent.showEntity(HoverEvent.ShowEntity.of(
            Key.key("minecraft", "player"),
            id,
            Component.text(name)
          )))
          .build()
      ).color(NamedTextColor.YELLOW),
      object(json -> {
        json.put(ComponentAdapter.TRANSLATE, KEY);
        json.put(StyleAdapter.COLOR, name(NamedTextColor.YELLOW));
        json.put(ComponentAdapter.TRANSLATE_WITH, array(with -> with.add(object(item -> {
          item.put(ComponentAdapter.TEXT, name);
          item.put(StyleAdapter.CLICK_EVENT, object(event -> {
            event.put(StyleAdapter.CLICK_EVENT_ACTION, name(ClickEvent.Action.SUGGEST_COMMAND));
            event.put(StyleAdapter.CLICK_EVENT_VALUE, command);
          }));
          item.put(StyleAdapter.HOVER_EVENT, object(event -> {
            event.put(StyleAdapter.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_ENTITY));
            event.put(StyleAdapter.HOVER_EVENT_CONTENTS, object(value -> {
              value.put(ShowEntityAdapter.TYPE, "minecraft:player");
              value.put(ShowEntityAdapter.ID, id.toString());
              value.put(ShowEntityAdapter.NAME, object(namej -> {
                namej.put(ComponentAdapter.TEXT, name);
              }));
            }));
          }));
        }))));
      })
    );
  }
}
