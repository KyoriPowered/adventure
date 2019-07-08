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
package net.kyori.text.format;

import com.google.common.collect.ImmutableMap;
import com.google.common.testing.EqualsTester;
import net.kyori.text.event.ClickEvent;
import org.junit.jupiter.api.Test;

import static net.kyori.text.TextAssertions.assertDecorations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class StyleTest {
  @Test
  void testSanity() {
    final Style c0 = Style.empty();
    assertNull(c0.color());
    assertDecorations(c0, ImmutableMap.of());
    assertNull(c0.clickEvent());
    assertNull(c0.hoverEvent());
    assertNull(c0.insertion());
  }

  @Test
  void testColorIfAbsent() {
    assertEquals(TextColor.GREEN, Style.of(TextColor.GREEN).color());
    assertEquals(TextColor.GREEN, Style.of(TextColor.GREEN).colorIfAbsent(TextColor.RED).color());
    assertEquals(TextColor.RED, Style.empty().colorIfAbsent(TextColor.RED).color());
  }

  @Test
  void testMerge_color() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.COLOR);
    assertEquals(TextColor.RED, s1.color());
    assertDecorations(s1, ImmutableMap.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.color(null));
  }

  @Test
  void testMerge_decorations() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.DECORATIONS);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableMap.of(TextDecoration.BOLD, TextDecoration.State.TRUE));
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testMerge_events() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.EVENTS);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableMap.of());
    assertNotNull(s1.clickEvent());
    assertEquals(s0, s1.clickEvent(null));
  }

  @Test
  void testMerge_insertion() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.INSERTION);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableMap.of());
    assertEquals("abc", s1.insertion());
    assertEquals(s0, s1.insertion(null));
  }

  private static Style merge(final Style a, final Style.Merge merge) {
    final Style b = Style.builder()
      .color(TextColor.RED)
      .decoration(TextDecoration.BOLD, true)
      .clickEvent(ClickEvent.runCommand("/foo"))
      .insertion("abc")
      .build();
    return a.merge(b, merge);
  }

  @Test
  void testEquals() {
    new EqualsTester()
      .addEqualityGroup(Style.empty())
      .addEqualityGroup(Style.of(TextColor.LIGHT_PURPLE))
      .testEquals();
  }
}
