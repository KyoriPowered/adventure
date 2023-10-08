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
package net.kyori.adventure.resource;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

/**
 * Represents a resource pack that can be sent to players.
 *
 * @see Audience#sendResourcePack(ResourcePack)
 * @since 4.15.0
 */
public interface ResourcePack extends Buildable<ResourcePack, ResourcePack.Builder>, Examinable {
  /**
   * Creates a resource pack.
   *
   * @param uri the uri
   * @param hash the sha-1 hash
   * @param required whether this resource pack is required or not
   * @return the resource pack
   * @since 4.15.0
   */
  static @NotNull ResourcePack resourcePack(final @NotNull URI uri, final @NotNull String hash, final boolean required) {
    return resourcePack(uri, hash, required, null);
  }

  /**
   * Creates a resource pack.
   *
   * @param uri the uri
   * @param hash the sha-1 hash
   * @param required whether this resource pack is required or not
   * @param prompt the prompt
   * @return the resource pack
   * @since 4.15.0
   */
  static @NotNull ResourcePack resourcePack(final @NotNull URI uri, final @NotNull String hash, final boolean required, final @Nullable Component prompt) {
    return new ResourcePackImpl(uri, hash, required, prompt);
  }

  /**
   * Create a new builder that will create a {@link ResourcePack}.
   *
   * @return a builder
   * @since 4.15.0
   */
  static @NotNull Builder builder() {
    return new ResourcePackImpl.BuilderImpl();
  }

  /**
   * Gets the uri.
   *
   * @return the uri
   * @since 4.15.0
   */
  @NotNull URI uri();

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

  /**
   * Create a new builder initialized with the attributes of this resource pack.
   *
   * @return the builder
   * @since 4.15.0
   */
  @Override
  default @NotNull Builder toBuilder() {
    return builder()
      .uri(this.uri())
      .hash(this.hash())
      .required(this.required())
      .prompt(this.prompt());
  }

  /**
   * A builder for resource packs.
   *
   * @since 4.15.0
   */
  interface Builder extends AbstractBuilder<ResourcePack>, Buildable.Builder<ResourcePack> {
    /**
     * Sets the uri.
     *
     * @param uri the uri
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder uri(final @NotNull URI uri);

    /**
     * Sets the hash.
     *
     * @param hash the hash
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder hash(final @NotNull String hash);

    /**
     * Sets whether this resource pack is required or not.
     *
     * @param required whether this resource pack is required or not
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder required(final boolean required);

    /**
     * Sets the prompt.
     *
     * @param prompt the prompt
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder prompt(final @Nullable Component prompt);

    /**
     * Builds.
     *
     * @return a new resource pack
     * @since 4.15.0
     */
    @Override
    @NotNull ResourcePack build();
  }
}
