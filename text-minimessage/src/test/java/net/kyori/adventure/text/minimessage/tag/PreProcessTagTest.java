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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;

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
}
