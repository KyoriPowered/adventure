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

import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.time.DurationOrTicks;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TitleImpl implements Examinable, Title {
  private final Component title;
  private final Component subtitle;
  private final @Nullable Times times;

  TitleImpl(final @NonNull Component title, final @NonNull Component subtitle, final @Nullable Times times) {
    this.title = title;
    this.subtitle = subtitle;
    this.times = times;
  }

  @Override
  public @NonNull Component title() {
    return this.title;
  }

  @Override
  public @NonNull Component subtitle() {
    return this.subtitle;
  }

  @Override
  public @Nullable Times times() {
    return this.times;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final TitleImpl that = (TitleImpl) other;
    return this.title.equals(that.title)
      && this.subtitle.equals(that.subtitle)
      && this.times.equals(that.times);
  }

  @Override
  public int hashCode() {
    int result = this.title.hashCode();
    result = (31 * result) + this.subtitle.hashCode();
    result = (31 * result) + this.times.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("title", this.title),
      ExaminableProperty.of("subtitle", this.subtitle),
      ExaminableProperty.of("times", this.times)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  static class TimesImpl implements Examinable, Times {
    private final DurationOrTicks fadeIn;
    private final DurationOrTicks stay;
    private final DurationOrTicks fadeOut;

    TimesImpl(final DurationOrTicks fadeIn, final DurationOrTicks stay, final DurationOrTicks fadeOut) {
      this.fadeIn = fadeIn;
      this.stay = stay;
      this.fadeOut = fadeOut;
    }

    @Override
    public @NonNull DurationOrTicks fadeIn() {
      return this.fadeIn;
    }

    @Override
    public @NonNull DurationOrTicks stay() {
      return this.stay;
    }

    @Override
    public @NonNull DurationOrTicks fadeOut() {
      return this.fadeOut;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final TimesImpl that = (TimesImpl) other;
      return this.fadeIn.equals(that.fadeIn)
        && this.stay.equals(that.stay)
        && this.fadeOut.equals(that.fadeOut);
    }

    @Override
    public int hashCode() {
      int result = this.fadeIn.hashCode();
      result = (31 * result) + this.stay.hashCode();
      result = (31 * result) + this.fadeOut.hashCode();
      return result;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("fadeIn", this.fadeIn),
        ExaminableProperty.of("stay", this.stay),
        ExaminableProperty.of("fadeOut", this.fadeOut)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }
  }
}
