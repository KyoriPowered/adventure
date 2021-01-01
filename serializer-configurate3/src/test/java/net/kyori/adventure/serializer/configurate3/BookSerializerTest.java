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
package net.kyori.adventure.serializer.configurate3;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BookSerializerTest implements ConfigurateTestBase {
  @Test
  void testBook() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(BookTypeSerializer.TITLE, ComponentTypeSerializer.TEXT).setValue("My book");
      n.getNode(BookTypeSerializer.AUTHOR).act(author -> {
        author.getNode(StyleSerializer.FONT).setValue("minecraft:uniform");
        author.getNode(ComponentTypeSerializer.TEXT).setValue("myself");
      });
      n.getNode(BookTypeSerializer.PAGES).act(pages -> {
        pages.appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("Page 1");
        pages.appendListNode().act(page -> {
          page.getNode(StyleSerializer.COLOR).setValue("dark_red");
          page.getNode(ComponentTypeSerializer.TEXT).setValue("Page 2");
        });
        pages.appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("Page 3");
      });
    });
    final Book deserialized = Book.builder().title(Component.text("My book"))
      .author(Component.text("myself", Style.style().font(Key.key("uniform")).build()))
      .addPage(Component.text("Page 1"))
      .addPage(Component.text("Page 2", NamedTextColor.DARK_RED))
      .addPage(Component.text("Page 3"))
      .build();

    this.assertRoundtrippable(BookTypeSerializer.TYPE, deserialized, node);
  }

  @Test
  void testNoTitleFails() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(BookTypeSerializer.AUTHOR).act(author -> {
        author.getNode(StyleSerializer.FONT).setValue("minecraft:uniform");
        author.getNode(ComponentTypeSerializer.TEXT).setValue("myself");
      });
      n.getNode(BookTypeSerializer.PAGES).appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("Page 1");
    });

    assertThrows(ObjectMappingException.class, () -> node.getValue(BookTypeSerializer.TYPE));
  }

  @Test
  void testNoAuthorFails() {
    final ConfigurationNode node = this.node(n -> {
      n.getNode(BookTypeSerializer.TITLE, ComponentTypeSerializer.TEXT).setValue("My book");
      n.getNode(BookTypeSerializer.PAGES).appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("Page 1");
    });

    assertThrows(ObjectMappingException.class, () -> node.getValue(BookTypeSerializer.TYPE));
  }
}
