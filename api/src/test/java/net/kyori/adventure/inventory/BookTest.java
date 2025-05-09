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
package net.kyori.adventure.inventory;

import com.google.common.testing.EqualsTester;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {
  private static final Component TITLE = Component.text("title");
  private static final Component AUTHOR = Component.text("author");

  private static Component page(final int index) {
    return Component.text("page." + index);
  }

  private static Stream<Component> pages(final int quantity) {
    return IntStream.range(0, quantity)
      .mapToObj(BookTest::page);
  }

  private static Component[] arrayOfPages(final int quantity) {
    return pages(quantity).toArray(Component[]::new);
  }

  private static List<Component> listOfPages(final int quantity) {
    return pages(quantity).collect(Collectors.toList());
  }

  @Test
  void testTitle() {
    final Book b0 = Book.book(TITLE, AUTHOR, arrayOfPages(1));
    final Book b1 = b0.title(TITLE.color(NamedTextColor.RED));
    assertEquals(TITLE, b0.title());
    assertEquals(TITLE.color(NamedTextColor.RED), b1.title());
  }

  @Test
  void testAuthor() {
    final Book b0 = Book.book(TITLE, AUTHOR, arrayOfPages(1));
    final Book b1 = b0.author(AUTHOR.color(NamedTextColor.RED));
    assertEquals(AUTHOR, b0.author());
    assertEquals(AUTHOR.color(NamedTextColor.RED), b1.author());
  }

  @Test
  void testPages() {
    final Book b0 = Book.book(TITLE, AUTHOR, arrayOfPages(1));
    final Book b1 = b0.pages(arrayOfPages(2));
    assertEquals(listOfPages(1), b0.pages());
    assertEquals(listOfPages(2), b1.pages());
  }

  @Test
  void testRebuild() {
    final Book book = Book.book(TITLE, AUTHOR, arrayOfPages(1));
    assertEquals(book, book.toBuilder().build());
  }

  @Test
  void testBuild() {
    final Book b0 = Book.builder()
      .title(TITLE)
      .author(AUTHOR)
      .pages(arrayOfPages(2))
      .build();
    assertEquals(Book.book(TITLE, AUTHOR, arrayOfPages(2)), b0);
    final Book b1 = Book.builder()
      .title(TITLE)
      .author(AUTHOR)
      .addPage(page(0))
      .addPage(page(1))
      .build();
    assertEquals(Book.book(TITLE, AUTHOR, arrayOfPages(2)), b1);
  }

  @Test
  void testEquality() {
    new EqualsTester()
      .addEqualityGroup(
        Book.book(TITLE, AUTHOR, arrayOfPages(1)),
        Book.book(TITLE, AUTHOR, listOfPages(1))
      )
      .addEqualityGroup(
        Book.book(TITLE, AUTHOR, arrayOfPages(2)),
        Book.book(TITLE, AUTHOR, listOfPages(2))
      )
      .testEquals();
  }
}
