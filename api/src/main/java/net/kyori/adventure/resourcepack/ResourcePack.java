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
package net.kyori.adventure.resourcepack;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a resource pack that can be sent to players.
 *
 * @see Audience#sendResourcePack(ResourcePack)
 * @since 4.15.0
 */
public interface ResourcePack extends Examinable {
  /**
   * Creates a resource pack.
   *
   * @param url the url
   * @param hash the sha-1 hash
   * @param required whether this resource pack is required or not
   * @return the resource pack
   * @since 4.15.0
   */
  static @NotNull ResourcePack resourcePack(final @NotNull String url, final @NotNull String hash, final boolean required) {
    return resourcePack(url, hash, required, null);
  }

  /**
   * Creates a resource pack.
   *
   * @param url the url
   * @param hash the sha-1 hash
   * @param required whether this resource pack is required or not
   * @param prompt the prompt
   * @return the resource pack
   * @since 4.15.0
   */
  static @NotNull ResourcePack resourcePack(final @NotNull String url, final @NotNull String hash, final boolean required, final @Nullable Component prompt) {
    return new ResourcePackImpl(url, hash, required, prompt);
  }

  /**
   * Gets the url.
   *
   * @return the url
   * @since 4.15.0
   */
  @NotNull String url();

  /**
   * Gets the hash.
   *
   * @return the hash
   * @since 4.15.0
   */
  @NotNull String hash();

  /**
   * Gets whether this resource pack is required
   * or not.
   *
   * @return True if this resource pack is required,
   * false otherwise
   * @since 4.15.0
   */
  boolean required();

  /**
   * Gets the prompt.
   *
   * @return the prompt
   * @since 4.15.0
   */
  @Nullable Component prompt();
}
