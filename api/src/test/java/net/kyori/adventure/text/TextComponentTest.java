/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.TextAssertions.assertDecorations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextComponentTest extends AbstractComponentTest<TextComponent, TextComponent.Builder> {
  @Override
  TextComponent.Builder builder() {
    return Component.text().content("foo");
  }

  @Test
  void testNoWarningThrownWhenNoLegacyFormattingDetected() {
    final TextComponent component = Component.text("This is a test.");
    assertNull(((TextComponentImpl) component).warnWhenLegacyFormattingDetected());
  }

  @Test
  void testWarningThrownWhenLegacyFormattingDetected() {
    final TextComponent component = Component.text(legacy('3') + "This is a test.");
    assertNotNull(((TextComponentImpl) component).warnWhenLegacyFormattingDetected());
  }

  private static String legacy(final char character) {
    return TextComponentImpl.SECTION_CHAR + String.valueOf(character);
  }

  @Test
  void testOfChildren() {
    assertSame(Component.empty(), Component.textOfChildren()); // empty array
    assertEquals(
      Component.text()
        .append(Component.text("a"))
        .append(Component.text().content("b"))
        .build(),
      Component.textOfChildren(
        Component.text("a"),
        Component.text().content("b")
      )
    );
  }

  @Test
  void testOf() {
    final TextComponent component = Component.text("foo");
    assertEquals("foo", component.content());
    assertNull(component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOfSameResult() {
    new EqualsTester()
      .addEqualityGroup(
        Component.text("foo", Style.style(TextColor.color(0x0a1ab9))),
        Component.text("foo", TextColor.color(0x0a1ab9)),
        Component.text("foo", TextColor.color(0x0a1ab9), ImmutableSet.of())
      )
      .addEqualityGroup(
        Component.text("foo", Style.style(TextColor.color(0x0a1ab9), TextDecoration.BOLD)),
        Component.text("foo", TextColor.color(0x0a1ab9), TextDecoration.BOLD),
        Component.text("foo", TextColor.color(0x0a1ab9), ImmutableSet.of(TextDecoration.BOLD))
      )
      .testEquals();
  }

  @Test
  void testOfKnownChar() {
    assertSame(Component.newline(), Component.text('\n'));
    assertSame(Component.space(), Component.text(' '));
  }

  @Test
  void testOf_color() {
    final TextComponent component = Component.text("foo", NamedTextColor.GREEN);
    assertEquals("foo", component.content());
    assertEquals(NamedTextColor.GREEN, component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOf_color_decorations() {
    final TextComponent component = Component.text("foo", NamedTextColor.GREEN, ImmutableSet.of(TextDecoration.BOLD));
    assertEquals("foo", component.content());
    assertEquals(NamedTextColor.GREEN, component.color());
    assertDecorations(component, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
  }

  @Test
  void testMake() {
    final TextComponent component = Component.text(builder -> {
      builder.content("foo");
      builder.color(NamedTextColor.DARK_PURPLE);
    });
    assertEquals("foo", component.content());
    assertEquals(NamedTextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testContains() {
    final Component child = Component.text("kittens");
    final Component component = Component.text()
      .content("cat")
      .append(child)
      .build();
    assertTrue(component.contains(child));
  }

  @Test
  void testContent() {
    final TextComponent c0 = Component.text("foo");
    final TextComponent c1 = c0.content("bar");
    assertEquals("foo", c0.content());
    assertEquals("bar", c1.content());
  }

  @Test
  void testBuildEmptyIsEmpty() {
    assertSame(Component.empty(), Component.text().build());
  }

  @Test
  void testWrapping() {
    final Component italic = Component.text("italic").decorate(TextDecoration.ITALIC);
    final Component notItalic = Component.text("non-italic");
    final Component parent = Component.empty().decoration(TextDecoration.ITALIC, true);

    final Component wrappedItalic = parent.append(italic.applyFallbackStyle(TextDecoration.ITALIC.withState(false)));
    final Component wrappedNotItalic = parent.append(notItalic.applyFallbackStyle(TextDecoration.ITALIC.withState(false)));

    assertEquals(wrappedItalic.compact(), Component.text("italic").decoration(TextDecoration.ITALIC, true));
    assertEquals(wrappedNotItalic.compact(), Component.text("non-italic").decoration(TextDecoration.ITALIC, false));
  }

  @Test
  void testAppendNewline() {
    final Component c0 = Component.text("tuba").appendNewline().append(Component.text("time"));
    final Component c1 = Component.text().content("tuba").appendNewline().append(Component.text("time")).build();

    final Component c0Compact = c0.compact();
    assertEquals(c0Compact, Component.text("tuba\ntime"));
    assertEquals(c0Compact, c1.compact());
  }

  @Test
  void testAppendSpace() {
    final Component c0 = Component.text("tuba").appendSpace().append(Component.text("time"));
    final Component c1 = Component.text().content("tuba").appendSpace().append(Component.text("time")).build();

    final Component c0Compact = c0.compact();
    assertEquals(c0Compact, Component.text("tuba time"));
    assertEquals(c0Compact, c1.compact());
  }
}
