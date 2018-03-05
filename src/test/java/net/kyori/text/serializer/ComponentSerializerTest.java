/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2018 KyoriPowered
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
package net.kyori.text.serializer;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentSerializerTest {
  @Test
  void testFrom() {
    assertEquals(TextComponent.of("foo"), ComponentSerializers.LEGACY.deserialize("foo"));
  }

  @Test
  void testFromColor() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(TextColor.GREEN).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE))
      .append(TextComponent.of("bar").color(TextColor.BLUE))
      .build();

    assertEquals(component, ComponentSerializers.LEGACY.deserialize("&a&lfoo&9bar", '&'));
  }

  @Test
  void testFromColorOverride() {
    final TextComponent component = TextComponent.builder("")
      .append(TextComponent.of("foo").color(TextColor.BLUE))
      .build();

    assertEquals(component, ComponentSerializers.LEGACY.deserialize("&a&9foo", '&'));
  }

  @Test
  void testToLegacy() {
    final TextComponent c1 = TextComponent.builder("hi")
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .append(
        TextComponent.of("foo")
          .color(TextColor.GREEN)
          .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      )
      .append(
        TextComponent.of("bar")
          .color(TextColor.BLUE)
      )
      .append(TextComponent.of("baz"))
      .build();
    assertEquals("§lhi§afoo§9§lbar§r§lbaz", ComponentSerializers.LEGACY.serialize(c1, '§'));

    final TextComponent c2 = TextComponent.builder()
      .content("")
      .color(TextColor.YELLOW)
      .append(TextComponent.builder()
        .content("Hello ")
        .append(
          TextComponent.builder()
            .content("world")
            .color(TextColor.GREEN)
            .build()
        )
        .append(TextComponent.of("!")) // Should be yellow
        .build()
      )
      .build();
    assertEquals("§eHello §aworld§e!", ComponentSerializers.LEGACY.serialize(c2, '§'));

    final TextComponent c3 = TextComponent.builder()
      .content("")
      .decoration(TextDecoration.BOLD, true)
      .append(
        TextComponent.builder()
          .content("")
          .color(TextColor.YELLOW)
          .append(TextComponent.builder()
            .content("Hello ")
            .append(
              TextComponent.builder()
                .content("world")
                .color(TextColor.GREEN)
                .build()
            )
            .append(TextComponent.of("!"))
            .build()
          )
          .build())
      .build();
    assertEquals("§e§lHello §a§lworld§e§l!", ComponentSerializers.LEGACY.serialize(c3, '§'));
  }
}
