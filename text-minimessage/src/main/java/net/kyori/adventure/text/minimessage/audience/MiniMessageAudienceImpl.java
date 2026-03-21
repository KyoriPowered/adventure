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
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

final class MiniMessageAudienceImpl implements MiniMessageAudience {

  private final @NotNull Audience delegate;
  private final @NotNull MiniMessage mini;

  MiniMessageAudienceImpl(final @NotNull Audience delegate, final @NotNull MiniMessage mini) {
    this.delegate = delegate;
    this.mini = mini;
  }

  @Override
  public void sendMessage(final @NotNull String message) {
    this.sendMessage(this.mini.deserialize(message));
  }

  @Override
  public void sendMessage(final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendMessage(this.mini.deserialize(message, tagResolver));
  }

  @Override
  public void sendMessage(final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendMessage(this.mini.deserialize(message, tagResolvers));
  }

  @Override
  public void sendMessage(final @NotNull String message, final ChatType.@NotNull Bound bound) {
    this.sendMessage(this.mini.deserialize(message), bound);
  }

  @Override
  public void sendActionBar(final @NotNull String message) {
    this.sendActionBar(this.mini.deserialize(message));
  }

  @Override
  public void sendActionBar(final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendActionBar(this.mini.deserialize(message, tagResolver));
  }

  @Override
  public void sendActionBar(final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendActionBar(this.mini.deserialize(message, tagResolvers));
  }

  @Override
  public void sendPlayerListHeader(final @NotNull String header) {
    this.sendPlayerListHeader(this.mini.deserialize(header));
  }

  @Override
  public void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListHeader(this.mini.deserialize(header, tagResolver));
  }

  @Override
  public void sendPlayerListHeader(final @NotNull String header, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendPlayerListHeader(this.mini.deserialize(header, tagResolvers));
  }

  @Override
  public void sendPlayerListFooter(final @NotNull String footer) {
    this.sendPlayerListFooter(this.mini.deserialize(footer));
  }

  @Override
  public void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListFooter(this.mini.deserialize(footer, tagResolver));
  }

  @Override
  public void sendPlayerListFooter(final @NotNull String footer, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendPlayerListFooter(this.mini.deserialize(footer, tagResolvers));
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header), this.mini.deserialize(footer));
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver tagResolver) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header, tagResolver), this.mini.deserialize(footer, tagResolver));
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull String header, final @NotNull String footer, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendPlayerListHeaderAndFooter(this.mini.deserialize(header, tagResolvers), this.mini.deserialize(footer, tagResolvers));
  }

  @Override
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message) {
    this.sendTitlePart(part, this.mini.deserialize(message));
  }

  @Override
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver tagResolver) {
    this.sendTitlePart(part, this.mini.deserialize(message, tagResolver));
  }

  @Override
  public void sendTitlePart(final @NotNull TitlePart<Component> part, final @NotNull String message, final @NotNull TagResolver @NotNull ... tagResolvers) {
    this.sendTitlePart(part, this.mini.deserialize(message, tagResolvers));
  }

  //delegate methods

  @Override
  @Deprecated
  public void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
    this.delegate.sendMessage(source, message, type);
  }

  @Override
  public void deleteMessage(final SignedMessage.@NotNull Signature signature) {
    this.delegate.deleteMessage(signature);
  }

  @Override
  public void sendActionBar(final @NotNull Component message) {
    this.delegate.sendActionBar(message);
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull Component header, final @NotNull Component footer) {
    this.delegate.sendPlayerListHeaderAndFooter(header, footer);
  }

  @Override
  public <T> void sendTitlePart(final @NotNull TitlePart<T> part, final @NotNull T value) {
    this.delegate.sendTitlePart(part, value);
  }

  @Override
  public void clearTitle() {
    this.delegate.clearTitle();
  }

  @Override
  public void resetTitle() {
    this.delegate.resetTitle();
  }

  @Override
  public void showBossBar(final @NotNull BossBar bar) {
    this.delegate.showBossBar(bar);
  }

  @Override
  public void hideBossBar(final @NotNull BossBar bar) {
    this.delegate.hideBossBar(bar);
  }

  @Override
  public void playSound(final @NotNull Sound sound) {
    this.delegate.playSound(sound);
  }

  @Override
  public void playSound(final @NotNull Sound sound, final double x, final double y, final double z) {
    this.delegate.playSound(sound, x, y, z);
  }

  @Override
  public void playSound(final @NotNull Sound sound, final Sound.@NotNull Emitter emitter) {
    this.delegate.playSound(sound, emitter);
  }

  @Override
  public void stopSound(final @NotNull SoundStop stop) {
    this.delegate.stopSound(stop);
  }

  @Override
  public void openBook(final @NotNull Book book) {
    this.delegate.openBook(book);
  }

}
