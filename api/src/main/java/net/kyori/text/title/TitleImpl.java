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
  static final Title CLEAR = new TitleImpl(Type.CLEAR, null, null);
  static final Title RESET = new TitleImpl(Type.RESET, null, null);

  static final long TICK_DURATION_NANOS = Duration.ofMillis(50).toNanos();

  static long ticks(final Duration duration) {
    return duration.toNanos() / TICK_DURATION_NANOS;
  }

  private final Type type;
  private final Component text;
  private final Times times;

  TitleImpl(final Type type, final Component text, final Times times) {
    if((type == Type.TITLE || type == Type.SUBTITLE || type == Type.ACTIONBAR) && text == null) throw new NullPointerException("text");
    if(type == Type.TIMES && times == null) throw new NullPointerException("times");
    this.type = type;
    this.text = text;
    this.times = times;
  }

  @Override
  public @NonNull Type type() {
    return this.type;
  }

  @Override
  public @Nullable Component text() {
    return this.text;
  }

  @Override
  public @Nullable Times times() {
    return this.times;
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final TitleImpl that = (TitleImpl) other;
    return this.type == that.type && Objects.equals(this.text, that.text) && Objects.equals(this.times, that.times);
  }

  @Override
  public int hashCode() {
    int result = this.type.hashCode();
    result = (31 * result) + Objects.hashCode(this.text);
    result = (31 * result) + Objects.hashCode(this.times);
    return result;
  }

  @Override
  public String toString() {
    return ShadyPines.toString(this, builder -> {
      builder.put("type", this.type);
      builder.put("text", this.text);
      builder.put("times", this.times);
    });
  }

  static final class TimesImpl implements Times {
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    TimesImpl(final int fadeIn, final int stay, final int fadeOut) {
      this.fadeIn = fadeIn;
      this.stay = stay;
      this.fadeOut = fadeOut;
    }

    @Override
    public int fadeIn() {
      return this.fadeIn;
    }

    @Override
    public int stay() {
      return this.stay;
    }

    @Override
    public int fadeOut() {
      return this.fadeOut;
    }

    @Override
    public boolean equals(final Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final TimesImpl that = (TimesImpl) other;
      return this.fadeIn == that.fadeIn && this.stay == that.stay && this.fadeOut == that.fadeOut;
    }

    @Override
    public int hashCode() {
      int result = this.fadeIn;
      result = (31 * result) + this.stay;
      result = (31 * result) + this.fadeOut;
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
