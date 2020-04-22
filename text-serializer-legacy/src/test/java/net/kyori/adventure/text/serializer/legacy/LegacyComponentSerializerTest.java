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
package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LegacyComponentSerializerTest {
  @Test
  void testSimpleFrom() {
    final TextComponent component = TextComponent.of("foo");
    assertEquals(component, LegacyComponentSerializer.legacy().deserialize("foo"));
    assertEquals(component, LegacyComponentSerializer.legacyLinking().deserialize("foo"));
  }

  @Test
  void testFromColor() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(TextComponent.of("bar").color(NamedTextColor.BLUE))
      .build();

    assertEquals(component, LegacyComponentSerializer.legacy().deserialize("&a&lfoo&9bar", '&'));
    assertEquals(component, LegacyComponentSerializer.legacyLinking().deserialize("&a&lfoo&9bar", '&'));
  }

  @Test
  void testFromColorOverride() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(NamedTextColor.BLUE))
      .build();

    assertEquals(component, LegacyComponentSerializer.legacy().deserialize("&a&9foo", '&'));
    assertEquals(component, LegacyComponentSerializer.legacyLinking().deserialize("&a&9foo", '&'));
  }

  @Test
  void testResetOverride() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(TextComponent.of("bar").color(NamedTextColor.DARK_GRAY))
      .build();

    assertEquals(component, LegacyComponentSerializer.legacy().deserialize("&a&lfoo&r&8bar", '&'));
    assertEquals(component, LegacyComponentSerializer.legacyLinking().deserialize("&a&lfoo&r&8bar", '&'));
  }

  @Test
  void testCompound() {
    final TextComponent component = TextComponent.builder()
      .content("hi there ")
      .append(TextComponent.builder("this bit is green ")
        .color(NamedTextColor.GREEN)
        .build())
      .append(TextComponent.of("this isn't ").style(Style.empty()))
      .append(TextComponent.builder("and woa, this is again")
        .color(NamedTextColor.GREEN)
        .build())
      .build();

    assertEquals("hi there &athis bit is green &rthis isn't &aand woa, this is again", LegacyComponentSerializer.legacy().serialize(component, '&'));
  }

  @Test
  void testToLegacy() {
    final TextComponent c1 = TextComponent.builder("hi")
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .append(
        TextComponent.of("foo")
          .color(NamedTextColor.GREEN)
          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      )
      .append(
        TextComponent.of("bar")
          .color(NamedTextColor.BLUE)
      )
      .append(TextComponent.of("baz"))
      .build();
    assertEquals("§lhi§afoo§9§lbar§r§lbaz", LegacyComponentSerializer.legacy().serialize(c1, '§'));

    final TextComponent c2 = TextComponent.builder()
      .content("")
      .color(NamedTextColor.YELLOW)
      .append(TextComponent.builder()
        .content("Hello ")
        .append(
          TextComponent.builder()
            .content("world")
            .color(NamedTextColor.GREEN)
            .build()
        )
        .append(TextComponent.of("!")) // Should be yellow
        .build()
      )
      .build();
    assertEquals("§eHello §aworld§e!", LegacyComponentSerializer.legacy().serialize(c2, '§'));

    final TextComponent c3 = TextComponent.builder()
      .content("")
      .decoration(TextDecoration.BOLD, true)
      .append(
        TextComponent.builder()
          .content("")
          .color(NamedTextColor.YELLOW)
          .append(TextComponent.builder()
            .content("Hello ")
            .append(
              TextComponent.builder()
                .content("world")
                .color(NamedTextColor.GREEN)
                .build()
            )
            .append(TextComponent.of("!"))
            .build()
          )
          .build())
      .build();
    assertEquals("§e§lHello §a§lworld§e§l!", LegacyComponentSerializer.legacy().serialize(c3, '§'));
  }
}
