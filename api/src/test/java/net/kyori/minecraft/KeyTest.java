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
package net.kyori.minecraft;

import org.junit.jupiter.api.Test;

import static net.kyori.text.TextAssertions.doWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyTest {
  @Test
  void testOf() {
    doWith(Key.of("empty"), key -> {
      assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
      assertEquals("empty", key.value());
    });

    doWith(Key.of("minecraft:empty"), key -> {
      assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
      assertEquals("empty", key.value());
    });

    doWith(Key.of(Key.MINECRAFT_NAMESPACE, "empty"), key -> {
      assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
      assertEquals("empty", key.value());
    });

    doWith(Key.of("realms", "empty"), key -> {
      assertEquals("realms", key.namespace());
      assertEquals("empty", key.value());
    });
  }

  @Test
  void testOfInvalid() {
    assertThrows(Key.KeyParseException.class, () -> Key.of("!"));
    assertThrows(Key.KeyParseException.class, () -> Key.of("Thing:abc"));
    assertThrows(Key.KeyParseException.class, () -> Key.of("abc:Thing"));
    assertThrows(Key.KeyParseException.class, () -> Key.of("a/b:empty"));
  }

  @Test
  void testStringRepresentation() {
    assertEquals("minecraft:empty", Key.of("empty").asString());
    assertEquals("minecraft:empty", Key.of("empty").toString());
  }

  @Test
  void testEquals() {
    assertEquals(Key.of("empty"), Key.of("empty"));
    assertNotEquals(Key.of("stone"), Key.of("air"));
  }

  @Test
  void testHashCode() {
    assertEquals(Key.of("empty").hashCode(), Key.of("empty").hashCode());
    assertNotEquals(Key.of("stone").hashCode(), Key.of("air").hashCode());
  }

  @Test
  void testCompare() {
    assertEquals(-1, Key.of("air").compareTo(Key.of("stone")));
    assertEquals(0, Key.of("empty").compareTo(Key.of("empty")));
    assertEquals(1, Key.of("stone").compareTo(Key.of("air")));
  }

  @Test
  void testTestNamespace() {
    assertTrue(KeyImpl.namespaceValid(Key.MINECRAFT_NAMESPACE));
    assertTrue(KeyImpl.namespaceValid("realms"));
    assertFalse(KeyImpl.namespaceValid("some/path"));
  }

  @Test
  void testTestValue() {
    assertTrue(KeyImpl.valueValid("empty"));
    assertTrue(KeyImpl.valueValid("some/path"));
  }
}
