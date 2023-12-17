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

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents information about a resource pack that can be sent to players.
 *
 * @see ResourcePackRequest
 * @see Audience#sendResourcePacks(ResourcePackInfoLike, ResourcePackInfoLike...)
 * @since 4.15.0
 */
public interface ResourcePackInfo extends Examinable, ResourcePackInfoLike {
  /**
   * Creates information about a resource pack.
   *
   * @param id the id
   * @param uri the uri
   * @param hash the sha-1 hash
   * @return the resource pack request
   * @since 4.15.0
   */
  static @NotNull ResourcePackInfo resourcePackInfo(final @NotNull UUID id, final @NotNull URI uri, final @NotNull String hash) {
    return new ResourcePackInfoImpl(id, uri, hash);
  }

  /**
   * Create a new builder that will create a {@link ResourcePackInfo}.
   *
   * @return a builder
   * @since 4.15.0
   */
  static @NotNull Builder resourcePackInfo() {
    return new ResourcePackInfoImpl.BuilderImpl();
  }

  /**
   * Gets the id.
   *
   * @return the id
   * @since 4.15.0
   */
  @NotNull UUID id();

  /**
   * Gets the uri.
   *
   * @return the uri
   * @since 4.15.0
   */
  @NotNull URI uri();

  /**
   * Gets the SHA-1 hash.
   *
   * @return the hash
   * @since 4.15.0
   */
  @NotNull String hash();

  @Override
  default @NotNull ResourcePackInfo asResourcePackInfo() {
    return this;
  }

  /**
   * A builder for resource pack requests.
   *
   * @since 4.15.0
   */
  interface Builder extends AbstractBuilder<ResourcePackInfo>, ResourcePackInfoLike {
    /**
     * Sets the id.
     *
     * @param id the id
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder id(final @NotNull UUID id);

    /**
     * Sets the uri.
     *
     * <p>If no UUID has been provided, setting a URL will set the ID to one based on the URL.</p>
     *
     * <p>This parameter is required.</p>
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
     * Builds.
     *
     * @return a new resource pack request
     * @since 4.15.0
     */
    @Override
    @NotNull ResourcePackInfo build();

    /**
     * Builds, computing a hash based on the provided URL.
     *
     * <p>The hash computation will perform a network request asynchronously, exposing the completed info via the returned future.</p>
     *
     * @return a future providing the new resource pack request
     * @since 4.15.0
     */
    default @NotNull CompletableFuture<ResourcePackInfo> computeHashAndBuild() {
      return this.computeHashAndBuild(ForkJoinPool.commonPool());
    }

    /**
     * Builds, computing a hash based on the provided URL.
     *
     * <p>The hash computation will perform a network request asynchronously, exposing the completed info via the returned future.</p>
     *
     * @param executor the executor to perform the hash computation on
     * @return a future providing the new resource pack request
     * @since 4.15.0
     */
    @NotNull CompletableFuture<ResourcePackInfo> computeHashAndBuild(final @NotNull Executor executor);

    @Override
    default @NotNull ResourcePackInfo asResourcePackInfo() {
      return this.build();
    }
  }
}
