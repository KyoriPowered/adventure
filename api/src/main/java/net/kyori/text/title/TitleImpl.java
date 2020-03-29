/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text.title;

import java.time.Duration;
import java.util.Objects;
import net.kyori.text.Component;
import net.kyori.text.util.ShadyPines;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TitleImpl implements Title {
  static final Title CLEAR = new TitleImpl(true, false);
  static final Title RESET = new TitleImpl(false, true);

  private final Component title;
  private final Component subtitle;
  private final Times times;
  private final boolean clear;
  private final boolean reset;

  TitleImpl(final TitleBuilder builder) {
    this.title = builder.title;
    this.subtitle = builder.subtitle;
    if(builder.hasTimes()) {
      this.times = new TitleImpl.TimesImpl(builder.fadeIn, builder.stay, builder.fadeOut);
    } else {
      this.times = null;
    }
    this.clear = builder.clear;
    this.reset = builder.reset;
  }

  TitleImpl(final Component title, final Component subtitle, final Times times) {
    this.title = title;
    this.subtitle = subtitle;
    this.times = times;
    this.clear = false;
    this.reset = false;
  }

  TitleImpl(final boolean clear, final boolean reset) {
    this.title = null;
    this.subtitle = null;
    this.times = null;
    this.clear = clear;
    this.reset = reset;
  }

  @Override
  public @Nullable Component title() {
    return this.title;
  }

  @Override
  public @Nullable Component subtitle() {
    return this.subtitle;
  }

  @Override
  public @Nullable Times times() {
    return this.times;
  }

  @Override
  public boolean shouldClear() {
    return this.clear;
  }

  @Override
  public boolean shouldReset() {
    return this.reset;
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final TitleImpl that = (TitleImpl) other;
    return Objects.equals(this.title, that.title)
      && Objects.equals(this.subtitle, that.subtitle)
      && Objects.equals(this.times, that.times)
      && this.clear == that.clear
      && this.reset == that.reset;
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.title);
    result = (31 * result) + Objects.hashCode(this.subtitle);
    result = (31 * result) + Objects.hashCode(this.times);
    result = (31 * result) + Boolean.hashCode(this.clear);
    result = (31 * result) + Boolean.hashCode(this.reset);
    return result;
  }

  @Override
  public String toString() {
    return ShadyPines.toString(this, builder -> {
      builder.put("title", this.title);
      builder.put("subtitle", this.subtitle);
      builder.put("times", this.times);
      builder.put("clear", this.clear);
      builder.put("reset", this.reset);
    });
  }

  static final class TimesImpl implements Times {
    private final Duration fadeIn;
    private final Duration stay;
    private final Duration fadeOut;

    TimesImpl(final Duration fadeIn, final Duration stay, final Duration fadeOut) {
      this.fadeIn = fadeIn;
      this.stay = stay;
      this.fadeOut = fadeOut;
    }

    @Override
    public @NonNull Duration fadeIn() {
      return this.fadeIn;
    }

    @Override
    public @NonNull Duration stay() {
      return this.stay;
    }

    @Override
    public @NonNull Duration fadeOut() {
      return this.fadeOut;
    }

    @Override
    public boolean equals(final Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final TimesImpl that = (TimesImpl) other;
      return Objects.equals(this.fadeIn, that.fadeIn) && Objects.equals(this.stay, that.stay) && Objects.equals(this.fadeOut, that.fadeOut);
    }

    @Override
    public int hashCode() {
      int result = Objects.hashCode(this.fadeIn);
      result = (31 * result) + Objects.hashCode(this.stay);
      result = (31 * result) + Objects.hashCode(this.fadeOut);
      return result;
    }

    @Override
    public String toString() {
      return ShadyPines.toString(this, builder -> {
        builder.put("fadeIn", this.fadeIn);
        builder.put("stay", this.stay);
        builder.put("fadeOut", this.fadeOut);
      });
    }
  }
}
