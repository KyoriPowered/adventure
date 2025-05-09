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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.Inserting;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CachingTagResolver implements TagResolver.WithoutArguments, MappableResolver, SerializableResolver {
  private static final Tag NULL_REPLACEMENT = (Inserting) () -> {
    throw new UnsupportedOperationException("no-op null tag");
  };

  private final Map<String, Tag> cache = new HashMap<>();
  private final TagResolver.WithoutArguments resolver;

  CachingTagResolver(final TagResolver.WithoutArguments resolver) {
    this.resolver = resolver;
  }

  private Tag query(final @NotNull String key) {
    return this.cache.computeIfAbsent(key, k -> {
      final @Nullable Tag result = this.resolver.resolve(k);
      return result == null ? NULL_REPLACEMENT : result;
    });
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name) {
    final Tag potentialValue = this.query(name);
    return potentialValue == NULL_REPLACEMENT ? null : potentialValue;
  }

  @Override
  public boolean has(final @NotNull String name) {
    return this.query(name) != NULL_REPLACEMENT;
  }

  @Override
  public boolean contributeToMap(final @NotNull Map<String, Tag> map) {
    if (this.resolver instanceof MappableResolver) {
      return ((MappableResolver) this.resolver).contributeToMap(map);
    } else {
      return false;
    }
  }

  @Override
  public void handle(final @NotNull Component serializable, final @NotNull ClaimConsumer consumer) {
    if (this.resolver instanceof SerializableResolver) {
      ((SerializableResolver) this.resolver).handle(serializable, consumer);
    }
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CachingTagResolver)) {
      return false;
    }
    final CachingTagResolver that = (CachingTagResolver) other;
    return Objects.equals(this.resolver, that.resolver);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.resolver);
  }
}
