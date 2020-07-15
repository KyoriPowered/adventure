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

import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.adventure.text.TextAssertions.assertDecorations;
import static net.kyori.test.WeirdAssertions.doWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StyleTest {
  @Test
  void testEmptyIsActuallyEmpty() {
    final Style s0 = Style.empty();
    assertNull(s0.color());
    assertDecorations(s0, ImmutableSet.of(), ImmutableSet.of());
    assertNull(s0.clickEvent());
    assertNull(s0.hoverEvent());
    assertNull(s0.insertion());
    assertNull(s0.font());
  }

  @Test
  void testOfColor() {
    assertSame(Style.empty(), Style.of((TextColor) null));
    assertEquals(NamedTextColor.GREEN, Style.of(NamedTextColor.GREEN).color());
  }

  @Test
  void testMake() {
    final Style s0 = Style.make(builder -> {
      builder.color(NamedTextColor.RED);
      builder.decoration(TextDecoration.BOLD, true);
    });
    assertEquals(NamedTextColor.RED, s0.color());
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
  }

  @Test
  void testHasDecoration() {
    final Style s0 = Style.empty();
    assertFalse(s0.hasDecoration(TextDecoration.BOLD));
    final Style s1 = Style.of(TextDecoration.BOLD);
    assertTrue(s1.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testOf_decorations() {
    final Style s0 = Style.of(TextDecoration.BOLD, TextDecoration.ITALIC);
    assertNull(s0.color());
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD, TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testOf_colorAndDecorations() {
    final Style s0 = Style.of(NamedTextColor.GREEN, TextDecoration.BOLD, TextDecoration.ITALIC);
    assertEquals(NamedTextColor.GREEN, s0.color());
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD, TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testColorIfAbsent() {
    assertEquals(NamedTextColor.GREEN, Style.of(NamedTextColor.GREEN).colorIfAbsent(NamedTextColor.RED).color());
    assertEquals(NamedTextColor.RED, Style.empty().colorIfAbsent(NamedTextColor.RED).color());
  }

  @Test
  void testDecoration() {
    final Style s0 = Style.empty();
    assertDecorations(s0, ImmutableSet.of(), ImmutableSet.of());
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertDecorations(Style.empty().decoration(decoration, true), ImmutableSet.of(decoration), ImmutableSet.of());
    }
  }

  @Test
  void testDecorations() {
    assertThat(Style.empty().decorations()).containsExactlyEntriesIn(Stream.of(TextDecoration.values()).collect(Collectors.toMap(Function.identity(), decoration -> TextDecoration.State.NOT_SET)));
    assertThat(Style.of(TextDecoration.BOLD).decorations()).containsEntry(TextDecoration.BOLD, TextDecoration.State.TRUE);
  }

  @Test
  void testMerge() {
    final Style s0 = Style.empty();
    final Style s1 = s0.merge(Style.of(NamedTextColor.DARK_PURPLE));
    assertEquals(NamedTextColor.DARK_PURPLE, s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.color(null));
  }

  @Test
  void testMerge_none() {
    final Style s0 = Style.empty();
    final Style s1 = s0.merge(Style.of(NamedTextColor.DARK_PURPLE), Style.Merge.of());
    assertNull(s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.color(null));
  }

  @Test
  void testMerge_color() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.COLOR);
    assertEquals(NamedTextColor.RED, s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.color(null));
  }

  @Test
  void testMerge_decorations() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.DECORATIONS);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testMerge_events() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.EVENTS);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertNotNull(s1.clickEvent());
    assertEquals(s0, s1.clickEvent(null));
  }

  @Test
  void testMerge_insertion() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.INSERTION);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertEquals("abc", s1.insertion());
    assertEquals(s0, s1.insertion(null));
  }

  private static final Key TEST_FONT = Key.of("kyori", "kittenmoji");

  @Test
  void testMerge_font() {
    final Style s0 = Style.empty();
    final Style s1 = merge(s0, Style.Merge.FONT);
    assertNull(s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertEquals(TEST_FONT, s1.font());
    assertEquals(s0, s1.font(null));
  }

  private static Style merge(final Style a, final Style.Merge merge) {
    final Style b = Style.builder()
      .color(NamedTextColor.RED)
      .decoration(TextDecoration.BOLD, true)
      .clickEvent(ClickEvent.runCommand("/foo"))
      .insertion("abc")
      .font(TEST_FONT)
      .build();
    return a.merge(b, merge);
  }

  @Test
  void testMergeStrategy() {
    final Style s0 = Style.of(NamedTextColor.BLACK);
    final Style s1 = s0.merge(Style.of(NamedTextColor.DARK_PURPLE), Style.Merge.Strategy.ALWAYS);
    assertEquals(NamedTextColor.DARK_PURPLE, s1.color());
    final Style s2 = s0.merge(Style.of(NamedTextColor.DARK_PURPLE), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    assertEquals(NamedTextColor.BLACK, s2.color());
  }

  @Test
  void testBuilderColorIfAbsent() {
    assertEquals(NamedTextColor.GREEN, Style.builder().colorIfAbsent(NamedTextColor.GREEN).build().color());
    assertEquals(NamedTextColor.GREEN, Style.builder().color(NamedTextColor.GREEN).colorIfAbsent(NamedTextColor.RED).build().color());
  }

  @Test
  void testBuilderDecoration() {
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertDecorations(Style.builder().decoration(decoration, true).build(), ImmutableSet.of(decoration), ImmutableSet.of());
    }
  }

  @Test
  void testBuilderMerge_none() {
    final Style style = Style.of(NamedTextColor.DARK_PURPLE);
    doWith(Style.builder(), builder -> {
      builder.merge(style, new Style.Merge[0]);
      assertEquals(Style.empty(), builder.build());
    });
    doWith(Style.builder(), builder -> {
      builder.merge(style, ImmutableSet.of());
      assertEquals(Style.empty(), builder.build());
    });
  }

  @Test
  void testBuilderMerge_all() {
    final Style style = Style.of(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
    doWith(Style.builder(), builder -> {
      builder.merge(style);
      assertEquals(style, builder.build());
    });
  }

  @Test
  void testBuilderMerge_color() {
    final Style style = Style.of(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
    doWith(Style.builder(), builder -> {
      builder.merge(style, Style.Merge.COLOR);
      assertEquals(Style.of(NamedTextColor.DARK_PURPLE), builder.build());
    });
    doWith(Style.builder(), builder -> {
      builder.merge(style, ImmutableSet.of(Style.Merge.COLOR));
      assertEquals(Style.of(NamedTextColor.DARK_PURPLE), builder.build());
    });
  }

  @Test
  void testEquals() {
    new EqualsTester()
      .addEqualityGroup(
        Style.empty(),
        Style.builder().build(),
        Style.builder().color(NamedTextColor.DARK_PURPLE).color(null).build()
      )
      .addEqualityGroup(
        Style.of(NamedTextColor.LIGHT_PURPLE),
        Style.of(NamedTextColor.LIGHT_PURPLE, ImmutableSet.of())
      )
      .addEqualityGroup(
        Style.of(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
        Style.of(NamedTextColor.DARK_PURPLE, ImmutableSet.of(TextDecoration.BOLD))
      )
      .testEquals();
  }
}
