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
import com.google.common.testing.EqualsTester;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.test.WeirdAssertions.assertAllEqualToEachOther;
import static net.kyori.test.WeirdAssertions.forEachTransformAndAssertIterable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractComponentTest<C extends BuildableComponent<C, B> & ScopedComponent<C>, B extends ComponentBuilder<C, B>> {
  abstract B builder();

  final C buildOne() {
    return this.builder().build();
  }

  @Test
  void testFreshlyBuiltHasEmptyStyle() {
    final C c0 = this.buildOne();
    assertSame(Style.empty(), c0.style());
  }

  @Test
  void testChildren() {
    final C c0 = this.buildOne();
    assertThat(c0.children()).isEmpty();
    final Component child = Component.text("foo");
    final Component c1 = c0.children(Collections.singletonList(child));
    assertThat(c1.children()).containsExactly(child).inOrder();
  }

  @Test
  void testDecorations() {
    Component component = this.buildOne();

    // The bold decoration should not be set at this point.
    assertFalse(component.hasDecoration(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.BOLD));

    component = component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);

    final Map<TextDecoration, TextDecoration.State> decorations = component.decorations();

    // The bold decoration should be set and true at this point.
    assertTrue(component.hasDecoration(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.TRUE, component.decoration(TextDecoration.BOLD));
    assertEquals(component.decoration(TextDecoration.BOLD), decorations.get(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.TRUE, decorations.get(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.NOT_SET, decorations.get(TextDecoration.OBFUSCATED));
  }

  @Test
  void testStyledAlways() {
    final Component c0 = this.builder().color(NamedTextColor.RED).decoration(TextDecoration.STRIKETHROUGH, true).build();
    final Component c1 = c0.style(style -> {
      style.color(NamedTextColor.GREEN);
    });
    assertEquals(NamedTextColor.GREEN, c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(TextDecoration.STRIKETHROUGH), ImmutableSet.of());
  }

  @Test
  void testStyledNever() {
    final Component c0 = this.builder().color(NamedTextColor.RED).decoration(TextDecoration.STRIKETHROUGH, true).build();
    final Component c1 = c0.style(style -> {
      style.color(NamedTextColor.GREEN);
    }, Style.Merge.Strategy.NEVER);
    assertEquals(NamedTextColor.GREEN, c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testStyledIfAbsentOnTarget() {
    final Component c0 = this.builder().color(NamedTextColor.RED).decoration(TextDecoration.STRIKETHROUGH, true).build();
    final Component c1 = c0.style(style -> {
      style.color(NamedTextColor.GREEN);
      style.decoration(TextDecoration.BOLD, false);
    }, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    assertEquals(NamedTextColor.GREEN, c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(TextDecoration.STRIKETHROUGH), ImmutableSet.of(TextDecoration.BOLD));
    final Component c2 = c0.style(style -> {
      style.decoration(TextDecoration.BOLD, false);
    }, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
    assertEquals(NamedTextColor.RED, c2.color());
    TextAssertions.assertDecorations(c2, ImmutableSet.of(TextDecoration.STRIKETHROUGH), ImmutableSet.of(TextDecoration.BOLD));
  }

  @Test
  void testResetStyle() {
    final C c0 = this.builder()
      .color(NamedTextColor.RED)
      .decoration(TextDecoration.BOLD, true)
      .clickEvent(ClickEvent.runCommand("/foo"))
      .build();
    final C c1 = c0.style(Style.empty());
    assertNull(c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c1.clickEvent());
    assertEquals(c1, c0.color(null).decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET).clickEvent(null));
  }

  @Test
  void testColor() {
    final C c0 = this.buildOne();
    assertNull(c0.color());
    final C c1 = c0.color(NamedTextColor.GREEN);
    assertEquals(NamedTextColor.GREEN, c1.color());
    assertEquals(c0, c1.color(null));
  }

  @Test
  void testDecoration() {
    final C c0 = this.buildOne();
    TextAssertions.assertDecorations(c0, ImmutableSet.of(), ImmutableSet.of());
    final C c1 = c0.decoration(TextDecoration.BOLD, true);
    TextAssertions.assertDecorations(c1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
    assertEquals(c0, c1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testClickEvent() {
    final C c0 = this.buildOne();
    assertNull(c0.clickEvent());
    final C c1 = c0.clickEvent(ClickEvent.runCommand("foo"));
    assertNotNull(c1.clickEvent());
    assertEquals(c0, c1.clickEvent(null));
  }

  @Test
  void testHoverEvent() {
    final C c0 = this.buildOne();
    assertNull(c0.hoverEvent());
    final C c1 = c0.hoverEvent(HoverEvent.showText(Component.text("hover")));
    assertNotNull(c1.hoverEvent());
    assertEquals(c0, c1.hoverEvent(null));
  }

  @Test
  void testInsertion() {
    final C c0 = this.buildOne();
    assertNull(c0.insertion());
    final C c1 = c0.insertion("foo");
    assertNotNull(c1.insertion());
    assertEquals(c0, c1.insertion(null));
  }

  @Test
  void testMergeStyle_color() {
    final C c0 = this.buildOne();
    assertNull(c0.color());
    TextAssertions.assertDecorations(c0, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c0.clickEvent());
    final C c1 = c0.mergeStyle(Component.text("xyz", NamedTextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(ClickEvent.runCommand("/foo")), Collections.singleton(Style.Merge.COLOR));
    assertEquals(NamedTextColor.RED, c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c1.clickEvent());
    assertEquals(c0, c1.color(null));
  }

  @Test
  void testMergeStyle_decorations() {
    final C c0 = this.buildOne();
    assertNull(c0.color());
    TextAssertions.assertDecorations(c0, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c0.clickEvent());
    final C c1 = c0.mergeStyle(Component.text("xyz", NamedTextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(ClickEvent.runCommand("/foo")), Collections.singleton(Style.Merge.DECORATIONS));
    assertNull(c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
    assertNull(c1.clickEvent());
    assertEquals(c0, c1.decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET));
  }

  @Test
  void testMergeStyle_events() {
    final C c0 = this.buildOne();
    assertNull(c0.color());
    TextAssertions.assertDecorations(c0, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c0.clickEvent());
    final C c1 = c0.mergeStyle(Component.text("xyz", NamedTextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(ClickEvent.runCommand("/foo")), Collections.singleton(Style.Merge.EVENTS));
    assertNull(c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(), ImmutableSet.of());
    assertNotNull(c1.clickEvent());
  }

  @Test
  void testSetStyle() {
    final C c0 = this.buildOne();
    assertNull(c0.color());
    TextAssertions.assertDecorations(c0, ImmutableSet.of(), ImmutableSet.of());
    assertNull(c0.clickEvent());
    final C c1 = c0.style(Component.text("xyz", NamedTextColor.RED, ImmutableSet.of(TextDecoration.BOLD)).clickEvent(ClickEvent.runCommand("/foo")).style());
    assertEquals(NamedTextColor.RED, c1.color());
    TextAssertions.assertDecorations(c1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
    assertNotNull(c1.clickEvent());
    assertEquals(c0, c1.color(null).decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET).clickEvent(null));
  }

  @Test
  void testSetStyleOnBuilder() {
    final B b0 = this.builder();
    b0.color(NamedTextColor.RED);
    final C c0 = b0.build();
    b0.style(Style.style(NamedTextColor.GREEN));
    final C c1 = b0.build();
    assertEquals(NamedTextColor.RED, c0.color());
    assertEquals(NamedTextColor.GREEN, c1.color());
  }

  @Test
  void testStyleConsumer() {
    final B b0 = this.builder();
    b0.style(style -> {
      style.color(NamedTextColor.RED);
    });
    final C c0 = b0.build();
    assertEquals(NamedTextColor.RED, c0.color());
  }

  @Test
  void testRebuildWithNoChanges() {
    final C c0 = this.buildOne();
    assertEquals(c0, c0.toBuilder().build());
  }

  @Test
  void testRebuildEmptyChildren() {
    final C c0 = this.buildOne();
    final B b0 = c0.toBuilder();
    final C c1 = b0.build();
    assertEquals(c0, c1);
    assertThat(c0.children()).isEmpty();
    assertThat(c1.children()).isEmpty();
  }

  @Test
  void testRebuildChild() {
    final Component child = Component.text("abc");
    final C c0 = this.builder().append(child).build();
    final B b0 = c0.toBuilder();
    final C c1 = b0.build();
    assertEquals(c0, c1);
    forEachTransformAndAssertIterable(Arrays.asList(c0, c1), Component::children, subject -> subject.containsExactly(child).inOrder());
  }

  @Test
  void testRebuildChildren() {
    final Component child1 = Component.text("abc");
    final Component child2 = Component.text("def");
    final C c0 = this.builder().append(child1, child2).build();
    final C c1 = this.builder().append(Arrays.asList(child1, child2)).build();
    final B b0 = c0.toBuilder();
    final B b1 = c1.toBuilder();
    final C c2 = b0.build();
    final C c3 = b1.build();
    assertAllEqualToEachOther(c0, c1, c2, c3);
    forEachTransformAndAssertIterable(Arrays.asList(c0, c1, c2, c3), Component::children, subject -> subject.containsExactly(child1, child2).inOrder());
  }

  @Test
  void testHasStyling() {
    Component component = this.buildOne();
    assertFalse(component.hasStyling());
    component = component.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);
    assertTrue(component.hasStyling());
    component = component.style(Style.empty());
    assertFalse(component.hasStyling());
  }

  @Test
  void testAsHoverEvent() {
    final C c0 = this.buildOne().color(null);
    final HoverEvent<Component> e0 = HoverEvent.showText(c0);
    assertSame(c0, e0.value()); // value should be untouched
    final HoverEvent<Component> e1 = c0.asHoverEvent(component -> component.color(NamedTextColor.RED));
    assertEquals(c0, e1.value().color(null));
    assertEquals(e0, e1.value(e1.value().color(null)));
  }

  @Test
  void testBasicEquals() {
    new EqualsTester()
      .addEqualityGroup(this.builder().build())
      .addEqualityGroup(this.builder().color(NamedTextColor.RED).build())
      .testEquals();
  }

  // -----------------
  // ---- Builder ----
  // -----------------

  @Test
  void testBuilderApplyDeep() {
    final C c0 = this.builder()
      .append(Component.text("a", NamedTextColor.RED))
      .append(Component.text("b", NamedTextColor.RED))
      .applyDeep(builder -> builder.color(NamedTextColor.GREEN))
      .build();
    final List<Component> children = c0.children();
    assertThat(children).hasSize(2);
    for (final Component child : children) {
      assertEquals(NamedTextColor.GREEN, child.color());
    }
  }

  @Test
  void testCollectorNoSeparator() {
    final Component joined = Stream.of(
      Component.text("Hello", NamedTextColor.RED),
      Component.text("World")
    ).collect(Component.toComponent());
    final Component expected = Component.text()
      .append(Component.text("Hello", NamedTextColor.RED))
      .append(Component.text("World"))
      .build();

    assertEquals(expected, joined);
  }

  @Test
  void testCollectorWithSeparator() {
    final Component joined = Stream.of(
      Component.text("Hello", NamedTextColor.RED),
      Component.text("World")
    ).collect(Component.toComponent(Component.space()));

    final Component expected = Component.text()
      .append(Component.text("Hello", NamedTextColor.RED))
      .append(Component.space())
      .append(Component.text("World"))
      .build();

    assertEquals(expected, joined);
  }

  @Test
  void testCollectOnEmptyStreamReturnsEmptyComponent() {
    assertEquals(Component.empty(), Stream.<Component>of().collect(Component.toComponent()));
  }
}
