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
package net.kyori.adventure.text.event;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A handler for callback click events.
 *
 * @since 4.13.0
 */
@FunctionalInterface
public interface ClickCallback {
  /**
   * Perform an action for this event.
   *
   * @param audience the single-user audience who is attempting to execute this callback function.
   * @since 4.13.0
   */
  void accept(final Audience audience);

  /**
   * Options to configure how a callback can be executed.
   *
   * @since 4.13.0
   */
  @ApiStatus.NonExtendable
  interface Options extends Examinable {

    /**
     * Create a new builder.
     *
     * @return the new builder
     * @since 4.13.0
     */
    static @NotNull Builder builder() {
      return new ClickCallbackOptionsImpl.BuilderImpl();
    }

    /**
     * Create a new builder populating from existing options.
     *
     * @param existing the existing options to populate this builder with
     * @return the new builder
     * @since 4.13.0
     */
    static @NotNull Builder builder(final @NotNull Options existing) {
      return new ClickCallbackOptionsImpl.BuilderImpl(existing);
    }

    /**
     * Whether the callback can be executed multiple times.
     *
     * <p>By default callbacks are single-use.</p>
     *
     * @return whether this callback is multi-use
     * @since 4.13.0
     */
    boolean multiUse();

    /**
     * How long this callback will last until it is made invalid.
     *
     * <p>By default callbacks last 12 hours..</p>
     *
     * @return the duration of this callback
     * @since 4.13.0
     */
    @NotNull Duration lifetime();

    /**
     * A builder for callback options.
     *
     * @since 4.13.0
     */
    @ApiStatus.NonExtendable
    interface Builder extends AbstractBuilder<Options> {
      /**
       * Set whether the callback can be multi-use.
       *
       * @param multiUse whether multiple clicks are allowed
       * @return this builder
       * @since 4.13.0
       */
      @NotNull Builder multiUse(boolean multiUse);

      /**
       * Set how long the callback should last from sending.
       *
       * @param duration the duration of this callback, from the time it is sent
       * @return this builder
       * @since 4.13.0
       */
      @NotNull Builder lifetime(final @NotNull TemporalAmount duration);
    }
  }

  /**
   * A provider for actually producing click callbacks.
   *
   * @since 4.13.0
   */
  @PlatformAPI
  @ApiStatus.Internal
  interface Provider {

    /**
     * Create a real click event based on the provided parameters.
     *
     * @param callback the callback to execute
     * @param options the options to apply to this callback
     * @return a created click event that will execute the provided callback with options
     * @since 4.13.0
     */
    @NotNull ClickEvent create(final @NotNull ClickCallback callback, final ClickCallback.@NotNull Options options);
  }
}
