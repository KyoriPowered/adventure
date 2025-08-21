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

import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectComponentTest extends AbstractComponentTest<ObjectComponent, ObjectComponent.Builder> {
  @Override
  ObjectComponent.Builder builder() {
    return Component.object().sprite(Key.key("sprite"));
  }

  @Test
  void testSprite() {
    final ObjectComponent c0 = Component.object(Key.key("atlas"), Key.key("sprite"));
    final ObjectComponent c1 = c0.sprite(Key.key("sprite1"));
    assertEquals(Key.key("sprite"), c0.sprite());
    assertEquals(Key.key("sprite1"), c1.sprite());
    assertEquals(Key.key("atlas"), c1.atlas());
  }

  @Test
  void testAtlas() {
    final ObjectComponent c0 = Component.object(Key.key("atlas"), Key.key("sprite"));
    final ObjectComponent c1 = c0.atlas(Key.key("atlas1"));
    assertEquals(Key.key("atlas"), c0.atlas());
    assertEquals(Key.key("atlas1"), c1.atlas());
    assertEquals(Key.key("sprite"), c1.sprite());
  }
}
