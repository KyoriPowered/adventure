/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;

class HoverTagTest extends AbstractTest {
  @Test
  void testSerializeHover() {
    final String expected = "<hover:show_text:'---'>Some hover</hover> that ends here";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text("Some hover").hoverEvent(HoverEvent.showText(Component.text("---"))))
      .append(Component.text(" that ends here"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeParentHover() {
    final String expected = "<hover:show_text:'<red>---<bold><blue>-'>This is a child with hover";

    final TextComponent.Builder builder = Component.text().hoverEvent(HoverEvent.showText(Component.text()
      .content("---").color(NamedTextColor.RED)
      .append(Component.text("-", NamedTextColor.BLUE, TextDecoration.BOLD))))
      .append(Component.text("This is a child with hover"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeHoverWithNested() {
    final String expected = "<hover:show_text:'<red>---<bold><blue>-'>Some hover</hover> that ends here";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text("Some hover").hoverEvent(
        HoverEvent.showText(Component.text("---").color(NamedTextColor.RED)
          .append(Component.text("-", NamedTextColor.BLUE, TextDecoration.BOLD)))))
      .append(Component.text(" that ends here"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeShowItemHover() {
    final TextComponent.Builder input = text()
      .content("test")
      .hoverEvent(HoverEvent.showItem(Key.key("minecraft", "stone"), 5));
    final String expected = "<hover:show_item:stone:5>test";
    this.assertSerializedEquals(expected, input);
  }

  @Test
  void testSerializeShowEntityHover() {
    final UUID uuid = UUID.randomUUID();
    final String nameString = "<gold>Custom Name!";
    final Component name = PARSER.deserialize(nameString);
    final TextComponent.Builder input = text()
      .content("test")
      .hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name));
    final String expected = String.format("<hover:show_entity:zombie:%s:'%s'>test", uuid, nameString);
    this.assertSerializedEquals(expected, input);
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/172
  @Test
  void testSerializeHoverWithExtraFollowing() {
    final Component component = Component.text("START", NamedTextColor.AQUA)
      .append(Component.text("HOVERED", style(NamedTextColor.BLUE, HoverEvent.showText(Component.text("Text on hover")))))
      .append(Component.text("END"));
    final String expected = "<aqua>START<blue><hover:show_text:'Text on hover'>HOVERED</hover></blue>END";

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testHover() {
    final String input = "<hover:show_text:\"<red>test\">TEST";
    final Component expected = text("TEST").hoverEvent(text("test").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHover2() {
    final String input = "<hover:show_text:'<red>test'>TEST";
    final Component expected = text("TEST").hoverEvent(text("test").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHoverWithColon() {
    final String input = "<hover:show_text:\"<red>test:TEST\">TEST";
    final Component expected = text("TEST").hoverEvent(text("test:TEST").color(RED));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testHoverMultiline() {
    final String input = "<hover:show_text:'<red>test\ntest2'>TEST";
    final Component expected = text("TEST").hoverEvent(text("test\ntest2").color(RED));

    this.assertParsedEquals(expected, input);
  }

  // GH-101
  @Test
  void testHoverWithInsertingComponent() {
    final String input = "<red><hover:show_text:\"Test\"><lang:item.minecraft.stick>";
    final Component expected = translatable("item.minecraft.stick").hoverEvent(showText(text("Test"))).color(RED);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testShowItemHover() {
    final Component expected = text("test").hoverEvent(HoverEvent.showItem(Key.key("minecraft", "stone"), 5));
    final String input = "<hover:show_item:'minecraft:stone':5>test";
    final String input1 = "<hover:show_item:'minecraft:stone':'5'>test";
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected, input1);
  }

  @Test
  void testShowEntityHover() {
    final UUID uuid = UUID.randomUUID();
    final String nameString = "<gold>Custom Name!";
    final Component name = PARSER.deserialize(nameString);
    final Component expected = text("test").hoverEvent(HoverEvent.showEntity(Key.key("minecraft", "zombie"), uuid, name));
    final String input = String.format("<hover:show_entity:'minecraft:zombie':%s:'%s'>test", uuid, nameString);
    final String input1 = String.format("<hover:show_entity:zombie:'%s':'%s'>test", uuid, nameString);
    this.assertParsedEquals(expected, input);
    this.assertParsedEquals(expected, input1);
  }

  // https://github.com/KyoriPowered/adventure-text-minimessage/issues/140
  @Test
  void testStringPlaceholderInHover() {
    final String input = "<hover:show_text:'Word: <word>'><gold>Hover to see the word!";

    final Component expected = text("Hover to see the word!", GOLD)
      .hoverEvent(text("Word: Adventure"));

    this.assertParsedEquals(expected, input, component("word", text("Adventure")));
  }

  // GH-37
  @Test
  void testPhil() {
    final String input = "<red><hover:show_text:'Message 1\nMessage 2'>My Message";
    final Component expected = text("My Message").hoverEvent(showText(text("Message 1\nMessage 2"))).color(RED);

    this.assertParsedEquals(expected, input);
  }
}
