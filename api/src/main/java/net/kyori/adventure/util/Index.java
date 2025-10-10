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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A bi-directional map in which keys and values must be unique.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 4.0.0
 */
sealed interface Index<K, V> permits IndexImpl {

  /**
   * Creates an index map.
   *
   * @param type the value type
   * @param keyFunction the key function
   * @param <K> the key type
   * @param <V> the value type
   * @return the key map
   * @since 4.0.0
   */
  static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, final Function<? super V, ? extends K> keyFunction) {
    return IndexImpl.create(type, keyFunction, type.getEnumConstants());
  }

  /**
   * Creates an index map.
   *
   * @param type the value type
   * @param keyFunction the key function
   * @param values the values
   * @param <K> the key type
   * @param <V> the value type
   * @return the key map
   * @since 4.0.0
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, final Function<? super V, ? extends K> keyFunction, final V... values) {
    return IndexImpl.create(values, length -> new EnumMap<>(type), keyFunction);
  }

  /**
   * Creates an index map.
   *
   * @param keyFunction the key function
   * @param values the values
   * @param <K> the key type
   * @param <V> the value type
   * @return the key map
   * @since 4.0.0
   */
  @SafeVarargs
  @SuppressWarnings("varargs")
  static <K, V> Index<K, V> create(final Function<? super V, ? extends K> keyFunction, final V... values) {
    return IndexImpl.create(values, HashMap::new, keyFunction);
  }

  /**
   * Creates an index map.
   *
   * @param keyFunction the key function
   * @param constants the constants
   * @param <K> the key type
   * @param <V> the value type
   * @return the key map
   * @since 4.0.0
   */
  static <K, V> Index<K, V> create(final Function<? super V, ? extends K> keyFunction, final List<V> constants) {
    return IndexImpl.create(constants, HashMap::new, keyFunction);
  }

  /**
   * Gets the keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  Set<K> keys();

  /**
   * Gets the key for a value.
   *
   * @param value the value
   * @return the key
   * @since 4.0.0
   */
  @Nullable K key(final V value);

  /**
   * Gets the key for a value or throws an exception.
   *
   * @param value the value
   * @return the key
   * @throws NoSuchElementException if there is no key for the value
   * @since 4.11.0
   */
  K keyOrThrow(final V value);

  /**
   * Gets a key by its value or returns a fallback key.
   *
   * @param value the value
   * @param defaultKey the fallback key
   * @return the key
   * @since 4.11.0
   */
  @Contract("_, null -> null; _, !null -> !null")
  K keyOr(final V value, final @Nullable K defaultKey);

  /**
   * Gets the keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  Set<V> values();

  /**
   * Gets a value by its key.
   *
   * @param key the key
   * @return the value
   * @since 4.0.0
   */
  @Nullable V value(final K key);

  /**
   * Gets a value by its key.
   *
   * @param key the key
   * @return the value
   * @throws NoSuchElementException if there is no value for the key
   * @since 4.11.0
   */
  V valueOrThrow(final K key);

  /**
   * Gets a value by its key or returns a fallback value.
   *
   * @param key the key
   * @param defaultValue the fallback value
   * @return the value
   * @since 4.11.0
   */
  @Contract("_, null -> null; _, !null -> !null")
  V valueOr(final K key, final @Nullable V defaultValue);

  /**
   * Get an unmodifiable mapping of index entries from key to value.
   *
   * @return a mapping from key to value in the index
   * @since 4.10.0
   */
  Map<K, V> keyToValue();

  /**
   * Get an unmodifiable mapping of index entries from value to key.
   *
   * @return a mapping from value to key in the index
   * @since 4.10.0
   */
  Map<V, K> valueToKey();
}
