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
package net.kyori.adventure.text.minimessage.tag;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.TestBase;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class TagResolverTest {

  @Test
  void testEmptyBuilder() {
    assertEquals(TagResolver.empty(), TagResolver.builder().build());
  }

  @Test
  void testBuilderUnpacksSingleElement() {
    final TagResolver.WithoutArguments test = key -> Tag.miniMessage("hello");
    assertEquals(test, TagResolver.builder().resolver(test).build());
  }

  @Test
  void testSingleAndResolversCombine() {
    final List<TagResolver> placeholders = Arrays.asList(
      Placeholder.component("foo", Component.text("fizz")),
      Placeholder.miniMessage("overlapping", "from list")
    );
    final TagResolver.WithoutArguments resolver = key -> {
      switch (key) {
        case "one": return Tag.miniMessage("fish");
        case "overlapping": return Tag.miniMessage("from resolver");
        default: return null;
      }
    };

    final TagResolver built = TagResolver.builder()
      .resolvers(placeholders)
      .resolver(resolver)
      .build();

    // from placeholders only
    assertEquals(Component.text("fizz"), ((Inserting) resolveForTest(built, "foo")).value());

    // from resolver only
    assertEquals("fish", ((PreProcess) resolveForTest(built, "one")).value());

    // shared, resolver takes priority
    assertEquals("from resolver", ((PreProcess) resolveForTest(built, "overlapping")).value());
  }

  private static @NotNull Tag resolveForTest(final TagResolver resolver, final String tag) {
    try {
      final Context ctx = TestBase.dummyContext("help i shouldn't be seen");
      final @Nullable Tag result = resolver.resolve(tag, TestBase.emptyArgumentQueue(ctx), ctx);
      assertNotNull(result, () -> "tag " + tag + " from resolver " + resolver);
      return result;
    } catch (final ParsingException ex) {
      fail(ex);
      throw new RuntimeException(ex);
    }
  }

}
