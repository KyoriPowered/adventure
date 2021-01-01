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
package net.kyori.adventure.identity;

import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An identity used to track the sender of messages for the social interaction features
 * introduced in <em>Minecraft: Java Edition</em> 1.16.4.
 *
 * @since 4.0.0
 * @sinceMinecraft 1.16
 */
public interface Identity extends Examinable {
  /**
   * Gets the {@code null} identity.
   *
   * <p>This should only be used when no players can be linked to a message.</p>
   *
   * @return the {@code null} identity
   * @since 4.0.0
   */
  static @NonNull Identity nil() {
    return Identities.NIL;
  }

  /**
   * Creates an identity.
   *
   * @param uuid the uuid
   * @return an identity
   * @since 4.0.0
   */
  static @NonNull Identity identity(final @NonNull UUID uuid) {
    if(uuid.equals(Identities.NIL.uuid())) return Identities.NIL;
    return new IdentityImpl(uuid);
  }

  /**
   * Gets the uuid.
   *
   * @return the uuid
   * @since 4.0.0
   */
  @NonNull UUID uuid();

  @Override
  default @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("uuid", this.uuid()));
  }
}
