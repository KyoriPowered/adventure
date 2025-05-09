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
package net.kyori.adventure.text;

import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.TextAssertions.assertDecorations;

class ComponentDecorationTest {

  @Test
  void testDecorationIfAbsent() {
    final Style s0 = Style.style(TextDecoration.BOLD);
    final Component c0 = Component.text("tuba time", s0)
      .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)
      .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    assertDecorations(c0.style(), ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
  }

  @Test
  void testDecorationIfAbsentWithBuilder() {
    final Style s0 = Style.style(TextDecoration.BOLD);
    final Component c0 = Component.text().style(s0).content("tuba time")
      .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)
      .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
      .build();
    assertDecorations(c0.style(), ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
  }
}
