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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class CompoundTagImpl implements CompoundTag {
  static final CompoundTag EMPTY = new CompoundTagImpl(Collections.emptyMap());
  private final Map<String, Tag> tags;

  CompoundTagImpl() {
    this(new HashMap<>());
  }

  CompoundTagImpl(final Map<String, Tag> tags) {
    this.tags = tags;
  }

  public boolean contains(final @NonNull String key, final @NonNull TagType<?> type) {
    final /* @Nullable */ Tag tag = this.tags.get(key);
    return tag != null && type.test(tag.type());
  }

  @Override
  public @NonNull Set<String> keySet() {
    return Collections.unmodifiableSet(this.tags.keySet());
  }

  @Override
  public @Nullable Tag get(final String key) {
    return this.tags.get(key);
  }

  @Override
  public @NonNull CompoundTag put(final @NonNull String key, @NonNull final Tag tag) {
    return this.edit(map -> map.put(key, tag));
  }

  @Override
  public byte getByte(final @NonNull String key, final byte defaultValue) {
    if(this.contains(key, TagTypes.BYTE)) {
      return ((NumberTag) this.tags.get(key)).byteValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putByte(final @NonNull String key, final byte value) {
    return this.edit(map -> map.put(key, ByteTag.of(value)));
  }

  @Override
  public short getShort(final @NonNull String key, final short defaultValue) {
    if(this.contains(key, TagTypes.SHORT)) {
      return ((NumberTag) this.tags.get(key)).shortValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putShort(final @NonNull String key, final short value) {
    return this.edit(map -> map.put(key, ShortTag.of(value)));
  }

  @Override
  public int getInt(final @NonNull String key, final int defaultValue) {
    if(this.contains(key, TagTypes.INT)) {
      return ((NumberTag) this.tags.get(key)).intValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putInt(final @NonNull String key, final int value) {
    return this.edit(map -> map.put(key, IntTag.of(value)));
  }

  @Override
  public long getLong(final @NonNull String key, final long defaultValue) {
    if(this.contains(key, TagTypes.LONG)) {
      return ((NumberTag) this.tags.get(key)).longValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putLong(final @NonNull String key, final long value) {
    return this.edit(map -> map.put(key, LongTag.of(value)));
  }

  @Override
  public float getFloat(final @NonNull String key, final float defaultValue) {
    if(this.contains(key, TagTypes.FLOAT)) {
      return ((NumberTag) this.tags.get(key)).floatValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putFloat(final @NonNull String key, final float value) {
    return this.edit(map -> map.put(key, FloatTag.of(value)));
  }

  @Override
  public double getDouble(final @NonNull String key, final double defaultValue) {
    if(this.contains(key, TagTypes.DOUBLE)) {
      return ((NumberTag) this.tags.get(key)).doubleValue();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putDouble(final @NonNull String key, final double value) {
    return this.edit(map -> map.put(key, DoubleTag.of(value)));
  }

  @Override
  public byte@NonNull[] getByteArray(final @NonNull String key) {
    if(this.contains(key, TagTypes.BYTE_ARRAY)) {
      return ((ByteArrayTag) this.tags.get(key)).value();
    }
    return new byte[0];
  }

  @Override
  public byte@NonNull[] getByteArray(final @NonNull String key, final byte@NonNull[] defaultValue) {
    if(this.contains(key, TagTypes.BYTE_ARRAY)) {
      return ((ByteArrayTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putByteArray(final @NonNull String key, final byte@NonNull[] value) {
    return this.edit(map -> map.put(key, ByteArrayTag.of(value)));
  }

  @Override
  public @NonNull String getString(final @NonNull String key, final @NonNull String defaultValue) {
    if(this.contains(key, TagTypes.STRING)) {
      return ((StringTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putString(final @NonNull String key, final @NonNull String value) {
    return this.edit(map -> map.put(key, StringTag.of(value)));
  }

  @Override
  public @NonNull ListTag getList(final @NonNull String key, final @NonNull ListTag defaultValue) {
    if(this.contains(key, TagTypes.LIST)) {
      return (ListTag) this.tags.get(key);
    }
    return defaultValue;
  }

  @Override
  public @NonNull ListTag getList(final @NonNull String key, final @NonNull TagType<? extends Tag> expectedType, final @NonNull ListTag defaultValue) {
    if(this.contains(key, TagTypes.LIST)) {
      final ListTag tag = (ListTag) this.tags.get(key);
      if(expectedType.test(tag.listType())) {
        return tag;
      }
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag getCompound(final @NonNull String key, final @NonNull CompoundTag defaultValue) {
    if(this.contains(key, TagTypes.COMPOUND)) {
      return (CompoundTag) this.tags.get(key);
    }
    return defaultValue;
  }

  @Override
  public int@NonNull[] getIntArray(final @NonNull String key) {
    if(this.contains(key, TagTypes.INT_ARRAY)) {
      return ((IntArrayTag) this.tags.get(key)).value();
    }
    return new int[0];
  }

  @Override
  public int@NonNull[] getIntArray(final @NonNull String key, final int@NonNull[] defaultValue) {
    if(this.contains(key, TagTypes.INT_ARRAY)) {
      return ((IntArrayTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putIntArray(final @NonNull String key, final int@NonNull[] value) {
    return this.edit(map -> map.put(key, IntArrayTag.of(value)));
  }

  @Override
  public long@NonNull[] getLongArray(final @NonNull String key) {
    if(this.contains(key, TagTypes.LONG_ARRAY)) {
      return ((LongArrayTag) this.tags.get(key)).value();
    }
    return new long[0];
  }

  @Override
  public long@NonNull[] getLongArray(final @NonNull String key, final long@NonNull[] defaultValue) {
    if(this.contains(key, TagTypes.LONG_ARRAY)) {
      return ((LongArrayTag) this.tags.get(key)).value();
    }
    return defaultValue;
  }

  @Override
  public @NonNull CompoundTag putLongArray(final @NonNull String key, final long@NonNull[] value) {
    return this.edit(map -> map.put(key, LongArrayTag.of(value)));
  }

  private CompoundTag edit(final Consumer<Map<String, Tag>> consumer) {
    final Map<String, Tag> tags = new HashMap<>(this.tags);
    consumer.accept(tags);
    return new CompoundTagImpl(tags);
  }

  @Override
  public boolean equals(final Object that) {
    return this == that || (that instanceof CompoundTagImpl && this.tags.equals(((CompoundTagImpl) that).tags));
  }

  @Override
  public int hashCode() {
    return this.tags.hashCode();
  }
}
