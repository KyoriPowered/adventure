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
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;

import java.util.Map;
import java.util.stream.Stream;

class TextComponentTest extends AbstractComponentTest<TextComponent> {
  @Override
  Stream<Map.Entry<TextComponent, JsonNode>> tests() {
    return Stream.of(
      entry(TextComponent.of("Hello, world."), json -> json.put(ComponentSerializer.TEXT, "Hello, world.")),
      entry(
        TextComponent.builder("c")
          .color(TextColor.GOLD)
          .append(TextComponent.of("o", TextColor.DARK_AQUA))
          .append(TextComponent.of("l", TextColor.LIGHT_PURPLE))
          .append(TextComponent.of("o", TextColor.DARK_PURPLE))
          .append(TextComponent.of("u", TextColor.BLUE))
          .append(TextComponent.of("r", TextColor.DARK_GREEN))
          .append(TextComponent.of("s", TextColor.RED))
          .build(),
        json -> {
          json.put(ComponentSerializer.TEXT, "c");
          json.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.GOLD));
          json.set(ComponentSerializer.EXTRA, array(extra -> {
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "o");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.DARK_AQUA));
            }));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "l");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.LIGHT_PURPLE));
            }));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "o");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.DARK_PURPLE));
            }));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "u");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.BLUE));
            }));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "r");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.DARK_GREEN));
            }));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "s");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.RED));
            }));
          }));
        }
      ),
      entry(
        TextComponent.builder("This is a test.")
          .color(TextColor.DARK_PURPLE)
          .hoverEvent(HoverEvent.showText(TextComponent.of("A test.")))
          .append(TextComponent.of(" "))
          .append(TextComponent.of("A what?", TextColor.DARK_AQUA))
          .build(),
        json -> {
          json.put(ComponentSerializer.TEXT, "This is a test.");
          json.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.DARK_PURPLE));
          json.set(StyleSerializer.HOVER_EVENT, object(event -> {
            event.put(StyleSerializer.HOVER_EVENT_ACTION, HoverEvent.Action.NAMES.name(HoverEvent.Action.SHOW_TEXT));
            event.set(StyleSerializer.HOVER_EVENT_VALUE, object(value -> value.put(ComponentSerializer.TEXT, "A test.")));
          }));
          json.set(ComponentSerializer.EXTRA, array(extra -> {
            extra.add(object(item -> item.put(ComponentSerializer.TEXT, " ")));
            extra.add(object(item -> {
              item.put(ComponentSerializer.TEXT, "A what?");
              item.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.DARK_AQUA));
            }));
          }));
        }
      )
    );
  }
}
