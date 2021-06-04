/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.pointer;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class PointersTest {
  @Test
  public void ofPointers() {
    final Pointer<String> pointer = Pointer.pointer(String.class, Key.key("adventure:test"));

    assertEquals(TriState.NOT_SET, Pointers.EMPTY.has(pointer));

    final Pointers p0 = Pointers.builder()
      .addPointer(pointer)
      .build();
    assertEquals(TriState.FALSE, p0.has(pointer));
    assertFalse(p0.get(pointer).isPresent());

    final Pointers p1 = Pointers.builder()
      .addPointerWithFixedValue(pointer, "test")
      .build();
    assertEquals(TriState.TRUE, p1.has(pointer));
    assertTrue(p1.get(pointer).isPresent());
    assertEquals("test", p1.get(pointer).get());
  }
}
