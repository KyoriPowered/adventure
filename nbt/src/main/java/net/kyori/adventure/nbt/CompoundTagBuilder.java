/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class CompoundTagBuilder implements CompoundBinaryTag.Builder {
  private @Nullable Map<String, BinaryTag> tags;

  private Map<String, BinaryTag> tags() {
    if (this.tags == null) {
      this.tags = new HashMap<>();
    }
    return this.tags;
  }

  @Override
  public CompoundBinaryTag.@NotNull Builder put(final @NotNull String key, final @NotNull BinaryTag tag) {
    this.tags().put(key, tag);
    return this;
  }

  @Override
  public CompoundBinaryTag.@NotNull Builder put(final @NotNull CompoundBinaryTag tag) {
    final Map<String, BinaryTag> tags = this.tags();
    for (final String key : tag.keySet()) {
      tags.put(key, tag.get(key));
    }
    return this;
  }

  @Override
  public CompoundBinaryTag.@NotNull Builder put(final @NotNull Map<String, ? extends BinaryTag> tags) {
    this.tags().putAll(tags);
    return this;
  }

  @Override
  public CompoundBinaryTag.@NotNull Builder remove(final @NotNull String key, final @Nullable Consumer<? super BinaryTag> removed) {
    if (this.tags != null) {
      final BinaryTag tag = this.tags.remove(key);
      if (removed != null) {
        removed.accept(tag);
      }
    }
    return this;
  }

  @Override
  public @NotNull CompoundBinaryTag build() {
    if (this.tags == null) return CompoundBinaryTag.empty();
    return new CompoundBinaryTagImpl(new HashMap<>(this.tags));
  }
}
