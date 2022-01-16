/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.text.minimessage.placeholder.Replacement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceholderResolverTest {

  @Test
  void testEmptyBuilder() {
    assertEquals(PlaceholderResolver.empty(), PlaceholderResolver.builder().build());
  }

  @Test
  void testBuilderUnpacksSingleElement() {
    final PlaceholderResolver test = key -> Replacement.miniMessage("hello");
    assertEquals(test, PlaceholderResolver.builder().resolver(test).build());
  }

  @Test
  void testSingleAndResolversCombine() {
    final List<Placeholder<?>> placeholders = Arrays.asList(
      Placeholder.component("foo", Component.text("fizz")),
      Placeholder.miniMessage("overlapping", "from list")
    );
    final PlaceholderResolver resolver = key -> {
      switch (key) {
        case "one": return Replacement.miniMessage("fish");
        case "overlapping": return Replacement.miniMessage("from resolver");
        default: return null;
      }
    };

    final PlaceholderResolver built = PlaceholderResolver.builder()
      .placeholders(placeholders)
      .resolver(resolver)
      .build();

    // from placeholders only
    assertEquals(Component.text("fizz"), built.resolve("foo").value());

    // from resolver only
    assertEquals("fish", built.resolve("one").value());

    // shared, resolver takes priority
    assertEquals("from resolver", built.resolve("overlapping").value());
  }

}
