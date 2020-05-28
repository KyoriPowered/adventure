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

import java.util.Arrays;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An audience full of other audiences.
 */
public interface MultiAudience extends Audience {
  /**
   * Creates a multi-audience.
   *
   * @param audiences the audiences
   * @return a multi-audience
   */
  static @NonNull MultiAudience of(final @NonNull Audience@NonNull... audiences) {
    return of(Arrays.asList(audiences));
  }

  /**
   * Creates a multi-audience.
   *
   * @param audiences the audiences
   * @return a multi-audience
   */
  static @NonNull MultiAudience of(final @NonNull Iterable<Audience> audiences) {
    return () -> audiences;
  }

  /**
   * Gets the audiences.
   *
   * @return the audiences
   */
  @NonNull Iterable<? extends Audience> audiences();

  @Override
  default void message(final @NonNull Component message) {
    this.audiences().forEach(audience -> audience.message(message));
  }

  @Override
  default void showBossBar(final @NonNull BossBar bar) {
    this.audiences().forEach(audience -> audience.showBossBar(bar));
  }

  @Override
  default void hideBossBar(final @NonNull BossBar bar) {
    this.audiences().forEach(audience -> audience.hideBossBar(bar));
  }

  @Override
  default void showActionBar(final @NonNull Component message) {
    this.audiences().forEach(audience -> audience.showActionBar(message));
  }

  @Override
  default void playSound(final @NonNull Sound sound) {
    this.audiences().forEach(audience -> audience.playSound(sound));
  }

  @Override
  default void stopSound(final @NonNull SoundStop stop) {
    this.audiences().forEach(audience -> audience.stopSound(stop));
  }

  @Override
  default void showTitle(final @NonNull Title title) {
    this.audiences().forEach(audience -> audience.showTitle(title));
  }

  @Override
  default void clearTitle() {
    this.audiences().forEach(Audience::clearTitle);
  }

  @Override
  default void resetTitle() {
    this.audiences().forEach(Audience::resetTitle);
  }
}
