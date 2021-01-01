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
package net.kyori.adventure.text.serializer.legacy;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/*
 * This is a hack.
 */

/**
 * A <b>legacy</b> format.
 *
 * @since 4.0.0
 */
public final class LegacyFormat implements Examinable {
  static final LegacyFormat RESET = new LegacyFormat(true);
  private final @Nullable NamedTextColor color;
  private final @Nullable TextDecoration decoration;
  private final boolean reset;

  /*
   * Separate constructors to ensure a format can never be more than one thing.
   */

  LegacyFormat(final @Nullable NamedTextColor color) {
    this.color = color;
    this.decoration = null;
    this.reset = false;
  }

  LegacyFormat(final @Nullable TextDecoration decoration) {
    this.color = null;
    this.decoration = decoration;
    this.reset = false;
  }

  private LegacyFormat(final boolean reset) {
    this.color = null;
    this.decoration = null;
    this.reset = reset;
  }

  /**
   * Gets the color.
   *
   * @return the color
   * @since 4.0.0
   */
  public @Nullable TextColor color() {
    return this.color;
  }

  /**
   * Gets the decoration.
   *
   * @return the decoration
   * @since 4.0.0
   */
  public @Nullable TextDecoration decoration() {
    return this.decoration;
  }

  /**
   * Gets if this format is a reset.
   *
   * @return {@code true} if a reset, {@code false} otherwise
   * @since 4.0.0
   */
  public boolean reset() {
    return this.reset;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final LegacyFormat that = (LegacyFormat) other;
    return this.color == that.color && this.decoration == that.decoration && this.reset == that.reset;
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.color);
    result = (31 * result) + Objects.hashCode(this.decoration);
    result = (31 * result) + Boolean.hashCode(this.reset);
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("decoration", this.decoration),
      ExaminableProperty.of("reset", this.reset)
    );
  }
}
