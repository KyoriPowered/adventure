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
package net.kyori.adventure.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A bi-directional map in which keys and values must be unique.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 4.0.0
 */
public final class Index<K, V> {
  private final Map<K, V> keyToValue;
  private final Map<V, K> valueToKey;

  private Index(final Map<K, V> keyToValue, final Map<V, K> valueToKey) {
    this.keyToValue = keyToValue;
    this.valueToKey = valueToKey;
  }

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
  public static <K, V extends Enum<V>> @NotNull Index<K, V> create(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction) {
    return create(type, keyFunction, type.getEnumConstants());
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
  public static <K, V extends Enum<V>> @NotNull Index<K, V> create(final Class<V> type, final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V@NotNull... values) {
    return create(values, length -> new EnumMap<>(type), keyFunction);
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
  public static <K, V> @NotNull Index<K, V> create(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull V@NotNull... values) {
    return create(values, HashMap::new, keyFunction);
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
  public static <K, V> @NotNull Index<K, V> create(final @NotNull Function<? super V, ? extends K> keyFunction, final @NotNull List<V> constants) {
    return create(constants, HashMap::new, keyFunction);
  }

  private static <K, V> @NotNull Index<K, V> create(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
    return create(Arrays.asList(values), valueToKeyFactory, keyFunction);
  }

  private static <K, V> @NotNull Index<K, V> create(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, final @NotNull Function<? super V, ? extends K> keyFunction) {
    final int length = values.size();
    final Map<K, V> keyToValue = new HashMap<>(length);
    final Map<V, K> valueToKey = valueToKeyFactory.apply(length); // to support using EnumMap instead of HashMap when possible
    for (int i = 0; i < length; i++) {
      final V value = values.get(i);
      final K key = keyFunction.apply(value);
      if (keyToValue.putIfAbsent(key, value) != null) {
        throw new IllegalStateException(String.format("Key %s already mapped to value %s", key, keyToValue.get(key)));
      }
      if (valueToKey.putIfAbsent(value, key) != null) {
        throw new IllegalStateException(String.format("Value %s already mapped to key %s", value, valueToKey.get(value)));
      }
    }
    return new Index<>(Collections.unmodifiableMap(keyToValue), Collections.unmodifiableMap(valueToKey));
  }

  /**
   * Gets the keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  public @NotNull Set<K> keys() {
    return Collections.unmodifiableSet(this.keyToValue.keySet());
  }

  /**
   * Gets the key for a value.
   *
   * @param value the value
   * @return the key
   * @since 4.0.0
   */
  public @Nullable K key(final @NotNull V value) {
    return this.valueToKey.get(value);
  }

  /**
   * Gets the key for a value or throws an exception.
   *
   * @param value the value
   * @return the key
   * @throws NoSuchElementException if there is no key for the value
   * @since 4.11.0
   */
  public @NotNull K keyOrThrow(final @NotNull V value) {
    final K key = this.key(value);
    if (key == null) {
      throw new NoSuchElementException("There is no key for value " + value);
    }
    return key;
  }

  /**
   * Gets a key by its value or returns a fallback key.
   *
   * @param value the value
   * @param defaultKey the fallback key
   * @return the key
   * @since 4.11.0
   */
  @Contract("_, null -> null; _, !null -> !null")
  public K keyOr(final @NotNull V value, final @Nullable K defaultKey) {
    final K key = this.key(value);
    return key == null ? defaultKey : key;
  }

  /**
   * Gets the keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  public @NotNull Set<V> values() {
    return Collections.unmodifiableSet(this.valueToKey.keySet());
  }

  /**
   * Gets a value by its key.
   *
   * @param key the key
   * @return the value
   * @since 4.0.0
   */
  public @Nullable V value(final @NotNull K key) {
    return this.keyToValue.get(key);
  }

  /**
   * Gets a value by its key.
   *
   * @param key the key
   * @return the value
   * @throws NoSuchElementException if there is no value for the key
   * @since 4.11.0
   */
  public @NotNull V valueOrThrow(final @NotNull K key) {
    final V value = this.value(key);
    if (value == null) {
      throw new NoSuchElementException("There is no value for key " + key);
    }
    return value;
  }

  /**
   * Gets a value by its key or returns a fallback value.
   *
   * @param key the key
   * @param defaultValue the fallback value
   * @return the value
   * @since 4.11.0
   */
  @Contract("_, null -> null; _, !null -> !null")
  public V valueOr(final @NotNull K key, final @Nullable V defaultValue) {
    final V value = this.value(key);
    return value == null ? defaultValue : value;
  }

  /**
   * Get an unmodifiable mapping of index entries from key to value.
   *
   * @return a mapping from key to value in the index
   * @since 4.10.0
   */
  public @NotNull Map<K, V> keyToValue() {
    return Collections.unmodifiableMap(this.keyToValue);
  }

  /**
   * Get an unmodifiable mapping of index entries from value to key.
   *
   * @return a mapping from value to key in the index
   * @since 4.10.0
   */
  public @NotNull Map<V, K> valueToKey() {
    return Collections.unmodifiableMap(this.valueToKey);
  }
}
