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
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLACK;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextColor.color;

class TransitionTagTest extends AbstractTest {

  @Test
  void testTransition() {
    final TextColor[] colors = new TextColor[] {BLACK, color(0x808080), WHITE, color(0x808080), BLACK};
    final float[] phases = new float[] {-1, 0.5f, 0, 0.5f, 1};
    for (int i = 0; i < colors.length; i++) {
      final String input = "<transition:white:black:" + phases[i] + ">Hello World";
      final Component expected = text("", colors[i]).append(text("Hello World"));
      this.assertParsedEquals(expected, input);
    }
  }
}
