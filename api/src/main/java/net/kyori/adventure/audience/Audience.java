/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.audience;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * An audience is a collection of {@link Viewer}s, supporting all
 * operations.
 *
 * <p>Actions are only passed onto contained viewers if they are
 * supported by the viewer.</p>
 */
public interface Audience extends Viewer, Viewer.Messages, Viewer.ActionBars, Viewer.Titles, Viewer.BossBars, Viewer.Sounds, Viewer.Books {
  /**
   * Gets an audience with no viewers.
   *
   * @return an audience
   */
  static @NonNull Audience empty() {
    return EmptyAudience.INSTANCE;
  }

  /**
   * Creates an audience that delegates to an array of viewers.
   *
   * @param viewers the delegate viewers
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Viewer@NonNull... viewers) {
    final int length = viewers.length;
    if(length == 0) {
      return empty();
    } else if(length == 1) {
      return viewers[0].asAudience();
    }
    return of(Arrays.asList(viewers));
  }

  /**
   * Creates an audience that delegates to a collection of viewers.
   *
   * @param viewers the delegate viewers
   * @return an audience
   */
  static @NonNull Audience of(final @NonNull Iterable<? extends Viewer> viewers) {
    return (MultiAudience) () -> viewers;
  }

  /**
   * Creates an audience that weakly delegates to another audience.
   *
   * @param audience the delegate audience
   * @return an audience
   */
  static @NonNull Audience weakOf(final @Nullable Audience audience) {
    return audience instanceof WeakAudience || audience instanceof EmptyAudience ? audience : new WeakAudience(audience);
  }

  /**
   * Applies the given {@code action} to the audience, and returns an
   * {@link Audience} encapsulating the sub-viewers (if any) which didn't support
   * the action.
   *
   * @param type the type of viewer the action requires
   * @param action the action
   * @param <T> the type of viewer
   * @return a {@link Viewer} of the sub-viewers the action couldn't be applied to
   */
  <T extends Viewer> @NonNull Audience perform(final @NonNull Class<T> type, final @NonNull Consumer<T> action);
}
