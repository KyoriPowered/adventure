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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("unchecked")
abstract class AbstractComponentTest<C extends BuildableComponent<C, B>, B extends BuildableComponent.Builder<C, B>> {
  abstract B builder();

  @Test
  void testColor() {
    final C c0 = this.builder().build();
    assertNull(c0.color());
    final C c1 = (C) c0.color(TextColor.GREEN);
    assertEquals(TextColor.GREEN, c1.color());
    assertEquals(c0, c1.color(null));
  }

  @Test
  void testDecoration() {
    final C c0 = this.builder().build();
    assertDecorations(c0, ImmutableMap.of());
    final C c1 = (C) c0.decoration(TextDecoration.BOLD, true);
    assertDecorations(c1, ImmutableMap.of(TextDecoration.BOLD, TextDecoration.State.TRUE));
    assertEquals(c0, c1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testClickEvent() {
    final C c0 = this.builder().build();
    assertNull(c0.clickEvent());
    final C c1 = (C) c0.clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "foo"));
    assertNotNull(c1.clickEvent());
    assertEquals(c0, c1.clickEvent(null));
  }

  @Test
  void testHoverEvent() {
    final C c0 = this.builder().build();
    assertNull(c0.hoverEvent());
    final C c1 = (C) c0.hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of("hover")));
    assertNotNull(c1.hoverEvent());
    assertEquals(c0, c1.hoverEvent(null));
  }

  @Test
  void testInsertion() {
    final C c0 = this.builder().build();
    assertNull(c0.insertion());
    final C c1 = (C) c0.insertion("foo");
    assertNotNull(c1.insertion());
    assertEquals(c0, c1.insertion(null));
  }

  @Test
  void testMergeStyle() {
    final C c0 = this.builder().build();
    assertNull(c0.color());
    assertDecorations(c0, ImmutableMap.of());
    assertNull(c0.clickEvent());
    final C c1 = (C) c0.mergeStyle(TextComponent.of("xyz", TextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/foo")));
    assertEquals(TextColor.RED, c1.color());
    assertDecorations(c1, ImmutableMap.of(TextDecoration.BOLD, TextDecoration.State.TRUE));
    assertNotNull(c1.clickEvent());
    assertEquals(c0, c1.color(null).decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET).clickEvent(null));
  }

  @Test
  void testMergeColor() {
    final C c0 = this.builder().build();
    assertNull(c0.color());
    assertDecorations(c0, ImmutableMap.of());
    assertNull(c0.clickEvent());
    final C c1 = (C) c0.mergeColor(TextComponent.of("xyz", TextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/foo")));
    assertEquals(TextColor.RED, c1.color());
    assertDecorations(c1, ImmutableMap.of());
    assertNull(c1.clickEvent());
    assertEquals(c0, c1.color(null));
  }

  @Test
  void testMergeDecorations() {
    final C c0 = this.builder().build();
    assertNull(c0.color());
    assertDecorations(c0, ImmutableMap.of());
    assertNull(c0.clickEvent());
    final C c1 = (C) c0.mergeDecorations(TextComponent.of("xyz", TextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/foo")));
    assertNull(c1.color());
    assertDecorations(c1, ImmutableMap.of(TextDecoration.BOLD, TextDecoration.State.TRUE));
    assertNull(c1.clickEvent());
    assertEquals(c0, c1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testMergeEvents() {
    final C c0 = this.builder().build();
    assertNull(c0.color());
    assertDecorations(c0, ImmutableMap.of());
    assertNull(c0.clickEvent());
    final C c1 = (C) c0.mergeEvents(TextComponent.of("xyz", TextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/foo")));
    assertNull(c1.color());
    assertDecorations(c1, ImmutableMap.of());
    assertNotNull(c1.clickEvent());
  }

  @Test
  void testResetStyle() {
    final C c0 = this.builder()
      .color(TextColor.RED)
      .decoration(TextDecoration.BOLD, true)
      .clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/foo"))
      .build();
    final C c1 = (C) c0.resetStyle();
    assertNull(c1.color());
    assertDecorations(c1, ImmutableMap.of());
    assertNull(c1.clickEvent());
    assertEquals(c1, c0.color(null).decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET).clickEvent(null));
  }

  @Test
  void testCopy() {
    final C c0 = this.builder().build();
    final C c1 = (C) c0.copy();
    assertEquals(c0, c1);
  }

  private static void assertDecorations(final Component component, final Map<TextDecoration, TextDecoration.State> expected) {
    if(expected.isEmpty()) {
      for(final TextDecoration decoration : TextDecoration.values()) {
        assertEquals(TextDecoration.State.NOT_SET, component.decoration(decoration));
      }
    } else {
      for(final Map.Entry<TextDecoration, TextDecoration.State> entry : expected.entrySet()) {
        assertEquals(entry.getValue(), component.decoration(entry.getKey()));
      }
    }
  }
}
