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
package net.kyori.adventure.title;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import static java.util.Objects.requireNonNull;

final class TitleImpl implements Title {
  private final Component title;
  private final Component subtitle;
  private final @Nullable Times times;

  TitleImpl(final @NotNull Component title, final @NotNull Component subtitle, final @Nullable Times times) {
    this.title = requireNonNull(title, "title");
    this.subtitle = requireNonNull(subtitle, "subtitle");
    this.times = times;
  }

  @Override
  public @NotNull Component title() {
    return this.title;
  }

  @Override
  public @NotNull Component subtitle() {
    return this.subtitle;
  }

  @Override
  public @Nullable Times times() {
    return this.times;
  }

  @Override
  @SuppressWarnings("unchecked") // compared with parts directly
  public <T> @UnknownNullability T part(final @NotNull TitlePart<T> part) {
    requireNonNull(part, "part");
    if (part == TitlePart.TITLE) {
      return (T) this.title;
    } else if (part == TitlePart.SUBTITLE) {
      return (T) this.subtitle;
    } else if (part == TitlePart.TIMES) {
      return (T) this.times;
    }

    throw new IllegalArgumentException("Don't know what " + part + " is.");
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final TitleImpl that = (TitleImpl) other;
    return this.title.equals(that.title)
      && this.subtitle.equals(that.subtitle)
      && Objects.equals(this.times, that.times);
  }

  @Override
  public int hashCode() {
    int result = this.title.hashCode();
    result = (31 * result) + this.subtitle.hashCode();
    result = (31 * result) + Objects.hashCode(this.times);
    return result;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("title", this.title),
      ExaminableProperty.of("subtitle", this.subtitle),
      ExaminableProperty.of("times", this.times)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static class TimesImpl implements Times {
    private final Duration fadeIn;
    private final Duration stay;
    private final Duration fadeOut;

    TimesImpl(final @NotNull Duration fadeIn, final @NotNull Duration stay, final @NotNull Duration fadeOut) {
      this.fadeIn = requireNonNull(fadeIn, "fadeIn");
      this.stay = requireNonNull(stay, "stay");
      this.fadeOut = requireNonNull(fadeOut, "fadeOut");
    }

    @Override
    public @NotNull Duration fadeIn() {
      return this.fadeIn;
    }

    @Override
    public @NotNull Duration stay() {
      return this.stay;
    }

    @Override
    public @NotNull Duration fadeOut() {
      return this.fadeOut;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (!(other instanceof TimesImpl)) return false;
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
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("fadeIn", this.fadeIn),
        ExaminableProperty.of("stay", this.stay),
        ExaminableProperty.of("fadeOut", this.fadeOut)
      );
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }
}
