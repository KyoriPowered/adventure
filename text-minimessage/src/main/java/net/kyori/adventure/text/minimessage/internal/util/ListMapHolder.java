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
package net.kyori.adventure.text.minimessage.internal.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A holder for a string and a map.
 *
 * @param list list to hold
 * @param map map to hold
 * @param <E> type of the list
 * @param <K> type of the map key
 * @param <V> type of the map value
 * @since 5.1.0
 */
public record ListMapHolder<E, K, V>(List<E> list, Map<K, V> map) {
  /**
   * Create a new empty {@link ListMapHolder}.
   *
   * @param <E> type of the list
   * @param <K> type of the map key
   * @param <V> type of the map value
   * @return a new empty instance
   * @since 5.1.0
   */
  public static <E, K, V> ListMapHolder<E, K, V> empty() {
    return new ListMapHolder<>(Collections.emptyList(), Collections.emptyMap());
  }

  /**
   * Create a new {@link ListMapHolder}.
   *
   * @param list list to hold
   * @param map map to hold
   * @param <E> type of the list
   * @param <K> type of the map key
   * @param <V> type of the map value
   * @return a new instance
   * @since 5.1.0
   */
  public static <E, K, V> ListMapHolder<E, K, V> of(final List<E> list, final Map<K, V> map) {
    return new ListMapHolder<>(list, map);
  }
}
