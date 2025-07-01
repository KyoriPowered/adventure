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

import java.util.Objects;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import net.kyori.adventure.text.event.DataComponentValue;
import org.jetbrains.annotations.NotNull;

class NBTDataComponentValueImpl implements NBTDataComponentValue {

  private final BinaryTag binaryTag;

  NBTDataComponentValueImpl(final @NotNull BinaryTag binaryTag) {
    this.binaryTag = binaryTag;
  }

  @Override
  public @NotNull BinaryTag binaryTag() {
    return this.binaryTag;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof NBTDataComponentValueImpl)) return false;
    final NBTDataComponentValueImpl that = (NBTDataComponentValueImpl) o;
    return Objects.equals(this.binaryTag, that.binaryTag);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.binaryTag);
  }

  static final class RemovedNBTComponentValueImpl extends NBTDataComponentValueImpl implements DataComponentValue.Removed {
    static final RemovedNBTComponentValueImpl INSTANCE = new RemovedNBTComponentValueImpl();

    RemovedNBTComponentValueImpl() {
      super(EndBinaryTag.endBinaryTag());
    }
  }
}
