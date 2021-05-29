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

import java.util.Optional;
import java.util.function.Supplier;
import net.kyori.adventure.util.Buildable;
import net.kyori.adventure.util.TriState;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.jetbrains.annotations.Contract;

/**
 * A collection of {@link Pointer pointers}.
 *
 * @since 4.8.0
 */
public interface Pointers extends Buildable<Pointers, Pointers.Builder> {
  /**
   * An empty set of pointers.
   *
   * @since 4.8.0
   */
  @NonNull Pointers EMPTY = PointersImpl.EMPTY;

  /**
   * Gets a new pointers builder.
   *
   * @return the builder
   * @see Builder
   * @since 4.8.0
   */
  @Contract(value = "-> new", pure = true)
  static @NonNull Builder builder() {
    return new PointersImpl.BuilderImpl();
  }

  /**
   * Gets the value of {@code pointer}.
   *
   * @param pointer the pointer
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  <T> @NonNull Optional<T> get(final @NonNull Pointer<T> pointer);

  /**
   * Gets the value of {@code pointer}.
   *
   * <p>If this {@code Audience} is unable to provide a value for {@code pointer}, {@code defaultValue} will be returned.</p>
   *
   * @param pointer the pointer
   * @param defaultValue the default value
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @PolyNull T getOrDefault(final @NonNull Pointer<T> pointer, final @PolyNull T defaultValue) {
    return this.get(pointer).orElse(defaultValue);
  }

  /**
   * Gets the value of {@code pointer}.
   *
   * <p>If this {@code Audience} is unable to provide a value for {@code pointer}, the value supplied by {@code defaultValue} will be returned.</p>
   *
   * @param pointer the pointer
   * @param defaultValue the default value supplier
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @PolyNull T getOrDefaultFrom(final @NonNull Pointer<T> pointer, final @NonNull Supplier<? extends T> defaultValue) {
    return this.get(pointer).orElseGet(defaultValue);
  }

  /**
   * Checks if these pointers contain a value for the given pointer.
   *
   * @param pointer the pointer
   * @param <T> the type
   * @return a tri-state
   * @since 4.8.0
   */
  <T> @NonNull TriState has(final @NonNull Pointer<T> pointer);

  /**
   * A builder of pointers.
   *
   * @see Pointers
   * @since 4.8.0
   */
  interface Builder extends Buildable.Builder<Pointers> {
    /**
     * Adds a pointer without a value.
     *
     * @param pointer the pointer
     * @param <T> the type
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_ -> this")
    default <T> @NonNull Builder addPointer(final @NonNull Pointer<T> pointer) {
      return this.addPointerWithFixedValue(pointer, null);
    }

    /**
     * Adds a pointer with an optional value.
     *
     * @param pointer the pointer
     * @param value the optional value
     * @param <T> the type
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_, _ -> this")
    default <T> @NonNull Builder addPointerWithFixedValue(final @NonNull Pointer<T> pointer, @Nullable T value) {
      return this.addPointerWithVariableValue(pointer, () -> value);
    }

    /**
     * Adds a pointer with an optional value.
     *
     * @param pointer the pointer
     * @param value the optional value
     * @param <T> the type
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_, _ -> this")
    <T> @NonNull Builder addPointerWithVariableValue(final @NonNull Pointer<T> pointer, @NonNull Supplier<@Nullable T> value);

    /**
     * Adds a parent from which values will be retrieved if they do not exist in this collection.
     *
     * @param parent the parent
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_ -> this")
    default @NonNull Builder parent(final @NonNull Pointered parent) {
      return this.parent(() -> parent);
    }

    /**
     * Adds a parent from which values will be retrieved if they do not exist in this collection.
     *
     * @param parent the parent
     * @return this builder
     * @since 4.8.0
     */
    @NonNull Builder parent(final @NonNull Supplier<Pointered> parent);
  }
}
