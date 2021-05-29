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
import net.kyori.adventure.util.TriState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class PointersImpl implements Pointers {
  static final Pointers EMPTY = new Pointers() {
    @Override
    public @NonNull <T> Optional<T> get(final @NonNull Pointer<T> pointer) {
      return Optional.empty();
    }

    @Override
    public @NonNull <T> TriState has(final @NonNull Pointer<T> pointer) {
      return TriState.NOT_SET;
    }

    @Override
    public Pointers.@NonNull Builder toBuilder() {
      return new PointersImpl.BuilderImpl();
    }
  };

  private final Map<Pointer<?>, Supplier<?>> pointers;
  private final Supplier<Pointered> parent;

  PointersImpl(final @NonNull BuilderImpl builder) {
    this.pointers = new HashMap<>(builder.pointers);
    this.parent = builder.parent;
  }

  @Override
  @SuppressWarnings("unchecked") // all values are checked on entry
  public @NonNull <T> Optional<T> get(final @NonNull Pointer<T> pointer) {
    if(!this.pointers.containsKey(pointer)) return this.parent.get().pointers().get(pointer);
    return Optional.ofNullable(((Supplier<T>) this.pointers.get(pointer)).get());
  }

  @Override
  @SuppressWarnings("unchecked") // all values are checked on entry
  public @NonNull <T> TriState has(final @NonNull Pointer<T> pointer) {
    if(!this.pointers.containsKey(Objects.requireNonNull(pointer, "pointer"))) {
      return this.parent.get().pointers().has(pointer);
    }
    return TriState.byBoolean(((Supplier<T>) this.pointers.get(pointer)).get() != null);
  }

  @Override
  public Pointers.@NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl implements Builder {
    private static final Pointered EMPTY_POINTERED = new Pointered() {};
    private static final Supplier<Pointered> EMPTY_PARENT = () -> EMPTY_POINTERED;

    private final Map<Pointer<?>, Supplier<?>> pointers;
    private Supplier<Pointered> parent;

    BuilderImpl() {
      this.pointers = new HashMap<>();
      this.parent = EMPTY_PARENT;
    }

    BuilderImpl(final @NonNull PointersImpl pointers) {
      this.pointers = new HashMap<>(pointers.pointers);
      this.parent = EMPTY_PARENT;
    }

    @Override
    public @NonNull <T> Builder addPointerWithVariableValue(final @NonNull Pointer<T> pointer, final @NonNull Supplier<@Nullable T> value) {
      this.pointers.put(Objects.requireNonNull(pointer, "pointer"), Objects.requireNonNull(value, "value"));
      return this;
    }

    @Override
    public @NonNull Builder parent(final @NonNull Supplier<Pointered> parent) {
      this.parent = Objects.requireNonNull(parent, "parent");
      return this;
    }

    @Override
    public @NonNull Pointers build() {
      return new PointersImpl(this);
    }
  }
}
