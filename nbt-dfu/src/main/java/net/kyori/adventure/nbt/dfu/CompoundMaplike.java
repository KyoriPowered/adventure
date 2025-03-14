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

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapLike;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;

class CompoundMaplike implements MapLike<BinaryTag> {
  private final CompoundBinaryTag tag;

  CompoundMaplike(final CompoundBinaryTag tag) {
    this.tag = tag;
  }

  @Override
  public BinaryTag get(final BinaryTag key) {
    return this.tag.get(BinaryTagOps.unwrap(key));
  }

  @Override
  public BinaryTag get(final String key) {
    return this.tag.get(key);
  }

  @Override
  public Stream<Pair<BinaryTag, BinaryTag>> entries() {
    return StreamSupport.stream(this.tag.spliterator(), false)
      .map(ent -> Pair.of(StringBinaryTag.stringBinaryTag(ent.getKey()), ent.getValue()));
  }
}
