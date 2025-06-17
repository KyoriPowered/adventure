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
package net.kyori.adventure.text.event;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

abstract class PayloadImpl implements ClickEvent.Payload {
  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static final class TextImpl extends PayloadImpl implements ClickEvent.Payload.Text {
    private final String value;

    TextImpl(final @NotNull String value) {
      this.value = value;
    }

    @Override
    public @NotNull String value() {
      return this.value;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("value", this.value)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final TextImpl that = (TextImpl) other;
      return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
      return this.value.hashCode();
    }
  }

  static final class IntImpl extends PayloadImpl implements ClickEvent.Payload.Int {
    private final int integer;

    IntImpl(final int integer) {
      this.integer = integer;
    }

    @Override
    public int integer() {
      return this.integer;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("integer", this.integer)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final IntImpl that = (IntImpl) other;
      return Objects.equals(this.integer, that.integer);
    }

    @Override
    public int hashCode() {
      return this.integer;
    }
  }

  static final class DialogImpl extends PayloadImpl implements ClickEvent.Payload.Dialog {
    private final DialogLike dialogLike;

    DialogImpl(final @NotNull DialogLike dialogLike) {
      this.dialogLike = dialogLike;
    }

    @Override
    public @NotNull DialogLike dialog() {
      return this.dialogLike;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("dialog", this.dialogLike)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final DialogImpl that = (DialogImpl) other;
      return Objects.equals(this.dialogLike, that.dialogLike);
    }

    @Override
    public int hashCode() {
      return this.dialogLike.hashCode();
    }
  }

  static final class CustomImpl extends PayloadImpl implements ClickEvent.Payload.Custom {
    private final Key key;
    private final String data;

    CustomImpl(final @NotNull Key key, final @NotNull String data) {
      this.key = key;
      this.data = data;
    }

    @Override
    public @NotNull Key key() {
      return this.key;
    }

    @Override
    public @NotNull String data() {
      return this.data;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("data", this.data)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (other == null || getClass() != other.getClass()) return false;
      final CustomImpl that = (CustomImpl) other;
      return Objects.equals(this.key, that.key) && Objects.equals(this.data, that.data);
    }

    @Override
    public int hashCode() {
      int result = this.key.hashCode();
      result = (31 * result) + this.data.hashCode();
      return result;
    }
  }
}
