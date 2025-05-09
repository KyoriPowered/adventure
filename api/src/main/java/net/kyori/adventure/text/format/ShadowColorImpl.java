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
package net.kyori.adventure.text.format;

import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ShadowColorImpl implements ShadowColor, Examinable {
  static final int NONE_VALUE = 0;
  static final ShadowColorImpl NONE = new ShadowColorImpl(NONE_VALUE);

  private final int value; // ARGB

  ShadowColorImpl(final int value) {
    this.value = value;
  }

  @Override
  public int value() {
    return this.value;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (!(other instanceof ShadowColorImpl)) return false;
    final ShadowColorImpl that = (ShadowColorImpl) other;
    return this.value == that.value;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(this.value);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("value", this.value) // todo: represent as hex?
    );
  }
}
