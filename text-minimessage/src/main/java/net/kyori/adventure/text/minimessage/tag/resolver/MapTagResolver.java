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
import org.jspecify.annotations.Nullable;

record MapTagResolver(Map<String, ? extends Tag> tagMap) implements TagResolver.WithoutArguments, MappableResolver {

  @Override
  public @Nullable Tag resolve(final String name) {
    return this.tagMap.get(name);
  }

  @Override
  public boolean has(final String name) {
    return this.tagMap.containsKey(name);
  }

  @Override
  public Class<? extends Tag> tagType(final String name) {
    final Tag tag = this.tagMap.get(name);
    return tag == null ? null : tag.getClass();
  }

  @Override
  public boolean contributeToMap(final Map<String, Tag> map) {
    map.putAll(this.tagMap);
    return true;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof MapTagResolver(Map<String, ? extends Tag> map))) {
      return false;
    }
    return Objects.equals(this.tagMap, map);
  }
}
