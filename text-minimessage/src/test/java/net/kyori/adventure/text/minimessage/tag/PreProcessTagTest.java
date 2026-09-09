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
package net.kyori.adventure.text.minimessage.tag;

import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreProcessTagTest extends AbstractTest {

  @Test
  void checkPreProcessTag() {
    final String input = "<test:'Hello! ':bla>";
    final Component expected = text("Hello! bla");

    this.assertParsedEquals(
      expected,
      input,
      TagResolver.resolver("test", (argumentQueue, context) ->
        Tag.preProcessParsed(argumentQueue.pop().value() + argumentQueue.pop().value()))
    );
  }

  @Test
  void checkSpecialChars() {
    final String input = "<test:'::':'<bla>'>";
    final Component expected = text("::<bla>");

    this.assertParsedEquals(
      expected,
      input,
      TagResolver.resolver("test", (argumentQueue, context) ->
        Tag.preProcessParsed(argumentQueue.pop().value() + argumentQueue.pop().value()))
    );
  }

  @Test
  void recursionTest() {
    final String input = "This is <recursion>!";
    final Component expected = text("This is !");

    this.assertParsedEquals(
      expected,
      input,
      parsed("recursion", "<recursion>")
    );
  }

  @Test
  void resolverWithoutTypeHintRemainsCompatible() {
    final AtomicInteger calls = new AtomicInteger();
    final TagResolver resolver = new TagResolver() {
      @Override
      public Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
        calls.incrementAndGet();
        return "probe".equals(name) ? Tag.selfClosingInserting(text("value")) : null;
      }

      @Override
      public boolean has(final String name) {
        return "probe".equals(name);
      }
    };

    assertParsedEquals(text("value"), "<probe>", resolver);
    assertEquals(2, calls.get());
  }

  @Test
  void resolverTypeHintAvoidsUnnecessaryResolution() {
    final AtomicInteger calls = new AtomicInteger();
    final TagResolver resolver = new TagResolver() {
      @Override
      public Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
        calls.incrementAndGet();
        return "probe".equals(name) ? Tag.selfClosingInserting(text("value")) : null;
      }

      @Override
      public Class<? extends Tag> tagType(final String name) {
        return "probe".equals(name) ? Inserting.class : null;
      }

      @Override
      public boolean has(final String name) {
        return "probe".equals(name);
      }
    };

    assertParsedEquals(text("value"), "<probe>", resolver);
    assertEquals(1, calls.get());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void preprocessFallbackIsNotSkippedByAnEarlierTypeHint(final boolean throwsException) {
    final TagResolver resolver = new TagResolver() {
      @Override
      public Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
        if (throwsException) {
          throw ctx.newException("Try the next resolver");
        }
        return null;
      }

      @Override
      public Class<? extends Tag> tagType(final String name) {
        return "probe".equals(name) ? Inserting.class : null;
      }

      @Override
      public boolean has(final String name) {
        return "probe".equals(name);
      }
    };

    assertParsedEquals(text("value"), "<probe>", TagResolver.resolver(parsed("probe", "value"), resolver));
  }

  @Test
  void preprocessTypeHintStillResolves() {
    final AtomicInteger calls = new AtomicInteger();
    final TagResolver resolver = new TagResolver() {
      @Override
      public Tag resolve(final String name, final ArgumentQueue arguments, final Context ctx) {
        calls.incrementAndGet();
        return "probe".equals(name) ? Tag.preProcessParsed("value") : null;
      }

      @Override
      public Class<? extends Tag> tagType(final String name) {
        return "probe".equals(name) ? PreProcess.class : null;
      }

      @Override
      public boolean has(final String name) {
        return "probe".equals(name);
      }
    };

    assertParsedEquals(text("value"), "<probe>", resolver);
    assertEquals(1, calls.get());
  }
}
