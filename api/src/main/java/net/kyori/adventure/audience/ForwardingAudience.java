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

import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

/**
 * An audience that delegates to another audience.
 */
@FunctionalInterface
public interface ForwardingAudience extends Audience.Everything {
  /**
   * Gets the delegate audience.
   *
   * @return the audience, or {@code null} to silently drop
   */
  @Nullable Audience audience();

  default <T extends Audience> void forward(final @NonNull Class<T> type, final @NonNull Consumer<T> action) {
    final /* @Nullable */ Audience audience = this.audience();
    if(audience != null && type.isInstance(audience)) {
      action.accept(type.cast(audience));
    }
  }

  @Override
  default void sendMessage(final @NonNull Component message) {
    forward(Audience.Message.class, a -> a.sendMessage(message));
  }

  @Override
  default void sendActionBar(final @NonNull Component message) {
    forward(Audience.ActionBar.class, a -> a.sendActionBar(message));
  }

  @Override
  default void showTitle(final @NonNull Title title) {
    forward(Audience.Title.class, a -> a.showTitle(title));
  }

  @Override
  default void clearTitle() {
    forward(Audience.Title.class, Title::clearTitle);
  }

  @Override
  default void resetTitle() {
    forward(Audience.Title.class, Title::resetTitle);
  }

  @Override
  default void showBossBar(final @NonNull BossBar bar) {
    forward(Audience.BossBar.class, a -> a.showBossBar(bar));
  }

  @Override
  default void hideBossBar(final @NonNull BossBar bar) {
    forward(Audience.BossBar.class, a -> a.hideBossBar(bar));
  }

  @Override
  default void playSound(final @NonNull Sound sound) {
    forward(Audience.Sound.class, a -> a.playSound(sound));
  }

  @Override
  default void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {
    forward(Audience.Sound.class, a -> a.playSound(sound, x, y, z));
  }

  @Override
  default void stopSound(final @NonNull SoundStop stop) {
    forward(Audience.Sound.class, a -> a.stopSound(stop));
  }

  @Override
  default void openBook(final @NonNull Book book) {
    forward(Audience.Book.class, a -> a.openBook(book));
  }
}
