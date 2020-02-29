/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text;

import com.google.common.collect.ImmutableSet;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static net.kyori.text.TextAssertions.assertDecorations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class KeybindComponentTest extends AbstractComponentTest<KeybindComponent, KeybindComponent.Builder> {
  @Override
  KeybindComponent.Builder builder() {
    return KeybindComponent.builder("key.jump");
  }

  @Test
  void testOf() {
    final KeybindComponent component = KeybindComponent.of("key.jump");
    assertEquals("key.jump", component.keybind());
    assertNull(component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOf_color() {
    final KeybindComponent component = KeybindComponent.of("key.jump", TextColor.GREEN);
    assertEquals("key.jump", component.keybind());
    assertEquals(TextColor.GREEN, component.color());
    assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testOf_color_decorations() {
    final KeybindComponent component = KeybindComponent.of("key.jump", TextColor.GREEN, ImmutableSet.of(TextDecoration.BOLD));
    assertEquals("key.jump", component.keybind());
    assertEquals(TextColor.GREEN, component.color());
    assertDecorations(component, ImmutableSet.of(TextDecoration.BOLD), ImmutableSet.of());
  }

  @Test
  void testMake() {
    final KeybindComponent component = KeybindComponent.make(builder -> {
      builder.keybind("key.jump");
      builder.color(TextColor.DARK_PURPLE);
    });
    assertEquals("key.jump", component.keybind());
    assertEquals(TextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testMake_content() {
    final KeybindComponent component = KeybindComponent.make("key.jump", builder -> builder.color(TextColor.DARK_PURPLE));
    assertEquals("key.jump", component.keybind());
    assertEquals(TextColor.DARK_PURPLE, component.color());
  }

  @Test
  void testKeybind() {
    final KeybindComponent c0 = KeybindComponent.of("key.jump");
    final KeybindComponent c1 = c0.keybind("key.up");
    assertEquals("key.jump", c0.keybind());
    assertEquals("key.up", c1.keybind());
  }
}
