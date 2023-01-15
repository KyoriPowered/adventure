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
package net.kyori.adventure.chat;

import java.security.SecureRandom;
import java.time.Instant;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Used for system messages ONLY
final class SignedMessageImpl implements SignedMessage {
  static final SecureRandom RANDOM = new SecureRandom();

  private final Instant instant;
  private final long salt;
  private final String message;
  private final Component unsignedContent;

  SignedMessageImpl(final String message, final Component unsignedContent) {
    this.instant = Instant.now();
    this.salt = RANDOM.nextLong();
    this.message = message;
    this.unsignedContent = unsignedContent;
  }

  @Override
  public @NotNull Instant timestamp() {
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
  public @Nullable Component unsignedContent() {
    return this.unsignedContent;
  }

  @Override
  public @NotNull String message() {
    return this.message;
  }

  @Override
  public @NotNull Identity identity() {
    return Identity.nil();
  }

  static final class SignatureImpl implements Signature {

    final byte[] signature;

    SignatureImpl(final byte[] signature) {
      this.signature = signature;
    }

    @Override
    public byte[] bytes() {
      return this.signature;
    }
  }
}
