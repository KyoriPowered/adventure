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
package net.kyori.adventure.nbt.dfu;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import java.util.function.UnaryOperator;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import org.jetbrains.annotations.Nullable;

public class ListBinaryTagBuilder implements ListBuilder<BinaryTag> {
  private final BinaryTagOps ops;
  private final ListBinaryTag.Builder<BinaryTag> builder = ListBinaryTag.builder();

  private @Nullable DataResult<?> error = null;

  public ListBinaryTagBuilder(final BinaryTagOps ops) {
    this.ops = ops;
  }

  @Override
  public DynamicOps<BinaryTag> ops() {
    return this.ops;
  }

  @Override
  public DataResult<BinaryTag> build(final BinaryTag prefix) {
    if (this.error != null) {
      return this.error.map(x -> (BinaryTag) null);
    }

    if (prefix.equals(this.ops.empty())) {
      return DataResult.success(this.builder.build());
    } else if (prefix instanceof ListBinaryTag) {
      for (final BinaryTag tag : (ListBinaryTag) prefix) {
        this.builder.add(tag);
      }
    }
    return DataResult.success(this.builder.build(), Lifecycle.stable());
  }

  @Override
  public ListBuilder<BinaryTag> add(final BinaryTag value) {
    this.builder.add(value);
    return this;
  }

  @Override
  public ListBuilder<BinaryTag> add(final DataResult<BinaryTag> value) {
    if (value.error().isPresent()) {
      return this.withErrorsFrom(value);
    } else {
      this.add(value.result().get());
    }
    return this;
  }

  @Override
  public ListBuilder<BinaryTag> withErrorsFrom(final DataResult<?> result) {
    if (this.error != null) {
      this.error = this.error.flatMap(x -> result);
    } else {
      this.error = result;
    }
    return this;
  }

  @Override
  public ListBuilder<BinaryTag> mapError(final UnaryOperator<String> onError) {
    if (this.error != null) {
      this.error = this.error.mapError(onError);
    }
    return this;
  }
}
