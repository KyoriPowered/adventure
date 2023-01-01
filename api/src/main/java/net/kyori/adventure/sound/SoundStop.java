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
package net.kyori.adventure.sound;

import java.util.function.Supplier;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A sound and/or a sound source, used for stopping in-game sounds that
 * are being played on a game client matching the given sound and/or sound source.
 *
 * <p>For clarification: a {@link SoundStop} consisting of the sound "ambient.weather.rain" and the source {@link Sound.Source#AMBIENT}
 * will only stop sounds matching BOTH parameters and not sounds matching only the sound or only the source.</p>
 *
 *
 * @see Audience#stopSound(SoundStop)
 * @since 4.0.0
 */
@ApiStatus.NonExtendable
public interface SoundStop extends Examinable {
  /**
   * Stops all sounds.
   *
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop all() {
    return SoundStopImpl.ALL;
  }

  /**
   * Stops all sounds named {@code sound}.
   *
   * @param sound the sound
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop named(final @NotNull Key sound) {
    requireNonNull(sound, "sound");
    return new SoundStopImpl(null) {
      @Override
      public @NotNull Key sound() {
        return sound;
      }
    };
  }

  /**
   * Stops all sounds named {@code sound}.
   *
   * @param sound the sound
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop named(final Sound.@NotNull Type sound) {
    requireNonNull(sound, "sound");
    return new SoundStopImpl(null) {
      @Override
      public @NotNull Key sound() {
        return sound.key();
      }
    };
  }

  /**
   * Stops all sounds named {@code sound}.
   *
   * @param sound the sound
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop named(final @NotNull Supplier<? extends Sound.Type> sound) {
    requireNonNull(sound, "sound");
    return new SoundStopImpl(null) {
      @Override
      public @NotNull Key sound() {
        return sound.get().key();
      }
    };
  }

  /**
   * Stops all sounds on source {@code source}.
   *
   * @param source the source
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop source(final Sound.@NotNull Source source) {
    requireNonNull(source, "source");
    return new SoundStopImpl(source) {
      @Override
      public @Nullable Key sound() {
        return null;
      }
    };
  }

  /**
   * Stops all sounds named {@code name} on source {@code source}.
   *
   * @param sound the sound
   * @param source the source
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop namedOnSource(final @NotNull Key sound, final Sound.@NotNull Source source) {
    requireNonNull(sound, "sound");
    requireNonNull(source, "source");
    return new SoundStopImpl(source) {
      @Override
      public @NotNull Key sound() {
        return sound;
      }
    };
  }

  /**
   * Stops all sounds named {@code name} on source {@code source}.
   *
   * @param sound the sound
   * @param source the source
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop namedOnSource(final Sound.@NotNull Type sound, final Sound.@NotNull Source source) {
    requireNonNull(sound, "sound");
    return namedOnSource(sound.key(), source);
  }

  /**
   * Stops all sounds named {@code name} on source {@code source}.
   *
   * @param sound the sound
   * @param source the source
   * @return a sound stopper
   * @since 4.0.0
   */
  static @NotNull SoundStop namedOnSource(final @NotNull Supplier<? extends Sound.Type> sound, final Sound.@NotNull Source source) {
    requireNonNull(sound, "sound");
    requireNonNull(source, "source");
    return new SoundStopImpl(source) {
      @Override
      public @NotNull Key sound() {
        return sound.get().key();
      }
    };
  }

  /**
   * Gets the sound.
   *
   * @return the sound
   * @since 4.0.0
   */
  @Nullable Key sound();

  /**
   * Gets the source.
   *
   * @return the source
   * @since 4.0.0
   */
  Sound.@Nullable Source source();
}
