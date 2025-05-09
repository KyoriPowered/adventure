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
import java.util.Collections;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.adventure.text.TextAssertions.assertDecorations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TranslatableComponentTest extends AbstractComponentTest<TranslatableComponent, TranslatableComponent.Builder> {
  @Override
  TranslatableComponent.Builder builder() {
    return Component.translatable().key("multiplayer.player.left");
  }

  @Test
  void testOf() {
    final TranslatableComponent component = Component.translatable("multiplayer.player.left");
    assertEquals("multiplayer.player.left", component.key());
    assertNull(component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOf_color() {
    final TranslatableComponent component = Component.translatable("multiplayer.player.left", NamedTextColor.GREEN);
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(NamedTextColor.GREEN, component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOf_color_decorations() {
    final TranslatableComponent c0 = Component.translatable("multiplayer.player.left", NamedTextColor.GREEN, TextDecoration.BOLD);
    final TranslatableComponent c1 = Component.translatable("multiplayer.player.left", NamedTextColor.GREEN, ImmutableSet.of(TextDecoration.BOLD));
    assertEquals("multiplayer.player.left", c1.key());
    assertEquals(NamedTextColor.GREEN, c0.color());
    assertEquals(NamedTextColor.GREEN, c1.color());
    assertDecorations(c0, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
    assertDecorations(c1, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
  }

  @Test
  void testMake() {
    final TranslatableComponent component = Component.translatable(builder -> {
      builder.key("multiplayer.player.left");
      builder.color(NamedTextColor.DARK_PURPLE);
    });
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(NamedTextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testMake_content() {
    final TranslatableComponent component = Component.translatable(builder -> builder.key("multiplayer.player.left").color(NamedTextColor.DARK_PURPLE));
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(NamedTextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testContains() {
    final Component child = Component.translatable("multiplayer.player.left");
    final Component component = Component.translatable()
      .key("multiplayer.player.left")
      .append(child)
      .build();
    assertTrue(component.contains(child));
  }

  @Test
  void testKey() {
    final TranslatableComponent c0 = Component.translatable("multiplayer.player.left");
    final TranslatableComponent c1 = c0.key("multiplayer.player.joined");
    assertEquals("multiplayer.player.left", c0.key());
    assertEquals("multiplayer.player.joined", c1.key());
  }

  @Test
  void testArgs_array() {
    final TranslatableComponent c0 = Component.translatable("multiplayer.player.left");
    final Component a0 = Component.text("foo");
    final TranslatableComponent c1 = c0.arguments(a0);
    assertThat(c0.arguments()).isEmpty();
    assertThat(c1.arguments()).containsExactly(TranslationArgument.component(a0)).inOrder();
  }

  @Test
  void testArgs_list() {
    final TranslatableComponent c0 = Component.translatable("multiplayer.player.left");
    final Component a0 = Component.text("foo");
    final TranslatableComponent c1 = c0.arguments(Collections.singletonList(a0));
    assertThat(c0.arguments()).isEmpty();
    assertThat(c1.arguments()).containsExactly(TranslationArgument.component(a0)).inOrder();
  }

  @Test
  void testBuilderArgs_singleBuilder() {
    final TranslatableComponent c0 = Component.translatable()
      .key("multiplayer.player.left")
      .arguments(Component.text().content("kashike"))
      .build();
    assertThat(c0.arguments()).hasSize(1);
    assertThat(c0.arguments()).containsExactly(TranslationArgument.component(Component.text("kashike"))).inOrder();
  }

  @Test
  void testBuilderArgs_singleComponent() {
    final TranslatableComponent c0 = Component.translatable()
      .key("multiplayer.player.left")
      .arguments(Component.text("kashike"))
      .build();
    assertThat(c0.arguments()).hasSize(1);
    assertThat(c0.arguments()).containsExactly(TranslationArgument.component(Component.text("kashike"))).inOrder();
  }

  @Test
  void testBuilderArgs_multiple() {
    final TranslatableComponent c0 = Component.translatable()
      .key("multiplayer.player.left")
      .arguments(
        Component.text().content("kashike"),
        Component.text().content("lucko")
      )
      .build();
    assertThat(c0.arguments()).hasSize(2);
    assertThat(c0.arguments()).containsExactly(
      TranslationArgument.component(Component.text("kashike")),
      TranslationArgument.component(Component.text("lucko"))
    ).inOrder();
  }

  // https://github.com/KyoriPowered/adventure/pull/252
  @Test
  void testBuilderArgs_multipleWithEmpty() {
    final TranslatableComponent c0 = Component.translatable()
      .key("multiplayer.player.joined")
      .arguments(
        Component.empty(),
        Component.text().content("kashike")
      )
      .build();
    assertThat(c0.arguments()).hasSize(2);
    assertThat(c0.arguments()).containsExactly(
      TranslationArgument.component(Component.empty()),
      TranslationArgument.component(Component.text("kashike"))
    ).inOrder();
  }

  @Test
  void testArgs_nonComponent() {
    final TranslatableComponent c0 = Component.translatable(
      "some.key",
      TranslationArgument.numeric(4),
      TranslationArgument.bool(true)
    );
    assertThat(c0.arguments()).hasSize(2);
    assertThat(c0.arguments()).containsExactly(
      TranslationArgument.numeric(4),
      TranslationArgument.bool(true)
    ).inOrder();
  }

  @Test
  void testBuilderArgs_nonComponent() {
    final TranslatableComponent c0 = Component.translatable()
      .key("some.key")
      .arguments(
        TranslationArgument.numeric(4.0f),
        TranslationArgument.bool(true)
      )
      .build();
    assertThat(c0.arguments()).hasSize(2);
    assertThat(c0.arguments()).containsExactly(
      TranslationArgument.numeric(4.0f),
      TranslationArgument.bool(true)
    ).inOrder();
  }

}
