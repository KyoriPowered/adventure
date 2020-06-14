/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

final class ListBinaryTagImpl implements ListBinaryTag {
  static final ListBinaryTag EMPTY = new ListBinaryTagImpl(BinaryTagTypes.END, Collections.emptyList());
  private final List<? extends BinaryTag> tags;
  private final BinaryTagType<? extends BinaryTag> type;

  ListBinaryTagImpl(final BinaryTagType<? extends BinaryTag> type, final List<? extends BinaryTag> tags) {
    this.tags = tags;
    this.type = type;
  }

  @Override
  public @NonNull BinaryTagType<? extends BinaryTag> listType() {
    return this.type;
  }

  @Override
  public int size() {
    return this.tags.size();
  }

  @Override
  public @NonNull BinaryTag get(@NonNegative final int index) {
    return this.tags.get(index);
  }

  @Override
  public @NonNull ListBinaryTag add(final BinaryTag tag) {
    return this.edit(tags -> tags.add(tag));
  }

  private ListBinaryTag edit(final Consumer<List<BinaryTag>> consumer) {
    final List<BinaryTag> tags = new ArrayList<>(this.tags);
    consumer.accept(tags);
    return new ListBinaryTagImpl(this.type, tags);
  }

  @Override
  public boolean equals(final Object that) {
    return this == that || (that instanceof ListBinaryTagImpl && this.tags.equals(((ListBinaryTagImpl) that).tags));
  }

  @Override
  public int hashCode() {
    return this.tags.hashCode();
  }

  @Override
  public String toString() {
    return "ListTagImpl{" +
      "tags=" + tags +
      ", type=" + type +
      '}';
  }
}
