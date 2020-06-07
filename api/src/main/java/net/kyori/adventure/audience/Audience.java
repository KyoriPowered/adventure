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

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

/**
 * A receiver of text-based messages.
 */
public interface Audience {
  /**
   * Gets an audience that does nothing.
   *
   * @return an audience
   */
  static @NonNull Audience empty() {
    return EmptyAudience.INSTANCE;
  }

  /**
   * Creates an audience that forwards to a collection of audiences.
   *
   * @param audiences the forwarding audiences
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Iterable<Audience> audiences) {
    return (MultiAudience) () -> audiences;
  }

  /**
   * Creates an audience that forwards to an array of audiences.
   *
   * @param audiences the forwarding audiences
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Audience@NonNull... audiences) {
    switch(audiences.length) {
      case 0: return empty();
      case 1: return (ForwardingAudience) () -> audiences[0];
      default: return of(Arrays.asList(audiences));
    }
  }

  /**
   * Sends a message.
   *
   * @param message the message
   */
  void sendMessage(final @NonNull Component message);

  /**
   * Sends a message on the action bar.
   *
   * @param message the message
   */
  void sendActionBar(final @NonNull Component message);

  /**
   * Shows a title.
   *
   * @param title the title
   */
  void showTitle(final @NonNull Title title);

  /**
   * Clears the currently displayed title.
   */
  void clearTitle();

  /**
   * Resets the title, subtitle, fade-in time, stay time, and fade-out time back to "unset".
   */
  void resetTitle();

  /**
   * Shows a bossbar.
   *
   * @param bar the bossbar
   */
  void showBossBar(final @NonNull BossBar bar);

  /**
   * Hides a bossbar.
   *
   * @param bar the bossbar
   */
  void hideBossBar(final @NonNull BossBar bar);

  /**
   * Plays a sound.
   *
   * @param sound the sound
   */
  void playSound(final @NonNull Sound sound);

  /**
   * Plays a sound.
   *
   * @param sound the sound
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
  void playSound(final @NonNull Sound sound, final double x, final double y, final double z);

  /**
   * Stops all sounds.
   *
   * @param stop the stop
   */
  void stopSound(final @NonNull SoundStop stop);
}
