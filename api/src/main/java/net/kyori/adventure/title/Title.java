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
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A title.
 */
public interface Title {
  /**
   * A duration which will preserve a client's existing time for the specific parameter.
   *
   * <p>Exact value subject to change.</p>
   */
  Duration KEEP = Duration.ofSeconds(-1);

  /**
   * Creates a title.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @param fadeInTime the fade-in duration
   * @param stayTime the stay duration
   * @param fadeOutTime the fade-out duration
   * @return the title
   */
  static @NonNull Title of(final @NonNull Component title, final @NonNull Component subtitle, final @NonNull Duration fadeInTime, final @NonNull Duration stayTime, final @NonNull Duration fadeOutTime) {
    return new TitleImpl(title, subtitle, fadeInTime, stayTime, fadeOutTime);
  }

  /**
   * Creates a title that maintains each client's existing title times.
   *
   * @param title the title
   * @param subtitle the subtitle
   * @return the title
   */
  static @NonNull Title of(final @NonNull Component title, final @NonNull Component subtitle) {
    return of(title, subtitle, KEEP, KEEP, KEEP);
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
   * Gets the time (in ticks) the title will fade-in.
   *
   * @return the time (in ticks) the title will fade-in
   */
  @NonNull Duration fadeInTime();

  /**
   * Gets the time (in ticks) the title will stay.
   *
   * @return the time (in ticks) the title will stay
   */
  @NonNull Duration stayTime();

  /**
   * Gets the time (in ticks) the title will fade-out.
   *
   * @return the time (in ticks) the title will fade-out
   */
  @NonNull Duration fadeOutTime();
}
