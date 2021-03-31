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

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Something that optionally has it's own weather, otherwise it has the weather of it's parent.
 *
 * @since 4.8.0
 */
public interface ChildWeatherHolder extends WeatherHolder {

  /**
   * Gets the parent of this child.
   *
   * @return the parent
   * @since 4.8.0
   */
  @NonNull WeatherHolder weatherParent();

  /**
   * Checks if this child has any weather set.
   *
   * @return {@code true} if it does
   * @since 4.8.0
   */
  boolean hasWeather();

  @Override
  default @NonNull Weather weather() {
    if(this.hasWeather()) return this.weather();
    return this.weatherParent().weather();
  }
}
