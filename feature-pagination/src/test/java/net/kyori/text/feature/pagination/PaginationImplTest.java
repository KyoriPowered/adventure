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

import net.kyori.text.TextComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaginationImplTest {
  @Test
  @SuppressWarnings("PointlessArithmeticExpression")
  void testLength() {
    assertEquals(0, PaginationImpl.length(TextComponent.empty()));
    assertEquals(0 + 3 + 1 + 3, PaginationImpl.length(
      TextComponent.builder()
        .append(TextComponent.of("abc"))
        .append(TextComponent.space())
        .append(TextComponent.of("def"))
      .build()
    ));
  }

  @Test
  void testRepeat() {
    assertEquals("aaa", PaginationImpl.repeat("a", 3));
    assertEquals("abcabcabc", PaginationImpl.repeat("abc", 3));
  }

  @Test
  void testPages() {
    assertEquals(0, PaginationImpl.pages(5, 0));
    assertEquals(1, PaginationImpl.pages(5, 1));
    assertEquals(1, PaginationImpl.pages(5, 4));
    assertEquals(1, PaginationImpl.pages(5, 5));
    assertEquals(2, PaginationImpl.pages(5, 6));
  }

  @Test
  void testPageInRange() {
    assertFalse(PaginationImpl.pageInRange(0, 2));
    assertFalse(PaginationImpl.pageInRange(-1, 2));
    assertFalse(PaginationImpl.pageInRange(3, 2));
    assertTrue(PaginationImpl.pageInRange(3, 3));
    assertTrue(PaginationImpl.pageInRange(3, 4));
  }
}
