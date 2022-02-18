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
package net.kyori.adventure.animation.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BossBarStateTest {

  @Test
  void test() {
    final BossBar bossBar = BossBar.bossBar(Component.empty(), 0.75f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_10, Collections.singleton(BossBar.Flag.DARKEN_SCREEN));
    final BossBar bossBar2 = BossBar.bossBar(Component.text("boss_bar"), 0.5f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

    assertTrue(bossBarEquals(bossBar, BossBarState.state(bossBar).asBossBar()));

    assertFalse(bossBarEquals(bossBar, bossBar2));

    final BossBarState state = BossBarState.state(bossBar);

    assertFalse(bossBarEquals(bossBar, bossBar2));

    state.apply(bossBar2);

    assertTrue(bossBarEquals(bossBar, bossBar2));
  }

  boolean bossBarEquals(BossBar b1, BossBar b2) {
    return b1.name().equals(b2.name()) &&
      b1.progress() == b2.progress() &&
      b1.color().equals(b2.color()) &&
      b1.overlay().equals(b2.overlay()) &&
      b1.flags().equals(b2.flags());
  }

}
