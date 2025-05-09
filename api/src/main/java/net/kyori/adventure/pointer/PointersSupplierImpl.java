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
package net.kyori.adventure.pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PointersSupplierImpl<T> implements PointersSupplier<T> {
  private final PointersSupplier<? super T> parent;
  private final Map<Pointer<?>, Function<T, ?>> resolvers;

  PointersSupplierImpl(final @NotNull BuilderImpl<T> builder) {
    this.parent = builder.parent;
    this.resolvers = new HashMap<>(builder.resolvers);
  }

  @Override
  public @NotNull Pointers view(final @NotNull T instance) {
    return new ForwardingPointers<>(instance, this);
  }

  @Override
  public <P> boolean supports(final @NotNull Pointer<P> pointer) {
    if (this.resolvers.containsKey(Objects.requireNonNull(pointer, "pointer"))) {
      return true;
    } else if (this.parent == null) {
      return false;
    } else {
      return this.parent.supports(pointer);
    }
  }

  @Override
  @SuppressWarnings("unchecked") // all values are checked on entry
  public @Nullable <P> Function<? super T, P> resolver(final @NotNull Pointer<P> pointer) {
    final Function<? super T, ?> resolver = this.resolvers.get(Objects.requireNonNull(pointer, "pointer"));

    if (resolver != null) {
      return (Function<? super T, P>) resolver;
    } else if (this.parent == null) {
      return null;
    } else {
      return this.parent.resolver(pointer);
    }
  }

  static final class ForwardingPointers<U> implements Pointers {
    private final U instance;
    private final PointersSupplierImpl<U> supplier;

    ForwardingPointers(final @NotNull U instance, final @NotNull PointersSupplierImpl<U> supplier) {
      this.instance = instance;
      this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked") // all values are checked on entry
    public @NotNull <T> Optional<T> get(final @NotNull Pointer<T> pointer) {
      Function<? super U, ?> resolver = this.supplier.resolvers.get(Objects.requireNonNull(pointer, "pointer"));

      // Fallback to the parent.
      if (resolver == null) {
        resolver = this.supplier.parent.resolver(pointer);
      }

      // Finally, wrap in an optional.
      if (resolver == null) {
        return Optional.empty();
      } else {
        return Optional.ofNullable((T) resolver.apply(this.instance));
      }
    }

    @Override
    public <T> boolean supports(final @NotNull Pointer<T> pointer) {
      return this.supplier.supports(pointer);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // all values are checked on entry
    public Pointers.@NotNull Builder toBuilder() {
      final Pointers.Builder builder = this.supplier.parent == null ? Pointers.builder() : this.supplier.parent.view(this.instance).toBuilder();

      for (final Map.Entry<Pointer<?>, Function<U, ?>> entry : this.supplier.resolvers.entrySet()) {
        builder.withDynamic(entry.getKey(), (Supplier) () -> entry.getValue().apply(this.instance));
      }

      return builder;
    }
  }

  static final class BuilderImpl<T> implements Builder<T> {
    private PointersSupplier<? super T> parent = null;
    private final Map<Pointer<?>, Function<T, ?>> resolvers;

    BuilderImpl() {
      this.resolvers = new HashMap<>();
    }

    @Override
    public @NotNull Builder<T> parent(final @Nullable PointersSupplier<? super T> parent) {
      this.parent = parent;
      return this;
    }

    @Override
    public @NotNull <P> Builder<T> resolving(final @NotNull Pointer<P> pointer, final @NotNull Function<T, P> resolver) {
      this.resolvers.put(pointer, resolver);
      return this;
    }

    @Override
    public @NotNull PointersSupplier<T> build() {
      return new PointersSupplierImpl<>(this);
    }
  }
}
