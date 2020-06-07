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

final class ListTagImpl implements ListTag {
  static final ListTag EMPTY = new ListTagImpl(TagTypes.END, Collections.emptyList());
  private final List<? extends Tag> tags;
  private final TagType<? extends Tag> type;

  ListTagImpl(final TagType<? extends Tag> type, final List<? extends Tag> tags) {
    this.tags = tags;
    this.type = type;
  }

  @Override
  public @NonNull TagType<? extends Tag> listType() {
    return this.type;
  }

  @Override
  public int size() {
    return this.tags.size();
  }

  @Override
  public @NonNull Tag get(@NonNegative final int index) {
    return this.tags.get(index);
  }

  @Override
  public @NonNull ListTag add(final Tag tag) {
    return this.edit(tags -> tags.add(tag));
  }

  private ListTag edit(final Consumer<List<Tag>> consumer) {
    final List<Tag> tags = new ArrayList<>(this.tags);
    consumer.accept(tags);
    return new ListTagImpl(this.type, tags);
  }

  @Override
  public boolean equals(final Object that) {
    return this == that || (that instanceof ListTagImpl && this.tags.equals(((ListTagImpl) that).tags));
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
