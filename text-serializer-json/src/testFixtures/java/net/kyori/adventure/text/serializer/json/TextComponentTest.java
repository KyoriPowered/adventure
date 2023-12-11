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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class TextComponentTest extends SerializerTest {
  @Test
  void testSimple() {
    final Component input = Component.text("Hello, world.");
    final JsonElement output = new JsonPrimitive("Hello, world.");

    assertEquals(output, this.serialize(input));
    assertEquals(input, this.deserialize(output));
  }

  @Test
  void testComplex1() {
    this.testObject(
      Component.text().content("c")
        .color(NamedTextColor.GOLD)
        .append(Component.text("o", NamedTextColor.DARK_AQUA))
        .append(Component.text("l", NamedTextColor.LIGHT_PURPLE))
        .append(Component.text("o", NamedTextColor.DARK_PURPLE))
        .append(Component.text("u", NamedTextColor.BLUE))
        .append(Component.text("r", NamedTextColor.DARK_GREEN))
        .append(Component.text("s", NamedTextColor.RED))
        .build(),
      json -> {
        json.addProperty(JSONComponentConstants.TEXT, "c");
        json.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.GOLD));
        json.add(JSONComponentConstants.EXTRA, array(extra -> {
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "o");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.DARK_AQUA));
          }));
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "l");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.LIGHT_PURPLE));
          }));
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "o");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.DARK_PURPLE));
          }));
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "u");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.BLUE));
          }));
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "r");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.DARK_GREEN));
          }));
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "s");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.RED));
          }));
        }));
      }
    );
  }

  @Test
  void testComplex2() {
    this.testObject(
      Component.text().content("This is a test.")
        .color(NamedTextColor.DARK_PURPLE)
        .hoverEvent(HoverEvent.showText(Component.text("A test.")))
        .append(Component.text(" "))
        .append(Component.text("A what?", NamedTextColor.DARK_AQUA))
        .build(),
      json -> {
        json.addProperty(JSONComponentConstants.TEXT, "This is a test.");
        json.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.DARK_PURPLE));
        json.add(JSONComponentConstants.HOVER_EVENT, object(event -> {
          event.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, name(HoverEvent.Action.SHOW_TEXT));
          event.addProperty(JSONComponentConstants.HOVER_EVENT_CONTENTS, "A test.");
        }));
        json.add(JSONComponentConstants.EXTRA, array(extra -> {
          extra.add(" ");
          extra.add(object(item -> {
            item.addProperty(JSONComponentConstants.TEXT, "A what?");
            item.addProperty(JSONComponentConstants.COLOR, name(NamedTextColor.DARK_AQUA));
          }));
        }));
      }
    );
  }
}
