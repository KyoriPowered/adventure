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
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class DataComponentValueConversionImpl<I, O> implements DataComponentValueConverterRegistry.Conversion<I, O> {
  private final Class<I> source;
  private final Class<O> destination;
  private final BiFunction<Key, I, O> conversion;

  DataComponentValueConversionImpl(final @NotNull Class<I> source, final @NotNull Class<O> destination, final @NotNull BiFunction<Key, I, O> conversion) {
    this.source = source;
    this.destination = destination;
    this.conversion = conversion;
  }

  @Override
  public @NotNull Class<I> source() {
    return this.source;
  }

  @Override
  public @NotNull Class<O> destination() {
    return this.destination;
  }

  @Override
  public @NotNull O convert(final @NotNull Key key, final @NotNull I input) {
    return this.conversion.apply(requireNonNull(key, "key"), requireNonNull(input, "input"));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("source", this.source),
      ExaminableProperty.of("destination", this.destination),
      ExaminableProperty.of("conversion", this.conversion)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    final DataComponentValueConversionImpl<?, ?> that = (DataComponentValueConversionImpl<?, ?>) other;
    return Objects.equals(this.source, that.source)
      && Objects.equals(this.destination, that.destination)
      && Objects.equals(this.conversion, that.conversion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.source, this.destination, this.conversion);
  }
}
