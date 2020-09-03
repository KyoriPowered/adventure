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

import java.util.function.Supplier;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.Index;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * A sound.
 *
 * @since 4.0.0
 */
public interface Sound {
  /**
   * Creates a new sound.
   *
   * @param name the name
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NonNull Sound of(final @NonNull Key name, final @NonNull Source source, final float volume, final float pitch) {
    requireNonNull(name, "name");
    requireNonNull(source, "source");
    return new SoundImpl(source, volume, pitch) {
      @Override
      public @NonNull Key name() {
        return name;
      }
    };
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NonNull Sound of(final @NonNull Type type, final @NonNull Source source, final float volume, final float pitch) {
    requireNonNull(type, "type");
    return of(type.key(), source, volume, pitch);
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NonNull Sound of(final @NonNull Supplier<? extends Type> type, final @NonNull Source source, final float volume, final float pitch) {
    requireNonNull(type, "type");
    requireNonNull(source, "source");
    return new SoundImpl(source, volume, pitch) {
      @Override
      public @NonNull Key name() {
        return type.get().key();
      }
    };
  }

  /**
   * Gets the name.
   *
   * @return the name
   * @since 4.0.0
   */
  @NonNull Key name();

  /**
   * Gets the source.
   *
   * @return the source
   * @since 4.0.0
   */
  @NonNull Source source();

  /**
   * Gets the volume.
   *
   * @return the volume
   * @since 4.0.0
   */
  float volume();

  /**
   * Gets the pitch.
   *
   * @return the pitch
   * @since 4.0.0
   */
  float pitch();

  /**
   * The sound source.
   *
   * @since 4.0.0
   */
  enum Source {
    MASTER("master"),
    MUSIC("music"),
    RECORD("record"),
    WEATHER("weather"),
    BLOCK("block"),
    HOSTILE("hostile"),
    NEUTRAL("neutral"),
    PLAYER("player"),
    AMBIENT("ambient"),
    VOICE("voice");

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Source> NAMES = Index.create(Source.class, source -> source.name);
    private final String name;

    Source(final String name) {
      this.name = name;
    }
  }

  /**
   * A sound type.
   *
   * @since 4.0.0
   */
  interface Type {
    /**
     * Gets the key.
     *
     * @return the key
     * @since 4.0.0
     */
    @NonNull Key key();
  }
}
