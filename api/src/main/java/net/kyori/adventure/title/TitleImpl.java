/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import static java.util.Objects.requireNonNull;

record TitleImpl(Component title, Component subtitle, @Nullable Times times) implements Title {
  TitleImpl(final @NotNull Component title, final @NotNull Component subtitle, final @Nullable Times times) {
    this.title = requireNonNull(title, "title");
    this.subtitle = requireNonNull(subtitle, "subtitle");
    this.times = times;
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
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("title", this.title),
      ExaminableProperty.of("subtitle", this.subtitle),
      ExaminableProperty.of("times", this.times)
    );
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  record TimesImpl(Duration fadeIn, Duration stay, Duration fadeOut) implements Times {
      TimesImpl(final @NotNull Duration fadeIn, final @NotNull Duration stay, final @NotNull Duration fadeOut) {
        this.fadeIn = requireNonNull(fadeIn, "fadeIn");
        this.stay = requireNonNull(stay, "stay");
        this.fadeOut = requireNonNull(fadeOut, "fadeOut");
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
      public @NotNull String toString() {
        return Internals.toString(this);
      }
    }
}
