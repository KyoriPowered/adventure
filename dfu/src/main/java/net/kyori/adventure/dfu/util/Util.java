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
package net.kyori.adventure.dfu.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

public class Util {
  private Util() {
  }

  private static final int SMALL_LIST_THRESHOLD = 8;

  public static <T> ToIntFunction<T> getLastIndexFunction(List<T> values) {
    Objects.requireNonNull(values, "Values list must not be null");

    int listSize = values.size();

    if (listSize < SMALL_LIST_THRESHOLD) {
      return values::lastIndexOf;  // Changed from indexOf to lastIndexOf to match method name
    } else {
      return buildLookupMap(values, listSize);
    }
  }

  private static <T> ToIntFunction<T> buildLookupMap(List<T> values, int size) {
    Object2IntMap<T> lookupMap = new Object2IntOpenHashMap<>(size);
    lookupMap.defaultReturnValue(-1);

    for (int index = 0; index < size; index++) {
      lookupMap.put(values.get(index), index);
    }

    return lookupMap;
  }
}
