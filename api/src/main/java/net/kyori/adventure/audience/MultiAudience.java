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
package net.kyori.adventure.audience;

import java.util.List;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MultiAudience implements Audience {
  private final List<Audience> audiences;

  public MultiAudience(final List<Audience> audiences) {
    this.audiences = audiences;
  }

  @Override
  public void message(final @NonNull Component message) {
    this.audiences.forEach(audience -> audience.message(message));
  }

  @Override
  public void showBossBar(final @NonNull BossBar bar) {
    this.audiences.forEach(audience -> audience.showBossBar(bar));
  }

  @Override
  public void hideBossBar(final @NonNull BossBar bar) {
    this.audiences.forEach(audience -> audience.hideBossBar(bar));
  }

  @Override
  public void showActionBar(final @NonNull Component message) {
    this.audiences.forEach(audience -> audience.showActionBar(message));
  }

  @Override
  public void playSound(final @NonNull Sound sound) {
    this.audiences.forEach(audience -> audience.playSound(sound));
  }

  @Override
  public void stopSound(final @NonNull SoundStop stop) {
    this.audiences.forEach(audience -> audience.stopSound(stop));
  }
}
