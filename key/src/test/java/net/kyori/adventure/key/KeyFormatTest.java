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
package net.kyori.adventure.key;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyFormatTest {
  private static final KeyFormat ALT_SEPARATOR = KeyFormat.keyFormat().separator('>').build();

  @Test
  void testDefaultBuilder() {
    final KeyFormat format = KeyFormat.keyFormat().build();
    assertEquals(Key.MINECRAFT_NAMESPACE, format.namespace());
    assertEquals(Key.DEFAULT_SEPARATOR, format.separator());
  }

  @SuppressWarnings("PatternValidation") // We are testing to ensure this throws an exception.
  @Test
  void testNamespaceBuilder() {
    final KeyFormat format = KeyFormat.namespace("adventure");
    assertEquals("adventure", format.namespace());
    assertEquals(Key.DEFAULT_SEPARATOR, format.separator());
    assertThrows(IllegalArgumentException.class, () -> KeyFormat.namespace("Adventure"));
    assertThrows(IllegalArgumentException.class, () -> KeyFormat.namespace(".."));
  }

  @Test
  void testNamespaceSeparatorBuilder() {
    final KeyFormat format = KeyFormat.keyFormat("adventure", '>');
    assertEquals("adventure", format.namespace());
    assertEquals('>', format.separator());
  }

  @SuppressWarnings("PatternValidation") // We are testing to ensure this throws an exception.
  @Test
  void testKey() {
    assertEquals(Key.key(Key.MINECRAFT_NAMESPACE, "empty"), KeyFormat.minecraft().key("empty"));
    assertThrows(InvalidKeyException.class, () -> KeyFormat.minecraft().key("Empty"));
    assertThrows(InvalidKeyException.class, () -> KeyFormat.minecraft().key("minecraft:empty"));
    assertThrows(InvalidKeyException.class, () -> KeyFormat.minecraft().key(":empty"));
  }

  @Test
  void testParse() {
    assertEquals(Key.key(Key.MINECRAFT_NAMESPACE, "empty"), KeyFormat.minecraft().parse("empty"));
    assertEquals(Key.key("adventure", "empty"), KeyFormat.minecraft().parse("adventure:empty"));
  }

  @Test
  void testParseSeparator() {
    assertEquals(Key.key("adventure", "empty"), ALT_SEPARATOR.parse("adventure>empty"));
    assertEquals(Key.key(Key.MINECRAFT_NAMESPACE, "empty"), ALT_SEPARATOR.parse(">empty"));
  }

  @Test
  void testParseable() {
    assertTrue(KeyFormat.minecraft().parseable("minecraft:empty"));
    assertFalse(KeyFormat.minecraft().parseable("minecraft:Empty"));
    assertTrue(ALT_SEPARATOR.parseable("minecraft>empty"));
    assertFalse(ALT_SEPARATOR.parseable("minecraft:empty"));
  }

  @Test
  void testAsString() {
    final Key key = Key.key("minecraft", "empty");
    assertEquals("minecraft:empty", KeyFormat.minecraft().asString(key));
    assertEquals("minecraft>empty", ALT_SEPARATOR.asString(key));
  }

  @Test
  void testAsMinimalString() {
    assertEquals("empty", KeyFormat.minecraft().asMinimalString(Key.key(Key.MINECRAFT_NAMESPACE, "empty")));
    assertEquals("adventure:empty", KeyFormat.minecraft().asMinimalString(Key.key("adventure", "empty")));
    assertEquals("adventure>empty", ALT_SEPARATOR.asMinimalString(Key.key("adventure", "empty")));
  }
}
