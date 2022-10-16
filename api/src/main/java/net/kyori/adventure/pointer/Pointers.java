/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A collection of {@link Pointer pointers}.
 *
 * @since 4.8.0
 */
public interface Pointers extends Buildable<Pointers, Pointers.Builder> {
  /**
   * Gets an empty pointers collection.
   *
   * @return the pointers
   * @since 4.8.0
   */
  @Contract(pure = true)
  static @NotNull Pointers empty() {
    return PointersImpl.EMPTY;
  }

  /**
   * Gets a new pointers builder.
   *
   * @return the builder
   * @see Builder
   * @since 4.8.0
   */
  @Contract(pure = true)
  static @NotNull Builder builder() {
    return new PointersImpl.BuilderImpl();
  }

  /**
   * Gets all {@code pointer} in this collection which have the given value type.
   *
   * @param valueType the type class of the pointer
   * @param <V> the type of the pointer
   * @return an unmodifiable set of pointers filtered by the given type
   * @since 4.10.0
   */
  @SuppressWarnings("unchecked")
  default <V> @NotNull @Unmodifiable Set<Pointer<V>> pointers(final @NotNull Class<V> valueType) {
    Objects.requireNonNull(valueType, "valueType");
    final Set<Pointer<V>> filteredPointers = new HashSet<>();
    for (final Pointer<?> pointer : this.pointers()) {
      if (valueType.isAssignableFrom(pointer.type()))
        filteredPointers.add((Pointer<V>) pointer);
    }
    return Collections.unmodifiableSet(filteredPointers);
  }

  /**
   * Gets all {@code pointer} in this pointer collection.
   *
   * @return an unmodifiable set of pointers in this pointer collection
   * @throws UnsupportedOperationException if the implementing class does not support querying for pointers
   * @since 4.10.0
   */
  default @NotNull @Unmodifiable Set<Pointer<?>> pointers() {
    throw new UnsupportedOperationException("The underlying Pointers implementation does not support querying for pointers.");
  }

  /**
   * Gets the value of {@code pointer}.
   *
   * @param pointer the pointer
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  <T> @NotNull Optional<T> get(final @NotNull Pointer<T> pointer);

  /**
   * Gets the value of {@code pointer}.
   *
   * <p>If a value for {@code pointer} is unable to be provided, {@code defaultValue} will be returned.</p>
   *
   * @param pointer the pointer
   * @param defaultValue the default value
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  @Contract("_, null -> _; _, !null -> !null")
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @Nullable T getOrDefault(final @NotNull Pointer<T> pointer, final @Nullable T defaultValue) {
    return this.get(pointer).orElse(defaultValue);
  }

  /**
   * Gets the value of {@code pointer}.
   *
   * <p>If a value for {@code pointer} is unable to be provided, the value supplied by {@code defaultValue} will be returned.</p>
   *
   * @param pointer the pointer
   * @param defaultValue the default value supplier
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @UnknownNullability T getOrDefaultFrom(final @NotNull Pointer<T> pointer, final @NotNull Supplier<? extends T> defaultValue) {
    return this.get(pointer).orElseGet(defaultValue);
  }

  /**
   * Checks if a given pointer is supported.
   *
   * <p>This will return {@code true} when a mapping for the provided pointer exists, even if the value for the pointer is {@code null}.</p>
   *
   * @param pointer the pointer
   * @param <T> the type
   * @return if the pointer is supported
   * @since 4.8.0
   */
  <T> boolean supports(final @NotNull Pointer<T> pointer);

  /**
   * A builder of pointers.
   *
   * @see Pointers
   * @since 4.8.0
   */
  interface Builder extends AbstractBuilder<Pointers>, Buildable.Builder<Pointers> {
    /**
     * Adds a pointer with a static, optional value.
     *
     * @param pointer the pointer
     * @param value the optional value
     * @param <T> the type
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_, _ -> this")
    default <T> @NotNull Builder withStatic(final @NotNull Pointer<T> pointer, final @Nullable T value) {
      return this.withDynamic(pointer, () -> value);
    }

    /**
     * Adds a pointer with a dynamic value provided by a supplier.
     *
     * @param pointer the pointer
     * @param value the value supplier
     * @param <T> the type
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_, _ -> this")
    <T> @NotNull Builder withDynamic(final @NotNull Pointer<T> pointer, @NotNull Supplier<@Nullable T> value);
  }
}
