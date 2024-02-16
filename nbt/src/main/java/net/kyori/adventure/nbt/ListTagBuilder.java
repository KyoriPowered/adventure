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

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T> {
  private @Nullable List<BinaryTag> tags;
  private BinaryTagType<? extends BinaryTag> elementType;

  ListTagBuilder() {
    this(BinaryTagTypes.END);
  }

  ListTagBuilder(final BinaryTagType<? extends BinaryTag> type) {
    this.elementType = type;
  }

  @Override
  public ListBinaryTag.@NotNull Builder<T> add(final BinaryTag tag) {
    ListBinaryTagImpl.noAddEnd(tag);
    // set the type if it has not yet been set
    if (this.elementType == BinaryTagTypes.END) {
      this.elementType = tag.type();
    }
    // check after changing from an empty tag
    ListBinaryTagImpl.mustBeSameType(tag, this.elementType);
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tag);
    return this;
  }

  @Override
  public ListBinaryTag.@NotNull Builder<T> add(final Iterable<? extends T> tagsToAdd) {
    for (final T tag : tagsToAdd) {
      this.add(tag);
    }
    return this;
  }

  @Override
  public @NotNull ListBinaryTag build() {
    if (this.tags == null) return ListBinaryTag.empty();
    return new ListBinaryTagImpl(this.elementType, new ArrayList<>(this.tags));
  }
}
