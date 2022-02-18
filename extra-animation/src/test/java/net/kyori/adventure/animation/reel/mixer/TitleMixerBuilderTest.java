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
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import net.kyori.adventure.animation.container.reel.mixer.TitleMixer;
import net.kyori.adventure.animation.container.reel.mixer.TitleMixerBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TitleMixerBuilderTest {

  ComponentReel t = FrameReel.component(Component.text("We'll"), Component.text("We'll Be")).repeatLastFrame(2);

  ComponentReel t2 = FrameReel.component(Component.text("You won't"), Component.text("You won't be")).repeatLastFrame(2);

  ComponentReel st = FrameReel.componentReelBuilder().appendEmpties(2).append(Component.text("Right")).append(Component.text("Right Back")).build();

  ComponentReel st2 = FrameReel.componentReelBuilder().appendEmpties(2).append(Component.text("Left")).append(Component.text("Left front")).build();

  FrameReel<Title.Times> ts = FrameReel.reel(
    Title.Times.times(Duration.ofMillis(30), Duration.ofMillis(20), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(50), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(50), Duration.ZERO),
    Title.Times.times(Duration.ZERO, Duration.ofMillis(20), Duration.ofMillis(30))
  );

  FrameReel<Title.Times> ts2 = FrameReel.reel(
    Title.DEFAULT_TIMES,
    Title.DEFAULT_TIMES,
    Title.DEFAULT_TIMES,
    Title.DEFAULT_TIMES
  );

  TitleMixer tm = ReelMixer.title(t, st, ts);

  TitleMixerBuilder tmb = tm.toBuilder();

  @Test
  void targetSize() {
    assertThrows(IllegalArgumentException.class, () -> tmb.targetSize(-1));
  }

  @Test
  void titleReel() {
    assertEquals(t2, tmb.titleReel(t2).build().titleReel());
  }

  @Test
  void titleReelFromGen() {
    assertEquals(FrameReel.component(Component.text(123)).repeatLastFrame(3), tmb.titleReel(FrameReelGenerator.componentFrameReplicator(Component.text(123))).build().titleReel());
  }

  @Test
  void subtitleReel() {
    assertEquals(st2, tmb.subtitleReel(st2).build().subtitleReel());
  }

  @Test
  void subtitleReelFromGen() {
    assertEquals(FrameReel.component(Component.text(123)).repeatLastFrame(3), tmb.subtitleReel(FrameReelGenerator.componentFrameReplicator(Component.text(123))).build().subtitleReel());
  }

  @Test
  void timesReel() {
    assertEquals(ts2, tmb.timesReel(ts2).build().timesReel());
  }

  @Test
  void timesReelFromGen() {
    assertEquals(FrameReel.reel(Title.DEFAULT_TIMES).repeatLastFrame(3), tmb.timesReel(FrameReelGenerator.frameReplicator(Title.DEFAULT_TIMES)).build().timesReel());
  }
}
