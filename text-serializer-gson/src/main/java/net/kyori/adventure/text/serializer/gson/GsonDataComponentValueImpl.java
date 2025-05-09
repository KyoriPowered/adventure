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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GsonDataComponentValueImpl implements GsonDataComponentValue {
  private final JsonElement element;

  GsonDataComponentValueImpl(final @NotNull JsonElement element) {
    this.element = element;
  }

  @Override
  public @NotNull JsonElement element() {
    return this.element;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("element", this.element)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    final GsonDataComponentValueImpl that = (GsonDataComponentValueImpl) other;
    return Objects.equals(this.element, that.element);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.element);
  }

  static final class RemovedGsonComponentValueImpl extends GsonDataComponentValueImpl implements DataComponentValue.Removed {
    static final RemovedGsonComponentValueImpl INSTANCE = new RemovedGsonComponentValueImpl();

    private RemovedGsonComponentValueImpl() {
      super(JsonNull.INSTANCE);
    }
  }
}
