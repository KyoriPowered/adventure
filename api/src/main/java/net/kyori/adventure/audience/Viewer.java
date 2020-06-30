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
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * A receiver of text-based media.
 */
public interface Viewer {
  /**
   * Applies the given {@code action} to the viewer, and returns an
   * {@link Audience} encapsulating the sub-viewers (if any) which didn't support
   * the action.
   *
   * @param type the type of viewer the action requires
   * @param action the action
   * @param <T> the type of viewer
   * @return a {@link Viewer} of the sub-viewers the action couldn't be applied to
   */
  default <T extends Viewer> @NonNull Audience perform(final @NonNull Class<T> type, final @NonNull Consumer<T> action) {
    requireNonNull(type, "type");
    requireNonNull(action, "action");
    if(type.isInstance(this)) {
      action.accept(type.cast(this));
      return Audience.empty();
    } else {
      return this.asAudience();
    }
  }

  /**
   * Widens this viewer to implement {@link Audience all operations},
   * failing silently with a no-op when a method isn't supported.
   *
   * @return a stub audience
   */
  default @NonNull Audience asAudience() {
    if(this instanceof Audience) {
      return (Audience) this;
    }
    return (ForwardingAudience) () -> this;
  }

  /**
   * A viewer that supports messages.
   */
  interface Messages extends Viewer {
    /**
     * Sends a message.
     *
     * @param message the message
     */
    void sendMessage(final @NonNull Component message);
  }

  /**
   * A viewer that supports action bars.
   */
  interface ActionBars extends Viewer {
    /**
     * Sends a message on the action bar.
     *
     * @param message the message
     */
    void sendActionBar(final @NonNull Component message);
  }

  /**
   * A viewer that supports titles.
   */
  interface Titles extends Viewer {
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
  }

  /**
   * A viewer that supports boss bars.
   */
  interface BossBars extends Viewer {
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
  }

  /**
   * A viewer that supports sounds.
   */
  interface Sounds extends Viewer {
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

  /**
   * A viewer that supports books.
   */
  interface Books extends Viewer {
    /**
     * Opens a book.
     *
     * <p>Opens a virtual book for the client, no item will be persisted.</p>
     *
     * @param book the book
     */
    void openBook(final @NonNull Book book);
  }
}
