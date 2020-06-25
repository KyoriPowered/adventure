/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * An index map.
 *
 * @param <K> the key type used for indexing
 * @param <E> the type
 */
public final class Index<K, E> {
  private final Map<K, E> keyToValue;
  private final Map<E, K> valueToKey;

  private Index(final Map<K, E> keyToValue, final Map<E, K> valueToKey) {
    this.keyToValue = keyToValue;
    this.valueToKey = valueToKey;
  }

  /**
   * Creates an index map.
   *
   * @param type the type
   * @param indexFunction the index function
   * @param <K> the key type used for indexing
   * @param <E> the type
   * @return the key map
   */
  public static <K, E extends Enum<E>> @NonNull Index<K, E> create(final Class<E> type, final @NonNull Function<? super E, ? extends K> indexFunction) {
    return create(type, indexFunction, type.getEnumConstants());
  }

  /**
   * Creates an index map.
   *
   * @param type the type
   * @param indexFunction the index function
   * @param constants the constants
   * @param <K> the key type used for indexing
   * @param <E> the type
   * @return the key map
   */
  @SafeVarargs
  public static <K, E extends Enum<E>> @NonNull Index<K, E> create(final Class<E> type, final @NonNull Function<? super E, ? extends K> indexFunction, final @NonNull E@NonNull... constants) {
    return create(constants, length -> new EnumMap<>(type), indexFunction);
  }

  /**
   * Creates an index map.
   *
   * @param indexFunction the key provider
   * @param constants the constants
   * @param <K> the key type used for indexing
   * @param <E> the type
   * @return the key map
   */
  @SafeVarargs
  public static <K, E> @NonNull Index<K, E> create(final @NonNull Function<? super E, ? extends K> indexFunction, final @NonNull E@NonNull... constants) {
    return create(constants, HashMap<E, K>::new /* explicit type params needed to fix build on JDK8 */, indexFunction);
  }

  @SuppressWarnings("ForLoopReplaceableByForEach")
  private static <K, E> @NonNull Index<K, E> create(final E[] constants, final IntFunction<Map<E, K>> valueToKeyFactory, final @NonNull Function<? super E, ? extends K> indexFunction) {
    final int length = constants.length;
    final Map<K, E> keyToValue = new HashMap<>(length);
    final Map<E, K> valueToKey = valueToKeyFactory.apply(length); // to support using EnumMap instead of HashMap when possible
    for(int i = 0; i < length; i++) {
      final E constant = constants[i];
      final K key = indexFunction.apply(constant);
      keyToValue.put(key, constant);
      valueToKey.put(constant, key);
    }
    return new Index<>(Collections.unmodifiableMap(keyToValue), Collections.unmodifiableMap(valueToKey));
  }

  /**
   * Gets the key for a value.
   *
   * @param value the value
   * @return the key
   */
  public @Nullable K key(final @NonNull E value) {
    return this.valueToKey.get(value);
  }

  /**
   * Gets a value by its key.
   *
   * @param key the key
   * @return the value
   */
  public @Nullable E value(final @NonNull K key) {
    return this.keyToValue.get(key);
  }
}
