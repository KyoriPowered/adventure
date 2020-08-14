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
package net.kyori.adventure.title;

import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.time.DurationOrTicks;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A title.
 */
public interface Title {
  /**
   * The default times.
   */
  Times DEFAULT_TIMES = Times.of(DurationOrTicks.ticks(10), DurationOrTicks.ticks(70), DurationOrTicks.ticks(20));

  /**
   * Creates a title.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @param times the times
   * @return the title
   */
  static @NonNull Title of(final @NonNull Component title, final @NonNull Component subtitle, final @Nullable Times times) {
    return new TitleImpl(title, subtitle, times);
  }

  /**
   * Gets the title.
   *
   * @return the title
   */
  @NonNull Component title();

  /**
   * Gets the subtitle.
   *
   * @return the subtitle
   */
  @NonNull Component subtitle();

  /**
   * Gets the times.
   *
   * @return the times
   */
  @Nullable Times times();

  interface Times {
    /**
     * Creates times.
     *
     * @param fadeIn the fade-in time
     * @param stay the stay time
     * @param fadeOut the fade-eut time
     * @return times
     */
    static @NonNull Times of(final @NonNull Duration fadeIn, final @NonNull Duration stay, final @NonNull Duration fadeOut) {
      return of(DurationOrTicks.duration(fadeIn), DurationOrTicks.duration(stay), DurationOrTicks.duration(fadeOut));
    }

    /**
     * Creates times.
     *
     * @param fadeIn the fade-in time
     * @param stay the stay time
     * @param fadeOut the fade-eut time
     * @return times
     */
    static @NonNull Times of(final @NonNull DurationOrTicks fadeIn, final @NonNull DurationOrTicks stay, final @NonNull DurationOrTicks fadeOut) {
      return new TitleImpl.TimesImpl(fadeIn, stay, fadeOut);
    }

    /**
     * Gets the time the title will fade-in.
     *
     * @return the time the title will fade-in
     */
    @NonNull DurationOrTicks fadeIn();

    /**
     * Gets the time the title will stay.
     *
     * @return the time the title will stay
     */
    @NonNull DurationOrTicks stay();

    /**
     * Gets the time the title will fade-out.
     *
     * @return the time the title will fade-out
     */
    @NonNull DurationOrTicks fadeOut();
  }
}
