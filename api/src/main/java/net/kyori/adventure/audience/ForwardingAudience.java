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
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * An audience that delegates to a single viewer.
 */
@FunctionalInterface
public interface ForwardingAudience extends Audience {
  /**
   * Creates a {@link ForwardingAudience} which delegates to {@code viewer}.
   *
   * @param viewer the viewer
   * @return the forwarding audience
   */
  static ForwardingAudience of(final @Nullable Viewer viewer) {
    return new ForwardingAudienceImpl(viewer);
  }

  /**
   * Gets the delegate viewer.
   *
   * @return the viewer, or {@code null} to silently drop
   */
  @Nullable Viewer viewer();

  @Override
  default <T extends Viewer> @NonNull Audience perform(final @NonNull Class<T> type, final @NonNull Consumer<T> action) {
    requireNonNull(type, "type");
    requireNonNull(action, "action");
    final Viewer viewer = this.viewer();
    if(viewer == null) {
      return Audience.empty();
    }
    if(viewer instanceof Audience) {
      final Audience audience = (Audience) viewer;
      return audience.perform(type, action);
    }
    if(type.isInstance(viewer)) {
      action.accept(type.cast(viewer));
      return Audience.empty();
    } else {
      return this;
    }
  }

  @Override
  default void sendMessage(final @NonNull Component message) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Messages) ((Viewer.Messages) viewer).sendMessage(message);
  }

  @Override
  default void sendActionBar(final @NonNull Component message) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.ActionBars) ((Viewer.ActionBars) viewer).sendActionBar(message);
  }

  @Override
  default void showTitle(final @NonNull Title title) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Titles) ((Viewer.Titles) viewer).showTitle(title);
  }

  @Override
  default void clearTitle() {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Titles) ((Viewer.Titles) viewer).clearTitle();
  }

  @Override
  default void resetTitle() {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Titles) ((Viewer.Titles) viewer).resetTitle();
  }

  @Override
  default void showBossBar(final @NonNull BossBar bar) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.BossBars) ((Viewer.BossBars) viewer).showBossBar(bar);
  }

  @Override
  default void hideBossBar(final @NonNull BossBar bar) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.BossBars) ((Viewer.BossBars) viewer).hideBossBar(bar);
  }

  @Override
  default void playSound(final @NonNull Sound sound) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Sounds) ((Viewer.Sounds) viewer).playSound(sound);
  }

  @Override
  default void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Sounds) ((Viewer.Sounds) viewer).playSound(sound, x, y, z);
  }

  @Override
  default void stopSound(final @NonNull SoundStop stop) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Sounds) ((Viewer.Sounds) viewer).stopSound(stop);
  }

  @Override
  default void openBook(final @NonNull Book book) {
    final Viewer viewer = this.viewer();
    if(viewer instanceof Viewer.Books) ((Viewer.Books) viewer).openBook(book);
  }
}

/* package */ final class ForwardingAudienceImpl implements ForwardingAudience {
  private final Viewer viewer;

  /* package */ ForwardingAudienceImpl(final @Nullable Viewer viewer) {
    this.viewer = viewer;
  }

  @Override
  public @Nullable Viewer viewer() {
    return this.viewer;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final ForwardingAudienceImpl that = (ForwardingAudienceImpl) other;
    return Objects.equals(this.viewer, that.viewer);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.viewer);
  }
}
