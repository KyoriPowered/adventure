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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class FeatureFlagSetImpl implements FeatureFlagSet {
  static final FeatureFlagSet EMPTY = new FeatureFlagSetImpl(new IdentityHashMap<>());
  private final IdentityHashMap<FeatureFlag<?>, Object> values;

  FeatureFlagSetImpl(final IdentityHashMap<FeatureFlag<?>, Object> values) {
    this.values = new IdentityHashMap<>(values);
  }

  @Override
  public boolean has(final FeatureFlag<?> flag) {
    return this.values.containsKey(requireNonNull(flag, "flag"));
  }

  @Override
  public <V> V value(final FeatureFlag<V> flag) {
    final V value = flag.type().cast(this.values.get(requireNonNull(flag, "flag")));
    return value == null ? flag.defaultValue() : value;
  }

  static final class VersionedImpl implements Versioned {
    private final SortedMap<Integer, FeatureFlagSet> sets;
    private final int targetVersion;
    private final FeatureFlagSet filtered;

    VersionedImpl(final SortedMap<Integer, FeatureFlagSet> sets, final int targetVersion, final FeatureFlagSet filtered) {
      this.sets = sets;
      this.targetVersion = targetVersion;
      this.filtered = filtered;
    }

    @Override
    public boolean has(final @NotNull FeatureFlag<?> flag) {
      return this.filtered.has(flag);
    }

    @Override
    public <V> V value(final @NotNull FeatureFlag<V> flag) {
      return this.filtered.value(flag);
    }

    @Override
    public @NotNull Map<Integer, FeatureFlagSet> childSets() {
      return Collections.unmodifiableSortedMap(this.sets);
    }

    @Override
    public @NotNull Versioned at(final int version) {
      return new VersionedImpl(this.sets, version, flattened(this.sets, version));
    }

    public static FeatureFlagSet flattened(final SortedMap<Integer, FeatureFlagSet> versions, final int targetVersion) {
      final Map<Integer, FeatureFlagSet> applicable = versions.headMap(targetVersion + 1);
      final FeatureFlagSet.Builder builder = FeatureFlagSet.builder();
      for (final FeatureFlagSet child : applicable.values()) {
        builder.values(child);
      }

      return builder.build();
    }
  }

  static final class BuilderImpl implements FeatureFlagSet.Builder {
    private final IdentityHashMap<FeatureFlag<?>, Object> values = new IdentityHashMap<>();

    @Override
    public @NotNull FeatureFlagSet build() {
      if (this.values.isEmpty()) return EMPTY;

      return new FeatureFlagSetImpl(this.values);
    }

    @Override
    public <V> @NotNull Builder value(final @NotNull FeatureFlag<V> flag, final @NotNull V value) {
      this.values.put(
        requireNonNull(flag, "flag"),
        requireNonNull(value, "value")
      );
      return this;
    }

    @Override
    public @NotNull Builder values(final @NotNull FeatureFlagSet existing) {
      if (existing instanceof FeatureFlagSetImpl) {
        this.values.putAll(((FeatureFlagSetImpl) existing).values);
      } else if (existing instanceof VersionedImpl) {
        this.values.putAll(((FeatureFlagSetImpl) ((VersionedImpl) existing).filtered).values);
      } else {
        throw new IllegalArgumentException("existing set " + existing + " is of an unknown implementation type");
      }
      return this;
    }
  }

  static final class VersionedBuilderImpl implements FeatureFlagSet.VersionedBuilder {
    private final Map<Integer, FeatureFlagSetImpl.BuilderImpl> builders = new TreeMap<>();

    @Override
    public FeatureFlagSet.@NotNull Versioned build() {
      if (this.builders.isEmpty()) {
        return new VersionedImpl(Collections.emptySortedMap(), 0, FeatureFlagSet.empty());
      }

      final SortedMap<Integer, FeatureFlagSet> built = new TreeMap<>();
      for (final Map.Entry<Integer, FeatureFlagSetImpl.BuilderImpl> entry : this.builders.entrySet()) {
        built.put(entry.getKey(), entry.getValue().build());
      }
      // generate 'flattened' latest element
      return new VersionedImpl(built, built.lastKey(), VersionedImpl.flattened(built, built.lastKey()));
    }

    @Override
    public @NotNull VersionedBuilder version(final int version, final Consumer<Builder> versionBuilder) {
      requireNonNull(versionBuilder, "versionBuilder")
        .accept(this.builders.computeIfAbsent(version, $ -> new FeatureFlagSetImpl.BuilderImpl()));
      return this;
    }
  }
}
