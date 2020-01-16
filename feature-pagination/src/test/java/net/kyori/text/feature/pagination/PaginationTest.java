/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaginationTest {
  private static final Component TITLE = TextComponent.of("The Things");
  private static final Component EMPTY = TextComponent.of("E M P T Y");
  private static final Pagination<String> PAGINATION = Pagination.builder()
    .renderer(new Pagination.Renderer() {
      @Override
      public @NonNull Component renderEmpty() {
        return EMPTY;
      }
    })
    .build(TITLE, (value, index) -> Collections.singleton(value == null ? TextComponent.of("<null>") : TextComponent.of(value, TextColor.GOLD)), page -> "/page " + page);
  private static final List<String> CONTENT_0 = Collections.emptyList();
  private static final List<String> CONTENT_2 = content(2);
  private static final List<String> CONTENT_14 = content(14);

  private static List<String> content(final int size) {
    return Stream.generate(() -> UUID.randomUUID().toString().substring(24))
      .limit(size)
      .collect(Collectors.toList());
  }

  @Test
  void testRender_empty() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_0, 1);
    assertEquals(1, rendered.size());
    assertEquals(EMPTY, rendered.get(0));
  }

  @Test
  void testRender_unknownPage() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_14, 0);
    assertEquals(1, rendered.size());
    assertEquals(Pagination.DEFAULT_RENDERER.renderUnknownPage(0, 3), rendered.get(0));
  }

  @Test
  void testRender_noPrevious() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_14, 1);
    assertEquals(6 + 1 + 1, rendered.size());
    assertTrue(rendered.get(0).contains(TITLE));
    for(int i = 1; i < 7; i++) {
      assertEquals(TextColor.GOLD, rendered.get(i).color());
    }

    final String c7 = rendered.get(7).toString();
    assertTrue(c7.contains("Next") && !c7.contains("Previous"));
  }

  @Test
  void testRender() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_14, 2);
    assertEquals(6 + 1 + 1, rendered.size());
    assertTrue(rendered.get(0).contains(TITLE));
    for(int i = 1; i < 7; i++) {
      assertEquals(TextColor.GOLD, rendered.get(i).color());
    }

    final String c7 = rendered.get(7).toString();
    assertTrue(c7.contains("Next") && c7.contains("Previous"));
  }

  @Test
  void testRender_noNextOrPrevious() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_2, 1);
    assertEquals(2 + 1 + 1, rendered.size());
    assertTrue(rendered.get(0).contains(TITLE));
    for(int i = 1; i < 2; i++) {
      assertEquals(TextColor.GOLD, rendered.get(i).color());
    }
    final String c3 = rendered.get(3).toString();
    assertTrue(!c3.contains("Next") && !c3.contains("Previous"));
  }

  @Test
  void testRender_noNextButPrevious() {
    final List<? extends Component> rendered = PAGINATION.render(CONTENT_14, 3);
    assertEquals(2 + 1 + 1, rendered.size());
    assertTrue(rendered.get(0).contains(TITLE));
    for(int i = 1; i < 2; i++) {
      assertEquals(TextColor.GOLD, rendered.get(i).color());
    }

    final String c3 = rendered.get(3).toString();
    assertTrue(!c3.contains("Next") && c3.contains("Previous"));
  }
}
