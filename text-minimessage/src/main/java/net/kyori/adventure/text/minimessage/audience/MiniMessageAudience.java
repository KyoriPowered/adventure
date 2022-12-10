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
package net.kyori.adventure.text.minimessage.audience;

import java.util.Collections;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ForwardingAudience} that provides additional methods for sending MiniMessage strings.
 *
 * @since 4.13.0
 */
public final class MiniMessageAudience implements ForwardingAudience {

  private final @NotNull Iterable<Audience> delegate;
  private final @NotNull MiniMessage mini;

  private MiniMessageAudience(final @NotNull Audience delegate, final @NotNull MiniMessage mini) {
    this.delegate = Collections.singleton(delegate);
    this.mini = mini;
  }

  /**
   * Creates a new {@link MiniMessageAudience} that forwards to the provided {@link Audience}.
   * Uses the {@link MiniMessage} instance provided by {@link MiniMessage#miniMessage()}.
   *
   * @param audience the audience to forward to
   * @return a new MiniMessageAudience
   * @since 4.13.0
   */
  public static @NotNull MiniMessageAudience audience(final @NotNull Audience audience) {
    return new MiniMessageAudience(audience, MiniMessage.miniMessage());
  }

  /**
   * Creates a new {@link MiniMessageAudience} that forwards to the provided {@link Audience}.
   *
   * @param audience the audience to forward to
   * @param mini the MiniMessage instance to use
   * @return a new MiniMessageAudience
   * @since 4.13.0
   */
  public static @NotNull MiniMessageAudience audience(final @NotNull Audience audience, final @NotNull MiniMessage mini) {
    return new MiniMessageAudience(audience, mini);
  }

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendMessage(final @NotNull String message) {
    this.sendMessage(this.mini.deserialize(message));
  }

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendMessage(final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendMessage(this.mini.deserialize(message, tagResolver));
  }

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendMessage(final @NotNull String message, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendMessage(this.mini.deserialize(message, tagResolvers));
  }

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param bound the bound chat type
   * @since 4.13.0
   */
  public void sendMessage(final @NotNull String message, final ChatType.@NotNull Bound bound) {
    this.sendMessage(this.mini.deserialize(message), bound);
  }

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendActionBar(final @NotNull String message) {
    this.sendActionBar(this.mini.deserialize(message));
  }

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendActionBar(final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendActionBar(this.mini.deserialize(message, tagResolver));
  }

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendActionBar(final @NotNull String message, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendActionBar(this.mini.deserialize(message, tagResolvers));
  }

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendPlayerListHeader(final @NotNull String header) {
    this.sendPlayerListHeader(this.mini.deserialize(header));
  }

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListHeader(this.mini.deserialize(header, tagResolver));
  }

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendPlayerListHeader(this.mini.deserialize(header, tagResolvers));
  }

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendPlayerListFooter(final @NotNull String footer) {
    this.sendPlayerListFooter(this.mini.deserialize(footer));
  }

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListFooter(this.mini.deserialize(footer, tagResolver));
  }

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendPlayerListFooter(this.mini.deserialize(footer, tagResolvers));
  }

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header), this.mini.deserialize(footer));
  }

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header, tagResolver), this.mini.deserialize(footer, tagResolver));
  }

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header, tagResolvers), this.mini.deserialize(footer, tagResolvers));
  }

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @since 4.13.0
   */
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message) {
    this.sendTitlePart(part, this.mini.deserialize(message));
  }

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendTitlePart(part, this.mini.deserialize(message, tagResolver));
  }

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver @NotNull... tagResolvers) {
    this.sendTitlePart(part, this.mini.deserialize(message, tagResolvers));
  }

  @Override
  public @NotNull Iterable<? extends Audience> audiences() {
    return this.delegate;
  }
}
