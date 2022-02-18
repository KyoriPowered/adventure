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
package net.kyori.adventure.animation.reel;

import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractGenericFrameReelTest {

  @Test
  void reversed() {
    assertEquals(Arrays.asList("a", "b", "c"), FrameReel.reel("c", "b", "a").reversed().frames());
  }

  @Test
  void append() {
    assertEquals(Arrays.asList(Component.text("Mine"), Component.text("craft")), FrameReel.component(Component.text("Mine")).append(Component.text("craft")).frames());
  }

  @Test
  void appendContainer() {
    assertEquals(Arrays.asList(1, 2, 3, 4), FrameReel.reel(1, 2).append(FrameReel.reel(3, 4)).frames());
    assertEquals(Arrays.asList(1, 2, 3, 4), FrameReel.reel(1, 2, 3, 4).append(FrameReel.emptyReel()).frames());
  }

  @Test
  void repeatLastFrame() {
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(true, false).repeatLastFrame(1).frames());
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(true, false, false).repeatLastFrame(0).frames());
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(true, false, false).repeatLastFrame(-10).frames());
  }

  @Test
  void repeatAllFrames() {
    assertEquals(Arrays.asList(true, false, true, false, true, false), FrameReel.reel(true, false).repeatAllFrames(2).frames());
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(true, false, false).repeatAllFrames(0).frames());
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(true, false, false).repeatAllFrames(-10).frames());
  }

  @Test
  void subreel() {
    assertEquals(FrameReel.reel(1, 2), FrameReel.reel(1, 2, 3).subReel(0, 1));
  }

  @Test
  void map() {
    assertEquals(FrameReel.reel(1, 2, 3, 4, 5), FrameReel.reel("1", "2", "3", "4", "5").map(Integer::parseInt, FrameReel.reelFactory()));
  }

}
