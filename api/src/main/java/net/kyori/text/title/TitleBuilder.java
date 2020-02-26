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
import java.util.function.Consumer;
import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class TitleBuilder implements Title.Builder {
  @Nullable Component title;
  @Nullable Component subtitle;
  @Nullable Component actionbar;
  @Nullable TimesImpl times;
  boolean clear;
  boolean reset;

  @Override
  public Title.@NonNull Builder title(final @NonNull Component title) {
    this.title = title;
    return this;
  }

  @Override
  public Title.@NonNull Builder subtitle(final @NonNull Component subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  @Override
  public Title.@NonNull Builder actionbar(final @NonNull Component actionbar) {
    this.actionbar = actionbar;
    return this;
  }

  @Override
  public Title.@NonNull Builder times(final @NonNull Consumer<Times> consumer) {
    final TimesImpl times = new TimesImpl();
    consumer.accept(times);
    this.times = times;
    return this;
  }

  @Override
  public Title.@NonNull Builder clear(final boolean clear) {
    this.clear = clear;
    return this;
  }

  @Override
  public Title.@NonNull Builder reset(final boolean reset) {
    this.reset = reset;
    return this;
  }

  @Override
  public @NonNull Title build() {
    if(this.title == null && this.subtitle == null && this.actionbar == null && this.times == null && !this.clear && !this.reset) {
      throw new IllegalStateException("empty");
    }
    return new TitleImpl(this);
  }

  static class TimesImpl implements Times {
    int fadeIn;
    int stay;
    int fadeOut;

    @Override
    public Times fadeIn(final @NonNull Duration duration) {
      return this.fadeIn((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times stay(final @NonNull Duration duration) {
      return this.stay((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times fadeOut(final @NonNull Duration duration) {
      return this.fadeOut((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times fadeIn(final int duration) {
      this.fadeIn = duration;
      return this;
    }

    @Override
    public Times stay(final int duration) {
      this.stay = duration;
      return this;
    }

    @Override
    public Times fadeOut(final int duration) {
      this.fadeOut = duration;
      return this;
    }

    Title.@NonNull Times build() {
      return new TitleImpl.TimesImpl(this.fadeIn, this.stay, this.fadeOut);
    }
  }
}
