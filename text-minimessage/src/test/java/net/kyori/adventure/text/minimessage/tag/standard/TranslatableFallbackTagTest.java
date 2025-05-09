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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

class TranslatableFallbackTagTest extends AbstractTest {
  @Test
  void testSerializeTranslatable() {
    final String expected = "You should get a <lang_or:block.minecraft.diamond_block:'Fallback :('/>!";

    final TextComponent.Builder builder = Component.text()
      .content("You should get a ")
      .append(Component.translatable("block.minecraft.diamond_block").fallback("Fallback :("))
      .append(Component.text("!"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSerializeTranslatableWithArgs() {
    final String expected = "<lang_or:some_key:fallback:\"<red>:arg' 1\":'<blue>arg 2'>";

    final Component translatable = Component.translatable()
      .key("some_key")
      .fallback("fallback")
      .arguments(text(":arg' 1", NamedTextColor.RED), text("arg 2", NamedTextColor.BLUE))
      .build();

    this.assertSerializedEquals(expected, translatable);
  }

  @Test
  void testTranslatable() {
    final String input = "You should get a <lang_or:block.minecraft.diamond_block:'Diamond Block'>!";
    final Component expected = text("You should get a ")
      .append(translatable("block.minecraft.diamond_block").fallback("Diamond Block")
        .append(text("!")));

    this.assertParsedEquals(expected, input);
  }
}
