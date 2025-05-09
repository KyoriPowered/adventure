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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.kyori.adventure.text.event.DataComponentValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * An {@link DataComponentValue} implementation that holds a JsonElement.
 *
 * <p>This holder is exposed to allow conversions to/from gson data holders.</p>
 *
 * @since 4.17.0
 */
@ApiStatus.NonExtendable
public interface GsonDataComponentValue extends DataComponentValue {
  /**
   * Create a box for item data that can be understood by the gson serializer.
   *
   * @param data the item data to hold
   * @return a newly created item data holder instance
   * @since 4.17.0
   */
  static GsonDataComponentValue gsonDataComponentValue(final @NotNull JsonElement data) {
    if (data instanceof JsonNull) {
      return GsonDataComponentValueImpl.RemovedGsonComponentValueImpl.INSTANCE;
    } else {
      return new GsonDataComponentValueImpl(requireNonNull(data, "data"));
    }
  }

  /**
   * The contained element, intended for read-only use.
   *
   * @return a copy of the contained element
   * @since 4.17.0
   */
  @NotNull JsonElement element();
}
