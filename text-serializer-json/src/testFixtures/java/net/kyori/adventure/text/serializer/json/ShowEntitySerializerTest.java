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
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ShowEntitySerializerTest extends SerializerTest {
  @Test
  void testWithStringUuid() {
    final UUID id = UUID.randomUUID();
    this.testStyle(Style.style().hoverEvent(HoverEvent.showEntity(Key.key("zombie"), id)).build(), json -> {
      json.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
        hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, "show_entity");
        hover.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(contents -> {
          contents.addProperty(JSONComponentConstants.SHOW_ENTITY_TYPE, "minecraft:zombie");
          contents.addProperty(JSONComponentConstants.SHOW_ENTITY_ID, id.toString());
        }));
      }));
    });
  }

  @Test
  void testWithIntArrayUuid() {
    final UUID id = UUID.randomUUID();
    assertEquals(
      Component.text("", Style.style().hoverEvent(HoverEvent.showEntity(Key.key("zombie"), id)).build()),
      this.deserialize(object(comp -> {
        comp.addProperty(JSONComponentConstants.TEXT, "");
        comp.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, "show_entity");
          hover.add(JSONComponentConstants.HOVER_EVENT_CONTENTS, object(contents -> {
            contents.addProperty(JSONComponentConstants.SHOW_ENTITY_TYPE, "minecraft:zombie");
            contents.add(JSONComponentConstants.SHOW_ENTITY_ID, array(idArray -> {
              idArray.add((int) (id.getMostSignificantBits() >> 32));
              idArray.add((int) (id.getMostSignificantBits() & 0xffffffffl));
              idArray.add((int) (id.getLeastSignificantBits() >> 32));
              idArray.add((int) (id.getLeastSignificantBits() & 0xffffffffl));
            }));
          }));
        }));
      })
      )
    );
  }
}
