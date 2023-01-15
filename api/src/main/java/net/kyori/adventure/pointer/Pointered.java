/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Something that can retrieve values based on a given {@link Pointer}.
 *
 * @since 4.8.0
 */
public interface Pointered {
  /**
   * Gets the value of {@code pointer}.
   *
   * @param pointer the pointer
   * @param <T> the type
   * @return the value
   * @since 4.8.0
   */
  default <T> @NotNull Optional<T> get(final @NotNull Pointer<T> pointer) {
    return this.pointers().get(pointer);
  }

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
  @Contract("_, null -> _; _, !null -> !null")
  @SuppressWarnings("checkstyle:MethodName")
  default <T> @Nullable T getOrDefault(final @NotNull Pointer<T> pointer, final @Nullable T defaultValue) {
    return this.pointers().getOrDefault(pointer, defaultValue);
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
    return this.pointers().getOrDefaultFrom(pointer, defaultValue);
  }

  /**
   * Gets the pointers for this object.
   *
   * @return the pointers
   * @since 4.8.0
   */
  default @NotNull Pointers pointers() {
    return Pointers.empty();
  }
}
