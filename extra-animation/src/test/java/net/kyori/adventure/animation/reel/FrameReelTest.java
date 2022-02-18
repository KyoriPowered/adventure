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
import net.kyori.adventure.animation.container.reel.FrameReelBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameReelTest {

  @Test
  void testReelVarargs() {
    assertArrayEquals(new Integer[]{1, 2, 3}, FrameReel.reel(1, 2, 3).frames().toArray(new Integer[0]));
  }

  @Test
  void testReelList() {
    assertEquals(Arrays.asList('a', 'b', 'c'), FrameReel.reel(Arrays.asList('a', 'b', 'c')).frames());
  }

  @Test
  void testReelFrameContainer() {
    assertEquals(Collections.singletonList(Title.title(Component.text("I love"), Component.text("Minecraft"))), FrameReel.reel(FrameReel.reel(Title.title(Component.text("I love"), Component.text("Minecraft")))).frames());
  }

  @Test
  void testReelSubContainers() {
    assertEquals(Arrays.asList(true, false, false), FrameReel.reel(FrameReel.reel(true), FrameReel.reel(false), FrameReel.reel(false)).frames());
  }

  @Test
  void emptyReel() {
    assertEquals(0, FrameReel.emptyReel().frames().size());
  }

  @Test
  void reelBuilder() {
    final FrameReelBuilder<String> builder = FrameReel.reelBuilder();
    assertEquals(Arrays.asList("a1!", "b2@", "c3#"), builder.append("a1!").append("b2@").append("c3#").build().frames());
  }

  @Test
  void testComponentVarargs() {
    assertArrayEquals(new Component[]{Component.text("a"), Component.text("b")}, FrameReel.component(Component.text("a"), Component.text("b")).frames().toArray(new Component[0]));
  }

  @Test
  void testComponentList() {
    assertEquals(Collections.singletonList(Component.empty()), FrameReel.component(Collections.singletonList(Component.empty())).frames());
  }

  @Test
  void testComponentFrameContainer() {
    assertEquals(Arrays.asList(Component.text(1), Component.text(2)), FrameReel.component(FrameReel.reel(Component.text(1), Component.text(2))).frames());
  }

  @Test
  void testComponentSubContainers() {
    assertEquals(Arrays.asList(Component.text("!"), Component.text("@"), Component.text("#")), FrameReel.component(FrameReel.reel(Component.text("!")), FrameReel.component(Component.text("@")), FrameReel.component(Component.text("#"))).frames());
  }

  @Test
  void emptyComponent() {
    assertEquals(0, FrameReel.emptyComponent().frames().size());
  }

  @Test
  void componentBuilder() {
    assertEquals(Arrays.asList(Component.text('/'), Component.text(1)), FrameReel.componentReelBuilder().append(Component.text('/')).append(Component.text(1)).build().frames());
  }
}
