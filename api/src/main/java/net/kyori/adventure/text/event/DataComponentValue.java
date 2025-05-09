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
package net.kyori.adventure.text.event;

import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

/**
 * A holder for the value of an item's data component.
 *
 * <p>The exact value is platform-specific. Serializers may provide their
 * own implementations as well, and any logic to serialize or deserialize
 * should be done per-serializer.</p>
 *
 * @since 4.17.0
 * @sinceMinecraft 1.20.5
 */
public interface DataComponentValue extends Examinable {
  /**
   * Get a marker value to indicate that a data component's value should be removed.
   *
   * @return the removed holder
   * @since 4.17.0
   * @sinceMinecraft 1.20.5
   */
  static DataComponentValue.@NotNull Removed removed() {
    return RemovedDataComponentValueImpl.REMOVED;
  }

  /**
   * Represent an {@link DataComponentValue} that can be represented as a binary tag.
   *
   * @since 4.17.0
   * @sinceMinecraft 1.20.5
   */
  interface TagSerializable extends DataComponentValue {

    /**
     * Convert this value into a binary tag value.
     *
     * @return the binary tag value
     * @since 4.17.0
     * @sinceMinecraft 1.20.5
     */
    @NotNull BinaryTagHolder asBinaryTag();
  }

  /**
   * Only valid in a patch-style usage, indicating that the data component with a certain key should be removed.
   *
   * @since 4.17.0
   * @sinceMinecraft 1.20.5
   */
  interface Removed extends DataComponentValue {
  }
}
