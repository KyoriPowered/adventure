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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link Audience} that provides additional methods for sending MiniMessage strings.
 *
 * @since 4.13.0
 */
public interface MiniMessageAudience extends Audience {

  /**
   * Creates a new {@link MiniMessageAudience} that forwards to the provided {@link Audience}.
   * Uses the {@link MiniMessage} instance provided by {@link MiniMessage#miniMessage()}.
   *
   * @param audience the audience to forward to
   * @return a new MiniMessageAudience
   * @since 4.13.0
   */
  static @NotNull MiniMessageAudience audience(final @NotNull Audience audience) {
    return audience(audience, MiniMessage.miniMessage());
  }

  /**
   * Creates a new {@link MiniMessageAudience} that forwards to the provided {@link Audience}.
   *
   * @param audience the audience to forward to
   * @param mini the MiniMessage instance to use
   * @return a new MiniMessageAudience
   * @since 4.13.0
   */
  static @NotNull MiniMessageAudience audience(final @NotNull Audience audience, final @NotNull MiniMessage mini) {
    return new MiniMessageAudienceImpl(audience, mini);
  }

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendMessage(final @NotNull String message);

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendMessage(final @NotNull String message, final @NotNull TagResolver tagResolver);

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendMessage(final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers);

  /**
   * Sends a MiniMessage string to this audience.
   *
   * @param message the message with MiniMessage strings to send
   * @param bound the bound chat type
   * @since 4.13.0
   */
  void sendMessage(final @NotNull String message, ChatType.@NotNull Bound bound);

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendActionBar(final @NotNull String message);

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendActionBar(final @NotNull String message, final @NotNull TagResolver tagResolver);

  /**
   * Sends a MiniMessage string to this audience as action bar.
   *
   * @param message the action bar with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendActionBar(final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers);

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendPlayerListHeader(final @NotNull String header);

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver tagResolver);

  /**
   * Sends a MiniMessage string to this audience as player list header.
   *
   * @param header the title header with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver @NotNull ... tagResolvers);

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendPlayerListFooter(final @NotNull String footer);

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver tagResolver);

  /**
   * Sends a MiniMessage string to this audience as player list footer.
   *
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver @NotNull ... tagResolvers);

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer);

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver tagResolver);

  /**
   * Sends two MiniMessage strings to this audience as player list header and footer respectively.
   *
   * @param header the title header with MiniMessage strings to send
   * @param footer the title footer with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver @NotNull ... tagResolvers);

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @since 4.13.0
   */
  void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message);

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @param tagResolver the tag resolver to parse additional tags
   * @since 4.13.0
   */
  void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver tagResolver);

  /**
   * Sends a MiniMessage string to this audience as title part.
   *
   * @param part the part of the title to send
   * @param message the content of the title part with MiniMessage strings to send
   * @param tagResolvers the tag resolvers to parse additional tags
   * @since 4.13.0
   */
  void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers);

}
