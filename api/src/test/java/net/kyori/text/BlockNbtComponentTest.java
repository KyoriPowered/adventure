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

import net.kyori.text.BlockNbtComponent.LocalPos;
import net.kyori.text.BlockNbtComponent.WorldPos;
import net.kyori.text.BlockNbtComponent.WorldPos.Coordinate;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockNbtComponentTest extends AbstractComponentTest<BlockNbtComponent, BlockNbtComponent.Builder> {
  @Override
  BlockNbtComponent.Builder builder() {
    return BlockNbtComponent.builder().nbtPath("abc").absoluteWorldPos(1, 2, 3);
  }

  @Test
  void testOf() {
    final BlockNbtComponent component = BlockNbtComponent.of("abc", LocalPos.of(1.0d, 2.0d, 3.0d));
    assertEquals("abc", component.nbtPath());
    assertEquals(LocalPos.of(1.0d, 2.0d, 3.0d), component.pos());
    assertNull(component.color());
    for(final TextDecoration decoration : TextDecoration.values()) {
      assertEquals(TextDecoration.State.NOT_SET, component.decoration(decoration));
    }
  }

  @Test
  void testNbtPath() {
    final BlockNbtComponent c0 = BlockNbtComponent.of("abc", LocalPos.of(1.0d, 2.0d, 3.0d));
    final BlockNbtComponent c1 = c0.nbtPath("def");
    assertEquals("abc", c0.nbtPath());
    assertEquals("def", c1.nbtPath());
    assertEquals(LocalPos.of(1.0d, 2.0d, 3.0d), c1.pos());
  }

  @Test
  void testPos() {
    final BlockNbtComponent c0 = BlockNbtComponent.of("abc", LocalPos.of(1.0d, 2.0d, 3.0d));
    final BlockNbtComponent c1 = c0.pos(WorldPos.of(Coordinate.absolute(1), Coordinate.relative(2), Coordinate.absolute(3)));
    assertEquals(LocalPos.of(1.0d, 2.0d, 3.0d), c0.pos());
    assertEquals(WorldPos.of(Coordinate.absolute(1), Coordinate.relative(2), Coordinate.absolute(3)), c1.pos());
    assertEquals("abc", c1.nbtPath());

    assertTrue(c0.pos() instanceof LocalPos);
    assertEquals(1, ((LocalPos) c0.pos()).left());
    assertEquals(2, ((LocalPos) c0.pos()).up());
    assertEquals(3, ((LocalPos) c0.pos()).forwards());

    assertTrue(c1.pos() instanceof WorldPos);
    assertEquals(Coordinate.Type.ABSOLUTE, ((WorldPos) c1.pos()).x().type());
    assertEquals(Coordinate.Type.RELATIVE, ((WorldPos) c1.pos()).y().type());
    assertEquals(Coordinate.Type.ABSOLUTE, ((WorldPos) c1.pos()).z().type());
    assertEquals(1, ((WorldPos) c1.pos()).x().value());
    assertEquals(2, ((WorldPos) c1.pos()).y().value());
    assertEquals(3, ((WorldPos) c1.pos()).z().value());
  }

  @Test
  void testRebuildWithNoChanges() {
    final BlockNbtComponent component = BlockNbtComponent.of("abc", LocalPos.of(1.0d, 2.0d, 3.0d));
    assertEquals(component, component.toBuilder().build());
  }
}
