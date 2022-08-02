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
package net.kyori.adventure.chat;

import java.security.SecureRandom;
import java.time.Instant;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

final class UnsignedSignedMessageImpl implements SignedMessage {
  static final SecureRandom RANDOM = new SecureRandom();

  private final Instant instant;
  private final long salt;
  private final Component message;
  private final String plain;
  private final Identity identity;

  UnsignedSignedMessageImpl(final Component message, final String plain, final Identity identity) {
    this.instant = Instant.now();
    this.salt = RANDOM.nextLong();
    this.message = message;
    this.plain = plain;
    this.identity = identity;
  }

  @Override
  public @NotNull Instant timeStamp() {
    return this.instant;
  }

  @Override
  public long salt() {
    return this.salt;
  }

  @Override
  public Signature signature() {
    return null;
  }

  @Override
  public @NotNull Component message() {
    return this.message;
  }

  @Override
  public @NotNull String plain() {
    return this.plain;
  }

  @Override
  public @NotNull Identity identity() {
    return this.identity;
  }
}
