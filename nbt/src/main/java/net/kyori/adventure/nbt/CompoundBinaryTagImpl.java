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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.jetbrains.annotations.Debug;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@Debug.Renderer(text = "\"CompoundBinaryTag[length=\" + this.tags.size() + \"]\"", childrenArray = "this.tags.entrySet().toArray()", hasChildren = "!this.tags.isEmpty()")
record CompoundBinaryTagImpl(Map<String, BinaryTag> tags) implements CompoundBinaryTag {

  static CompoundBinaryTag create(final Map<String, BinaryTag> tags) {
    return new CompoundBinaryTagImpl(Map.copyOf(tags));
  }

  static final CompoundBinaryTag EMPTY = new CompoundBinaryTagImpl(Collections.emptyMap());

  @Override
  public boolean contains(final String key) {
    return this.tags.containsKey(key);
  }

  @Override
  public boolean contains(final String key, final BinaryTagType<?> type) {
    final BinaryTag tag = this.tags.get(requireNonNull(key, "key"));
    return tag != null && requireNonNull(type, "type").test(tag.type());
  }

  @Override
  public Set<String> keySet() {
    return Collections.unmodifiableSet(this.tags.keySet());
  }

  @Override
  public @Nullable BinaryTag get(final String key) {
    return this.tags.get(key);
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
  public CompoundBinaryTag put(final String key, final BinaryTag tag) {
    return this.edit(map -> map.put(key, tag));
  }

  @Override
  public CompoundBinaryTag put(final CompoundBinaryTag tag) {
    return this.edit(map -> {
      for (final String key : tag.keySet()) {
        map.put(key, tag.get(key));
      }
    });
  }

  @Override
  public CompoundBinaryTag put(final Map<String, ? extends BinaryTag> tags) {
    return this.edit(map -> map.putAll(tags));
  }

  @Override
  public CompoundBinaryTag remove(final String key, final @Nullable Consumer<? super BinaryTag> removed) {
    if (!this.tags.containsKey(key)) {
      return this;
    }
    return this.edit(map -> {
      final BinaryTag tag = map.remove(key);
      if (removed != null) {
        removed.accept(tag);
      }
    });
  }

  @Override
  public byte getByte(final String key, final byte defaultValue) {
    if (this.contains(key, BinaryTagTypes.BYTE)) {
      return ((NumberBinaryTag) this.tags.get(key)).byteValue();
    }
    return defaultValue;
  }

  @Override
  public short getShort(final String key, final short defaultValue) {
    if (this.contains(key, BinaryTagTypes.SHORT)) {
      return ((NumberBinaryTag) this.tags.get(key)).shortValue();
    }
    return defaultValue;
  }

  @Override
  public int getInt(final String key, final int defaultValue) {
    if (this.contains(key, BinaryTagTypes.INT)) {
      return ((NumberBinaryTag) this.tags.get(key)).intValue();
    }
    return defaultValue;
  }

  @Override
  public long getLong(final String key, final long defaultValue) {
    if (this.contains(key, BinaryTagTypes.LONG)) {
      return ((NumberBinaryTag) this.tags.get(key)).longValue();
    }
    return defaultValue;
  }

  @Override
  public float getFloat(final String key, final float defaultValue) {
    if (this.contains(key, BinaryTagTypes.FLOAT)) {
      return ((NumberBinaryTag) this.tags.get(key)).floatValue();
    }
    return defaultValue;
  }

  @Override
  public double getDouble(final String key, final double defaultValue) {
    if (this.contains(key, BinaryTagTypes.DOUBLE)) {
      return ((NumberBinaryTag) this.tags.get(key)).doubleValue();
    }
    return defaultValue;
  }

  @Override
  public byte[] getByteArray(final String key) {
    if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
      return ((ByteArrayBinaryTag) this.tags.get(key)).value();
    }
    return new byte[0];
  }

  @Override
  public byte @Nullable [] getByteArray(final String key, final byte @Nullable [] defaultValue) {
    if (this.contains(key, BinaryTagTypes.BYTE_ARRAY)) {
      return ((ByteArrayBinaryTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @Nullable String getString(final String key, final @Nullable String defaultValue) {
    if (this.contains(key, BinaryTagTypes.STRING)) {
      return ((StringBinaryTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @Nullable ListBinaryTag getList(final String key, final @Nullable ListBinaryTag defaultValue) {
    if (this.contains(key, BinaryTagTypes.LIST)) {
      return (ListBinaryTag) this.tags.get(key);
    }
    return defaultValue;
  }

  @Override
  public @Nullable ListBinaryTag getList(final String key, final BinaryTagType<? extends BinaryTag> expectedType, final @Nullable ListBinaryTag defaultValue) {
    if (this.contains(key, BinaryTagTypes.LIST)) {
      final ListBinaryTag tag = (ListBinaryTag) this.tags.get(key);
      if (expectedType.test(tag.elementType())) {
        return tag;
      }
    }
    return defaultValue;
  }

  @Override
  public @Nullable CompoundBinaryTag getCompound(final String key, final @Nullable CompoundBinaryTag defaultValue) {
    if (this.contains(key, BinaryTagTypes.COMPOUND)) {
      return (CompoundBinaryTag) this.tags.get(key);
    }
    return defaultValue;
  }

  @Override
  public int[] getIntArray(final String key) {
    if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
      return ((IntArrayBinaryTag) this.tags.get(key)).value();
    }
    return new int[0];
  }

  @Override
  public int @Nullable [] getIntArray(final String key, final int @Nullable [] defaultValue) {
    if (this.contains(key, BinaryTagTypes.INT_ARRAY)) {
      return ((IntArrayBinaryTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public long[] getLongArray(final String key) {
    if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
      return ((LongArrayBinaryTag) this.tags.get(key)).value();
    }
    return new long[0];
  }

  @Override
  public long @Nullable [] getLongArray(final String key, final long @Nullable [] defaultValue) {
    if (this.contains(key, BinaryTagTypes.LONG_ARRAY)) {
      return ((LongArrayBinaryTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Stream<Map.Entry<String, ? extends BinaryTag>> stream() {
    return (Stream) this.tags.entrySet().stream();
  }

  private CompoundBinaryTag edit(final Consumer<Map<String, BinaryTag>> consumer) {
    final Map<String, BinaryTag> tags = new HashMap<>(this.tags);
    consumer.accept(tags);
    return new CompoundBinaryTagImpl(new HashMap<>(tags)); // explicitly copy
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Iterator<Map.Entry<String, ? extends BinaryTag>> iterator() {
    return (Iterator) this.tags.entrySet().iterator();
  }

  @Override
  public void forEach(final Consumer<? super Map.Entry<String, ? extends BinaryTag>> action) {
    this.tags.entrySet().forEach(requireNonNull(action, "action"));
  }
}
