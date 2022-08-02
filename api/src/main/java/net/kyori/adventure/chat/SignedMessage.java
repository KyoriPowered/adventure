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

import java.time.Instant;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A signed chat message.
 *
 * @since 4.12.0
 * @sinceMinecraft 1.19
 */
@ApiStatus.NonExtendable
public interface SignedMessage extends Identified, ComponentLike {

  /**
   * Creates a signature wrapper.
   *
   * @param signature the signature
   * @return a new signature
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_ -> new", pure = true)
  static @NotNull Signature signature(final byte[] signature) {
    return new Signature(signature);
  }

  /**
   * Creates an unsigned {@link SignedMessage} for an {@link Identity}.
   *
   * @param component the message component
   * @param plain the plain text message
   * @param identity the identity for the signed message header
   * @return a new unsigned {@link SignedMessage}
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_, _, _ -> new", pure = true)
  static @NotNull SignedMessage unsigned(final @NotNull Component component, final @NotNull String plain, final @NotNull Identity identity) {
    return new UnsignedSignedMessageImpl(component, plain, identity);
  }

  /**
   * Creates an unsigned {@link SignedMessage} for an {@link Identity}.
   *
   * @param component the message component
   * @param plain the plain text message
   * @param identified the identity for the signed message header
   * @return a new unsigned {@link SignedMessage}
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_, _, _ -> new", pure = true)
  static @NotNull SignedMessage unsigned(final @NotNull Component component, final @NotNull String plain, final @NotNull Identified identified) {
    return new UnsignedSignedMessageImpl(component, plain, identified.identity());
  }

  /**
   * The time that the message was sent.
   *
   * @return the timestamp
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @NotNull Instant timeStamp();

  /**
   * The salt.
   *
   * @return the salt
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  long salt();

  /**
   * The signature of the message.
   *
   * @return the signature
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @Nullable Signature signature();

  /**
   * The signed component.
   *
   * @return the component
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @NotNull Component message();

  @Override
  @NotNull
  default Component asComponent() {
    return this.message();
  }

  /**
   * The plain string message.
   *
   * @return the plain string message
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @NotNull String plain();

  /**
   * A signature wrapper type.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  final class Signature {

    final byte[] signature;

    private Signature(final byte[] signature) {
      this.signature = signature;
    }

    /**
     * Gets the bytes for this signature.
     *
     * @return the bytes
     * @since 4.12.0
     * @sinceMinecraft 1.19
     */
    @Contract(pure = true)
    public byte[] bytes() {
      return this.signature;
    }
  }
}
