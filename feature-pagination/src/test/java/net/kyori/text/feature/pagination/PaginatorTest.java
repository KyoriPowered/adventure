/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.feature.pagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.Maps.immutableEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginatorTest {
  @Test
  void testForEachPageEntry() {
    final Collection<String> content = Arrays.asList("one", "two", "three", "four", "five");
    testForEachPageEntry(content);
    testForEachPageEntry(new LinkedHashSet<>(content));
  }

  private static void testForEachPageEntry(final Collection<String> content) {
    final List<Map.Entry<Integer, String>> results = new ArrayList<>(5);

    Paginator.forEachPageEntry(content, 10, 1, (value, index) -> results.add(immutableEntry(index, value)));
    assertEquals(Arrays.asList(immutableEntry(0, "one"), immutableEntry(1, "two"), immutableEntry(2, "three"), immutableEntry(3, "four"), immutableEntry(4, "five")), results);
    results.clear();

    Paginator.forEachPageEntry(content, 3, 1, (value, index) -> results.add(immutableEntry(index, value)));
    assertEquals(Arrays.asList(immutableEntry(0, "one"), immutableEntry(1, "two"), immutableEntry(2, "three")), results);
    results.clear();

    Paginator.forEachPageEntry(content, 3, 2, (value, index) -> results.add(immutableEntry(index, value)));
    assertEquals(Arrays.asList(immutableEntry(3, "four"), immutableEntry(4, "five")), results);
    results.clear();

    Paginator.forEachPageEntry(content, 1, 2, (value, index) -> results.add(immutableEntry(index, value)));
    assertEquals(Collections.singletonList(immutableEntry(1, "two")), results);
    results.clear();
  }
}
