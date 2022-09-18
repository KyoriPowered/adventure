/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * A receiver of Minecraft media.
 *
 * <p><code>Audience</code> is designed to be a universal interface for any player,
 * command sender, console, or otherwise who can receive text, titles,
 * boss bars, and other Minecraft media. It is also designed for a group of
 * receivers such as a team, server, world, or permission.</p>
 * <p>In the past, Minecraft platforms have typically reserved methods such as
 * <code>showTitle</code> for a <code>Player</code> interface. While this is good
 * textbook object-oriented design, it presents two key drawbacks: 1) there
 * is no abstraction for groups of players, such as a <code>Server</code> or a
 * <code>Team</code> and 2) it add boilerplate for handling special cases like
 * console or command senders.</p>
 * <p>Consider the use-case of sending a message and title to every player on a
 * server, and also sending a message to console. Without an <code>Audience</code>,
 * the code might look like this:</p>
 * <pre>
 *   Server server;
 *   for (Player player : server.getPlayers()) {
 *     player.sendMessage(...);
 *     player.showTitle(...);
 *   }
 *   server.getConsole().sendMessage(...);</pre>
 * <p>Now, if <code>Server</code> implemented <code>Audience</code>, its unified interface
 * would allow users to easily send media without if-guarding console or
 * iterating through the list of players:</p>
 * <pre>
 *   Server server;
 *   server.sendMessage(...); // Sends a message to players and console
 *   server.showTitle(...); // Shows a title to players, silently ignored by console</pre>
 * <p>When an <code>Audience</code> is unable to perform an operation, such as sending
 * a boss bar to console, it will silently fail, without logging. This
 * requirement allows users to easily send media to a group of
 * <code>Audience</code>s without checking each for compatibility.</p>
 * <p>While the scope of <code>Audience</code> may be expanded in the future to support
 * new Minecraft media such as the player list, its interface will remain stateless
 * and any new methods will be stubbed by default.</p>
 *
 * @see ForwardingAudience
 * @since 4.0.0
 */
public interface Audience extends Pointered {
  /**
   * Gets an audience that does nothing.
   *
   * @return a do-nothing audience
   * @since 4.0.0
   */
  static @NotNull Audience empty() {
    return EmptyAudience.INSTANCE;
  }

  /**
   * Creates an audience that forwards to many other audiences.
   *
   * @param audiences an array of audiences, can be empty
   * @return an audience
   * @see ForwardingAudience
   * @since 4.0.0
   */
  static @NotNull Audience audience(final @NotNull Audience@NotNull... audiences) {
    final int length = audiences.length;
    if (length == 0) {
      return empty();
    } else if (length == 1) {
      return audiences[0];
    }
    return audience(Arrays.asList(audiences));
  }

  /**
   * Creates an audience that forwards to many other audiences.
   *
   * <p>The underlying <code>Iterable</code> is not copied, therefore any changes
   * made will be reflected in <code>Audience</code>.</p>
   *
   * @param audiences an iterable of audiences, can be empty
   * @return an audience
   * @see ForwardingAudience
   * @since 4.0.0
   */
  static @NotNull ForwardingAudience audience(final @NotNull Iterable<? extends Audience> audiences) {
    return () -> audiences;
  }

  /**
   * Provides a collector to create a forwarding audience from a stream of audiences.
   *
   * <p>The audience produced is immutable and can be reused as desired.</p>
   *
   * @return a collector to create a forwarding audience
   * @since 4.0.0
   */
  static @NotNull Collector<? super Audience, ?, ForwardingAudience> toAudience() {
    return Audiences.COLLECTOR;
  }

  /**
   * Filters this audience.
   *
   * <p>The returned {@code Audience} may be the same, or a completely different one.</p>
   *
   * <p>Container audiences such as {@link ForwardingAudience} may or may not have their own identity.
   * If they do, they <em>may</em> test themselves against the provided {@code filter} first, and if the test fails return an empty audience skipping any contained children.
   * If they do not, they <em>must not</em> test themselves against the filter, only testing their children.</p>
   *
   * @param filter a filter that determines if an audience should be included
   * @return an audience providing a snapshot of all audiences that match the predicate when this method is invoked
   * @since 4.9.0
   */
  default @NotNull Audience filterAudience(final @NotNull Predicate<? super Audience> filter) {
    return filter.test(this)
      ? this
      : empty();
  }

  /**
   * Executes an action against all audiences.
   *
   * <p>If you implement {@code Audience} and not {@link ForwardingAudience} in your own code, and your audience forwards to
   * other audiences, then you <b>must</b> override this method and provide each audience to {@code action}.</p>
   *
   * <p>If an implementation of {@code Audience} has its own identity distinct from its contained children, it <em>may</em> test
   * itself against the provided {@code filter} first, and  if the test fails return an empty audience skipping any contained children.
   * If it does not, it <em>must not</em> test itself against the filter, only testing its children.</p>
   *
   * @param action the action
   * @since 4.9.0
   */
  default void forEachAudience(final @NotNull Consumer<? super Audience> action) {
    action.accept(this);
  }

  /**
   * Sends a chat message with a {@link Identity#nil() nil} identity to this {@link Audience}.
   *
   * @param message a message
   * @see Component
   * @see #sendMessage(Identified, ComponentLike)
   * @see #sendMessage(Identity, ComponentLike)
   * @since 4.1.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull ComponentLike message) {
    this.sendMessage(Identity.nil(), message);
  }

  /**
   * Sends a chat message from the given {@link Identified} to this {@link Audience}.
   *
   * @param source the source of the message
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identified source, final @NotNull ComponentLike message) {
    this.sendMessage(source, message.asComponent());
  }

  /**
   * Sends a chat message from the entity represented by the given {@link Identity} (or the game using {@link Identity#nil()}) to this {@link Audience}.
   *
   * @param source the identity of the source of the message
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identity source, final @NotNull ComponentLike message) {
    this.sendMessage(source, message.asComponent());
  }

  /**
   * Sends a chat message with a {@link Identity#nil() nil} identity to this {@link Audience}.
   *
   * @param message a message
   * @see Component
   * @see #sendMessage(Identified, Component)
   * @see #sendMessage(Identity, Component)
   * @since 4.1.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Component message) {
    this.sendMessage(Identity.nil(), message);
  }

  /**
   * Sends a chat message from the given {@link Identified} to this {@link Audience}.
   *
   * @param source the source of the message
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identified source, final @NotNull Component message) {
    this.sendMessage(source, message, MessageType.SYSTEM);
  }

  /**
   * Sends a chat message from the entity represented by the given {@link Identity} (or the game using {@link Identity#nil()}) to this {@link Audience}.
   *
   * @param source the identity of the source of the message
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identity source, final @NotNull Component message) {
    this.sendMessage(source, message, MessageType.SYSTEM);
  }

  /**
   * Sends a chat message with a {@link Identity#nil() nil} identity to this {@link Audience}.
   *
   * @param message a message
   * @param type the type
   * @see Component
   * @see #sendMessage(Identified, ComponentLike, MessageType)
   * @see #sendMessage(Identity, ComponentLike, MessageType)
   * @since 4.1.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull ComponentLike message, final @NotNull MessageType type) {
    this.sendMessage(Identity.nil(), message, type);
  }

  /**
   * Sends a chat message from the given {@link Identified} to this {@link Audience}.
   *
   * @param source the source of the message
   * @param message a message
   * @param type the type
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identified source, final @NotNull ComponentLike message, final @NotNull MessageType type) {
    this.sendMessage(source, message.asComponent(), type);
  }

  /**
   * Sends a chat message from the entity represented by the given {@link Identity} (or the game using {@link Identity#nil()}) to this {@link Audience}.
   *
   * @param source the identity of the source of the message
   * @param message a message
   * @param type the type
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Identity source, final @NotNull ComponentLike message, final @NotNull MessageType type) {
    this.sendMessage(source, message.asComponent(), type);
  }

  /**
   * Sends a chat message with a {@link Identity#nil() nil} identity to this {@link Audience}.
   *
   * @param message a message
   * @param type the type
   * @see Component
   * @see #sendMessage(Identified, Component, MessageType)
   * @see #sendMessage(Identity, Component, MessageType)
   * @since 4.1.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendMessage(final @NotNull Component message, final @NotNull MessageType type) {
    this.sendMessage(Identity.nil(), message, type);
  }

  /**
   * Sends a chat message.
   *
   * @param source the source of the message
   * @param message a message
   * @param type the type
   * @see Component
   * @since 4.0.0
   */
  default void sendMessage(final @NotNull Identified source, final @NotNull Component message, final @NotNull MessageType type) {
    this.sendMessage(source.identity(), message, type);
  }

  /**
   * Sends a chat message.
   *
   * @param source the identity of the source of the message
   * @param message a message
   * @param type the type
   * @see Component
   * @since 4.0.0
   */
  default void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
  }

  /**
   * Sends a message on the action bar.
   *
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendActionBar(final @NotNull ComponentLike message) {
    this.sendActionBar(message.asComponent());
  }

  /**
   * Sends a message on the action bar.
   *
   * @param message a message
   * @see Component
   * @since 4.0.0
   */
  default void sendActionBar(final @NotNull Component message) {
  }

  /**
   * Sends the player list header.
   *
   * <p>Depending on the implementation of this {@code Audience}, an existing footer may be displayed. If you wish
   * to set both the header and the footer, please use {@link #sendPlayerListHeaderAndFooter(ComponentLike, ComponentLike)}.</p>
   *
   * @param header the header
   * @since 4.3.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendPlayerListHeader(final @NotNull ComponentLike header) {
    this.sendPlayerListHeader(header.asComponent());
  }

  /**
   * Sends the player list header.
   *
   * <p>Depending on the implementation of this {@code Audience}, an existing footer may be displayed. If you wish
   * to set both the header and the footer, please use {@link #sendPlayerListHeaderAndFooter(Component, Component)}.</p>
   *
   * @param header the header
   * @since 4.3.0
   */
  default void sendPlayerListHeader(final @NotNull Component header) {
    this.sendPlayerListHeaderAndFooter(header, Component.empty());
  }

  /**
   * Sends the player list footer.
   *
   * <p>Depending on the implementation of this {@code Audience}, an existing footer may be displayed. If you wish
   * to set both the header and the footer, please use {@link #sendPlayerListHeaderAndFooter(ComponentLike, ComponentLike)}.</p>
   *
   * @param footer the footer
   * @since 4.3.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendPlayerListFooter(final @NotNull ComponentLike footer) {
    this.sendPlayerListFooter(footer.asComponent());
  }

  /**
   * Sends the player list footer.
   *
   * <p>Depending on the implementation of this {@code Audience}, an existing footer may be displayed. If you wish
   * to set both the header and the footer, please use {@link #sendPlayerListHeaderAndFooter(Component, Component)}.</p>
   *
   * @param footer the footer
   * @since 4.3.0
   */
  default void sendPlayerListFooter(final @NotNull Component footer) {
    this.sendPlayerListHeaderAndFooter(Component.empty(), footer);
  }

  /**
   * Sends the player list header and footer.
   *
   * @param header the header
   * @param footer the footer
   * @since 4.3.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void sendPlayerListHeaderAndFooter(final @NotNull ComponentLike header, final @NotNull ComponentLike footer) {
    this.sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
  }

  /**
   * Sends the player list header and footer.
   *
   * @param header the header
   * @param footer the footer
   * @since 4.3.0
   */
  default void sendPlayerListHeaderAndFooter(final @NotNull Component header, final @NotNull Component footer) {
  }

  /**
   * Shows a title.
   *
   * @param title a title
   * @see Title
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void showTitle(final @NotNull Title title) {
    final Title.Times times = title.times();
    if (times != null) this.sendTitlePart(TitlePart.TIMES, times);
    
    this.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
    this.sendTitlePart(TitlePart.TITLE, title.title());
  }

  /**
   * Shows a part of a title.
   *
   * @param part the part
   * @param value the value
   * @param <T> the type of the value of the part
   * @throws IllegalArgumentException if a title part that is not in {@link TitlePart} is used
   * @since 4.9.0
   */
  default <T> void sendTitlePart(final @NotNull TitlePart<T> part, final @NotNull T value) {
  }

  /**
   * Clears the title, if one is being displayed.
   *
   * @see Title
   * @since 4.0.0
   */
  default void clearTitle() {
  }

  /**
   * Resets the title and timings back to their default.
   *
   * @see Title
   * @since 4.0.0
   */
  default void resetTitle() {
  }

  /**
   * Shows a boss bar.
   *
   * @param bar a boss bar
   * @see BossBar
   * @since 4.0.0
   */
  default void showBossBar(final @NotNull BossBar bar) {
  }

  /**
   * Hides a boss bar.
   *
   * @param bar a boss bar
   * @see BossBar
   * @since 4.0.0
   */
  default void hideBossBar(final @NotNull BossBar bar) {
  }

  /**
   * Check if this audience has been shown this BossBar
   * @param bar BossBar
   * @return boolean
   * @since 4.12.0
   */
  default boolean hasBossBar(final @NotNull BossBar bar) {
    return false;
  }

  /**
   * Returns a set containing all BossBars this Audience has been shown
   * @return An unmodifiable set of BossBars
   * @since 4.12.0
   */
  default @NotNull @UnmodifiableView Set<BossBar> getBossBars() {
    return Collections.emptySet();
  }

  /**
   * Plays a sound at the location of the recipient of the sound.
   *
   * <p>To play a sound that follows the recipient, use {@link #playSound(Sound, Sound.Emitter)} with {@link Sound.Emitter#self()}.</p>
   *
   * @param sound a sound
   * @see Sound
   * @since 4.0.0
   */
  default void playSound(final @NotNull Sound sound) {
  }

  /**
   * Plays a sound at a location.
   *
   * @param sound a sound
   * @param x x coordinate
   * @param y y coordinate
   * @param z z coordinate
   * @see Sound
   * @since 4.0.0
   */
  default void playSound(final @NotNull Sound sound, final double x, final double y, final double z) {
  }

  /**
   * Plays a sound from an emitter, usually an entity.
   *
   * <p>
   *   Sounds played using this method will follow the emitter unless the sound is a custom sound.
   *   In this case the sound will be played at the location of the emitter and will not follow them.
   * </p>
   *
   * <p>To play a sound that follows the recipient, use {@link Sound.Emitter#self()}.</p>
   *
   * <p><b>Note</b>: Due to <a href="https://bugs.mojang.com/browse/MC-138832">MC-138832</a>, the volume and pitch may be ignored when using this method.</p>
   *
   * @param sound a sound
   * @param emitter an emitter
   * @since 4.8.0
   */
  default void playSound(final @NotNull Sound sound, final Sound.@NotNull Emitter emitter) {
  }

  /**
   * Stops a sound.
   *
   * @param sound the sound
   * @since 4.8.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void stopSound(final @NotNull Sound sound) {
    this.stopSound(Objects.requireNonNull(sound, "sound").asStop());
  }

  /**
   * Stops a sound, or many sounds.
   *
   * @param stop a sound stop
   * @see SoundStop
   * @since 4.0.0
   */
  default void stopSound(final @NotNull SoundStop stop) {
  }

  /**
   * Opens a book.
   *
   * <p>When possible, no item should persist after closing the book.</p>
   *
   * @param book a book
   * @see Book
   * @since 4.0.0
   */
  @ForwardingAudienceOverrideNotRequired
  default void openBook(final Book.@NotNull Builder book) {
    this.openBook(book.build());
  }

  /**
   * Opens a book.
   *
   * <p>When possible, no item should persist after closing the book.</p>
   *
   * @param book a book
   * @see Book
   * @since 4.0.0
   */
  default void openBook(final @NotNull Book book) {
  }
}
