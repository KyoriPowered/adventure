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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class InheritanceAwareMapImpl<C, V> implements InheritanceAwareMap<C, V> {
  private static final Object NONE = new Object(); // null sentinel for CHM
  @SuppressWarnings({"rawtypes", "unchecked"})
  static final InheritanceAwareMapImpl EMPTY = new InheritanceAwareMapImpl(false, Collections.emptyMap());

  private final Map<Class<? extends C>, V> declaredValues;
  private final boolean strict;
  private transient final ConcurrentMap<Class<? extends C>, Object> cache = new ConcurrentHashMap<>();

  InheritanceAwareMapImpl(final boolean strict, final Map<Class<? extends C>, V> declaredValues) {
    this.strict = strict;
    this.declaredValues = declaredValues;
  }

  @Override
  public boolean containsKey(final @NotNull Class<? extends C> clazz) {
    return this.get(clazz) != null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @Nullable V get(final @NotNull Class<? extends C> clazz) {
    final Object ret = this.cache.computeIfAbsent(clazz, c -> {
      final @Nullable V value = this.declaredValues.get(c);
      if (value != null) return value;

      for (final Map.Entry<Class<? extends C>, V> entry : this.declaredValues.entrySet()) {
        if (entry.getKey().isAssignableFrom(c)) {
          return entry.getValue();
        }
      }

      return NONE;
    });

    return ret == NONE ? null : (V) ret;
  }

  @Override
  public @NotNull InheritanceAwareMap<C, V> with(final @NotNull Class<? extends C> clazz, final @NotNull V value) {
    if (Objects.equals(this.declaredValues.get(clazz), value)) return this;
    if (this.strict) validateNoneInHierarchy(clazz, this.declaredValues);

    final Map<Class<? extends C>, V> newValues = new LinkedHashMap<>(this.declaredValues);
    newValues.put(clazz, value);
    return new InheritanceAwareMapImpl<>(this.strict, Collections.unmodifiableMap(newValues));
  }

  @Override
  public @NotNull InheritanceAwareMap<C, V> without(final @NotNull Class<? extends C> clazz) {
    if (!this.declaredValues.containsKey(clazz)) return this;

    final Map<Class<? extends C>, V> newValues = new LinkedHashMap<>(this.declaredValues);
    newValues.remove(clazz);
    return new InheritanceAwareMapImpl<>(this.strict, Collections.unmodifiableMap(newValues));
  }

  static final class BuilderImpl<C, V> implements Builder<C, V> {
    private boolean strict;
    private final Map<Class<? extends C>, V> values = new LinkedHashMap<>();

    @Override
    public @NotNull InheritanceAwareMap<C, V> build() {
      return new InheritanceAwareMapImpl<>(this.strict, Collections.unmodifiableMap(new LinkedHashMap<>(this.values)));
    }

    @Override
    public @NotNull Builder<C, V> strict(final boolean strict) {
      if (strict && !this.strict) { // re-validate contents
        for (final Class<? extends C> clazz : this.values.keySet()) {
          validateNoneInHierarchy(clazz, this.values);
        }
      }
      this.strict = strict;
      return this;
    }

    @Override
    public @NotNull Builder<C, V> put(final @NotNull Class<? extends C> clazz, final @NotNull V value) {
      if (this.strict) validateNoneInHierarchy(clazz, this.values);
      this.values.put(
        requireNonNull(clazz, "clazz"),
        requireNonNull(value, "value")
      );
      return this;
    }

    @Override
    public @NotNull Builder<C, V> remove(final @NotNull Class<? extends C> clazz) {
      this.values.remove(requireNonNull(clazz, "clazz"));
      return this;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull Builder<C, V> putAll(final @NotNull InheritanceAwareMap<? extends C, ? extends V> map) {
      final InheritanceAwareMapImpl<?, V> impl = (InheritanceAwareMapImpl<?, V>) map;
      if (this.strict) {
        if (!this.values.isEmpty() || !impl.strict) { // validate all
          for (final Map.Entry<? extends Class<?>, V> entry : impl.declaredValues.entrySet()) {
            validateNoneInHierarchy(entry.getKey(), this.values);
            this.values.put((Class<? extends C>) entry.getKey(), entry.getValue());
          }
          return this;
        }
      }

      // otherwise (simpler)
      this.values.putAll((Map) impl.declaredValues);
      return this;
    }
  }

  private static void validateNoneInHierarchy(final Class<?> beingRegistered, final Map<? extends Class<?>, ?> entries) {
    for (final Class<?> clazz : entries.keySet()) {
      testHierarchy(clazz, beingRegistered);
    }
  }

  private static void testHierarchy(final Class<?> existing, final Class<?> beingRegistered) {
    if (!existing.equals(beingRegistered) && (existing.isAssignableFrom(beingRegistered) || beingRegistered.isAssignableFrom(existing))) {
      throw new IllegalArgumentException("Conflict detected between already registered type " + existing
        + " and newly registered type " + beingRegistered + "! Types in a strict inheritance-aware map must not share a common hierarchy!");
    }
  }
}
