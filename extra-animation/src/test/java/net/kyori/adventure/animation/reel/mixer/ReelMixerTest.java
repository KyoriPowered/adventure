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
import net.kyori.adventure.animation.container.reel.mixer.BossBarMixer;
import net.kyori.adventure.animation.container.reel.mixer.MonoTypeReelMixerBuilder;
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import net.kyori.adventure.animation.container.reel.mixer.TitleMixer;
import net.kyori.adventure.animation.util.BossBarState;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReelMixerTest {

  FrameReel<String> fr1 = FrameReel.reel("frame1", "frame2", "frame3");

  FrameReel<String> fr2 = FrameReel.reel("frame1", "frame2", "frame-three");

  ComponentReel t = FrameReel.component(Component.text("We'll"), Component.text("We'll Be")).repeatLastFrame(2);

  ComponentReel st = FrameReel.componentReelBuilder().appendEmpties(2).append(Component.text("Right")).append(Component.text("Right Back")).build();

  FrameReel<Title.Times> tr = FrameReel.reel(TitleMixer.DEFAULT_ANIMATION_TIMES).repeatLastFrame(3);

  @Test
  void mixerVarargs() {
    assertEquals(Arrays.asList(fr1, fr2), ReelMixer.mixer(fr1, fr2).reels());
  }

  @Test
  void mixerList() {
    assertEquals(Arrays.asList(fr1, fr1), ReelMixer.mixer(Arrays.asList(fr1, fr1)).reels());
  }

  @Test
  void mixerBuilder() {
    MonoTypeReelMixerBuilder<ComponentReel> builder = ReelMixer.mixerBuilder();
    assertEquals(Collections.singletonList(t), builder.append(t).build().reels());
  }

  @Test
  void titleTitlesSubtitlesTimesFrames() {
    TitleMixer mixer = ReelMixer.title(t, st, FrameReel.reel(Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ZERO)));
    assertEquals(t, mixer.titleReel());
    assertEquals(st, mixer.subtitleReel());
    assertEquals(mixer.timesReel(), FrameReel.reel(Title.Times.times(Duration.ZERO, Duration.ofMillis(500), Duration.ZERO)));
  }

  @Test
  void titleReel() {
    assertEquals(4, t.size());
    assertEquals(4, st.size());
    TitleMixer tm = ReelMixer.title(FrameReel.reel(
      Title.title(t.frames().get(0), st.frames().get(0), TitleMixer.DEFAULT_ANIMATION_TIMES),
      Title.title(t.frames().get(1), st.frames().get(1), TitleMixer.DEFAULT_ANIMATION_TIMES),
      Title.title(t.frames().get(2), st.frames().get(2), TitleMixer.DEFAULT_ANIMATION_TIMES),
      Title.title(t.frames().get(3), st.frames().get(3), TitleMixer.DEFAULT_ANIMATION_TIMES)));
    assertEquals(t, tm.titleReel());
    assertEquals(st, tm.subtitleReel());
    assertEquals(tr, tm.timesReel());
  }

  @Test
  void titleMixerBuilder() {
    TitleMixer tm = ReelMixer.titleMixerBuilder().titleReel(t).subtitleReel(st).build();

    assertEquals(t, tm.titleReel());
    assertEquals(st, tm.subtitleReel());
    assertEquals(tr, tm.timesReel());
  }

  @Test
  void bossBarOfContainers() {
    ComponentReel c1 = FrameReel.component(Component.text("Boss"), Component.text("Bar"));
    FrameReel<Float> c2 = FrameReel.reel(1f, 0.5f);
    FrameReel<BossBar.Color> c3 = FrameReel.reel(BossBar.Color.RED, BossBar.Color.GREEN);
    FrameReel<BossBar.Overlay> c4 = FrameReel.reel(BossBar.Overlay.NOTCHED_12, BossBar.Overlay.PROGRESS);
    FrameReel<Set<BossBar.Flag>> c5 = FrameReel.reel(Stream.of(Arrays.asList(BossBar.Flag.CREATE_WORLD_FOG, BossBar.Flag.PLAY_BOSS_MUSIC), Collections.singletonList(BossBar.Flag.DARKEN_SCREEN)).map(HashSet::new).collect(Collectors.toList()));

    final BossBarMixer mixer = ReelMixer.bossBar(c1, c2, c3, c4, c5);

    assertEquals(c1, mixer.nameReel());
    assertEquals(c2, mixer.progressReel());
    assertEquals(c3, mixer.colorReel());
    assertEquals(c4, mixer.overlayReel());

    final FrameReel<Set<BossBar.Flag>> m5 = mixer.flagsReel();

    assertTrue(c5.frames().containsAll(m5.frames()));
    assertTrue(m5.frames().containsAll(c5.frames()));

    assertEquals(c5, mixer.flagsReel());
  }

  @Test
  void bossBarOfContainer() {
    ComponentReel c1 = FrameReel.component(Component.text("Boss"), Component.text("Bar"));
    FrameReel<Float> c2 = FrameReel.reel(1f, 0.5f);
    FrameReel<BossBar.Color> c3 = FrameReel.reel(BossBar.Color.RED, BossBar.Color.GREEN);
    FrameReel<BossBar.Overlay> c4 = FrameReel.reel(BossBar.Overlay.NOTCHED_12, BossBar.Overlay.PROGRESS);
    FrameReel<Set<BossBar.Flag>> c5 = FrameReel.reel(Stream.of(Arrays.asList(BossBar.Flag.CREATE_WORLD_FOG, BossBar.Flag.PLAY_BOSS_MUSIC), Collections.singletonList(BossBar.Flag.DARKEN_SCREEN)).map(HashSet::new).collect(Collectors.toList()));

    final BossBarMixer mixer = ReelMixer.bossBar(FrameReel.reel(BossBarState.state(
      Component.text("Boss"),
      1f,
      BossBar.Color.RED,
      BossBar.Overlay.NOTCHED_12,
      BossBar.Flag.CREATE_WORLD_FOG, BossBar.Flag.PLAY_BOSS_MUSIC
    ), BossBarState.state(
      Component.text("Bar"),
      0.5f,
      BossBar.Color.GREEN,
      BossBar.Overlay.PROGRESS,
      BossBar.Flag.DARKEN_SCREEN
    )));

    assertEquals(c1, mixer.nameReel());
    assertEquals(c2, mixer.progressReel());
    assertEquals(c3, mixer.colorReel());
    assertEquals(c4, mixer.overlayReel());

    final FrameReel<Set<BossBar.Flag>> m5 = mixer.flagsReel();

    assertTrue(c5.frames().containsAll(m5.frames()));
    assertTrue(m5.frames().containsAll(c5.frames()));

    assertEquals(c5, mixer.flagsReel());
  }

}
