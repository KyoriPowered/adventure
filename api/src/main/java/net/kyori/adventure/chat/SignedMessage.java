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

import java.time.Instant;
import java.util.stream.Stream;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
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
public interface SignedMessage extends Identified, Examinable {

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
    return new SignedMessageImpl.SignatureImpl(signature);
  }

  /**
   * Creates a system {@link SignedMessage}.
   *
   * @param message the message
   * @param unsignedContent the optional unsigned component content
   * @return a new system {@link SignedMessage}
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_, _ -> new", pure = true)
  static @NotNull SignedMessage system(final @NotNull String message, final @Nullable ComponentLike unsignedContent) {
    return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
  }

  /**
   * The time that the message was sent.
   *
   * @return the timestamp
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @NotNull Instant timestamp();

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
   * The unsigned component content.
   *
   * @return the component or null
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @Nullable Component unsignedContent();

  /**
   * The plain string message.
   *
   * @return the plain string message
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  @NotNull String message();

  /**
   * Checks if this message is a system message.
   *
   * @return true if system
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  default boolean isSystem() {
    return this.identity() == Identity.nil();
  }

  /**
   * Checks if this message can be deleted via {@link net.kyori.adventure.audience.Audience#deleteMessage(SignedMessage)}.
   *
   * @return true if supports deletion
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(pure = true)
  default boolean canDelete() {
    return this.signature() != null;
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("timestamp", this.timestamp()),
      ExaminableProperty.of("salt", this.salt()),
      ExaminableProperty.of("signature", this.signature()),
      ExaminableProperty.of("unsignedContent", this.unsignedContent()),
      ExaminableProperty.of("message", this.message())
    );
  }

  /**
   * A signature wrapper type.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @ApiStatus.NonExtendable
  interface Signature extends Examinable {

    /**
     * Gets the bytes for this signature.
     *
     * @return the bytes
     * @since 4.12.0
     * @sinceMinecraft 1.19
     */
    @Contract(pure = true)
    byte[] bytes();

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
    }
  }
}
