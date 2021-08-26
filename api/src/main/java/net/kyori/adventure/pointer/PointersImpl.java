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
package net.kyori.adventure.pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PointersImpl implements Pointers {
  static final Pointers EMPTY = new Pointers() {
    @Override
    public @NotNull <T> Optional<T> get(final @NotNull Pointer<T> pointer) {
      return Optional.empty();
    }

    @Override
    public <T> boolean supports(final @NotNull Pointer<T> pointer) {
      return false;
    }

    @Override
    public Pointers.@NotNull Builder toBuilder() {
      return new PointersImpl.BuilderImpl();
    }

    @Override
    public String toString() {
      return "EmptyPointers";
    }
  };

  private final Map<Pointer<?>, Supplier<?>> pointers;

  PointersImpl(final @NotNull BuilderImpl builder) {
    this.pointers = new HashMap<>(builder.pointers);
  }

  @Override
  @SuppressWarnings("unchecked") // all values are checked on entry
  public @NotNull <T> Optional<T> get(final @NotNull Pointer<T> pointer) {
    Objects.requireNonNull(pointer, "pointer");
    final Supplier<?> supplier = this.pointers.get(pointer);
    if (supplier == null) {
      return Optional.empty();
    } else {
      return Optional.ofNullable((T) supplier.get());
    }
  }

  @Override
  public <T> boolean supports(final @NotNull Pointer<T> pointer) {
    Objects.requireNonNull(pointer, "pointer");
    return this.pointers.containsKey(pointer);
  }

  @Override
  public @NotNull Pointers.Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private final Map<Pointer<?>, Supplier<?>> pointers;

    BuilderImpl() {
      this.pointers = new HashMap<>();
    }

    BuilderImpl(final @NotNull PointersImpl pointers) {
      this.pointers = new HashMap<>(pointers.pointers);
    }

    @Override
    public @NotNull <T> Builder withDynamic(final @NotNull Pointer<T> pointer, final @NotNull Supplier<@Nullable T> value) {
      this.pointers.put(Objects.requireNonNull(pointer, "pointer"), Objects.requireNonNull(value, "value"));
      return this;
    }

    @Override
    public @NotNull Pointers build() {
      return new PointersImpl(this);
    }
  }
}
