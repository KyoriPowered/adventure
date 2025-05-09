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
package net.kyori.adventure.util;

import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndexTest {
  private static final Index<String, Thing> THINGS = Index.create(Thing.class, thing -> thing.name, Thing.ABC, Thing.DEF);

  @Test
  void testCreateWithNonUniqueKey() {
    assertThrows(IllegalStateException.class, () -> Index.create(NonUniqueThing.class, thing -> thing.name));
  }

  @Test
  void testCreateWithNonUniqueValue() {
    assertThrows(IllegalStateException.class, () -> Index.create(Thing.class, thing -> UUID.randomUUID().toString(), Thing.ABC, Thing.ABC));
  }

  @Test
  void testKey() {
    for (final Thing thing : Thing.values()) {
      if (thing == Thing.NOT_PRESENT) continue;
      assertEquals(thing.name, THINGS.key(thing));
    }
  }

  @Test
  void testValue() {
    for (final Thing thing : Thing.values()) {
      if (thing == Thing.NOT_PRESENT) continue;
      assertEquals(thing, THINGS.value(thing.name));
    }
  }

  @Test
  void testValueOrThrow() {
    assertThrows(NoSuchElementException.class, () -> THINGS.valueOrThrow("__NO_VALUE__"));
  }

  @Test
  void testKeyOrThrow() {
    assertThrows(NoSuchElementException.class, () -> THINGS.keyOrThrow(Thing.NOT_PRESENT));
  }

  @Test
  void testValueOrDefault() {
    assertEquals(Thing.NOT_PRESENT, THINGS.valueOr("__NO_VALUE__", Thing.NOT_PRESENT));
  }

  @Test
  void testKeyOrDefault() {
    assertEquals("__DEFAULT__", THINGS.keyOr(Thing.NOT_PRESENT, "__DEFAULT__"));
  }

  @Test
  void testKeys() {
    assertThat(THINGS.keys()).containsExactly("abc", "def");
  }

  @Test
  void testValues() {
    assertThat(THINGS.values()).containsExactly(Thing.ABC, Thing.DEF);
  }

  private enum Thing {
    ABC("abc"),
    DEF("def"),
    NOT_PRESENT("not_present");

    private final String name;

    Thing(final String name) {
      this.name = name;
    }
  }

  private enum NonUniqueThing {
    ABC("abc"),
    DEF("abc"); // ABC is also "abc"

    private final String name;

    NonUniqueThing(final String name) {
      this.name = name;
    }
  }
}
