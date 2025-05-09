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

import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MapTagResolver implements TagResolver.WithoutArguments, MappableResolver {
  private final Map<String, ? extends Tag> tagMap;

  MapTagResolver(final @NotNull Map<String, ? extends Tag> placeholderMap) {
    this.tagMap = placeholderMap;
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name) {
    return this.tagMap.get(name);
  }

  @Override
  public boolean contributeToMap(final @NotNull Map<String, Tag> map) {
    map.putAll(this.tagMap);
    return true;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MapTagResolver)) {
      return false;
    }
    final MapTagResolver that = (MapTagResolver) other;
    return Objects.equals(this.tagMap, that.tagMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tagMap);
  }
}
