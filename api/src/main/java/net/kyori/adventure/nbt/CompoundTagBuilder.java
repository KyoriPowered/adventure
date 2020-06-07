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

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;

final class CompoundTagBuilder implements CompoundTag.Builder {
  private final Map<String, Tag> tags = new HashMap<>();

  @Override
  public CompoundTag.@NonNull Builder put(final @NonNull String key, @NonNull final Tag tag) {
    this.tags.put(key, tag);
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putByte(final @NonNull String key, final byte value) {
    this.tags.put(key, ByteTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putShort(final @NonNull String key, final short value) {
    this.tags.put(key, ShortTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putInt(final @NonNull String key, final int value) {
    this.tags.put(key, IntTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putLong(final @NonNull String key, final long value) {
    this.tags.put(key, LongTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putFloat(final @NonNull String key, final float value) {
    this.tags.put(key, FloatTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putDouble(final @NonNull String key, final double value) {
    this.tags.put(key, DoubleTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putByteArray(final @NonNull String key, final byte@NonNull[] value) {
    this.tags.put(key, ByteArrayTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putString(final @NonNull String key, final @NonNull String value) {
    this.tags.put(key, StringTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putIntArray(final @NonNull String key, final int@NonNull[] value) {
    this.tags.put(key, IntArrayTag.of(value));
    return this;
  }

  @Override
  public CompoundTag.@NonNull Builder putLongArray(final @NonNull String key, final long@NonNull[] value) {
    this.tags.put(key, LongArrayTag.of(value));
    return this;
  }

  @Override
  public @NonNull CompoundTag build() {
    return new CompoundTagImpl(new HashMap<>(this.tags));
  }
}
