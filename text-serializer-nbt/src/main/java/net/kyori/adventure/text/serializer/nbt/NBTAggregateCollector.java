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
package net.kyori.adventure.text.serializer.nbt;

import java.util.Arrays;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*sealed*/ interface NBTAggregateCollector {

  void add(final @NotNull BinaryTag tag);

  @NotNull BinaryTag collect();

  static @NotNull NBTAggregateCollector create(final @NotNull NBTComponentSerializerImpl serializer) {
    return serializer.options().value(NBTSerializerOptions.EMIT_OPTIMIZED_LISTS) ? new Initial() : new ListCollector();
  }

  final class Initial implements NBTAggregateCollector {

    private @Nullable NBTAggregateCollector delegate = null;

    private Initial() {
    }

    @Override
    public void add(final @NotNull BinaryTag tag) {
      if (this.delegate != null) {
        this.delegate.add(tag);
      } else if (tag instanceof ByteBinaryTag) {
        this.delegate = new ByteArrayCollector(((ByteBinaryTag) tag).value());
      } else if (tag instanceof IntBinaryTag) {
        this.delegate = new IntArrayCollector(((IntBinaryTag) tag).value());
      } else if (tag instanceof LongBinaryTag) {
        this.delegate = new LongArrayCollector(((LongBinaryTag) tag).value());
      } else {
        this.delegate = new ListCollector(tag);
      }
    }

    @Override
    public @NotNull BinaryTag collect() {
      return this.delegate == null ? ListBinaryTag.empty() : this.delegate.collect();
    }
  }

  final class ListCollector implements NBTAggregateCollector {

    private final ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.heterogeneousListBinaryTag();

    private ListCollector() {
    }

    private ListCollector(final @NotNull BinaryTag firstElement) {
      this.add(firstElement);
    }

    @Override
    public void add(final @NotNull BinaryTag tag) {
      this.builder.add(tag);
    }

    @Override
    public @NotNull BinaryTag collect() {
      return this.builder.build().wrapHeterogeneity();
    }
  }

  final class ByteArrayCollector implements NBTAggregateCollector {

    private byte @NotNull [] array;
    private @Nullable NBTAggregateCollector delegate = null;

    private ByteArrayCollector(final byte firstElement) {
      this.array = new byte[]{firstElement};
    }

    @Override
    public void add(final @NotNull BinaryTag tag) {
      if (this.delegate != null) {
        this.delegate.add(tag);
      } else if (tag instanceof ByteBinaryTag) {
        final int index = this.array.length;
        this.array = Arrays.copyOf(this.array, index + 1);
        this.array[index] = ((ByteBinaryTag) tag).value();
      } else {
        this.delegate = new ListCollector();
        for (final byte element : this.array) {
          this.delegate.add(ByteBinaryTag.byteBinaryTag(element));
        }
        this.delegate.add(tag);
      }
    }

    @Override
    public @NotNull BinaryTag collect() {
      return this.delegate == null ? ByteArrayBinaryTag.byteArrayBinaryTag(this.array) : this.delegate.collect();
    }
  }

  final class IntArrayCollector implements NBTAggregateCollector {

    private int @NotNull [] array;
    private @Nullable NBTAggregateCollector delegate = null;

    private IntArrayCollector(final int firstElement) {
      this.array = new int[]{firstElement};
    }

    @Override
    public void add(final @NotNull BinaryTag tag) {
      if (this.delegate != null) {
        this.delegate.add(tag);
      } else if (tag instanceof IntBinaryTag) {
        final int index = this.array.length;
        this.array = Arrays.copyOf(this.array, index + 1);
        this.array[index] = ((IntBinaryTag) tag).value();
      } else {
        this.delegate = new ListCollector();
        for (final int element : this.array) {
          this.delegate.add(IntBinaryTag.intBinaryTag(element));
        }
        this.delegate.add(tag);
      }
    }

    @Override
    public @NotNull BinaryTag collect() {
      return this.delegate == null ? IntArrayBinaryTag.intArrayBinaryTag(this.array) : this.delegate.collect();
    }
  }

  final class LongArrayCollector implements NBTAggregateCollector {

    private long @NotNull [] array;
    private @Nullable NBTAggregateCollector delegate = null;

    private LongArrayCollector(final long firstElement) {
      this.array = new long[]{firstElement};
    }

    @Override
    public void add(final @NotNull BinaryTag tag) {
      if (this.delegate != null) {
        this.delegate.add(tag);
      } else if (tag instanceof LongBinaryTag) {
        final int index = this.array.length;
        this.array = Arrays.copyOf(this.array, index + 1);
        this.array[index] = ((LongBinaryTag) tag).value();
      } else {
        this.delegate = new ListCollector();
        for (final long element : this.array) {
          this.delegate.add(LongBinaryTag.longBinaryTag(element));
        }
        this.delegate.add(tag);
      }
    }

    @Override
    public @NotNull BinaryTag collect() {
      return this.delegate == null ? LongArrayBinaryTag.longArrayBinaryTag(this.array) : this.delegate.collect();
    }
  }
}
