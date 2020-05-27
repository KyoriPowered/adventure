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

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;

/* package */ final class TitleImpl implements Examinable, Title {
  private final Component title;
  private final Component subtitle;
  private final Duration fadeInTime;
  private final Duration stayTime;
  private final Duration fadeOutTime;

  /* package */ TitleImpl(final @NonNull Component title, final @NonNull Component subtitle, final @NonNull Duration fadeInTime, final @NonNull Duration stayTime, final @NonNull Duration fadeOutTime) {
    this.title = title;
    this.subtitle = subtitle;
    this.fadeInTime = fadeInTime;
    this.stayTime = stayTime;
    this.fadeOutTime = fadeOutTime;
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
  public @NonNull Duration fadeInTime() {
    return this.fadeInTime;
  }

  @Override
  public @NonNull Duration stayTime() {
    return this.stayTime;
  }

  @Override
  public @NonNull Duration fadeOutTime() {
    return this.fadeOutTime;
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final TitleImpl that = (TitleImpl) other;
    return Objects.equals(this.title, that.title)
      && Objects.equals(this.subtitle, that.subtitle)
      && Objects.equals(this.fadeInTime, that.fadeInTime)
      && Objects.equals(this.stayTime, that.stayTime)
      && Objects.equals(this.fadeOutTime, that.fadeOutTime);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.title);
    result = (31 * result) + Objects.hashCode(this.subtitle);
    result = (31 * result) + Objects.hashCode(this.fadeInTime);
    result = (31 * result) + Objects.hashCode(this.stayTime);
    result = (31 * result) + Objects.hashCode(this.fadeOutTime);
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("title", this.title),
      ExaminableProperty.of("subtitle", this.subtitle),
      ExaminableProperty.of("fadeInTime", this.fadeInTime),
      ExaminableProperty.of("stayTime", this.stayTime),
      ExaminableProperty.of("fadeOutTime", this.fadeOutTime)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
