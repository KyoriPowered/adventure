/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A name map.
 *
 * @param <T> the type
 */
public final class NameMap<T> {
  private final Map<String, T> byName;
  private final Map<T, String> byValue;

  private NameMap(final Map<String, T> byName, final Map<T, String> byValue) {
    this.byName = byName;
    this.byValue = byValue;
  }

  /**
   * Creates a name map.
   *
   * @param constants the constants
   * @param namer the name provider
   * @param <T> the type
   * @return the name map
   */
  public static <T extends Enum<T>> @NonNull NameMap<T> create(final T @NonNull [] constants, final @NonNull Function<T, String> namer) {
    final Map<String, T> byName = new HashMap<>(constants.length);
    final Map<T, String> byValue = new HashMap<>(constants.length);
    for(int i = 0, length = constants.length; i < length; i++) {
      final T constant = constants[i];
      final String name = namer.apply(constant);
      byName.put(name, constant);
      byValue.put(constant, name);
    }
    return new NameMap<>(Collections.unmodifiableMap(byName), Collections.unmodifiableMap(byValue));
  }

  /**
   * Gets the name for a value.
   *
   * @param value the value
   * @return the name
   */
  public @NonNull String name(final @NonNull T value) {
    return this.byValue.get(value);
  }

  /**
   * Gets a value by its name.
   *
   * @param name the name
   * @return the value
   */
  public @NonNull Optional<T> get(final @NonNull String name) {
    return Optional.ofNullable(this.byName.get(name));
  }
}
