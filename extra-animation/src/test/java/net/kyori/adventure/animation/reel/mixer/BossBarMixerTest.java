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
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BossBarMixerTest {

  final ComponentReel n1 = FrameReel.component(Component.text("text"), Component.empty());

  final ComponentReel n2 = FrameReel.component(Component.empty(), Component.empty());

  final FrameReel<Float> p1 = FrameReel.reel(1f, 0.5f);

  final FrameReel<Float> p2 = FrameReel.reel(0.25f, 0.5f);

  final FrameReel<BossBar.Color> c1 = FrameReel.reel(BossBar.Color.BLUE, BossBar.Color.RED);

  final FrameReel<BossBar.Color> c2 = FrameReel.reel(BossBar.Color.RED, BossBar.Color.GREEN);

  final FrameReel<BossBar.Overlay> o1 = FrameReel.reel(BossBar.Overlay.PROGRESS, BossBar.Overlay.NOTCHED_6);

  final FrameReel<BossBar.Overlay> o2 = o1.reversed();

  final FrameReel<Collection<BossBar.Flag>> f1 = FrameReel.reel(Arrays.asList(BossBar.Flag.CREATE_WORLD_FOG, BossBar.Flag.PLAY_BOSS_MUSIC), Collections.emptySet());

  final FrameReel<Set<BossBar.Flag>> f2 = FrameReel.reel(Collections.emptySet(), Collections.emptySet());

  final BossBarMixer bm = ReelMixer.bossBar(n1, p1, c1, o1, f1);

  @Test
  void nameReel() {
    assertEquals(n1, bm.nameReel());
  }

  @Test
  void setNameReel() {
    assertEquals(n2, bm.nameReel(n2).nameReel());
  }

  @Test
  void transformNameReel() {
    assertEquals(n2, bm.transformNameReel(p -> n2).nameReel());
  }

  @Test
  void progressReel() {
    assertEquals(p1, bm.progressReel());
  }

  @Test
  void testProgressReel() {
    assertEquals(p2, bm.progressReel(p2).progressReel());
  }

  @Test
  void transformProgressReel() {
    assertEquals(p2, bm.transformProgressReel(p -> p2).progressReel());
  }

  @Test
  void colorReel() {
    assertEquals(c1, bm.colorReel());
  }

  @Test
  void testColorReel() {
    assertEquals(c2, bm.colorReel(c2).colorReel());
  }

  @Test
  void transformColorReel() {
    assertEquals(c2, bm.transformColorReel(c -> c2).colorReel());
  }

  @Test
  void overlayReel() {
    assertEquals(o1, bm.overlayReel());
  }

  @Test
  void testOverlayReel() {
    assertEquals(o2, bm.overlayReel(o2).overlayReel());
  }

  @Test
  void transformOverlayReel() {
    assertEquals(o2, bm.transformOverlayReel(o -> o2).overlayReel());
  }

  @Test
  void flagsReel() {
    assertEquals(FrameReel.reel(f1.frames().stream().map(HashSet::new).collect(Collectors.toList())), bm.flagsReel());
  }

  @Test
  void testFlagsReel() {
    assertEquals(f2, bm.flagsReel(f2).flagsReel());
  }

  @Test
  void transformFlagsReel() {
    assertEquals(f2, bm.transformFlagsReel(f -> f2).flagsReel());
  }
}
