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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

public final class TextComponentTest extends SerializerTest {
  @Test
  public void testSimple() {
    this.test(
      Component.text("Hello, world."),
      StringBinaryTag.stringBinaryTag("Hello, world.")
    );
  }

  @Test
  public void testComplex1() {
    this.test(
      Component.text().content("c")
        .color(NamedTextColor.GOLD)
        .append(Component.text("o", NamedTextColor.DARK_AQUA))
        .append(Component.text("l", NamedTextColor.LIGHT_PURPLE))
        .append(Component.text("o", NamedTextColor.DARK_PURPLE))
        .append(Component.text("u", NamedTextColor.BLUE))
        .append(Component.text("r", NamedTextColor.DARK_GREEN))
        .append(Component.text("s", NamedTextColor.RED))
        .build(),
      CompoundBinaryTag.builder()
        .putString("text", "c")
        .putString("color", "gold")
        .put(
          "extra",
          ListBinaryTag.builder()
            .add(CompoundBinaryTag.builder()
              .putString("text", "o")
              .putString("color", "dark_aqua")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString("text", "l")
              .putString("color", "light_purple")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString("text", "o")
              .putString("color", "dark_purple")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString("text", "u")
              .putString("color", "blue")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString("text", "r")
              .putString("color", "dark_green")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString("text", "s")
              .putString("color", "red")
              .build())
            .build()
        )
        .build()
    );
  }

  @Test
  public void testComplex2() {
    this.test(
      Component.text().content("This is a test.")
        .color(NamedTextColor.DARK_PURPLE)
        .hoverEvent(HoverEvent.showText(Component.text("A test.")))
        .append(Component.text(" "))
        .append(Component.text("A what?", NamedTextColor.DARK_AQUA))
        .build(),
      CompoundBinaryTag.builder()
        .putString("text", "This is a test.")
        .putString("color", "dark_purple")
        .put(
          "hover_event",
          CompoundBinaryTag.builder()
            .putString("action", "show_text")
            .putString("value", "A test.")
            .build()
        )
        .put(
          "extra",
          ListBinaryTag.heterogeneousListBinaryTag()
            .add(StringBinaryTag.stringBinaryTag(" "))
            .add(CompoundBinaryTag.builder()
              .putString("text", "A what?")
              .putString("color", "dark_aqua")
              .build())
            .build()
            .wrapHeterogeneity()
        )
        .build()
    );
  }
}
