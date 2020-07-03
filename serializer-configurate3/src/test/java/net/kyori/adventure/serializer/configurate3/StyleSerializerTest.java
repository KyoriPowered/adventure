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
package net.kyori.adventure.serializer.configurate3;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StyleSerializerTest implements ConfigurateTestBase {
  @Test
  void testSerializeFont() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.FONT).setValue("adventure:meow");
    });
    final Style style = Style.style()
      .font(Key.key("adventure", "meow"))
      .build();

    this.assertRoundtrippable(StyleSerializer.TYPE, style, node);
  }

  @Test
  void testSerializeHexColor() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.COLOR).setValue("#123456");
    });
    final Style style = Style.style()
      .color(TextColor.color(0x123456))
      .build();

    this.assertRoundtrippable(StyleSerializer.TYPE, style, node);
  }

  @Test
  void testSerializeNumericColor() throws ObjectMappingException {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.COLOR).setValue(0x123456);
    });
    final Style style = Style.style()
      .color(TextColor.color(0x123456))
      .build();

    // we won't roundtrip to this format
    assertEquals(style, node.getValue(StyleSerializer.TYPE));
  }

  @Test
  void testSerializeNamedColor() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.COLOR).setValue("dark_red");
    });
    final Style style = Style.style()
      .color(NamedTextColor.DARK_RED)
      .build();

    this.assertRoundtrippable(StyleSerializer.TYPE, style, node);
  }

  @Test
  void testSerializeInsertion() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.INSERTION).setValue("i'd like to get a cat!");
    });
    final Style style = Style.style()
      .insertion("i'd like to get a cat!")
      .build();

    this.assertRoundtrippable(StyleSerializer.TYPE, style, node);
  }

  @Test
  void testSerializeClickEvent() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(StyleSerializer.CLICK_EVENT).act(event -> {
        event.getNode(StyleSerializer.CLICK_EVENT_ACTION).setValue("open_url");
        event.getNode(StyleSerializer.CLICK_EVENT_VALUE).setValue("https://kyori.net");
      });
    });
    final Style style = Style.style()
      .clickEvent(ClickEvent.openUrl("https://kyori.net"))
      .build();

    this.assertRoundtrippable(StyleSerializer.TYPE, style, node);
  }

  // Hover event tested in HoverEventSerializersTest
}
