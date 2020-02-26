/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text.title;

import java.time.Duration;
import java.util.function.Consumer;
import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A title.
 */
public interface Title {
  /**
   * Creates a title.
   *
   * @param title the title
   * @return the title
   */
  static @NonNull Title title(final @NonNull Component title) {
    return Title.builder().title(title).build();
  }

  /**
   * Creates a title.
   *
   * @param subtitle the subtitle
   * @return the title
   */
  static @NonNull Title subtitle(final @NonNull Component subtitle) {
    return Title.builder().subtitle(subtitle).build();
  }

  /**
   * Creates a title.
   *
   * @param actionbar the actionbar
   * @return the title
   */
  static @NonNull Title actionbar(final @NonNull Component actionbar) {
    return Title.builder().actionbar(actionbar).build();
  }

  /**
   * Creates a title that sets times.
   *
   * @param fadeIn the fade-in duration
   * @param stay the stay duration
   * @param fadeOut the fade-out duration
   * @return the title
   */
  static @NonNull Title times(final @NonNull Duration fadeIn, final @NonNull Duration stay, final @NonNull Duration fadeOut) {
    return times((int) TitleImpl.ticks(fadeIn), (int) TitleImpl.ticks(stay), (int) TitleImpl.ticks(fadeOut));
  }

  /**
   * Creates a title that sets times.
   *
   * @param fadeIn the fade-in duration, in ticks
   * @param stay the stay duration, in ticks
   * @param fadeOut the fade-out duration, in ticks
   * @return the title
   */
  static @NonNull Title times(final int fadeIn, final int stay, final int fadeOut) {
    return Title.builder().times(times -> times.fadeIn(fadeIn).stay(stay).fadeOut(fadeOut)).build();
  }

  /**
   * Gets a title that will clear.
   *
   * @return a title that will clear
   */
  static @NonNull Title clear() {
    return TitleImpl.CLEAR;
  }

  /**
   * Gets a title that will reset.
   *
   * @return a title that will reset
   */
  static @NonNull Title reset() {
    return TitleImpl.RESET;
  }

  /**
   * Creates a builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new TitleBuilder();
  }

  /**
   * Gets the title.
   *
   * @return the title
   */
  @Nullable Component title();

  /**
   * Gets the subtitle.
   *
   * @return the subtitle
   */
  @Nullable Component subtitle();

  /**
   * Gets the actionbar.
   *
   * @return the actionbar
   */
  @Nullable Component actionbar();

  /**
   * Gets the times.
   *
   * @return the times
   */
  @Nullable Times times();

  boolean shouldClear();

  boolean shouldReset();

  interface Builder {
    @NonNull Builder title(final @NonNull Component title);

    @NonNull Builder subtitle(final @NonNull Component subtitle);

    @NonNull Builder actionbar(final @NonNull Component actionbar);

    @NonNull Builder times(final @NonNull Consumer<Times> consumer);

    @NonNull Builder clear(final boolean clear);

    @NonNull Builder reset(final boolean reset);

    @NonNull Title build();

    interface Times {
      @NonNull Times fadeIn(final @NonNull Duration duration);

      @NonNull Times stay(final @NonNull Duration duration);

      @NonNull Times fadeOut(final @NonNull Duration duration);

      @NonNull Times fadeIn(final int duration);

      @NonNull Times stay(final int duration);

      @NonNull Times fadeOut(final int duration);
    }
  }

  /**
   * Title times.
   */
  interface Times {
    /**
     * Gets the time (in ticks) the title will fade-in.
     *
     * @return the time (in ticks) the title will fade-in
     */
    int fadeIn();

    /**
     * Gets the time (in ticks) the title will stay.
     *
     * @return the time (in ticks) the title will stay
     */
    int stay();

    /**
     * Gets the time (in ticks) the title will fade-out.
     *
     * @return the time (in ticks) the title will fade-out
     */
    int fadeOut();
  }
}
