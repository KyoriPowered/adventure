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

import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ListComponentDeserializationTest extends SerializerTest {
  @Test
  public void testStringListDeserialization() {
    assertEquals(
      Component.text()
        .content("a")
        .append(Component.text("b"))
        .append(Component.text("c"))
        .build(),
      this.deserialize(
        ListBinaryTag.builder(BinaryTagTypes.STRING)
          .add(StringBinaryTag.stringBinaryTag("a"))
          .add(StringBinaryTag.stringBinaryTag("b"))
          .add(StringBinaryTag.stringBinaryTag("c"))
          .build()
      )
    );
  }

  @Test
  public void testCompoundListDeserialization() {
    assertEquals(
      Component.text()
        .content("x")
        .color(NamedTextColor.RED)
        .append(Component.translatable("message.disconnection", Style.style(TextDecoration.BOLD)))
        .append(Component.text("z", Style.style(TextDecoration.ITALIC.withState(false), NamedTextColor.DARK_AQUA)))
        .append(
          Component.text()
            .content("qwerty")
            .color(NamedTextColor.BLACK)
            .append(Component.text("abc"))
            .build()
        )
        .build(),
      this.deserialize(
        ListBinaryTag.builder(BinaryTagTypes.COMPOUND)
          .add(
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "x")
              .putString(ComponentTreeConstants.COLOR, "red")
              .put(
                ComponentTreeConstants.EXTRA,
                ListBinaryTag.builder(BinaryTagTypes.COMPOUND)
                  .add(
                    CompoundBinaryTag.builder()
                      .putString(ComponentTreeConstants.TRANSLATE, "message.disconnection")
                      .putBoolean("bold", true)
                      .build()
                  )
                  .build()
              )
              .build()
          )
          .add(
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "z")
              .putString(ComponentTreeConstants.COLOR, "dark_aqua")
              .putBoolean("italic", false)
              .build()
          )
          .add(
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "qwerty")
              .putString(ComponentTreeConstants.COLOR, "black")
              .put(
                ComponentTreeConstants.EXTRA,
                ListBinaryTag.builder(BinaryTagTypes.STRING)
                  .add(StringBinaryTag.stringBinaryTag("abc"))
                  .build()
              )
              .build()
          )
          .build()
      )
    );
  }

  @Test
  public void testHeterogeneousListDeserialization() {
    assertEquals(
      Component.text()
        .content("a")
        .color(NamedTextColor.RED)
        .append(Component.empty())
        .append(Component.text("b", NamedTextColor.YELLOW))
        .append(Component.text("qwerty"))
        .build(),
      this.deserialize(
        ListBinaryTag.heterogeneousListBinaryTag()
          .add(
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "a")
              .putString(ComponentTreeConstants.COLOR, "red")
              .build()
          )
          .add(StringBinaryTag.stringBinaryTag(""))
          .add(
            CompoundBinaryTag.builder()
              .putString(ComponentTreeConstants.TEXT, "b")
              .putString(ComponentTreeConstants.COLOR, "yellow")
              .build()
          )
          .add(StringBinaryTag.stringBinaryTag("qwerty"))
          .build()
          .wrapHeterogeneity()
      )
    );
  }
}
