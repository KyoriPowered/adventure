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
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FrameReelGeneratorTest {

  @Test
  void fromReel() {
    assertEquals(FrameReel.reel(1, 2, 3), FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).asFrameReel());

    assertEquals(FrameReel.reel(1, 2, 3), FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).createReel(3));
    assertEquals(FrameReel.reel(1, 2), FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).createReel(2));

    assertEquals(FrameReel.emptyReel(), FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).createReel(0));

    assertThrows(IllegalArgumentException.class, () -> FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).createReel(4));
  }

  @Test
  void literalReel() {
    assertEquals(FrameReelGenerator.fromReel(FrameReel.reel(1, 2, 3)).asInflexible().createReel(3), FrameReelGenerator.literalReel(FrameReel.reel(1, 2, 3)).createReel(3));

    assertEquals(FrameReel.reel(1, 2, 3), FrameReelGenerator.literalReel(FrameReel.reel(1, 2, 3)).createReel(3));
    assertThrows(IllegalArgumentException.class, () -> FrameReelGenerator.literalReel(FrameReel.reel(1, 2, 3)).createReel(2));

    assertThrows(IllegalArgumentException.class, () -> FrameReelGenerator.literalReel(FrameReel.reel(1, 2, 3)).createReel(4));
  }

  @Test
  void fromComponentReel() {
    assertEquals(FrameReel.component(Component.text(1), Component.text(2), Component.text(3)), FrameReelGenerator.fromComponentReel(FrameReel.component(Component.text(1), Component.text(2), Component.text(3))).createReel(3));
    assertEquals(FrameReel.component(Component.text(1), Component.text(2)), FrameReelGenerator.fromComponentReel(FrameReel.component(Component.text(1), Component.text(2), Component.text(3))).createReel(2));
    assertEquals(FrameReel.component(Component.text(1), Component.text(2), Component.text(3), Component.empty()), FrameReelGenerator.fromComponentReel(FrameReel.component(Component.text(1), Component.text(2), Component.text(3))).createReel(4));
    assertEquals(FrameReel.emptyComponent(), FrameReelGenerator.fromComponentReel(FrameReel.component(Component.text(1), Component.text(2), Component.text(3))).createReel(0));
  }

  @Test
  void frameReplicatorWithEmpty() {
    assertEquals(FrameReel.reel('a', 'a', 'a'), FrameReelGenerator.frameReplicator('a', FrameReel.reelFactory()).createReel(3));
    assertEquals(FrameReel.reel('a', 'a'), FrameReelGenerator.frameReplicator('a', FrameReel.reelFactory()).createReel(2));
    assertEquals(FrameReel.reel('a'), FrameReelGenerator.frameReplicator('a', FrameReel.reelFactory()).createReel(1));
    assertEquals(FrameReel.emptyReel(), FrameReelGenerator.frameReplicator('a', FrameReel.reelFactory()).createReel(0));
  }

  @Test
  void frameReplicator() {
    assertEquals(FrameReel.reel('a', 'a', 'a'), FrameReelGenerator.frameReplicator('a').createReel(3));
    assertEquals(FrameReel.reel('a', 'a'), FrameReelGenerator.frameReplicator('a').createReel(2));
    assertEquals(FrameReel.reel('a'), FrameReelGenerator.frameReplicator('a').createReel(1));
    assertEquals(FrameReel.emptyReel(), FrameReelGenerator.frameReplicator('a').createReel(0));
  }

  @Test
  void componentFrameReplicator() {
    assertEquals(FrameReel.component(Component.text("just a frame")).repeatLastFrame(3), FrameReelGenerator.componentFrameReplicator(Component.text("just a frame")).createReel(4));
    assertEquals(FrameReel.component(Component.text("just a frame")), FrameReelGenerator.componentFrameReplicator(Component.text("just a frame")).createReel(1));
    assertEquals(FrameReel.emptyComponent(), FrameReelGenerator.componentFrameReplicator(Component.text("just a frame")).createReel(0));
  }

  @Test
  void reelReplicator() {
    assertEquals(FrameReel.reel(1, 2, 1, 2, 1, 2), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(6));
    assertEquals(FrameReel.reel(1, 2, 1, 2, 1), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(5));
    assertEquals(FrameReel.reel(1, 2, 1, 2), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(4));
    assertEquals(FrameReel.reel(1, 2, 1), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(3));
    assertEquals(FrameReel.reel(1, 2), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(2));
    assertEquals(FrameReel.reel(1), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(1));
    assertEquals(FrameReel.emptyReel(), FrameReelGenerator.reelReplicator(FrameReel.reel(1, 2)).createReel(0));
  }

  @Test
  void gradient() {
    assertEquals(FrameReel.reel(0f, 0.25f, 0.5f, 0.75f, 1f), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(5));
    assertEquals(FrameReel.reel(0f, 1/3f, 2/3f, 1f), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(4));
    assertEquals(FrameReel.reel(0f, 0.5f, 1f), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(3));
    assertEquals(FrameReel.reel(0f, 1f), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(2));
    assertEquals(FrameReel.reel(1f), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(1));
    assertEquals(FrameReel.emptyReel(), FrameReelGenerator.gradient(Function.identity(), FrameReel.reelFactory()).createReel(0));
  }
}
