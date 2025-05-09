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
package net.kyori.adventure.pointer;

import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class PointersTest {
  @Test
  public void ofPointers() {
    final Pointer<String> pointer = Pointer.pointer(String.class, Key.key("adventure:test"));

    assertFalse(Pointers.empty().supports(pointer));

    final Pointers p0 = Pointers.builder()
      .withStatic(pointer, null)
      .build();
    assertTrue(p0.supports(pointer));
    assertFalse(p0.get(pointer).isPresent());

    final Pointers p1 = Pointers.builder()
      .withStatic(pointer, "test")
      .build();
    assertTrue(p1.supports(pointer));
    assertTrue(p1.get(pointer).isPresent());
    assertEquals("test", p1.get(pointer).get());
    assertEquals("test", p1.get(pointer).get()); // make sure the value doesn't change

    final StringBuilder s = new StringBuilder("test");
    final Supplier<String> supplier = () -> {
      final String result = s.toString();
      s.reverse();
      return result;
    };
    final Pointers p2 = Pointers.builder()
      .withDynamic(pointer, supplier)
      .build();
    assertTrue(p2.supports(pointer));
    assertEquals("test", p2.getOrDefault(pointer, null));
    assertEquals("tset", p2.getOrDefault(pointer, null)); // make sure the value does change
  }

  @Test
  public void ofPointersSuppliers() {
    final Pointer<String> p0 = Pointer.pointer(String.class, Key.key("adventure:test1"));
    final Pointer<String> p1 = Pointer.pointer(String.class, Key.key("adventure:test2"));

    final PointersSupplier<Object> parent = PointersSupplier.builder()
      .resolving(p0, (object) -> "0")
      .build();
    final PointersSupplier<Integer> child = PointersSupplier.<Integer>builder()
      .parent(parent)
      .resolving(p1, (object) -> "1")
      .build();

    assertTrue(child.supports(p0));
    assertTrue(child.supports(p1));

    final Pointers childPointers = child.view(10);
    assertEquals("0", childPointers.get(p0).get());
    assertEquals("1", childPointers.get(p1).get());

    final Pointers rebuilt = childPointers.toBuilder().withStatic(p0, "1").build();
    assertEquals("1", rebuilt.get(p0).get());
    assertEquals("1", rebuilt.get(p1).get());
  }
}
