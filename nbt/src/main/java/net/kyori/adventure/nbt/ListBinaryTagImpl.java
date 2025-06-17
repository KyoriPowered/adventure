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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@Debug.Renderer(text = "\"ListBinaryTag[type=\" + this.type.toString() + \"]\"", childrenArray = "this.tags.toArray()", hasChildren = "!this.tags.isEmpty()")
final class ListBinaryTagImpl extends AbstractBinaryTag implements ListBinaryTag {
  static final ListBinaryTag EMPTY = new ListBinaryTagImpl(BinaryTagTypes.END, false, Collections.emptyList());
  private final List<BinaryTag> tags;
  private final boolean permitsHeterogeneity;
  private final BinaryTagType<? extends BinaryTag> elementType;
  private final int hashCode;

  ListBinaryTagImpl(final BinaryTagType<? extends BinaryTag> elementType, final boolean permitsHeterogeneity, final List<BinaryTag> tags) {
    this.tags = Collections.unmodifiableList(tags);
    this.permitsHeterogeneity = permitsHeterogeneity;
    this.elementType = elementType;
    this.hashCode = tags.hashCode();
  }

  @Override
  public @NotNull BinaryTagType<? extends BinaryTag> elementType() {
    return this.elementType;
  }

  @Override
  public int size() {
    return this.tags.size();
  }

  @Override
  public boolean isEmpty() {
    return this.tags.isEmpty();
  }

  @Override
  public @NotNull BinaryTag get(@Range(from = 0, to = Integer.MAX_VALUE) final int index) {
    return this.tags.get(index);
  }

  @Override
  public @NotNull ListBinaryTag set(final int index, final @NotNull BinaryTag newTag, final @Nullable Consumer<? super BinaryTag> removed) {
    final BinaryTagType<?> targetType = ListBinaryTagImpl.validateTagType(newTag, this.elementType, this.permitsHeterogeneity);
    return this.edit(tags -> {
      final BinaryTag oldTag = tags.set(index, newTag);
      if (removed != null) {
        removed.accept(oldTag);
      }
    }, targetType);
  }

  @Override
  public @NotNull ListBinaryTag remove(final int index, final @Nullable Consumer<? super BinaryTag> removed) {
    return this.edit(tags -> {
      final BinaryTag oldTag = tags.remove(index);
      if (removed != null) {
        removed.accept(oldTag);
      }
    }, null);
  }

  @Override
  public @NotNull ListBinaryTag add(final BinaryTag tag) {
    final BinaryTagType<?> targetType = validateTagType(tag, this.elementType, this.permitsHeterogeneity);
    return this.edit(tags -> tags.add(tag), targetType);
  }

  @Override
  public @NotNull ListBinaryTag add(final Iterable<? extends BinaryTag> tagsToAdd) {
    if (tagsToAdd instanceof Collection<?> && ((Collection<?>) tagsToAdd).isEmpty()) {
      return this;
    }
    final BinaryTagType<?> type = ListBinaryTagImpl.validateTagType(tagsToAdd, this.permitsHeterogeneity);
    return this.edit(tags -> {
      for (final BinaryTag tag : tagsToAdd) {
        tags.add(tag);
      }
    }, type);
  }

  // An end tag cannot be an element in a list tag
  static void noAddEnd(final BinaryTag tag) {
    if (tag.type() == BinaryTagTypes.END) {
      throw new IllegalArgumentException(String.format("Cannot add a %s to a %s", BinaryTagTypes.END, BinaryTagTypes.LIST));
    }
  }

  // Cannot have different element types in a list tag unless we're in heterogeneous mode
  static BinaryTagType<?> validateTagType(final Iterable<? extends BinaryTag> tags, final boolean permitHeterogeneity) {
    BinaryTagType<?> type = null;
    for (final BinaryTag tag : tags) {
      if (type == null) {
        noAddEnd(tag);
        type = tag.type();
      } else {
        validateTagType(tag, type, permitHeterogeneity);
        if (type != tag.type()) {
          type = BinaryTagTypes.LIST_WILDCARD;
        }
      }
    }
    return type;
  }

  // An end tag cannot be an element in a list tag
  // AND Cannot have a different element type in a list tag unless we're in heterogeneous mode
  static BinaryTagType<?> validateTagType(final BinaryTag tag, final BinaryTagType<? extends BinaryTag> type, final boolean permitHeterogenity) {
    noAddEnd(tag);
    if (type == BinaryTagTypes.END) {
      return tag.type();
    }

    if (tag.type() != type && !permitHeterogenity) {
      throw new IllegalArgumentException(String.format("Trying to add tag of type %s to list of %s", tag.type(), type));
    }
    return tag.type() != type ? BinaryTagTypes.LIST_WILDCARD : type;
  }

  private ListBinaryTag edit(final Consumer<List<BinaryTag>> consumer, final @Nullable BinaryTagType<? extends BinaryTag> maybeElementType) {
    final List<BinaryTag> tags = new ArrayList<>(this.tags);
    consumer.accept(tags);
    BinaryTagType<? extends BinaryTag> elementType = this.elementType;
    // set the type if it has not yet been set
    if (maybeElementType != null) {
      elementType = maybeElementType;
    }
    return new ListBinaryTagImpl(elementType, this.permitsHeterogeneity, new ArrayList<>(tags)); // explicitly copy
  }

  @Override
  public @NotNull Stream<BinaryTag> stream() {
    return this.tags.stream();
  }

  @Override
  public @NotNull ListBinaryTag unwrapHeterogeneity() {
    // Unlock where it makes sense
    if (!this.permitsHeterogeneity) {
      if (this.elementType != BinaryTagTypes.COMPOUND) {
        return new ListBinaryTagImpl(this.elementType, true, this.tags);
      } else {
        List<BinaryTag> newTags = null;
        BinaryTag current;
        for (final ListIterator<BinaryTag> it = this.tags.listIterator(); it.hasNext();) {
          current = it.next();
          final BinaryTag unboxed = ListBinaryTag0.unbox((CompoundBinaryTag) current);
          // only initialize newTags if we need to unbox something
          if (unboxed != current && newTags == null) {
            newTags = new ArrayList<>(this.tags.size());
            for (int idx = it.nextIndex() - 1, ptr = 0; ptr < idx; ptr++) {
              newTags.add(this.tags.get(ptr));
            }
          }
          // if we're already initialized, unconditionally add
          if (newTags != null) {
            newTags.add(unboxed);
          }
        }
        return new ListBinaryTagImpl(newTags == null ? BinaryTagTypes.COMPOUND : BinaryTagTypes.LIST_WILDCARD, true, newTags == null ? this.tags : newTags);
      }
    }

    // heterogeneity-permitted nothing to unbox
    return this;
  }

  @Override
  public @NotNull ListBinaryTag wrapHeterogeneity() {
    if (this.elementType != BinaryTagTypes.LIST_WILDCARD) {
      return this;
    }

    final List<BinaryTag> newTags = new ArrayList<>(this.tags.size());
    for (final BinaryTag tag : this.tags) {
      newTags.add(ListBinaryTag0.box(tag));
    }

    return new ListBinaryTagImpl(BinaryTagTypes.COMPOUND, false, newTags);
  }

  @Override
  public @NotNull Iterator<BinaryTag> iterator() {
    final Iterator<BinaryTag> iterator = this.tags.iterator();
    return new Iterator<BinaryTag>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public BinaryTag next() {
        return iterator.next();
      }

      @Override
      public void forEachRemaining(final Consumer<? super BinaryTag> action) {
        iterator.forEachRemaining(action);
      }
    };
  }

  @Override
  public void forEach(final Consumer<? super BinaryTag> action) {
    this.tags.forEach(action);
  }

  @Override
  public Spliterator<BinaryTag> spliterator() {
    return Spliterators.spliterator(this.tags, Spliterator.ORDERED | Spliterator.IMMUTABLE);
  }

  @Override
  public boolean equals(final Object that) {
    return this == that || (that instanceof ListBinaryTagImpl && this.tags.equals(((ListBinaryTagImpl) that).tags));
  }

  @Override
  public int hashCode() {
    return this.hashCode;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("tags", this.tags),
      ExaminableProperty.of("type", this.elementType)
    );
  }
}

final class ListBinaryTag0 {
  private static final String WRAPPER_KEY = "";

  private ListBinaryTag0() {
  }

  static BinaryTag unbox(final CompoundBinaryTag compound) {
    if (compound.size() == 1) {
      final BinaryTag potentialValue = compound.get(WRAPPER_KEY);
      if (potentialValue != null) return potentialValue;
    }

    return compound;
  }

  static CompoundBinaryTag box(final BinaryTag tag) {
    if (needsBox(tag)) {
      return new CompoundBinaryTagImpl(Collections.singletonMap(WRAPPER_KEY, tag));
    } else {
      return (CompoundBinaryTag) tag;
    }
  }

  private static boolean needsBox(final BinaryTag tag) {
    if (!(tag instanceof CompoundBinaryTag)) {
      return true;
    }

    final CompoundBinaryTag compound = (CompoundBinaryTag) tag;
    return compound.size() == 1 && compound.get(WRAPPER_KEY) != null;
  }
}
