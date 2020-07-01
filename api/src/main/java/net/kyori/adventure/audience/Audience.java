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
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A receiver of Minecraft media.
 * <p><tt>Audience</tt> is designed to be a universal interface for any player,
 * command sender, console, or otherwise who can receive text, titles,
 * boss bars, and other Minecraft media. It is also designed for a group of
 * receivers such as a team, server, world, or permission.</p>
 * <p>In the past, Minecraft platforms have typically reserved methods such as
 * <tt>showTitle</tt> for a <tt>Player</tt> interface. While this is good
 * textbook object-oriented design, it presents two key drawbacks: 1) there
 * is no abstraction for groups of players, such as a <tt>Server</tt> or a
 * <tt>Team</tt> and 2) it add boilerplate for handling special cases like
 * console or command senders.</p>
 * <p>Consider the use-case of sending a message and title to every player on a
 * server, and also sending a message to console. Without an <tt>Audience</tt>,
 * the code might look like this:</p>
 * <pre>
 *   Server server;
 *   for (Player player : server.getPlayers()) {
 *     player.sendMessage(...);
 *     player.showTitle(...);
 *   }
 *   server.getConsole().sendMessage(...);</pre>
 * <p>Now, if <tt>Server</tt> implemented <tt>Audience</tt>, its unified interface
 * would allow users to easily send media without if-guarding console or
 * iterating through the list of players:</p>
 * <pre>
 *   Server server;
 *   server.sendMessage(...); // Sends a message to players and console
 *   server.showTitle(...); // Shows a title to players, silently ignored by console</pre>
 * <p>When an <tt>Audience</tt> is unable to perform an operation, such as sending
 * a boss bar to console, it will silently fail, without logging. This
 * requirement allows users to easily send media to a group of
 * <tt>Audience</tt>s without checking each for compatibility. The only
 * required implementation is {@link #sendMessage(Component)}, which does not
 * provide a default stub.</p>
 * <p>While the scope of <tt>Audience</tt> may be expanded in the future to support
 * new Minecraft media such as the player list, its interface will remain stateless
 * and any new methods will be stubbed by default.</p>
 *
 * @see ForwardingAudience
 * @since 1.0
 * @version 1.0
 */
public interface Audience {
  /**
   * Gets an audience that does nothing.
   *
   * @since 1.0
   * @return a do-nothing audience
   */
  static @NonNull Audience empty() {
    return EmptyAudience.INSTANCE;
  }

  /**
   * Creates an audience that forwards to many other audiences.
   *
   * @since 1.0
   * @see ForwardingAudience
   * @param audiences an array of audiences, can be empty
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Audience@NonNull... audiences) {
    switch(audiences.length) {
      case 0: return empty();
      case 1: return audiences[0];
      default: return of(Arrays.asList(audiences));
    }
  }

  /**
   * Creates an audience that forwards to many other audiences.
   *
   * <p>The underlying <tt>Iterable</tt> is not copied, therefore any changes
   * made will be reflected in <tt>Audience</tt>.</p>
   *
   * @since 1.0
   * @see ForwardingAudience
   * @param audiences an iterable of audiences, can be empty
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Iterable<? extends Audience> audiences) {
    return (ForwardingAudience) audiences;
  }

  /**
   * Sends a chat message.
   *
   * @since 1.0
   * @see Component
   * @param message a message
   */
  void sendMessage(final @NonNull Component message);

  /**
   * Sends a message on the action bar.
   *
   * @since 1.0
   * @see Component
   * @param message a message
   */
  default void sendActionBar(final @NonNull Component message) {}

  /**
   * Shows a title.
   *
   * @since 1.0
   * @see Title
   * @param title a title
   */
  default void showTitle(final @NonNull Title title) {}

  /**
   * Clears the title, if one is being displayed.
   *
   * @since 1.0
   * @see Title
   */
  default void clearTitle() {}

  /**
   * Resets the title and timings back to their default.
   *
   * @since 1.0
   * @see Title
   */
  default void resetTitle() {}

  /**
   * Shows a boss bar.
   *
   * @since 1.0
   * @see BossBar
   * @param bar a boss bar
   */
  default void showBossBar(final @NonNull BossBar bar) {}

  /**
   * Hides a boss bar.
   *
   * @since 1.0
   * @see BossBar
   * @param bar a boss bar
   */
  default void hideBossBar(final @NonNull BossBar bar) {}

  /**
   * Plays a sound.
   *
   * @since 1.0
   * @see Sound
   * @param sound a sound
   */
  default void playSound(final @NonNull Sound sound) {}

  /**
   * Plays a sound at a location.
   *
   * @since 1.0
   * @see Sound
   * @param sound a sound
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   */
  default void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {}

  /**
   * Stops a sound, or many sounds.
   *
   * @since 1.0
   * @see SoundStop
   * @param stop a sound stop
   */
  default void stopSound(final @NonNull SoundStop stop) {}

  /**
   * Opens a book.
   *
   * <p>When possible, no item should persist after closing the book.</p>
   *
   * @since 1.0
   * @see Book
   * @param book a book
   */
  default void openBook(final @NonNull Book book) {}
}
