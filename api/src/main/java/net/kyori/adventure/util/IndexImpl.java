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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

record IndexImpl<K, V>(Map<K, V> keyToValue, Map<V, K> valueToKey) implements Index<K, V> {

  static <K, V> Index<K, V> create(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, final Function<? super V, ? extends K> keyFunction) {
    return create(Arrays.asList(values), valueToKeyFactory, keyFunction);
  }

  static <K, V> Index<K, V> create(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, final Function<? super V, ? extends K> keyFunction) {
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
    return new IndexImpl<>(Collections.unmodifiableMap(keyToValue), Collections.unmodifiableMap(valueToKey));
  }

  @Override
  public Set<K> keys() {
    return Collections.unmodifiableSet(this.keyToValue.keySet());
  }

  @Override
  public @Nullable K key(final V value) {
    return this.valueToKey.get(value);
  }

  @Override
  public K keyOrThrow(final V value) {
    final K key = this.key(value);
    if (key == null) {
      throw new NoSuchElementException("There is no key for value " + value);
    }
    return key;
  }

  @Override
  @Contract("_, null -> null; _, !null -> !null")
  public K keyOr(final V value, final @Nullable K defaultKey) {
    final K key = this.key(value);
    return key == null ? defaultKey : key;
  }

  @Override
  public Set<V> values() {
    return Collections.unmodifiableSet(this.valueToKey.keySet());
  }

  @Override
  public @Nullable V value(final K key) {
    return this.keyToValue.get(key);
  }

  @Override
  public V valueOrThrow(final K key) {
    final V value = this.value(key);
    if (value == null) {
      throw new NoSuchElementException("There is no value for key " + key);
    }
    return value;
  }

  @Override
  public V valueOr(final K key, final @Nullable V defaultValue) {
    final V value = this.value(key);
    return value == null ? defaultValue : value;
  }

  @Override
  public Map<K, V> keyToValue() {
    return Collections.unmodifiableMap(this.keyToValue);
  }

  @Override
  public Map<V, K> valueToKey() {
    return Collections.unmodifiableMap(this.valueToKey);
  }
}
