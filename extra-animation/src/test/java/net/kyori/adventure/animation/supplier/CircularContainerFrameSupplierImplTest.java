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
package net.kyori.adventure.animation.supplier;

import net.kyori.adventure.animation.container.reel.FrameReel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircularContainerFrameSupplierImplTest {

  @Test
  void nextFrame() {
    assertThrows(IllegalArgumentException.class, () -> ContainerFrameSupplier.circular(FrameReel.reel(1, 2, 3), -1));

    {
      final ContainerFrameSupplier<Integer, FrameReel<Integer>> cfs = ContainerFrameSupplier.circular(FrameReel.reel(1, 2, 3), 0);

      for (int i = 0; i < 300; i += 3) {
        assertEquals(1, cfs.frame(i));
        assertEquals(2, cfs.frame(i + 1));
        assertEquals(3, cfs.frame(i + 2));
      }
    }
    {
      final ContainerFrameSupplier<Integer, FrameReel<Integer>> cfs = ContainerFrameSupplier.circular(FrameReel.reel(1, 2, 3), 1);

      assertEquals(1, cfs.frame(0));

      for (int i = 1; i < 201; i += 2) {
        assertEquals(2, cfs.frame(i));
        assertEquals(3, cfs.frame(i + 1));
      }
    }
    {
      final ContainerFrameSupplier<Integer, FrameReel<Integer>> cfs = ContainerFrameSupplier.circular(FrameReel.reel(1, 2, 3), 2);

      assertEquals(1, cfs.frame(0));
      assertEquals(2, cfs.frame(1));

      for (int i = 2; i < 102; i++) {
        assertEquals(3, cfs.frame(i));
      }
    }

    assertThrows(IllegalArgumentException.class, () -> ContainerFrameSupplier.circular(FrameReel.reel(1, 2, 3), 3));
  }

}
