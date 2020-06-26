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
package net.kyori.adventure.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractNBTComponentTest<C extends NBTComponent<C, B> & ScopedComponent<C>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentTest<C, B> {
  @Test
  void testBuildWithInterpret() {
    final C c0 = this.buildOne();
    assertFalse(c0.interpret());
    final C c1 = this.builder().interpret(true).build();
    assertTrue(c1.interpret());
  }

  @Test
  void testInterpret() {
    final C c0 = this.buildOne();
    final C c1 = c0.interpret(true);
    assertFalse(c0.interpret());
    assertTrue(c1.interpret());
  }

  @Test
  void testNbtPath() {
    final C c0 = this.buildOne();
    final C c1 = c0.nbtPath("ghi");
    assertEquals("abc", c0.nbtPath());
    assertEquals("ghi", c1.nbtPath());
    assertEquals(c0, c1.nbtPath(c0.nbtPath()));
  }
}
