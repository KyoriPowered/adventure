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
package net.kyori.adventure.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Supplier;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An iterable that delegates the {@link #iterator()} and {@link #spliterator()} calls to a {@link Supplier}.
 *
 * @param <T> the type of the iterable
 * @since 4.8.0
 */
public final class DelegatingIterable<T> implements Iterable<T> {
  private final Supplier<Iterator<T>> iteratorSupplier;
  private final Supplier<Spliterator<T>> spliteratorSupplier;

  /**
   * Creates a new delegating iterable.
   *
   * @param iteratorSupplier the iterator supplier
   * @param spliteratorSupplier the spliterator supplier
   * @since 4.8.0
   */
  public DelegatingIterable(final @NonNull Supplier<Iterator<T>> iteratorSupplier, final @NonNull Supplier<Spliterator<T>> spliteratorSupplier) {
    this.iteratorSupplier = Objects.requireNonNull(iteratorSupplier, "iteratorSupplier");
    this.spliteratorSupplier = Objects.requireNonNull(spliteratorSupplier, "spliteratorSupplier");
  }

  @Override
  public @NonNull Iterator<T> iterator() {
    return this.iteratorSupplier.get();
  }

  @Override
  public @NonNull Spliterator<T> spliterator() {
    return this.spliteratorSupplier.get();
  }
}
