/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

final class EmptyAudience implements Audience {
  static final EmptyAudience INSTANCE = new EmptyAudience();

  @Override
  public @NotNull <T> Optional<T> get(final @NotNull Pointer<T> pointer) {
    return Optional.empty();
  }

  @Contract("_, null -> null; _, !null -> !null")
  @Override
  public <T> @Nullable T getOrDefault(final @NotNull Pointer<T> pointer, final @Nullable T defaultValue) {
    return defaultValue;
  }

  @Override
  public <T> @UnknownNullability T getOrDefaultFrom(final @NotNull Pointer<T> pointer, final @NotNull Supplier<? extends T> defaultValue) {
    return defaultValue.get();
  }

  @Override
  public @NotNull Audience filterAudience(final @NotNull Predicate<? super Audience> filter) {
    return this;
  }

  @Override
  public void forEachAudience(final @NotNull Consumer<? super Audience> action) {
  }

  @Override
  public void sendMessage(final @NotNull ComponentLike message) {
  }

  @Override
  public void sendMessage(final @NotNull Component message) {
  }

  @Override
  @Deprecated
  public void sendMessage(final @NotNull Identified source, final @NotNull Component message, final @NotNull MessageType type) {
  }

  @Override
  @Deprecated
  public void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
  }

  @Override
  public void sendMessage(final @NotNull Component message, final ChatType.@NotNull Bound boundChatType) {
  }

  @Override
  public void sendMessage(final @NotNull SignedMessage signedMessage, final ChatType.@NotNull Bound boundChatType) {
  }

  @Override
  public void deleteMessage(final SignedMessage.@NotNull Signature signature) {
  }

  @Override
  public void sendActionBar(final @NotNull ComponentLike message) {
  }

  @Override
  public void sendPlayerListHeader(final @NotNull ComponentLike header) {
  }

  @Override
  public void sendPlayerListFooter(final @NotNull ComponentLike footer) {
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull ComponentLike header, final @NotNull ComponentLike footer) {
  }

  @Override
  public void openBook(final Book.@NotNull Builder book) {
  }

  @Override
  public boolean equals(final Object that) {
    return this == that;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public String toString() {
    return "EmptyAudience";
  }
}
