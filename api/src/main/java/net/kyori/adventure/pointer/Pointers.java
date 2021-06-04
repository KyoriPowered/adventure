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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

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
  @NotNull Pointers EMPTY = PointersImpl.EMPTY;

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
   * <p>If this {@code Audience} is unable to provide a value for {@code pointer}, {@code defaultValue} will be returned.</p>
   *
   * @param pointer the pointer
   * @param defaultValue the default value
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  @Contract("_, !null -> !null; _, null -> null")
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @Nullable T getOrDefault(final @NotNull Pointer<T> pointer, final @Nullable T defaultValue) {
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
  default <T> @UnknownNullability T getOrDefaultFrom(final @NotNull Pointer<T> pointer, final @NotNull Supplier<? extends T> defaultValue) {
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
  <T> @NotNull TriState has(final @NotNull Pointer<T> pointer);

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
    default <T> @NotNull Builder addPointer(final @NotNull Pointer<T> pointer) {
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
    default <T> @NotNull Builder addPointerWithFixedValue(final @NotNull Pointer<T> pointer, @Nullable T value) {
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
    <T> @NotNull Builder addPointerWithVariableValue(final @NotNull Pointer<T> pointer, @NotNull Supplier<@Nullable T> value);

    /**
     * Adds a parent from which values will be retrieved if they do not exist in this collection.
     *
     * @param parent the parent
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_ -> this")
    default @NotNull Builder parent(final @NotNull Pointered parent) {
      return this.parent(() -> parent);
    }

    /**
     * Adds a parent from which values will be retrieved if they do not exist in this collection.
     *
     * @param parent the parent
     * @return this builder
     * @since 4.8.0
     */
    @NotNull Builder parent(final @NotNull Supplier<Pointered> parent);
  }
}
