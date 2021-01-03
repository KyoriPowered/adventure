/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.identity.Identified;
import org.checkerframework.checker.nullness.qual.NonNull;

final class EmptyAudience implements Audience {
  static final EmptyAudience INSTANCE = new EmptyAudience();

  @Override
  public void sendMessage(final @NonNull ComponentLike message) {
  }

  @Override
  public void sendMessage(final @NonNull Identified source, final @NonNull ComponentLike message) {
  }

  @Override
  public void sendMessage(final @NonNull Identity source, final @NonNull ComponentLike message) {
  }

  @Override
  public void sendMessage(final @NonNull ComponentLike message, final @NonNull MessageType type) {
  }

  @Override
  public void sendMessage(final @NonNull Identified source, final @NonNull ComponentLike message, final @NonNull MessageType type) {
  }

  @Override
  public void sendMessage(final @NonNull Identity source, final @NonNull ComponentLike message, final @NonNull MessageType type) {
  }

  @Override
  public void sendActionBar(final @NonNull ComponentLike message) {
  }

  @Override
  public void sendPlayerListHeader(final @NonNull ComponentLike header) {
  }

  @Override
  public void sendPlayerListFooter(final @NonNull ComponentLike footer) {
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NonNull ComponentLike header, final @NonNull ComponentLike footer) {
  }

  @Override
  public void openBook(final Book.@NonNull Builder book) {
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
