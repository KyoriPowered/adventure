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

import java.util.List;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A request to apply one or more resource packs to a client.
 *
 * <p>Multiple packs are only supported since Minecraft 1.20.3.</p>
 *
 * @since 4.15.0
 */
public interface ResourcePackRequest extends Examinable, ResourcePackRequestLike {
  /**
   * Create a basic request to apply the provided resource packs.
   *
   * @param first the first pack
   * @param others the other packs to apply
   * @return the created request
   * @since 4.15.0
   */
  static @NotNull ResourcePackRequest addingRequest(final @NotNull ResourcePackInfoLike first, final @NotNull ResourcePackInfoLike@NotNull... others) {
    return ResourcePackRequest.resourcePackRequest().packs(first, others).replace(false).build();
  }

  /**
   * Create a builder for a resource pack request.
   *
   * @return the pack request builder
   * @since 4.15.0
   */
  static @NotNull Builder resourcePackRequest() {
    return new ResourcePackRequestImpl.BuilderImpl();
  }

  /**
   * Create a builder for a resource pack request, based on an existing request.
   *
   * @param existing the existing request
   * @return the pack request builder
   * @since 4.15.0
   */
  static @NotNull Builder resourcePackRequest(final @NotNull ResourcePackRequest existing) {
    return new ResourcePackRequestImpl.BuilderImpl(requireNonNull(existing, "existing"));
  }

  /**
   * The resource packs to apply.
   *
   * @return an unmodifiable list of packs to apply
   * @since 4.15.0
   */
  @NotNull List<ResourcePackInfo> packs();

  /**
   * Set the resource packs to apply.
   *
   * @param packs the packs to apply
   * @return an updated pack request
   * @since 4.15.0
   */
  @NotNull ResourcePackRequest packs(final @NotNull Iterable<? extends ResourcePackInfoLike> packs);

  /**
   * A callback to respond to resource pack application status events.
   *
   * <p>This method will return {@link ResourcePackCallback#noOp()} if no callback has been set.</p>
   *
   * @return the callback
   * @since 4.15.0
   */
  @NotNull ResourcePackCallback callback();

  /**
   * Set the callback to respond to resource pack application status events.
   *
   * @param cb the callback
   * @return an updated pack request
   * @since 4.15.0
   */
  @NotNull ResourcePackRequest callback(final @NotNull ResourcePackCallback cb);

  /**
   * Whether to replace or add to existing resource packs.
   *
   * @return whether to replace existing resource packs
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  boolean replace();

  /**
   * Set whether to replace or add to existing resource packs.
   *
   * @param replace whether to replace existing server packs
   * @return an updated pack request
   * @since 4.15.0
   */
  @NotNull ResourcePackRequest replace(final boolean replace);

  /**
   * Gets whether the resource packs in this request are required.
   *
   * <p>Vanilla clients will disconnect themselves if their player
   * rejects a required pack, but implementations will not necessarily
   * perform any additional serverside validation. The {@link #callback()}
   * can provide more information about the client's reaction.</p>
   *
   * @return True if the resource pack is required,
   *     false otherwise
   * @since 4.15.0
   */
  boolean required();

  /**
   * Gets the prompt that will be provided when requesting these packs.
   *
   * @return the prompt
   * @since 4.15.0
   */
  @Nullable Component prompt();

  @Override
  default @NotNull ResourcePackRequest asResourcePackRequest() {
    return this;
  }

  /**
   * A builder for resource pack requests.
   *
   * @since 4.15.0
   */
  interface Builder extends AbstractBuilder<ResourcePackRequest>, ResourcePackRequestLike {
    /**
     * Set the resource packs to apply.
     *
     * @param first the first pack to apply
     * @param others additional packs to apply
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_, _ -> this")
    @NotNull Builder packs(final @NotNull ResourcePackInfoLike first, final @NotNull ResourcePackInfoLike@NotNull... others);

    /**
     * Set the resource packs to apply.
     *
     * @param packs the packs to apply
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder packs(final @NotNull Iterable<? extends ResourcePackInfoLike> packs);

    /**
     * Set the callback to respond to resource pack application status events.
     *
     * @param cb the callback
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder callback(final @NotNull ResourcePackCallback cb);

    /**
     * Set whether to replace or add to existing resource packs.
     *
     * @param replace whether to replace existing server packs
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder replace(final boolean replace);

    /**
     * Sets whether the resource pack is required or not.
     *
     * <p>Vanilla clients will disconnect themselves if their player
     * rejects a required pack, but implementations will not necessarily
     * perform any additional serverside validation. The {@link #callback()}
     * can provide more information about the client's reaction.</p>
     *
     * @param required whether the resource pack is required or not
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

    @Override
    default @NotNull ResourcePackRequest asResourcePackRequest() {
      return this.build();
    }
  }
}
