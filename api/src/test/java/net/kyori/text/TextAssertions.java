/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import java.util.Map;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextDecoration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextAssertions {
  public static void assertDecorations(final Component component, final Map<TextDecoration, TextDecoration.State> expected) {
    assertDecorations(component.style(), expected);
  }

  public static void assertDecorations(final Style style, final Map<TextDecoration, TextDecoration.State> expected) {
    if(expected.isEmpty()) {
      for(final TextDecoration decoration : TextDecoration.values()) {
        assertEquals(TextDecoration.State.NOT_SET, style.decoration(decoration));
      }
    } else {
      for(final Map.Entry<TextDecoration, TextDecoration.State> entry : expected.entrySet()) {
        assertEquals(entry.getValue(), style.decoration(entry.getKey()));
      }
    }
  }
}
