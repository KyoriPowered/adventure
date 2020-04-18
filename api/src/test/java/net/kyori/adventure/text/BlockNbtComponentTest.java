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
package net.kyori.adventure.text;

import com.google.common.collect.ImmutableSet;
import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockNbtComponentTest extends AbstractNbtComponentTest<BlockNbtComponent, BlockNbtComponent.Builder> {
  @Override
  BlockNbtComponent.Builder builder() {
    return BlockNbtComponent.builder().nbtPath("abc").absoluteWorldPos(1, 2, 3);
  }

  @Test
  void testOf() {
    final BlockNbtComponent component = BlockNbtComponent.of("abc", BlockNbtComponent.LocalPos.of(1d, 2d, 3d));
    assertEquals("abc", component.nbtPath());
    assertEquals(BlockNbtComponent.LocalPos.of(1d, 2d, 3d), component.pos());
    assertNull(component.color());
    TextAssertions.assertDecorations(component, ImmutableSet.of(), ImmutableSet.of());
  }

  @Test
  void testPos() {
    final BlockNbtComponent c0 = BlockNbtComponent.of("abc", BlockNbtComponent.LocalPos.of(1d, 2d, 3d));
    final BlockNbtComponent c1 = c0.pos(BlockNbtComponent.WorldPos.of(BlockNbtComponent.WorldPos.Coordinate.absolute(1), BlockNbtComponent.WorldPos.Coordinate.relative(2), BlockNbtComponent.WorldPos.Coordinate.absolute(3)));
    assertEquals(BlockNbtComponent.LocalPos.of(1d, 2d, 3d), c0.pos());
    assertEquals(BlockNbtComponent.WorldPos.of(BlockNbtComponent.WorldPos.Coordinate.absolute(1), BlockNbtComponent.WorldPos.Coordinate.relative(2), BlockNbtComponent.WorldPos.Coordinate.absolute(3)), c1.pos());
    assertEquals("abc", c1.nbtPath());

    assertTrue(c0.pos() instanceof BlockNbtComponent.LocalPos);
    assertEquals(1, ((BlockNbtComponent.LocalPos) c0.pos()).left());
    assertEquals(2, ((BlockNbtComponent.LocalPos) c0.pos()).up());
    assertEquals(3, ((BlockNbtComponent.LocalPos) c0.pos()).forwards());

    assertTrue(c1.pos() instanceof BlockNbtComponent.WorldPos);
    assertEquals(BlockNbtComponent.WorldPos.Coordinate.Type.ABSOLUTE, ((BlockNbtComponent.WorldPos) c1.pos()).x().type());
    assertEquals(BlockNbtComponent.WorldPos.Coordinate.Type.RELATIVE, ((BlockNbtComponent.WorldPos) c1.pos()).y().type());
    assertEquals(BlockNbtComponent.WorldPos.Coordinate.Type.ABSOLUTE, ((BlockNbtComponent.WorldPos) c1.pos()).z().type());
    assertEquals(1, ((BlockNbtComponent.WorldPos) c1.pos()).x().value());
    assertEquals(2, ((BlockNbtComponent.WorldPos) c1.pos()).y().value());
    assertEquals(3, ((BlockNbtComponent.WorldPos) c1.pos()).z().value());
  }

  @Test
  void testPosEquality() {
    new EqualsTester()
      .addEqualityGroup(BlockNbtComponent.LocalPos.of(1d, 2d, 3d))
      .addEqualityGroup(BlockNbtComponent.WorldPos.of(BlockNbtComponent.WorldPos.Coordinate.absolute(1), BlockNbtComponent.WorldPos.Coordinate.absolute(2), BlockNbtComponent.WorldPos.Coordinate.absolute(3)))
      .testEquals();
  }
}
