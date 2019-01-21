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

import com.google.common.collect.ImmutableSet;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TranslatableComponentTest extends AbstractComponentTest<TranslatableComponent, TranslatableComponent.Builder> {
  @Override
  TranslatableComponent.Builder builder() {
    return TranslatableComponent.builder("multiplayer.player.left");
  }

  @Test
  void testOf() {
    final TranslatableComponent component = TranslatableComponent.of("multiplayer.player.left");
    assertEquals("multiplayer.player.left", component.key());
    assertNull(component.color());
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(decoration));
    }
  }

  @Test
  void testOf_color() {
    final TranslatableComponent component = TranslatableComponent.of("multiplayer.player.left", TextColor.GREEN);
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(TextColor.GREEN, component.color());
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(decoration));
    }
  }

  @Test
  void testOf_color_decorations() {
    final TranslatableComponent component = TranslatableComponent.of("multiplayer.player.left", TextColor.GREEN, ImmutableSet.of(TextDecoration.BOLD));
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(TextColor.GREEN, component.color());
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.OBFUSCATED));
    assertEquals(TextDecoration.State.TRUE, component.decoration(TextDecoration.BOLD));
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.STRIKETHROUGH));
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.UNDERLINED));
    assertEquals(TextDecoration.State.NOT_SET, component.decoration(TextDecoration.ITALIC));
  }

  @Test
  void testMake() {
    final TranslatableComponent component = TranslatableComponent.make(builder -> {
      builder.key("multiplayer.player.left");
      builder.color(TextColor.DARK_PURPLE);
    });
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(TextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testMake_content() {
    final TranslatableComponent component = TranslatableComponent.make("multiplayer.player.left", builder -> builder.color(TextColor.DARK_PURPLE));
    assertEquals("multiplayer.player.left", component.key());
    assertEquals(TextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testContains() {
    final Component child = TranslatableComponent.of("multiplayer.player.left");
    final Component component = TranslatableComponent.builder()
      .key("multiplayer.player.left")
      .append(child)
      .build();
    assertTrue(component.contains(child));
  }

  @Test
  void testKey() {
    final TranslatableComponent c0 = TranslatableComponent.of("multiplayer.player.left");
    final TranslatableComponent c1 = c0.key("multiplayer.player.joined");
    assertEquals("multiplayer.player.left", c0.key());
    assertEquals("multiplayer.player.joined", c1.key());
  }

  @Test
  void testRebuildWithNoChanges() {
    final TranslatableComponent component = TranslatableComponent.of("multiplayer.player.left", TextColor.RED);
    assertEquals(component, component.toBuilder().build());
  }
}
