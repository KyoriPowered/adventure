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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

class SpriteTagTest extends AbstractTest {
  @Test
  void testSerializeSprite() {
    final String expected = "This sentence is <sprite:'minecraft:block/fire_0'/>!";

    final TextComponent.Builder builder = Component.text()
      .append(Component.text("This sentence is "))
      .append(Component.object(ObjectComponent.Contents.sprite(Key.key("block/fire_0"))))
      .append(Component.text("!"));

    this.assertSerializedEquals(expected, builder);
  }

  @Test
  void testSprite() {
    final String input = "<sprite:block/stone> is hard.";
    final Component expected = Component.text()
      .append(Component.object(ObjectComponent.Contents.sprite(Key.key("block/stone"))))
      .append(Component.text(" is hard."))
      .build();

    this.assertParsedEquals(expected, input);
  }
}
