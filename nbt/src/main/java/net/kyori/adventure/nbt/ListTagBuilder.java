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
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T> {
  private static final int DEFAULT_CAPACITY = -1;

  private @Nullable List<BinaryTag> tags;
  private final boolean permitsHeterogeneity;
  private BinaryTagType<? extends BinaryTag> elementType;
  private final int initialCapacity;

  ListTagBuilder(final boolean permitsHeterogeneity) {
    this(permitsHeterogeneity, BinaryTagTypes.END);
  }

  ListTagBuilder(final boolean permitsHeterogeneity, final int initialCapacity) {
    this(permitsHeterogeneity, BinaryTagTypes.END, initialCapacity);
  }

  ListTagBuilder(final boolean permitsHeterogeneity, final BinaryTagType<? extends BinaryTag> type) {
    this(permitsHeterogeneity, type, DEFAULT_CAPACITY);
  }

  ListTagBuilder(final boolean permitsHeterogeneity, final BinaryTagType<? extends BinaryTag> type, final int initialCapacity) {
    this.permitsHeterogeneity = permitsHeterogeneity;
    this.elementType = type;
    this.initialCapacity = initialCapacity;
  }

  @Override
  public ListBinaryTag.Builder<T> add(final BinaryTag tag) {
    // check after changing from an empty tag
    this.elementType = ListBinaryTagImpl.validateTagType(tag, this.elementType, this.permitsHeterogeneity);
    if (this.tags == null) {
      if (this.initialCapacity != DEFAULT_CAPACITY) {
        if (this.initialCapacity < 0) throw new IllegalArgumentException("initialCapacity cannot be less than 0, was " + this.initialCapacity);
        this.tags = new ArrayList<>(this.initialCapacity);
      } else {
        this.tags = new ArrayList<>();
      }
    }
    this.tags.add(tag);
    return this;
  }

  @Override
  public ListBinaryTag.Builder<T> add(final Iterable<? extends T> tagsToAdd) {
    for (final T tag : tagsToAdd) {
      this.add(tag);
    }
    return this;
  }

  @Override
  public ListBinaryTag build() {
    if (this.tags == null) return ListBinaryTag.empty();
    return new ListBinaryTagImpl(this.elementType, this.permitsHeterogeneity, new ArrayList<>(this.tags)); // explicitly copy
  }
}
