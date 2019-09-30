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

import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageNbtComponentTest extends AbstractComponentTest<StorageNbtComponent, StorageNbtComponent.Builder> {
  @Override
  StorageNbtComponent.Builder builder() {
    return StorageNbtComponent.builder().nbtPath("abc").storage("def");
  }

  @Test
  void testOf() {
    final StorageNbtComponent component = StorageNbtComponent.of("abc", "def");
    assertEquals("abc", component.nbtPath());
    assertEquals("def", component.storage());
    assertNull(component.color());
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(decoration));
    }
  }

  @Test
  void testNbtPath() {
    final StorageNbtComponent c0 = StorageNbtComponent.of("abc", "def");
    final StorageNbtComponent c1 = c0.nbtPath("ghi");
    assertEquals("abc", c0.nbtPath());
    assertEquals("ghi", c1.nbtPath());
    assertEquals("def", c1.storage());
  }

  @Test
  void testSelector() {
    final StorageNbtComponent c0 = StorageNbtComponent.of("abc", "def:ghi");
    final StorageNbtComponent c1 = c0.storage("ghi:jkl");
    assertEquals("def:ghi", c0.storage());
    assertEquals("ghi:jkl", c1.storage());
    assertEquals("abc", c1.nbtPath());
  }

  @Test
  void testRebuildWithNoChanges() {
    final StorageNbtComponent component = StorageNbtComponent.of("test", "test");
    assertEquals(component, component.toBuilder().build());
  }

  @Test
  void testIdPattern() {
    final Predicate<String> tester = StorageNbtComponentImpl.BuilderImpl.ID_PATTERN.asPredicate();

    assertTrue(tester.test("apple"));
    assertFalse(tester.test("Ronald"));
    assertFalse(tester.test("stone:Ronald"));
    assertFalse(tester.test("Minecraft:apple"));
    assertFalse(tester.test("mine-c/ra.ft:appl.e"));
    assertTrue(tester.test("mine-c_ra.ft:appl.e"));
    assertTrue(tester.test("minecraft:apple"));
  }

}
