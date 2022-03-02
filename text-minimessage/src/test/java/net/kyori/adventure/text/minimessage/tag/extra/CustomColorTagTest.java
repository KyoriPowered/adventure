/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.extra;

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomColorTagTest {

  @Test
  void testCustomColor() {
    final String input = "<orange>Orange";

    final Component expected = Component.text("Orange", TextColor.color(0xfc6a03));

    final Map<String, TextColor> colorMap = new HashMap<>();
    colorMap.put("orange", TextColor.color(0xfc6a03));

    Assertions.assertEquals(expected, MiniMessage.builder().editTags(b -> b.resolver(ExtraTags.color(colorMap))).build().deserialize(input)
    );
  }

  @Test
  void testCustomColorWithQualifier() {
    final String input = "<qualifier:orange>Orange";

    final Component expected = Component.text("Orange", TextColor.color(0xfc6a03));

    final Map<String, TextColor> colorMap = new HashMap<>();
    colorMap.put("orange", TextColor.color(0xfc6a03));

    Assertions.assertEquals(
      expected,
      MiniMessage.builder().editTags(b -> b.resolver(ExtraTags.color("qualifier", colorMap))).build().deserialize(input)
    );
  }

  @Test
  void testCustomColorFromMc() {
    final String input = "<special:gold>This is gold text";

    final Component expected = Component.text("This is gold text", TextColor.color(0xd4af37));

    final Map<String, TextColor> colorMap = new HashMap<>();
    colorMap.put("gold", TextColor.color(0xd4af37));

    Assertions.assertEquals(
      expected,
      MiniMessage.builder().editTags(b -> b.resolver(ExtraTags.color("special", colorMap))).build().deserialize(input)
    );
  }
}
