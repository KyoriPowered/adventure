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
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A handler for callback click events.
 *
 * @param <T> audience type
 * @since 4.13.0
 */
@FunctionalInterface
public interface ClickCallback<T extends Audience> {
  /**
   * The default lifetime of a callback after creating it, 12 hours.
   *
   * @since 4.13.0
   */
  Duration DEFAULT_LIFETIME = Duration.ofHours(12);

  /**
   * Indicate that a callback should have unlimited uses.
   *
   * @since 4.13.0
   */
  int UNLIMITED_USES = -1;

  /**
   * Adjust this callback to accept any audience, and perform the appropriate filtering.
   *
   * @param <W> the wider type
   * @param <N> the narrower type
   * @param original the original callback of a narrower audience type
   * @param type the audience type to accept
   * @param otherwise the action to perform on the audience if it is not of the appropriate type
   * @return a new callback
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  static <W extends Audience, N extends W> @NotNull ClickCallback<W> widen(final @NotNull ClickCallback<N> original, final @NotNull Class<N> type, final @Nullable Consumer<? super Audience> otherwise) {
    return audience -> {
      if (type.isInstance(audience)) {
        original.accept(type.cast(audience));
      } else if (otherwise != null) {
        otherwise.accept(audience);
      }
    };
  }

  /**
   * Adjust this callback to accept any audience, and perform the appropriate filtering.
   *
   * <p>No message will be sent if the audience is not of the appropriate type.</p>
   *
   * @param <W> the wider type
   * @param <N> the narrower type
   * @param original the original callback of a narrower audience type
   * @param type the audience type to accept
   * @return a new callback
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  static <W extends Audience, N extends W> @NotNull ClickCallback<W> widen(final @NotNull ClickCallback<N> original, final @NotNull Class<N> type) {
    return widen(original, type, null);
  }

  /**
   * Perform an action for this event.
   *
   * @param audience the single-user audience who is attempting to execute this callback function.
   * @since 4.13.0
   */
  void accept(final @NotNull T audience);

  /**
   * Filter audiences that receive this click callback.
   *
   * <p>Actions from audiences that do not match this predicate will be silently ignored.</p>
   *
   * @param filter the filter to test audiences with
   * @return a filtered callback action
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  default @NotNull ClickCallback<T> filter(final @NotNull Predicate<T> filter) {
    return this.filter(filter, null);
  }

  /**
   * Filter audiences that receive this click callback.
   *
   * @param filter the filter to test audiences with
   * @param otherwise the action to perform on the audience if the conditions are not met
   * @return a filtered callback action
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  default @NotNull ClickCallback<T> filter(final @NotNull Predicate<T> filter, final @Nullable Consumer<? super Audience> otherwise) {
    return audience -> {
      if (filter.test(audience)) {
        this.accept(audience);
      } else if (otherwise != null) {
        otherwise.accept(audience);
      }
    };
  }

  /**
   * Require that audiences receiving this callback have a certain permission.
   *
   * <p>For audiences without permissions information, this test will always fail.</p>
   *
   * <p>Actions from audiences that do not match this predicate will be silently ignored.</p>
   *
   * @param permission the permission to check
   * @return a modified callback
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  default @NotNull ClickCallback<T> requiringPermission(final @NotNull String permission) {
    return this.requiringPermission(permission, null);
  }

  /**
   * Require that audiences receiving this callback have a certain permission.
   *
   * <p>For audiences without permissions information, this test will always fail.</p>
   *
   * @param permission the permission to check
   * @param otherwise the action to perform on the audience if the conditions are not met
   * @return a modified callback
   * @since 4.13.0
   */
  @CheckReturnValue
  @Contract(pure = true)
  default @NotNull ClickCallback<T> requiringPermission(final @NotNull String permission, final @Nullable Consumer<? super Audience> otherwise) {
    return this.filter(audience -> audience.getOrDefault(PermissionChecker.POINTER, ClickCallbackInternals.ALWAYS_FALSE).test(permission), otherwise);
  }

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
     * The number of times this callback can be executed.
     *
     * <p>By default callbacks are single-use.</p>
     *
     * @return allowable use count, or {@link #UNLIMITED_USES}
     * @since 4.13.0
     */
    int uses();

    /**
     * How long this callback will last until it is made invalid.
     *
     * <p>By default callbacks last the value of {@link #DEFAULT_LIFETIME}.</p>
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
       * Set the number of uses allowed for this callback.
       *
       * @param useCount the number of allowed uses, or {@link ClickCallback#UNLIMITED_USES}
       * @return this builder
       * @since 4.13.0
       */
      @NotNull Builder uses(int useCount);

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
    @NotNull ClickEvent create(final @NotNull ClickCallback<Audience> callback, final @NotNull Options options);
  }
}
