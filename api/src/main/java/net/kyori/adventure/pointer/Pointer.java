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

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A pointer to a resource.
 *
 * @param <V> the value type
 * @since 4.8.0
 */
public interface Pointer<V> extends Examinable {
  /**
   * Creates a pointer.
   *
   * @param type the value type
   * @param key the key
   * @param <V> the value type
   * @return the pointer
   * @since 4.8.0
   */
  static <V> @NotNull Pointer<V> pointer(final @NotNull Class<V> type, final @NotNull Key key) {
    return new PointerImpl<>(type, key);
  }

  /**
   * Creates a forward pointer to a pointer of another pointered resource.
   *
   * <p>The forwarder returns a null value of the pointer does not exist or its value is null in the target.</p>
   *
   * @param pointered the forward target
   * @param targetPointer the pointer on the forward target
   * @param <T> the value type of the pointer
   * @return a supplier that forwards to the given pointer
   * @since 4.10.0
   */
  static <T> @NotNull Supplier<T> forward(@NotNull final Pointered pointered, @NotNull final Pointer<T> targetPointer) {
    return forwardWithDefault(pointered, targetPointer, null);
  }

  /**
   * Creates a forward pointer with a default value to a pointer of another pointered resource.
   *
   * <p>The forwarder returns the default value if the pointer does not exist in the target.</p>
   *
   * @param pointered the forward target
   * @param targetPointer the pointer on the forward target
   * @param defaultValue the default value to use if the target pointer does not exist
   * @param <T> the value type of the pointer
   * @return a supplier that forwards to the given pointer
   * @since 4.10.0
   */
  static <T> @NotNull Supplier<T> forwardWithDefault(@NotNull final Pointered pointered, @NotNull final Pointer<T> targetPointer, @Nullable final T defaultValue) {
    Objects.requireNonNull(pointered, "pointered");
    Objects.requireNonNull(targetPointer, "targetPointer");
    return () -> pointered.getOrDefault(targetPointer, defaultValue);
  }

  /**
   * Gets the value type.
   *
   * @return the value type
   * @since 4.8.0
   */
  @NotNull Class<V> type();

  /**
   * Gets the key.
   *
   * @return the key
   * @since 4.8.0
   */
  @NotNull Key key();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("type", this.type()),
      ExaminableProperty.of("key", this.key())
    );
  }
}
