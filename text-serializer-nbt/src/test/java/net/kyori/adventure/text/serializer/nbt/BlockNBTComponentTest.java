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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.nbt.SerializerTests.testComponent;

final class BlockNBTComponentTest {
  @Test
  void testLocal() {
    final String nbtPath = "abc";

    final double left = 1.23D;
    final double up = 2.0D;
    final double forwards = 3.89D;

    testComponent(
      Component.blockNBT()
        .nbtPath(nbtPath)
        .localPos(left, up, forwards)
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.NBT, nbtPath)
        .putString(ComponentTreeConstants.NBT_BLOCK, "^" + left + " ^" + up + " ^" + forwards)
        .build()
    );
  }

  @Test
  void testAbsoluteWorld() {
    final String nbtPath = "xyz";

    final int x = 4;
    final int y = 5;
    final int z = 6;

    testComponent(
      Component.blockNBT()
        .nbtPath(nbtPath)
        .absoluteWorldPos(x, y, z)
        .interpret(true)
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.NBT, nbtPath)
        .putBoolean(ComponentTreeConstants.NBT_INTERPRET, true)
        .putString(ComponentTreeConstants.NBT_BLOCK, x + " " + y + " " + z)
        .build()
    );
  }

  @Test
  void testRelativeWorld() {
    final String nbtPath = "eeee";

    final int x = 7;
    final int y = 83;
    final int z = 900;

    testComponent(
      Component.blockNBT()
        .nbtPath(nbtPath)
        .relativeWorldPos(x, y, z)
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.NBT, nbtPath)
        .putString(ComponentTreeConstants.NBT_BLOCK, "~" + x + " ~" + y + " ~" + z)
        .build()
    );
  }

  @Test
  void testMixedAbsoluteAndRelative() {
    final String nbtPath = "qwert";

    final int x = 12;
    final int y = 3;
    final int z = 1200;

    testComponent(
      Component.blockNBT()
        .nbtPath(nbtPath)
        .worldPos(
          BlockNBTComponent.WorldPos.Coordinate.absolute(12),
          BlockNBTComponent.WorldPos.Coordinate.relative(3),
          BlockNBTComponent.WorldPos.Coordinate.absolute(1200)
        )
        .build(),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.NBT, nbtPath)
        .putString(ComponentTreeConstants.NBT_BLOCK, x + " ~" + y + " " + z)
        .build()
    );
  }
}
