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

import net.kyori.adventure.bossbar.BossBarContainer;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

/* package */ final class EmptyAudience implements Audience {
  /* package */ static final EmptyAudience INSTANCE = new EmptyAudience();

  @Override
  public void sendMessage(final @NonNull Component message) {
  }

  @Override
  public void sendActionBar(final @NonNull Component message) {
  }

  @Override
  public void showTitle(final @NonNull Title title) {
  }

  @Override
  public void clearTitle() {
  }

  @Override
  public void resetTitle() {
  }

  @Override
  public void showBossBar(final @NonNull BossBarContainer bar) {
  }

  @Override
  public void hideBossBar(final @NonNull BossBarContainer bar) {
  }

  @Override
  public void playSound(final @NonNull Sound sound) {
  }

  @Override
  public void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {
  }

  @Override
  public void stopSound(final @NonNull SoundStop stop) {
  }

  @Override
  public void openBook(final @NonNull Book book) {
  }
}
