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
package net.kyori.adventure.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameMapTest {
  private static final NameMap<Thing> THINGS = NameMap.create(Thing.class, thing -> thing.name);

  @Test
  void testName() {
    for(final Thing thing : Thing.values()) {
      assertEquals(thing.name, THINGS.name(thing));
    }
  }

  @Test
  void testValue() {
    for(final Thing thing : Thing.values()) {
      assertEquals(thing, THINGS.value(thing.name).orElse(null));
    }
  }

  private enum Thing {
    ABC("abc"),
    DEF("def");

    private final String name;

    Thing(final String name) {
      this.name = name;
    }
  }
}
