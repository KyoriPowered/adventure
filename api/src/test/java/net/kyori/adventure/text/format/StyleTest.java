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
package net.kyori.adventure.text.format;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.adventure.text.TextAssertions.assertDecorations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StyleTest {
  private static final Map<TextDecoration, TextDecoration.State> NOT_SET = Stream.of(TextDecoration.values())
    .collect(Collectors.toMap(Function.identity(), decoration -> TextDecoration.State.NOT_SET));

  private static Map<TextDecoration, TextDecoration.State> overrideNotSet(final Map<TextDecoration, TextDecoration.State> overrides) {
    final Map<TextDecoration, TextDecoration.State> map = new HashMap<>();
    map.putAll(NOT_SET);
    map.putAll(overrides);
    return map;
  }

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
  void testOfApplicables() {
    final Style s0 = Style.style(
      TextColor.color(0x00aa00),
      TextDecoration.BOLD,
      HoverEvent.showText(Component.empty())
    );
    final Style s1 = Style.style(ImmutableList.of(
      TextColor.color(0x00aa00),
      TextDecoration.BOLD,
      HoverEvent.showText(Component.empty())
    ));
    assertEquals(TextColor.color(0x00aa00), s0.color());
    assertThat(s0.decorations()).containsEntry(TextDecoration.BOLD, TextDecoration.State.TRUE);
    assertEquals(s0, s1);
  }

  @Test
  void testOfTextDecorationAndState() {
    final Style s0 = Style.style(
      TextDecoration.BOLD.withState(TextDecoration.State.TRUE),
      TextDecoration.ITALIC.withState(TextDecoration.State.FALSE)
    );
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
    final Style s1 = Style.style(
      TextDecoration.BOLD.withState(true),
      TextDecoration.ITALIC.withState(false)
    );
    assertDecorations(s1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
  }

  @Test
  void testOfTextDecorationAndStateOverridesWhenSame() {
    final Style s0 = Style.style(
      TextDecoration.BOLD.withState(TextDecoration.State.TRUE),
      TextDecoration.BOLD.withState(TextDecoration.State.FALSE)
    );
    assertDecorations(s0, ImmutableSet.of(), ImmutableSet.of(TextDecoration.BOLD));
    final Style s1 = Style.style(
      TextDecoration.BOLD.withState(true),
      TextDecoration.BOLD.withState(false)
    );
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of(TextDecoration.BOLD));
  }

  @Test
  void testOfColor() {
    assertSame(Style.empty(), Style.style((TextColor) null));
    assertEquals(NamedTextColor.GREEN, Style.style(NamedTextColor.GREEN).color());
  }

  @Test
  void testStyle_Consumer() {
    final Style s0 = Style.style(builder -> {
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
    final Style s1 = Style.style(TextDecoration.BOLD);
    assertTrue(s1.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testDecorate() {
    final Style s0 = Style.empty();
    assertDecorations(s0, ImmutableSet.of(), ImmutableSet.of());
    for (final TextDecoration decoration : TextDecoration.values()) {
      assertDecorations(Style.empty().decorate(decoration), ImmutableSet.of(decoration), ImmutableSet.of());
    }
  }

  @Test
  void testOf_decorations() {
    final Style s0 = Style.style(TextDecoration.BOLD, TextDecoration.ITALIC);
    assertNull(s0.color());
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD, TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testDecorationIfAbsent() {
    final Style s0 = Style.style(TextDecoration.BOLD)
      .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)
      .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
  }

  @Test
  void testDecorationIfAbsentWithBuilder() {
    final Style s0 = Style.style().decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)
      .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
      .build();
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of(TextDecoration.ITALIC));
  }

  @Test
  void testOf_colorAndDecorations() {
    final Style s0 = Style.style(NamedTextColor.GREEN, TextDecoration.BOLD, TextDecoration.ITALIC);
    assertEquals(NamedTextColor.GREEN, s0.color());
    assertDecorations(s0, ImmutableSet.of(TextDecoration.BOLD, TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testColorIfAbsent() {
    assertEquals(NamedTextColor.GREEN, Style.style(NamedTextColor.GREEN).colorIfAbsent(NamedTextColor.RED).color());
    assertEquals(NamedTextColor.RED, Style.empty().colorIfAbsent(NamedTextColor.RED).color());
  }

  @Test
  void testDecoration() {
    final Style s0 = Style.empty();
    assertDecorations(s0, ImmutableSet.of(), ImmutableSet.of());
    for (final TextDecoration decoration : TextDecoration.values()) {
      assertDecorations(Style.empty().decoration(decoration, true), ImmutableSet.of(decoration), ImmutableSet.of());
    }
  }

  @Test
  void testDecorations() {
    assertThat(Style.empty().decorations()).containsExactlyEntriesIn(NOT_SET);
    assertThat(Style.style(TextDecoration.BOLD).decorations()).containsEntry(TextDecoration.BOLD, TextDecoration.State.TRUE);
  }

  @Test
  void testSetDecorations() {
    final Style s0 = Style.empty().decorations(ImmutableMap.of(
      TextDecoration.BOLD, TextDecoration.State.TRUE
    ));
    assertThat(s0.decorations()).containsExactlyEntriesIn(overrideNotSet(ImmutableMap.of(TextDecoration.BOLD, TextDecoration.State.TRUE)));
  }

  @Test
  void testMerge() {
    final Style s0 = Style.empty();
    final Style s1 = s0.merge(Style.style(NamedTextColor.DARK_PURPLE));
    assertEquals(NamedTextColor.DARK_PURPLE, s1.color());
    assertDecorations(s1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(s1.clickEvent());
    assertEquals(s0, s1.color(null));
  }

  @Test
  void testMerge_none() {
    final Style s0 = Style.empty();
    final Style s1 = s0.merge(Style.style(NamedTextColor.DARK_PURPLE), Style.Merge.merges());
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

  private static final Key TEST_FONT = Key.key("kyori", "kittenmoji");

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
    final Style b = Style.style()
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
    final Style s0 = Style.style(NamedTextColor.BLACK);
    final Style s1 = s0.merge(Style.style(NamedTextColor.DARK_PURPLE), Style.Merge.Strategy.ALWAYS);
    assertEquals(NamedTextColor.DARK_PURPLE, s1.color());
    final Style s2 = s0.merge(Style.style(NamedTextColor.DARK_PURPLE), Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    assertEquals(NamedTextColor.BLACK, s2.color());
  }

  @Test
  void testBuilderColorIfAbsent() {
    assertEquals(NamedTextColor.GREEN, Style.style().colorIfAbsent(NamedTextColor.GREEN).build().color());
    assertEquals(NamedTextColor.GREEN, Style.style().color(NamedTextColor.GREEN).colorIfAbsent(NamedTextColor.RED).build().color());
  }

  @Test
  void testBuilderDecoration() {
    for (final TextDecoration decoration : TextDecoration.values()) {
      assertDecorations(Style.style().decoration(decoration, true).build(), ImmutableSet.of(decoration), ImmutableSet.of());
    }
  }

  @Test
  void testBuilderMerge_none() {
    final Style style = Style.style(NamedTextColor.DARK_PURPLE);
    assertEquals(
      Style.empty(),
      Style.style()
        .merge(style, new Style.Merge[0])
        .build()
    );
    assertEquals(
      Style.empty(),
      Style.style()
        .merge(style, ImmutableSet.of())
        .build()
    );
  }

  @Test
  void testBuilderMergeEmptyArray() {
    final Style style = Style.style(NamedTextColor.DARK_PURPLE);
    final Style.Builder builder = Style.style();
    builder.merge(style, new Style.Merge[0]);
    assertEquals(Style.empty(), builder.build());
  }

  @Test
  void testBuilderMergeEmptySet() {
    final Style style = Style.style(NamedTextColor.DARK_PURPLE);
    final Style.Builder builder = Style.style();
    builder.merge(style, ImmutableSet.of());
    assertEquals(Style.empty(), builder.build());
  }

  @Test
  void testBuilderMergeDefaults() {
    final Style style = Style.style(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
    final Style.Builder builder = Style.style();
    builder.merge(style);
    assertEquals(style, builder.build());
  }

  @Test
  void testBuilderMerge_color() {
    final Style style = Style.style(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD);
    assertEquals(
      Style.style(NamedTextColor.DARK_PURPLE),
      Style.style()
        .merge(style, Style.Merge.COLOR)
        .build()
    );
    assertEquals(
      Style.style(NamedTextColor.DARK_PURPLE),
      Style.style()
        .merge(style, ImmutableSet.of(Style.Merge.COLOR))
        .build()
    );
  }

  @Test
  void testUnmergeEmpty() {
    final Style s0 = Style.empty();
    final Style s1 = Style.style(NamedTextColor.DARK_RED, TextDecoration.BOLD, TextDecoration.ITALIC);
    final Style s2 = s1.unmerge(s0);
    assertEquals(NamedTextColor.DARK_RED, s2.color());
    assertDecorations(s2, ImmutableSet.of(TextDecoration.BOLD, TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testUnmerge() {
    final Style s0 = Style.style(NamedTextColor.DARK_RED, TextDecoration.BOLD);
    final Style s1 = Style.style(NamedTextColor.DARK_RED, TextDecoration.BOLD, TextDecoration.ITALIC);
    final Style s2 = s1.unmerge(s0);
    assertNull(s2.color());
    assertDecorations(s2, ImmutableSet.of(TextDecoration.ITALIC), ImmutableSet.of());
  }

  @Test
  void testUnmergeWithEmptyChild() {
    final Style s0 = Style.style(NamedTextColor.DARK_RED, TextDecoration.BOLD);
    final Style s1 = Style.empty();
    final Style s2 = s1.unmerge(s0);
    assertSame(s1, s2);
  }

  @Test
  void testEquals() {
    new EqualsTester()
      .addEqualityGroup(
        Style.empty(),
        Style.style().build(),
        Style.style().color(NamedTextColor.DARK_PURPLE).color(null).build()
      )
      .addEqualityGroup(
        Style.style(NamedTextColor.LIGHT_PURPLE),
        Style.style(NamedTextColor.LIGHT_PURPLE, ImmutableSet.of())
      )
      .addEqualityGroup(
        Style.style(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
        Style.style(NamedTextColor.DARK_PURPLE, ImmutableSet.of(TextDecoration.BOLD))
      )
      .testEquals();
  }
}
