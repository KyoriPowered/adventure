/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.bossbar;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BossBarTest {
  private final BossBar bar = BossBar.of(TextComponent.empty(), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);

  @Test
  void testOfFlags() {
    final BossBar bar = BossBar.of(TextComponent.empty(), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS, ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN));
    assertThat(bar.flags()).containsExactly(BossBar.Flag.DARKEN_SCREEN);
  }

  // ====

  @Test
  void testName() {
    assertEquals(TextComponent.of("A"), this.bar.name(TextComponent.of("A")).name());
    assertEquals(TextComponent.of("B"), this.bar.name(TextComponent.of("B")).name());
    assertEquals(TextComponent.of("B"), this.bar.name(TextComponent.of("B")).name());
  }

  @Test
  void testPercent() {
    assertEquals(0f, this.bar.percent(0f).percent());
    assertEquals(0.1f, this.bar.percent(0.1f).percent());
    assertEquals(0.1f, this.bar.percent(0.1f).percent());
  }

  @Test
  void testPercent_outOfRange() {
    assertThrows(IllegalArgumentException.class, () -> this.bar.percent(-1f));
    assertThrows(IllegalArgumentException.class, () -> this.bar.percent(1.1f));
  }

  @Test
  void testColor() {
    assertEquals(BossBar.Color.PINK, this.bar.color(BossBar.Color.PINK).color());
    assertEquals(BossBar.Color.PURPLE, this.bar.color(BossBar.Color.PURPLE).color());
  }

  @Test
  void testOverlay() {
    assertEquals(BossBar.Overlay.NOTCHED_6, this.bar.overlay(BossBar.Overlay.NOTCHED_6).overlay());
    assertEquals(BossBar.Overlay.PROGRESS, this.bar.overlay(BossBar.Overlay.PROGRESS).overlay());
  }

  @Test
  void testFlags() {
    assertEquals(ImmutableSet.of(), this.bar.flags(ImmutableSet.of()).flags());
    assertEquals(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN), this.bar.flags(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN)).flags());
    assertEquals(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN, BossBar.Flag.CREATE_WORLD_FOG), this.bar.flags(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN)).addFlags(BossBar.Flag.CREATE_WORLD_FOG).flags());
    assertEquals(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN), this.bar.flags(ImmutableSet.of(BossBar.Flag.DARKEN_SCREEN)).addFlags(BossBar.Flag.CREATE_WORLD_FOG).removeFlags(BossBar.Flag.CREATE_WORLD_FOG).flags());
  }
}
