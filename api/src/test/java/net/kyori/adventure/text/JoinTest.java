/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JoinTest {
  @Test
  void testJoin() {
    final JoinConfiguration config = JoinConfiguration.separator(Component.space());

    assertEquals(Component.empty(), Component.join(JoinConfiguration.separator(Component.space()), Collections.emptyList()));

    final Component c0 = Component.text("test");
    assertEquals(c0, Component.join(config, c0));

    final Component c1 = Component.join(
      config,
      IntStream.range(0, 3)
        .mapToObj(Component::text)
        .toArray(Component[]::new)
    );
    assertEquals(
      Component.text()
        .append(Component.text(0))
        .append(Component.space())
        .append(Component.text(1))
        .append(Component.space())
        .append(Component.text(2))
        .build(),
      c1
    );
  }

  @Test
  void testJoinWithFinalSeparator() {
    final JoinConfiguration config = JoinConfiguration.separators(Component.space(), Component.text(" and "));

    assertEquals(Component.empty(), Component.join(config, Collections.emptyList()));

    final Component c0 = Component.text("test");
    assertEquals(c0, Component.join(config, c0));

    final Component c1 = Component.join(
      config,
      IntStream.range(0, 3)
        .mapToObj(Component::text)
        .toArray(Component[]::new)
    );
    assertEquals(
      Component.text()
        .append(Component.text(0))
        .append(Component.space())
        .append(Component.text(1))
        .append(Component.text(" and "))
        .append(Component.text(2))
        .build(),
      c1
    );

    final Component c2 = Component.join(
      config,
      Component.text(0),
      Component.text(1)
    );
    assertEquals(
      Component.text()
        .append(Component.text(0))
        .append(Component.text(" and "))
        .append(Component.text(1))
        .build(),
      c2
    );
  }

  @Test
  void testJoinWithPrefixSuffix() {
    final JoinConfiguration config = JoinConfiguration.builder()
      .separator(Component.space())
      .prefix(Component.text("prefix"))
      .suffix(Component.text("suffix"))
      .build();

    assertEquals(
      Component.text()
        .append(Component.text("prefix"))
        .append(Component.text("suffix"))
        .build(),
      Component.join(config, Collections.emptyList())
    );

    final Component c0 = Component.join(
      config,
      IntStream.range(0, 3)
        .mapToObj(Component::text)
        .toArray(Component[]::new)
    );
    assertEquals(
      Component.text()
        .append(Component.text("prefix"))
        .append(Component.text(0))
        .append(Component.space())
        .append(Component.text(1))
        .append(Component.space())
        .append(Component.text(2))
        .append(Component.text("suffix"))
        .build(),
      c0
    );
  }

  @Test
  void testJoinWithConvertor() {
    final JoinConfiguration config = JoinConfiguration.builder()
      .separator(Component.space())
      .convertor(component -> component.asComponent().color(NamedTextColor.RED))
      .build();

    assertEquals(Component.empty(), Component.join(JoinConfiguration.separator(Component.space()), Collections.emptyList()));

    final Component c0 = Component.text("test");
    assertEquals(c0.color(NamedTextColor.RED), Component.join(config, c0));

    final Component c1 = Component.join(
      config,
      IntStream.range(0, 3)
        .mapToObj(Component::text)
        .toArray(Component[]::new)
    );
    assertEquals(
      Component.text()
        .append(Component.text(0, NamedTextColor.RED))
        .append(Component.space())
        .append(Component.text(1, NamedTextColor.RED))
        .append(Component.space())
        .append(Component.text(2, NamedTextColor.RED))
        .build(),
      c1
    );
  }

  @Test
  void testJoinWithNoSeparators() {
    final JoinConfiguration config = JoinConfiguration.noSeparators();

    assertEquals(Component.empty(), Component.join(config, Collections.emptyList()));

    final Component c0 = Component.text("test");
    assertEquals(c0, Component.join(config, c0));

    final Component c1 = Component.join(
      config,
      IntStream.range(0, 3)
        .mapToObj(Component::text)
        .toArray(Component[]::new)
    );
    assertEquals(
      Component.text()
        .append(Component.text(0))
        .append(Component.text(1))
        .append(Component.text(2))
        .build(),
      c1
    );
  }

  @Test
  void testJoinWithSerialComma() {
    final Component comma = Component.text(", ");
    final Component and = Component.text(" and ");
    final Component serialAnd = Component.text(", and ");

    final JoinConfiguration config = JoinConfiguration.builder()
      .separator(comma)
      .lastSeparator(and)
      .lastSeparatorIfSerial(serialAnd)
      .build();

    assertEquals(Component.empty(), Component.join(config, Collections.emptyList()));

    final Component c0 = Component.text("test");
    assertEquals(c0, Component.join(config, c0));

    final Component[] numbers = IntStream.range(0, 3)
      .mapToObj(Component::text)
      .toArray(Component[]::new);

    final Component c1 = Component.join(
      config,
      numbers[0],
      numbers[1]
    );
    assertEquals(
      Component.text()
        .append(numbers[0])
        .append(and)
        .append(numbers[1])
        .build(),
      c1
    );

    final Component c2 = Component.join(config, numbers);
    assertEquals(
      Component.text()
        .append(numbers[0])
        .append(comma)
        .append(numbers[1])
        .append(serialAnd)
        .append(numbers[2])
        .build(),
      c2
    );
  }

  @Test
  final void testWithPredicate() {
    final JoinConfiguration config = JoinConfiguration.builder()
      .predicate(component -> !(component instanceof TestComponentLike))
      .build();

    final ComponentLike[] components = new ComponentLike[] {Component.text("PASS"), new TestComponentLike(), Component.text("PASS")};

    final Component result = Component.join(config, components);
    assertEquals(
      Component.text()
        .append(Component.text("PASS"))
        .append(Component.text("PASS"))
        .build(),
      result
    );
  }

  @Test
  final void testStandardJoinConfigurationsNewLines() {
    final Component result = Component.join(JoinConfiguration.newlines(), Component.text("line 1"), Component.text("line 2"), Component.text("line 3"));
    assertEquals(
      Component.text()
        .append(Component.text("line 1"))
        .append(Component.newline())
        .append(Component.text("line 2"))
        .append(Component.newline())
        .append(Component.text("line 3"))
        .build(),
      result
    );
  }

  @Test
  final void testStandardJoinConfigurationsCommas() {
    final Component result = Component.join(JoinConfiguration.commas(false), Component.text("line 1"), Component.text("line 2"), Component.text("line 3"));
    assertEquals(
      Component.text()
        .append(Component.text("line 1"))
        .append(Component.text(","))
        .append(Component.text("line 2"))
        .append(Component.text(","))
        .append(Component.text("line 3"))
        .build(),
      result
    );
  }

  @Test
  final void testStandardJoinConfigurationsCommasSpaced() {
    final Component result = Component.join(JoinConfiguration.commas(true), Component.text("line 1"), Component.text("line 2"), Component.text("line 3"));
    assertEquals(
      Component.text()
        .append(Component.text("line 1"))
        .append(Component.text(", "))
        .append(Component.text("line 2"))
        .append(Component.text(", "))
        .append(Component.text("line 3"))
        .build(),
      result
    );
  }

  @Test
  final void testStandardJoinConfigurationsArrayLike() {
    final Component result = Component.join(JoinConfiguration.arrayLike(), Component.text("line 1"), Component.text("line 2"), Component.text("line 3"));
    assertEquals(
      Component.text()
        .append(Component.text("["))
        .append(Component.text("line 1"))
        .append(Component.text(", "))
        .append(Component.text("line 2"))
        .append(Component.text(", "))
        .append(Component.text("line 3"))
        .append(Component.text("]"))
        .build(),
      result
    );
  }

  @Test
  final void testJoinWithRootStyle() {
    final Style style = Style.style(NamedTextColor.RED, TextDecoration.BOLD);
    final Style epicStyle = Style.style(NamedTextColor.BLACK, TextDecoration.ITALIC);
    final List<Component> componentsToJoin = new ArrayList<>();
    componentsToJoin.add(Component.text("FIRST"));
    componentsToJoin.add(Component.text("SECOND", style));
    componentsToJoin.add(Component.text("THIRD"));
    final Component result = Component.join(JoinConfiguration.builder().separator(Component.text(",")).parentStyle(epicStyle).build(), componentsToJoin);

    assertEquals(
      Component.text().style(epicStyle)
        .append(Component.text("FIRST"))
        .append(Component.text(","))
        .append(Component.text("SECOND", style))
        .append(Component.text(","))
        .append(Component.text("THIRD"))
        .build(),
      result
    );
  }

  @Test
  final void testJoinWithRootStyleSingleComponent() {
    final List<Component> componentsToJoin = new ArrayList<>();
    componentsToJoin.add(Component.text("FIRST"));
    final Style epicStyle = Style.style(NamedTextColor.BLACK, TextDecoration.ITALIC);
    final Component result = Component.join(JoinConfiguration.builder().separator(Component.text(",")).parentStyle(epicStyle).build(), componentsToJoin);

    assertEquals(
      Component.text().style(epicStyle)
        .append(Component.text("FIRST"))
        .build(),
      result
    );
  }

  @Test
  final void testJoinWithRootStyleNoComponents() {
    final List<Component> componentsToJoin = new ArrayList<>();
    final Style epicStyle = Style.style(NamedTextColor.BLACK, TextDecoration.ITALIC);
    final Component result = Component.join(JoinConfiguration.builder().separator(Component.text(", ")).parentStyle(epicStyle).build(), componentsToJoin);

    assertEquals(
      Component.text().style(epicStyle).build(),
      result);
  }

  private static final class TestComponentLike implements ComponentLike {

    @Override
    public @NotNull Component asComponent() {
      return Component.text("FAIL");
    }
  }
}
