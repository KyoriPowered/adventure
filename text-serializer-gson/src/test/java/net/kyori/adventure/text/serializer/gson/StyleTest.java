/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StyleTest extends AbstractSerializeDeserializeTest<Style> {
  @Test
  void testWithDecorationAsColor() {
    final Style s0 = GsonComponentSerializer.GSON.fromJson(AbstractComponentTest.object(object -> {
      object.addProperty(StyleSerializer.COLOR, TextDecoration.NAMES.name(TextDecoration.BOLD));
    }), Style.class);
    assertNull(s0.color());
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testWithResetAsColor() {
    final Style s0 = GsonComponentSerializer.GSON.fromJson(AbstractComponentTest.object(object -> {
      object.addProperty(StyleSerializer.COLOR, "reset");
    }), Style.class);
    assertNull(s0.color());
    assertThat(s0.decorations()).isEmpty();
  }

  @Override
  Stream<Map.Entry<Style, JsonElement>> tests() {
    return Stream.of(
      entry(Style.empty(), json -> {}),
      entry(Style.of(TextColor.LIGHT_PURPLE), json -> json.addProperty(StyleSerializer.COLOR, name(TextColor.LIGHT_PURPLE))),
      entry(Style.of(TextDecoration.BOLD), json -> json.addProperty(name(TextDecoration.BOLD), true)),
      entry(Style.builder().insertion("honk").build(), json -> json.addProperty(StyleSerializer.INSERTION, "honk")),
      entry(
        Style.builder()
          .color(TextColor.RED)
          .decoration(TextDecoration.BOLD, true)
          .clickEvent(ClickEvent.openUrl("https://github.com"))
          .build(),
        json -> {
          json.addProperty(StyleSerializer.COLOR, name(TextColor.RED));
          json.addProperty(name(TextDecoration.BOLD), true);
          json.add(StyleSerializer.CLICK_EVENT, object(clickEvent -> {
            clickEvent.addProperty(StyleSerializer.CLICK_EVENT_ACTION, name(ClickEvent.Action.OPEN_URL));
            clickEvent.addProperty(StyleSerializer.CLICK_EVENT_VALUE, "https://github.com");
          }));
        }
      )
    );
  }

  static String name(final TextColor color) {
    return TextColor.NAMES.name(color);
  }

  static String name(final TextDecoration decoration) {
    return TextDecoration.NAMES.name(decoration);
  }

  static String name(final ClickEvent.Action action) {
    return ClickEvent.Action.NAMES.name(action);
  }

  static String name(final HoverEvent.Action action) {
    return HoverEvent.Action.NAMES.name(action);
  }

  @Override
  Style deserialize(final JsonElement json) {
    return GsonComponentSerializer.GSON.fromJson(json, Style.class);
  }

  @Override
  JsonElement serialize(final Style object) {
    return GsonComponentSerializer.GSON.toJsonTree(object);
  }
}
