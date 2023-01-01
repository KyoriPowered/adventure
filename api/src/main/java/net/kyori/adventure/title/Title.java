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
package net.kyori.adventure.title;

import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Ticks;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents an in-game title, which can be displayed across the centre of the screen.
 *
 * @see Times
 * @since 4.0.0
 */
@ApiStatus.NonExtendable
public interface Title extends Examinable {
  /**
   * The default times.
   *
   * @since 4.0.0
   */
  Times DEFAULT_TIMES = Times.times(Ticks.duration(10), Ticks.duration(70), Ticks.duration(20));

  /**
   * Creates a title.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @return the title
   * @since 4.0.0
   */
  static @NotNull Title title(final @NotNull Component title, final @NotNull Component subtitle) {
    return title(title, subtitle, DEFAULT_TIMES);
  }

  /**
   * Creates a title.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @param times the times
   * @return the title
   * @since 4.0.0
   */
  static @NotNull Title title(final @NotNull Component title, final @NotNull Component subtitle, final @Nullable Times times) {
    return new TitleImpl(title, subtitle, times);
  }

  /**
   * Gets the title.
   *
   * @return the title
   * @since 4.0.0
   */
  @NotNull Component title();

  /**
   * Gets the subtitle.
   *
   * @return the subtitle
   * @since 4.0.0
   */
  @NotNull Component subtitle();

  /**
   * Gets the times.
   *
   * @return the times
   * @since 4.0.0
   */
  @Nullable Times times();

  /**
   * Gets a part.
   *
   * @param part the part
   * @param <T> the type of the part
   * @return the value
   * @since 4.9.0
   */
  <T> @UnknownNullability T part(final @NotNull TitlePart<T> part);

  /**
   * Title times.
   *
   * @since 4.0.0
   */
  interface Times extends Examinable {
    /**
     * Creates times.
     *
     * @param fadeIn the fade-in time
     * @param stay the stay time
     * @param fadeOut the fade-out time
     * @return times
     * @since 4.0.0
     * @deprecated for removal since 4.10.0, use {@link #times()}
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @Deprecated
    static @NotNull Times of(final @NotNull Duration fadeIn, final @NotNull Duration stay, final @NotNull Duration fadeOut) {
      return times(fadeIn, stay, fadeOut);
    }

    /**
     * Creates times.
     *
     * @param fadeIn the fade-in time
     * @param stay the stay time
     * @param fadeOut the fade-out time
     * @return times
     * @since 4.10.0
     */
    static @NotNull Times times(final @NotNull Duration fadeIn, final @NotNull Duration stay, final @NotNull Duration fadeOut) {
      return new TitleImpl.TimesImpl(fadeIn, stay, fadeOut);
    }

    /**
     * Gets the time the title will fade-in.
     *
     * @return the time the title will fade-in
     * @since 4.0.0
     */
    @NotNull Duration fadeIn();

    /**
     * Gets the time the title will stay.
     *
     * @return the time the title will stay
     * @since 4.0.0
     */
    @NotNull Duration stay();

    /**
     * Gets the time the title will fade-out.
     *
     * @return the time the title will fade-out
     * @since 4.0.0
     */
    @NotNull Duration fadeOut();
  }
}
