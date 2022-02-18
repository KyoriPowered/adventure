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
package net.kyori.adventure.animation.reel.mixer;

import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.mixer.MonoTypeReelMixer;
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericMonoTypeReelMixerTest {

  FrameReel<String> fr1 = FrameReel.reel("frame1", "frame2", "frame3");

  FrameReel<String> fr2 = FrameReel.reel("frame1", "frame2", "frame-three");

  @Test
  void reels() {
    assertEquals(Arrays.asList(fr1, fr2), ReelMixer.mixer(fr1, fr2).reels());
  }

  @Test
  void reelGet() {
    final MonoTypeReelMixer<FrameReel<String>> mixer = ReelMixer.mixer(fr1, fr2);
    assertEquals(mixer.reel(0), fr1);
    assertEquals(mixer.reel(1), fr2);
  }

  @Test
  void reelSet() {
    assertEquals(Collections.singletonList(fr2), ReelMixer.mixer(fr1).reel(fr2, 0).reels());
  }

  @Test
  void transformReel() {
    assertEquals(Collections.singletonList(fr2), ReelMixer.mixer(fr1).transformReel(0, r -> fr2).reels());
  }

  @Test
  void append() {
    assertEquals(Arrays.asList(fr1, fr2), ReelMixer.mixer(fr1).append(fr2).reels());
  }
}
