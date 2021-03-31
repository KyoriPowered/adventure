/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.weather;

import java.time.Duration;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static net.kyori.adventure.weather.WeatherImpl.DEFAULT_LENGTH;

/**
 * Some weather.
 *
 * @since 4.8.0
 */
public interface Weather extends Examinable {

  /**
   * Clear skies with the default length of 5 minutes.
   *
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather clear() {
    return clear(DEFAULT_LENGTH);
  }

  /**
   * Clear skies with a specific length.
   *
   * @param length the length
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather clear(Weather.@NonNull Length length) {
    return weather(Type.CLEAR, length);
  }

  /**
   * Rain with the default length of 5 minutes.
   *
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather rain() {
    return rain(DEFAULT_LENGTH);
  }

  /**
   * Rain with a specific length.
   *
   * @param length the length
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather rain(Weather.@NonNull Length length) {
    return weather(Type.RAIN, length);
  }

  /**
   * Thunder with the default length of 5 minutes.
   *
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather thunder() {
    return thunder(DEFAULT_LENGTH);
  }

  /**
   * Thunder with a specific length.
   *
   * @param length the length
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather thunder(Weather.@NonNull Length length) {
    return weather(Type.THUNDER, length);
  }

  /**
   * Weather with a specific type and length.
   *
   * @param type the type
   * @param length the length
   * @return the weather
   * @since 4.8.0
   */
  static @NonNull Weather weather(final @NonNull Type type, Weather.@NonNull Length length) {
    return new WeatherImpl(type, length);
  }

  /**
   * Gets the length of this weather.
   *
   * @return the duration
   * @since 4.8.0
   */
  Weather.@NonNull Length length();

  /**
   * Gets the type of this weather.
   *
   * @return the type
   * @see Type
   * @since 4.8.0
   */
  @NonNull Type type();

  @Override
  default @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("type", this.type()),
      ExaminableProperty.of("length", this.length())
    );
  }

  /**
   * The types of weather.
   *
   * @since 4.8.0
   */
  enum Type {
    /**
     * Clear skies.
     *
     * @since 4.8.0
     */
    CLEAR,

    /**
     * Rain.
     *
     * @since 4.8.0
     */
    RAIN,

    /**
     * Thunder.
     *
     * @since 4.8.0
     */
    THUNDER
  }

  /**
   * The length of the weather.
   *
   * @since 4.8.0
   */
  interface Length extends Examinable {
    /**
     * A random weather length.
     *
     * @return the length
     * @since 4.8.0
     */
    static Length random() {
      return WeatherImpl.RANDOM;
    }

    /**
     * A specific weather length.
     *
     * @param duration the duration
     * @return the length
     * @throws IllegalArgumentException if the duration is not positive
     * @since 4.8.0
     */
    static Length of(final @NonNull Duration duration) {
      if(duration.isNegative() || duration.isZero()) throw new IllegalArgumentException("duration must be positive");
      return new WeatherImpl.LengthImpl(duration);
    }

    /**
     * Checks if the length is random.
     *
     * @return {@code true} if the length is random
     * @since 4.8.0
     */
    boolean isRandom();

    /**
     * Gets the duration, if any.
     *
     * @return the duration
     * @since 4.8.0
     */
    @Nullable Duration duration();

    @Override
    @NonNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("random", this.isRandom()),
        ExaminableProperty.of("duration", this.duration())
      );
    }
  }
}
