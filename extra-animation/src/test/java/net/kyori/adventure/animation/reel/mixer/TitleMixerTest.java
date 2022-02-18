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

import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TitleMixerTest {

  ComponentReel t = FrameReel.component(Component.text("We'll"), Component.text("We'll Be")).repeatLastFrame(2);

  ComponentReel st = FrameReel.componentReelBuilder().appendEmpties(2).append(Component.text("Right")).append(Component.text("Right Back")).build();
  
  FrameReel<Title.Times> ts = FrameReel.reel(
    Title.Times.times(Duration.ofMillis(30), Duration.ofMillis(20), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(50), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(50), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(20), Duration.ofMillis(30))
  );
  
  FrameReel<Title> assembled = FrameReel.reel(
    Title.title(t.frames().get(0), st.frames().get(0), ts.frames().get(0)),
    Title.title(t.frames().get(1), st.frames().get(1), ts.frames().get(1)),
    Title.title(t.frames().get(2), st.frames().get(2), ts.frames().get(2)),
    Title.title(t.frames().get(3), st.frames().get(3), ts.frames().get(3))
  );

  @Test
  void timesReel() {
    assertEquals(ts, ReelMixer.title(t, st, ts).timesReel());
  }

  @Test
  void timesReelSet() {
    assertEquals(FrameReel.emptyReel(), ReelMixer.title(t, st, ts).timesReel(FrameReel.emptyReel()).timesReel());
  }

  @Test
  void transformTimesReel() {
    assertEquals(FrameReel.emptyReel(), ReelMixer.title(t, st, ts).transformTimesReel(r -> FrameReel.emptyReel()).timesReel());
  }

  @Test
  void titleReel() {
    assertEquals(t, ReelMixer.title(t, st, ts).titleReel());
  }

  @Test
  void titleReelSet() {
    assertEquals(FrameReel.emptyComponent(), ReelMixer.title(t, st, ts).titleReel(FrameReel.emptyComponent()).titleReel());
  }

  @Test
  void transformTitleReel() {
    assertEquals(FrameReel.emptyComponent(), ReelMixer.title(t, st, ts).transformTitleReel(r -> FrameReel.emptyComponent()).titleReel());
  }

  @Test
  void subtitleReel() {
    assertEquals(st, ReelMixer.title(t, st, ts).subtitleReel());
  }

  @Test
  void testSubtitleReel() {
    assertEquals(FrameReel.emptyComponent(), ReelMixer.title(t, st, ts).subtitleReel(FrameReel.emptyComponent()).subtitleReel());
  }

  @Test
  void transformSubtitleReel() {
    assertEquals(FrameReel.emptyComponent(), ReelMixer.title(t, st, ts).transformSubtitleReel(r -> FrameReel.emptyComponent()).subtitleReel());
  }

  @Test
  void asFrameReel() {
    assertEquals(assembled, ReelMixer.title(t, st, ts).asFrameReel());
  }
}
