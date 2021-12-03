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
package net.kyori.adventure.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TriStateTest {
  @Test
  void testByBoolean() {
    assertEquals(TriState.FALSE, TriState.byBoolean(false));
    assertEquals(TriState.TRUE, TriState.byBoolean(true));
  }

  @Test
  void testByBooleanBoxed() {
    assertEquals(TriState.NOT_SET, TriState.byBoolean(null));
    assertEquals(TriState.FALSE, TriState.byBoolean(Boolean.FALSE));
    assertEquals(TriState.TRUE, TriState.byBoolean(Boolean.TRUE));
  }

  @Test
  void testToBoolean() {
    assertEquals(true, TriState.TRUE.toBoolean());
    assertEquals(false, TriState.FALSE.toBoolean());
    assertNull(TriState.NOT_SET.toBoolean());
  }

  @Test
  void testToBooleanOrElse() {
    assertTrue(TriState.TRUE.toBooleanOrElse(false));
    assertFalse(TriState.FALSE.toBooleanOrElse(true));

    assertTrue(TriState.NOT_SET.toBooleanOrElse(true));
    assertFalse(TriState.NOT_SET.toBooleanOrElse(false));
  }

  @Test
  void testToBooleanOrElseGet() {
    final AtomicInteger atomicCounter = new AtomicInteger(0);
    final Function<Boolean, Boolean> supplierCounter = b -> {
      atomicCounter.incrementAndGet();
      return b;
    };

    assertTrue(TriState.TRUE.toBooleanOrElseGet(() -> supplierCounter.apply(false)));
    assertFalse(TriState.FALSE.toBooleanOrElseGet(() -> supplierCounter.apply(true)));

    assertTrue(TriState.NOT_SET.toBooleanOrElseGet(() -> supplierCounter.apply(true)));
    assertFalse(TriState.NOT_SET.toBooleanOrElseGet(() -> supplierCounter.apply(false)));

    assertEquals(2, atomicCounter.get()); // Ensure that the supplier was only called twice for the two test cases.
  }
}
