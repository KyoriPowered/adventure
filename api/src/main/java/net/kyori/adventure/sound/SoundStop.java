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
package net.kyori.adventure.sound;

import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A sound stop.
 */
public interface SoundStop {
  /**
   * Stops all sounds.
   *
   * @return a sound stopper
   */
  static @NonNull SoundStop all() {
    return SoundStopImpl.ALL;
  }

  /**
   * Stops all sounds named {@code sound}.
   *
   * @param sound the sound
   * @return a sound stopper
   */
  static @NonNull SoundStop named(final @NonNull Key sound) {
    return new SoundStopImpl(requireNonNull(sound, "sound"), null);
  }

  /**
   * Stops all sounds on source {@code source}.
   *
   * @param source the source
   * @return a sound stopper
   */
  static @NonNull SoundStop source(final Sound.@NonNull Source source) {
    return new SoundStopImpl(null, requireNonNull(source, "source"));
  }

  /**
   * Stops all sounds named {@code name} on source {@code source}.
   *
   * @param sound the sound
   * @param source the source
   * @return a sound stopper
   */
  static @NonNull SoundStop namedOnSource(final @NonNull Key sound, final Sound.@NonNull Source source) {
    return new SoundStopImpl(requireNonNull(sound, "sound"), requireNonNull(source, "source"));
  }

  /**
   * Gets the sound.
   *
   * @return the sound
   */
  @Nullable Key sound();

  /**
   * Gets the source.
   *
   * @return the source
   */
  Sound.@Nullable Source source();
}
