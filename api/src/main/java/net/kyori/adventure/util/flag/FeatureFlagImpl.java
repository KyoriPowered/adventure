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
package net.kyori.adventure.util.flag;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class FeatureFlagImpl<V> implements FeatureFlag<V> {
  private static final Set<Key> KNOWN_KEYS = ConcurrentHashMap.newKeySet();

  private final Key id;
  private final Class<V> type;
  private final @Nullable V defaultValue; // excluded from equality comparisons, it does not form part of the flag identity

  FeatureFlagImpl(final @NotNull Key id, final @NotNull Class<V> type, final @Nullable V defaultValue) {
    this.id = id;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  static <T> FeatureFlag<T> flag(final Key id, final Class<T> type, final @Nullable T defaultValue) {
    if (!KNOWN_KEYS.add(id)) {
      throw new IllegalStateException("Key " + id + " has already been used. Feature flag keys must be unique.");
    }

    return new FeatureFlagImpl<>(
      requireNonNull(id, "id"),
      requireNonNull(type, "type"),
      defaultValue
    );
  }

  @Override
  public @NotNull Key id() {
    return this.id;
  }

  @Override
  public @NotNull Class<V> type() {
    return this.type;
  }

  @Override
  public @Nullable V defaultValue() {
    return this.defaultValue;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    final FeatureFlagImpl<?> that = (FeatureFlagImpl<?>) other;
    return Objects.equals(this.id, that.id)
      && Objects.equals(this.type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      this.id,
      this.type
    );
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("id", this.id),
      ExaminableProperty.of("type", this.type),
      ExaminableProperty.of("defaultValue", this.defaultValue)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }
}
