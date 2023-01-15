/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.Collections;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.examination.string.MultiLineStringExaminer;
import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractTest {
  protected static final MiniMessage PARSER = MiniMessage.builder().debug(System.out::print).build();

  protected void assertSerializedEquals(final @NotNull String expected, final @NotNull ComponentLike input) {
    final String string = PARSER.serialize(input.asComponent());
    assertEquals(expected, string);
  }

  protected void assertParsedEquals(final @NotNull Component expected, final @NotNull String input) {
    this.assertParsedEquals(PARSER, expected, input);
  }

  protected void assertParsedEquals(final @NotNull Component expected, final @NotNull String input, final @NotNull TagResolver... args) {
    this.assertParsedEquals(PARSER, expected, input, args);
  }

  protected void assertParsedEquals(final MiniMessage miniMessage, final Component expected, final String input) {
    final String expectedSerialized = this.prettyPrint(expected.compact());
    final String actual = this.prettyPrint(miniMessage.deserialize(input).compact());
    assertEquals(expectedSerialized, actual);
  }

  protected void assertParsedEquals(final MiniMessage miniMessage, final Component expected, final String input, final @NotNull TagResolver... args) {
    final String expectedSerialized = this.prettyPrint(expected.compact());
    final String actual = this.prettyPrint(miniMessage.deserialize(input, TagResolver.resolver(args)).compact());
    assertEquals(expectedSerialized, actual);
  }

  protected final String prettyPrint(final Component component) {
    return component.examine(MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining("\n"));
  }

  public static Context dummyContext(final String originalMessage) {
    return ContextImpl.of(false, null, originalMessage, (MiniMessageImpl) PARSER, TagResolver.empty(), UnaryOperator.identity(), Component::compact);
  }

  public static ArgumentQueue emptyArgumentQueue(final Context context) {
    return new ArgumentQueueImpl<>(context, Collections.<Tag.Argument>emptyList());
  }
}
