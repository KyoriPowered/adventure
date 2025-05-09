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

import java.util.function.Function;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A supplier of {@link Pointers} that allows for the implementation of pointers
 * in a static context without having to manually create a new pointers instance for
 * each instance of a given type.
 *
 * <p>An example of how this could be implemented is as follows:</p>
 * <pre>
 * public class MyPointeredObject extends SomePointeredParent implements Pointered {
 *   private static final PointersSupplier&lt;MyPointeredObject&gt; POINTERS = PointersSupplier.builder()
 *     .parent(SomePointeredParent.POINTERS) // Fallback to the parent to get pointers from.
 *     .resolving(Identity.UUID, MyPointeredObject::getUniqueId)
 *     .resolving(Identity.DISPLAY_NAME, MyPointeredObject::getDisplayName)
 *     .build();
 *
 *   &#64;Override
 *   public Pointers pointers() {
 *     return POINTERS.view(this);
 *   }
 * }
 * </pre>
 *
 * @param <T> the type
 * @since 4.17.0
 */
public interface PointersSupplier<T> {
  /**
   * Gets a new pointers supplier builder.
   *
   * @param <T> the type
   * @return the builder
   * @since 4.17.0
   */
  static <T> @NotNull Builder<T> builder() {
    return new PointersSupplierImpl.BuilderImpl<>();
  }

  /**
   * Creates a pointers view for the given instance.
   *
   * @param instance the instance
   * @return the view
   * @since 4.17.0
   */
  @NotNull Pointers view(final @NotNull T instance);

  /**
   * Checks if this supplier supports a given pointer.
   *
   * @param pointer the pointer
   * @param <P> the type of the pointer
   * @return if this supplier supports a given pointer
   * @since 4.17.0
   */
  <P> boolean supports(final @NotNull Pointer<P> pointer);

  /**
   * Returns the resolver for a given pointer (if any).
   *
   * @param pointer the pointer
   * @param <P> the type of the pointer
   * @return the resolver, if any
   * @since 4.17.0
   */
  <P> @Nullable Function<? super T, P> resolver(final @NotNull Pointer<P> pointer);

  /**
   * A builder for {@link PointersSupplier}.
   *
   * @param <T> the type to supply pointers for
   * @since 4.17.0
   */
  interface Builder<T> extends AbstractBuilder<PointersSupplier<T>> {
    /**
     * Sets (or removes, if {@code null}) the parent pointer supplier that will be used
     * to resolve pointers that are not supplied by this supplier.
     *
     * @param parent the parent
     * @return this builder
     * @since 4.17.0
     */
    @Contract("_ -> this")
    @NotNull Builder<T> parent(final @Nullable PointersSupplier<? super T> parent);

    /**
     * Adds a resolver for a given pointer.
     *
     * @param pointer the pointer
     * @param resolver the resolver
     * @param <P> the type of the pointer
     * @return this builder
     * @since 4.17.0
     */
    @Contract("_, _ -> this")
    <P> @NotNull Builder<T> resolving(final @NotNull Pointer<P> pointer, final @NotNull Function<T, P> resolver);
  }
}
