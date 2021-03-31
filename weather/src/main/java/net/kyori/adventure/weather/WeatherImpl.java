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
import java.util.Objects;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class WeatherImpl implements Weather {
  static final Length DEFAULT_LENGTH = Length.of(Duration.ofMillis(5));
  static final Length RANDOM = new LengthImpl(null);
  private final Type type;
  private final Length length;

  WeatherImpl(final @NonNull Type type, final Weather.@NonNull Length length) {
    this.type = type;
    this.length = length;
  }

  @Override
  public @NonNull Length length() {
    return this.length;
  }

  @Override
  public @NonNull Type type() {
    return this.type;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof WeatherImpl)) return false;
    final WeatherImpl that = (WeatherImpl) other;
    return that.type == this.type && this.length.equals(that.length);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.type, this.length);
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  static class LengthImpl implements Length {
    private final Duration duration;
    private final boolean random;

    LengthImpl(final @Nullable Duration duration) {
      this.duration = duration;
      this.random = duration == null;
    }

    @Override
    public boolean isRandom() {
      return this.random;
    }

    @Override
    public @Nullable Duration duration() {
      return this.duration;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(!(other instanceof LengthImpl)) return false;
      final LengthImpl that = (LengthImpl) other;
      return that.random == this.random && Objects.equals(that.duration, this.duration);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.random, this.duration);
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }
  }
}
