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
package net.kyori.adventure.text;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslationArgumentImpl {
  private static final Component TRUE = Component.text("true");
  private static final Component FALSE = Component.text("false");

  private TranslationArgumentImpl() {
  }

  static final class BooleanImpl implements TranslationArgument.Boolean {
    private final boolean value;

    BooleanImpl(final boolean value) {
      this.value = value;
    }

    @Override
    public java.lang.@NotNull Boolean value() {
      return this.value;
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component asComponent() {
      return this.value ? TRUE : FALSE;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final BooleanImpl that = (BooleanImpl) other;
      return this.value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.value);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("value", this.value)
      );
    }
  }

  static final class NumericImpl implements TranslationArgument.Numeric {
    private final Number value;

    NumericImpl(final Number value) {
      this.value = value;
    }

    @Override
    public @NotNull Number value() {
      return this.value;
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component asComponent() {
      return net.kyori.adventure.text.Component.text(String.valueOf(this.value));
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final NumericImpl that = (NumericImpl) other;
      return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.value);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("value", this.value)
      );
    }
  }

  static final class ComponentImpl implements TranslationArgument.Component {
    private final net.kyori.adventure.text.Component value;

    ComponentImpl(final net.kyori.adventure.text.Component value) {
      this.value = value;
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component value() {
      return this.value;
    }

    @Override
    public net.kyori.adventure.text.@NotNull Component asComponent() {
      return this.value;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final ComponentImpl that = (ComponentImpl) other;
      return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.value);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("value", this.value)
      );
    }
  }
}
