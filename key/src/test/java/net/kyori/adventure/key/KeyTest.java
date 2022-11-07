/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyTest {
  @Test
  void testOfValueOnly() {
    final Key key = Key.key("empty");
    assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
    assertEquals("empty", key.value());
  }

  @Test
  void testOfNamespaceAndValue() {
    final Key key = Key.key(Key.MINECRAFT_NAMESPACE, "empty");
    assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
    assertEquals("empty", key.value());
  }

  @Test
  void testOfNamespaceAndValueParsed() {
    final Key key = Key.key(Key.MINECRAFT_NAMESPACE + ":empty");
    assertEquals(Key.MINECRAFT_NAMESPACE, key.namespace());
    assertEquals("empty", key.value());
  }

  @Test
  void testOfInvalid() {
    assertEquals("!", assertThrows(InvalidKeyException.class, () -> Key.key("!")).keyValue());
    assertEquals("Thing", assertThrows(InvalidKeyException.class, () -> Key.key("Thing:abc")).keyNamespace());
    assertEquals("Thing", assertThrows(InvalidKeyException.class, () -> Key.key("abc:Thing")).keyValue());
    assertEquals("a/b", assertThrows(InvalidKeyException.class, () -> Key.key("a/b:empty")).keyNamespace());
  }

  @Test
  void testStringRepresentation() {
    assertEquals("minecraft:empty", Key.key("empty").asString());
    assertEquals("minecraft:empty", Key.key("empty").toString());
  }

  @Test
  void testEquality() {
    new EqualsTester()
      .addEqualityGroup(
        Key.key("minecraft", "air"),
        Key.key("air"),
        Key.key("minecraft:air")
      )
      .addEqualityGroup(
        Key.key("realms", "empty"),
        Key.key("realms:empty")
      )
      .testEquals();
  }

  @Test
  void testCompare() {
    assertTrue(Key.key("air").compareTo(Key.key("stone")) < 0);
    assertEquals(0, Key.key("empty").compareTo(Key.key("empty")));
    assertTrue(Key.key("stone").compareTo(Key.key("air")) > 0);
  }

  @Test
  void testParseable() {
    assertTrue(Key.parseable("minecraft:empty"));
    assertFalse(Key.parseable("minecraft:Empty"));
  }

  @Test
  void testParseableNamespace() {
    assertTrue(Key.parseableNamespace(Key.MINECRAFT_NAMESPACE));
    assertTrue(Key.parseableNamespace("realms"));
    assertFalse(Key.parseableNamespace("some/path"));
  }

  @Test
  void testParseableValue() {
    assertTrue(Key.parseableValue("empty"));
    assertTrue(Key.parseableValue("some/path"));
  }
}
