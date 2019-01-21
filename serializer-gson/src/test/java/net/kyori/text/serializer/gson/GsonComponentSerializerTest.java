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

import net.kyori.text.KeybindComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GsonComponentSerializerTest {
  @Test
  void testDeserializeKeybind() {
    assertEquals(KeybindComponent.of("key.jump"), GsonComponentSerializer.INSTANCE.deserialize("{\"keybind\":\"key.jump\"}"));
  }

  @Test
  void testDeserializeScore() {
    assertEquals(ScoreComponent.of("abc", "def"), GsonComponentSerializer.INSTANCE.deserialize("{\"score\":{\"name\":\"abc\",\"objective\":\"def\"}}"));
  }

  @Test
  void testDeserializeSelector() {
    assertEquals(SelectorComponent.of("@p"), GsonComponentSerializer.INSTANCE.deserialize("{\"selector\":\"@p\"}"));
  }

  @Test
  void testDeserializeText() {
    assertEquals(TextComponent.of("Hello, world."), GsonComponentSerializer.INSTANCE.deserialize("{\"text\":\"Hello, world.\"}"));
    assertEquals(TextComponent.builder("c")
      .color(TextColor.GOLD)
      .append(TextComponent.of("o", TextColor.DARK_AQUA))
      .append(TextComponent.of("l", TextColor.LIGHT_PURPLE))
      .append(TextComponent.of("o", TextColor.DARK_PURPLE))
      .append(TextComponent.of("u", TextColor.BLUE))
      .append(TextComponent.of("r", TextColor.DARK_GREEN))
      .append(TextComponent.of("s", TextColor.RED))
      .build(), GsonComponentSerializer.INSTANCE.deserialize("[{\"text\":\"c\",\"color\":\"gold\"},{\"text\":\"o\",\"color\":\"dark_aqua\"},{\"text\":\"l\",\"color\":\"light_purple\"},{\"text\":\"o\",\"color\":\"dark_purple\"},{\"text\":\"u\",\"color\":\"blue\"},{\"text\":\"r\",\"color\":\"dark_green\"},{\"text\":\"s\",\"color\":\"red\"}]"));
    assertEquals(TextComponent.builder("This is a test.")
      .color(TextColor.DARK_PURPLE)
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of("A test.")))
      .append(TextComponent.of(" "))
      .append(TextComponent.of("A what?", TextColor.DARK_AQUA))
      .build(), GsonComponentSerializer.INSTANCE.deserialize("[{\"text\":\"This is a test.\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"A test.\"}}},{\"text\":\" \"},{\"text\":\"A what?\",\"color\":\"dark_aqua\"}]"));
  }

  @Test
  void testDeserializeTranslatable() {
    assertEquals(TranslatableComponent.of("multiplayer.player.left"), GsonComponentSerializer.INSTANCE.deserialize("{\"translate\":\"multiplayer.player.left\"}"));
    assertEquals(TranslatableComponent.builder("multiplayer.player.left")
      .args(TextComponent.of("kashike"))
      .build(), GsonComponentSerializer.INSTANCE.deserialize("{\"translate\":\"multiplayer.player.left\",\"with\":[{\"text\":\"kashike\"}]}"));
  }

  @Test
  void testSerializeDeserialize() {
    final TextComponent expected = TextComponent.builder()
      .content("Hello!")
      .color(TextColor.DARK_PURPLE)
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://google.com/"))
      .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.builder().content(":o").color(TextColor.DARK_AQUA).build()))
      .build();
    final String json = GsonComponentSerializer.INSTANCE.serialize(expected);
    assertEquals(expected, GsonComponentSerializer.INSTANCE.deserialize(json));
  }

  @Test
  void testSerializeTranslatable() {
    final TranslatableComponent component = TranslatableComponent.of(
      "multiplayer.player.left",
      TextComponent.builder("kashike")
        .clickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg kashike "))
        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, TextComponent.of("{\"name\":\"kashike\",\"id\":\"eb121687-8b1a-4944-bd4d-e0a818d9dfe2\"}")))
        .build()
    ).color(TextColor.YELLOW);
    assertEquals(component, GsonComponentSerializer.INSTANCE.deserialize(GsonComponentSerializer.INSTANCE.serialize(component)));
  }
}
