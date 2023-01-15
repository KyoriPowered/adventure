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
package net.kyori.adventure.util;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;

/**
 * Standard game tick utilities.
 *
 * @since 4.0.0
 */
public interface Ticks {
  /**
   * The number of ticks that occur in one second.
   *
   * @since 4.0.0
   */
  int TICKS_PER_SECOND = 20;

  /**
   * A single tick duration, in milliseconds.
   *
   * @since 4.0.0
   */
  long SINGLE_TICK_DURATION_MS = 1000 / TICKS_PER_SECOND;

  /**
   * Converts ticks into a {@link Duration}.
   *
   * @param ticks the number of ticks
   * @return a duration
   * @since 4.0.0
   */
  static @NotNull Duration duration(final long ticks) {
    return Duration.ofMillis(ticks * SINGLE_TICK_DURATION_MS);
  }
}
