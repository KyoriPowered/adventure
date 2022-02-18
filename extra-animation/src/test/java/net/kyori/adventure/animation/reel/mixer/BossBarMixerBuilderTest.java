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
import net.kyori.adventure.animation.container.reel.mixer.BossBarMixer;
import net.kyori.adventure.animation.container.reel.mixer.BossBarMixerBuilder;
import net.kyori.adventure.animation.container.reel.mixer.ReelMixer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BossBarMixerBuilderTest {

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

  final BossBarMixerBuilder bmb = bm.toBuilder();

  @Test
  void targetSize() {
    assertThrows(IllegalArgumentException.class, () -> bmb.targetSize(-1));
  }

  @Test
  void nameReel() {
    assertEquals(n2, bmb.nameReel(n2).build().nameReel());
  }

  @Test
  void nameReelFromGenerator() {
    assertEquals(n2.subReel(0, 0).repeatLastFrame(1), bmb.nameReel(FrameReelGenerator.componentFrameReplicator(n2.frames().get(0))).build().nameReel());
  }

  @Test
  void progressReel() {
    assertEquals(p2, bmb.progressReel(p2).build().progressReel());
  }

  @Test
  void progressReelFromGenerator() {
    assertEquals(p2.subReel(0, 0).repeatLastFrame(1), bmb.progressReel(FrameReelGenerator.frameReplicator(p2.frames().get(0))).build().progressReel());
  }

  @Test
  void colorReel() {
    assertEquals(c2, bmb.colorReel(c2).build().colorReel());
  }

  @Test
  void colorReelFromGenerator() {
    assertEquals(c2.subReel(0, 0).repeatLastFrame(1), bmb.colorReel(FrameReelGenerator.frameReplicator(c2.frames().get(0))).build().colorReel());
  }

  @Test
  void overlayReel() {
    assertEquals(o2, bmb.overlayReel(o2).build().overlayReel());
  }

  @Test
  void overlayReelFromGenerator() {
    assertEquals(o2.subReel(0, 0).repeatLastFrame(1), bmb.overlayReel(FrameReelGenerator.frameReplicator(o2.frames().get(0))).build().overlayReel());
  }

  @Test
  void flagsReel() {
    assertEquals(f2, bmb.flagsReel(f2).build().flagsReel());
  }

  @Test
  void flagsReelFromGenerator() {
    assertEquals(f2.subReel(0, 0).repeatLastFrame(1), bmb.flagsReel(FrameReelGenerator.frameReplicator(f2.frames().get(0))).build().flagsReel());
  }
}
