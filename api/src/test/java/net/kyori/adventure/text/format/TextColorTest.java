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
package net.kyori.adventure.text.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextColorTest {
  @Test
  public void testPureColors() {
    final TextColor redInt = TextColor.of(0xFF0000);
    final TextColor greenInt = TextColor.of(0x00FF00);
    final TextColor blueInt = TextColor.of(0x0000FF);

    final TextColor red = TextColor.of(0xFF, 0x00, 0x00);
    final TextColor green = TextColor.of(0x00, 0xFF, 0x00);
    final TextColor blue = TextColor.of(0x00, 0x00, 0xFF);

    assertEquals(redInt, red);
    assertEquals(greenInt, green);
    assertEquals(blueInt, blue);
  }

  @Test
  public void testExtractComponents() {
    final TextColor purple = TextColor.of(0xFF00FF);
    assertEquals(0xFF, purple.red());
    assertEquals(0x00, purple.green());
    assertEquals(0xFF, purple.blue());

    final TextColor color = TextColor.of(0xBADA04);
    assertEquals(0xBA, color.red());
    assertEquals(0xDA, color.green());
    assertEquals(0x04, color.blue());
  }

}
