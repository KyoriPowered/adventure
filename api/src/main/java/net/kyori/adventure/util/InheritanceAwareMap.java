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
package net.kyori.adventure.util;

import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A map type that will traverse class hierarchy to find a value for a key.
 *
 * <p>These maps are null-hostile, so both keys and values must not be null.</p>
 *
 * <p>There is a concept of <em>strict</em> mode, where map values have to be strictly non-ambiguous.
 * When this enabled (by default it is not), a value will not be added if any subtypes or supertypes are already registered to the map.</p>
 *
 * <p>Inheritance aware maps are always immutable, so any mutation operations will apply any changes to a new, modified instance.</p>
 *
 * @param <C> the base class type
 * @param <V> the value type
 * @since 4.17.0
 */
public interface InheritanceAwareMap<C, V> {
  /**
   * Get an empty inheritance aware map.
   *
   * @param <K> class type upper bound
   * @param <E> value type
   * @return the map
   * @since 4.17.0
   */
  @SuppressWarnings("unchecked")
  static <K, E> @NotNull InheritanceAwareMap<K, E> empty() {
    return InheritanceAwareMapImpl.EMPTY;
  }

  /**
   * Create a new builder for an inheritance aware map.
   *
   * @param <K> class type upper bound
   * @param <E> value type
   * @return a new builder
   * @since 4.17.0
   */
  static <K, E> InheritanceAwareMap.@NotNull Builder<K, E> builder() {
    return new InheritanceAwareMapImpl.BuilderImpl<>();
  }

  /**
   * Create a new builder for an inheritance aware map.
   *
   * @param <K> class type upper bound
   * @param <E> value type
   * @param existing the existing map to populate the builder with
   * @return a new builder
   * @since 4.17.0
   */
  static <K, E> InheritanceAwareMap.@NotNull Builder<K, E> builder(final InheritanceAwareMap<? extends K, ? extends E> existing) {
    return new InheritanceAwareMapImpl.BuilderImpl<K, E>()
      .putAll(existing);
  }

  /**
   * Check whether this map contains a value (direct or computed) for the provided class.
   *
   * @param clazz the class type to check
   * @return whether such a value is present
   * @since 4.17.0
   */
  boolean containsKey(final @NotNull Class<? extends C> clazz);

  /**
   * Get the applicable value for the provided class.
   *
   * <p>This can be either a direct or inherited value.</p>
   *
   * @param clazz the class type
   * @return the value, if any is available
   * @since 4.17.0
   */
  @Nullable V get(final @NotNull Class<? extends C> clazz);

  /**
   * Get an updated inheritance aware map with the provided key changed.
   *
   * @param clazz the class type
   * @param value the value to update to
   * @return the updated map
   * @since 4.17.0
   */
  @CheckReturnValue
  @NotNull InheritanceAwareMap<C, V> with(final @NotNull Class<? extends C> clazz, final @NotNull V value);

  /**
   * Get an updated inheritance aware map with the provided key removed.
   *
   * @param clazz the class type to remove a direct value for
   * @return the updated map
   * @since 4.17.0
   */
  @CheckReturnValue
  @NotNull InheritanceAwareMap<C, V> without(final @NotNull Class<? extends C> clazz);

  /**
   * A builder for inheritance-aware maps.
   *
   * @param <C> the class type
   * @param <V> the value type
   * @since 4.17.0
   */
  interface Builder<C, V> extends AbstractBuilder<InheritanceAwareMap<C, V>> {
    /**
     * Set strict mode for this builder.
     *
     * <p>If this builder has values from when it was not in strict mode, all previous values will be re-validated for any hierarchy ambiguities.</p>
     *
     * @param strict whether to enable strict mode.
     * @return this builder
     * @since 4.17.0
     */
    @NotNull Builder<C, V> strict(final boolean strict);

    /**
     * Put another value in this map.
     *
     * @param clazz the class type
     * @param value the value for the provided type and any subtypes
     * @return this builder
     * @since 4.17.0
     */
    @NotNull Builder<C, V> put(final @NotNull Class<? extends C> clazz, final @NotNull V value);

    /**
     * Remove a value in this map.
     *
     * @param clazz the class type
     * @return this builder
     * @since 4.17.0
     */
    @NotNull Builder<C, V> remove(final @NotNull Class<? extends C> clazz);

    /**
     * Put values from an existing inheritance-aware map into this map.
     *
     * @param map the existing map
     * @return this builder
     * @since 4.17.0
     */
    @NotNull Builder<C, V> putAll(final @NotNull InheritanceAwareMap<? extends C, ? extends V> map);
  }

}
