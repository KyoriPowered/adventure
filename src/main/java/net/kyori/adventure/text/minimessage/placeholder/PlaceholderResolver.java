/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.placeholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A resolver for user-defined placeholders.
 *
 * @since 4.2.0
 */
public interface PlaceholderResolver {
  /**
   * Constructs a placeholder resolver from key-value pairs or {@link Placeholder} instances.
   *
   * <p>The {@code pairs} arguments must be a string key followed by a string or {@link ComponentLike} value or a {@link Placeholder}.</p>
   *
   * @param objects the objects
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver resolving(final @NotNull Object @NotNull ... objects) {
    final int size = Objects.requireNonNull(objects, "pairs").length;

    if (size == 0) return empty();

    String key = null;

    final Map<String, Placeholder> placeholderMap = new HashMap<>(size);
    for (int i = 0; i < size; i++) {
      final Object obj = objects[i];

      if (key == null) {
        // we are looking for a key or a placeholder
        if (obj instanceof Placeholder) {
          final Placeholder placeholder = (Placeholder) obj;
          placeholderMap.put(placeholder.key(), placeholder);
        } else if (obj instanceof String) {
          key = (String) obj;
        } else {
          throw new IllegalArgumentException("Argument " + i + " in pairs must be a String key or a Placeholder, was " + obj.getClass().getName());
        }
      } else {
        // we are looking for a value
        if (obj instanceof String) {
          placeholderMap.put(key, Placeholder.placeholder(key, (String) obj));
        } else if (obj instanceof ComponentLike) {
          placeholderMap.put(key, Placeholder.placeholder(key, (ComponentLike) obj));
        } else {
          throw new IllegalArgumentException("Argument " + i + " in pairs must be a String or ComponentLike value, was " + obj.getClass().getName());
        }

        key = null;
      }
    }

    if (key != null) {
      throw new IllegalArgumentException("Found key \"" + key + "\" in objects that wasn't followed by a value.");
    }

    if (placeholderMap.isEmpty()) return empty();

    return new MapPlaceholderResolver(placeholderMap);
  }

  /**
   * Constructs a placeholder resolver from key-value pairs.
   *
   * <p>The values must be instances of String, {@link ComponentLike} or {@link Placeholder}.</p>
   *
   * @param pairs the key-value pairs
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver pairs(final @NotNull Map<String, ?> pairs) {
    final int size = Objects.requireNonNull(pairs, "pairs").size();

    if (size == 0) return empty();

    final Map<String, Placeholder> placeholderMap = new HashMap<>(size);

    for (final Map.Entry<String, ?> entry : pairs.entrySet()) {
      final String key = Objects.requireNonNull(entry.getKey(), "pairs cannot contain null keys");
      final Object value = entry.getValue();

      if (value instanceof String) placeholderMap.put(key, Placeholder.placeholder(key, (String) value));
      else if (value instanceof ComponentLike) placeholderMap.put(key, Placeholder.placeholder(key, (ComponentLike) value));
      else if (value instanceof Placeholder) placeholderMap.put(key, (Placeholder) value);
      else
        throw new IllegalArgumentException("Values must be either ComponentLike or String but " + value + " was not.");
    }

    return new MapPlaceholderResolver(placeholderMap);
  }

  /**
   * Constructs a placeholder resolver from some placeholders.
   *
   * @param placeholders the placeholders
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver placeholders(final @NotNull Placeholder @NotNull ... placeholders) {
    if (Objects.requireNonNull(placeholders, "placeholders").length == 0) return empty();
    return placeholders(Arrays.asList(placeholders));
  }

  /**
   * Constructs a placeholder resolver from some placeholders.
   *
   * @param placeholders the placeholders
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver placeholders(final @NotNull Iterable<? extends Placeholder> placeholders) {
    final Map<String, Placeholder> placeholderMap = new HashMap<>();

    for (final Placeholder placeholder : Objects.requireNonNull(placeholders, "placeholders")) {
      Objects.requireNonNull(placeholder, "placeholders must not contain null elements");
      placeholderMap.put(placeholder.key(), placeholder);
    }

    if (placeholderMap.isEmpty()) return empty();

    return new MapPlaceholderResolver(placeholderMap);
  }

  /**
   * Constructs a placeholder resolver capable of resolving from multiple sources.
   *
   * @param placeholderResolvers the placeholder resolvers
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver combining(final @NotNull PlaceholderResolver @NotNull ... placeholderResolvers) {
    if (Objects.requireNonNull(placeholderResolvers, "placeholderResolvers").length == 1)
      return Objects.requireNonNull(placeholderResolvers[0], "placeholderResolvers must not contain null elements");
    return new GroupedPlaceholderResolver(Arrays.asList(placeholderResolvers));
  }

  /**
   * Constructs a placeholder resolver capable of resolving from multiple sources, in iteration order.
   *
   * <p>The provided iterable is copied. This means changes to the iterable will not reflect in the returned resolver.</p>
   *
   * @param placeholderResolvers the placeholder resolvers
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver combining(final @NotNull Iterable<? extends PlaceholderResolver> placeholderResolvers) {
    final List<PlaceholderResolver> placeholderResolverList = new ArrayList<>();

    for (final PlaceholderResolver placeholderResolver : Objects.requireNonNull(placeholderResolvers, "placeholderResolvers")) {
      placeholderResolverList.add(Objects.requireNonNull(placeholderResolver, "placeholderResolvers cannot contain null elements"));
    }

    final int size = placeholderResolverList.size();
    if (size == 0) return empty();
    if (size == 1) return placeholderResolverList.get(0);
    return new GroupedPlaceholderResolver(placeholderResolvers);
  }

  /**
   * Constructs a placeholder resolver capable of dynamically resolving placeholders.
   *
   * <p>The {@code resolver} function must return instances of String, {@link ComponentLike} or {@link Placeholder}.
   * The resolver can return {@code null} to indicate it cannot resolve a placeholder.</p>
   *
   * @param resolver the resolver
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver dynamic(final @NotNull Function<String, ?> resolver) {
    return new DynamicPlaceholderResolver(Objects.requireNonNull(resolver, "resolver"));
  }

  /**
   * Constructs a placeholder resolver that uses the provided filter to prevent the resolving of placeholders that match the filter.
   *
   * @param placeholderResolver the placeholder resolver
   * @param filter the filter
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver filtering(final @NotNull PlaceholderResolver placeholderResolver, final @NotNull Predicate<Placeholder> filter) {
    return new FilteringPlaceholderResolver(Objects.requireNonNull(placeholderResolver, "placeholderResolver"), Objects.requireNonNull(filter, "filter"));
  }

  /**
   * An empty placeholder resolver that will return {@code null} for all resolve attempts.
   *
   * @return the placeholder resolver
   * @since 4.2.0
   */
  static @NotNull PlaceholderResolver empty() {
    return EmptyPlaceholderResolver.INSTANCE;
  }

  /**
   * Checks if this placeholder resolver can resolve a placeholder from a key.
   *
   * @param key the key
   * @return if a placeholder can be resolved from this key
   * @since 4.2.0
   */
  boolean canResolve(final @NotNull String key);

  /**
   * Returns a placeholder from a given key, if any exist.
   *
   * @param key the key
   * @return the placeholder
   * @since 4.2.0
   */
  @Nullable Placeholder resolve(final @NotNull String key);
}
