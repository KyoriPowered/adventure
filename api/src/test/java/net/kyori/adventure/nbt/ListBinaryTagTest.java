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
package net.kyori.adventure.nbt;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListBinaryTagTest {
  @Test
  void testCreateEndBuilder() {
    assertThrows(IllegalArgumentException.class, () -> ListBinaryTag.builder(BinaryTagTypes.END));
  }

  @Test
  void testAddEnd() {
    final ListBinaryTag l0 = ListBinaryTag.empty();
    assertThrows(IllegalArgumentException.class, () -> l0.add(EndBinaryTag.get()));
  }

  @Test
  void testAddOtherToEndChangesType() {
    final ListBinaryTag l0 = ListBinaryTag.empty();
    final ListBinaryTag l1 = l0.add(IntBinaryTag.of(13));
    assertEquals(BinaryTagTypes.INT, l1.listType());
  }

  @Test
  void testMismatchedAdd() {
    final ListBinaryTag l0 = ListBinaryTag.of(BinaryTagTypes.BYTE, ImmutableList.of(ByteBinaryTag.of((byte) 0)));
    assertThrows(IllegalArgumentException.class, () -> l0.add(IntBinaryTag.of(1)));
  }

  @Test
  void testSet() {
    final IntBinaryTag i0 = IntBinaryTag.of(0);
    final IntBinaryTag i1 = IntBinaryTag.of(1);
    final IntBinaryTag i2 = IntBinaryTag.of(2);
    final ListBinaryTag l3 = ListBinaryTag.of(BinaryTagTypes.INT, ImmutableList.of(i0, i1, i2));
    final ListBinaryTag l2 = l3.set(1, i0, removed -> assertEquals(i1, removed));
    assertEquals(i0, l2.get(0));
    assertEquals(i0, l2.get(1));
    assertEquals(i2, l2.get(2));
    final ListBinaryTag l1 = l2.set(2, i0, null);
    assertEquals(i0, l1.get(0));
    assertEquals(i0, l1.get(1));
    assertEquals(i0, l1.get(2));

    // ensure original is untouched
    assertEquals(i0, l3.get(0));
    assertEquals(i1, l3.get(1));
    assertEquals(i2, l3.get(2));
  }

  @Test
  void testRemove() {
    final IntBinaryTag i0 = IntBinaryTag.of(0);
    final IntBinaryTag i1 = IntBinaryTag.of(1);
    final IntBinaryTag i2 = IntBinaryTag.of(2);
    final ListBinaryTag l3 = ListBinaryTag.of(BinaryTagTypes.INT, ImmutableList.of(i0, i1, i2));
    final ListBinaryTag l2 = l3.remove(1, removed -> assertEquals(i1, removed));
    assertEquals(2, l2.size());
    assertEquals(i0, l2.get(0));
    assertEquals(i2, l2.get(1));
    final ListBinaryTag l1 = l2.remove(0, null);
    assertEquals(1, l1.size());
    assertEquals(i2, l1.get(0));

    // ensure original is untouched
    assertEquals(i0, l3.get(0));
    assertEquals(i1, l3.get(1));
    assertEquals(i2, l3.get(2));
  }
}
