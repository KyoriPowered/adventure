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
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

final class TextComponentTest extends SerializerTest {
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
        .putString(ComponentTreeConstants.TEXT, "c")
        .putString(ComponentTreeConstants.COLOR, "gold")
        .put(
          ComponentTreeConstants.EXTRA,
          ListBinaryTag.builder()
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "o")
              .putString(ComponentTreeConstants.COLOR, "dark_aqua")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "l")
              .putString(ComponentTreeConstants.COLOR, "light_purple")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "o")
              .putString(ComponentTreeConstants.COLOR, "dark_purple")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "u")
              .putString(ComponentTreeConstants.COLOR, "blue")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "r")
              .putString(ComponentTreeConstants.COLOR, "dark_green")
              .build())
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "s")
              .putString(ComponentTreeConstants.COLOR, "red")
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
        .putString(ComponentTreeConstants.TEXT, "This is a test.")
        .putString(ComponentTreeConstants.COLOR, "dark_purple")
        .put(
          ComponentTreeConstants.HOVER_EVENT_SNAKE,
          CompoundBinaryTag.builder()
            .putString(ComponentTreeConstants.HOVER_EVENT_ACTION, "show_text")
            .putString(ComponentTreeConstants.HOVER_EVENT_VALUE, "A test.")
            .build()
        )
        .put(
          ComponentTreeConstants.EXTRA,
          ListBinaryTag.heterogeneousListBinaryTag()
            .add(StringBinaryTag.stringBinaryTag(" "))
            .add(CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "A what?")
              .putString(ComponentTreeConstants.COLOR, "dark_aqua")
              .build())
            .build()
            .wrapHeterogeneity()
        )
        .build()
    );
  }
}
