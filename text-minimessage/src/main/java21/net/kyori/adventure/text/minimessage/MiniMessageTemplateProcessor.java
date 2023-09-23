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

import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A template processor to produce MiniMessage strings from Java 21 string templates.
 *
 * @since 4.99.99
 */
public final class MiniMessageTemplateProcessor implements StringTemplate.Processor<Component, ParsingException> {
  private static final String ARG_NAME = "__template_arg";
  private final MiniMessage parser;

  /**
   * Create a new template processor for a parser instance.
   *
   * @param parserInstance the parser instance
   * @return the template processor instance
   * @since 4.99.99
   */
  public static @NotNull MiniMessageTemplateProcessor templateProcessor(final @NotNull MiniMessage parserInstance) {
    return new MiniMessageTemplateProcessor(requireNonNull(parserInstance));
  }

  /**
   * Wrap a string so that it can be interpreted as a parsed tag, rather than the unparsed tag that regular arguments are.
   *
   * @param mm the MiniMessage content to interpret as a parsed tag
   * @return the parsed tag
   * @since 4.99.99
   */
  public static @NotNull Object parsed(final @NotNull String mm) {
    return new ParsedWrapper(mm);
  }

  MiniMessageTemplateProcessor(final @NotNull MiniMessage parser) {
    this.parser = parser;
  }

  @Override
  public @NotNull Component process(final @NotNull StringTemplate stringTemplate) throws ParsingException {
    final var extraArg = this.resolver(stringTemplate.values());
    final StringBuilder mmBuilder = new StringBuilder();
    final var stringFragments = stringTemplate.fragments();
    if (!stringFragments.isEmpty()) {
      mmBuilder.append(stringFragments.get(0));
    }

    for (int i = 1; i < stringFragments.size(); i++) {
      mmBuilder.append("<" + ARG_NAME + ":").append(i - 1).append(">");
      mmBuilder.append(stringFragments.get(i));
    }

    return this.parser.deserialize(mmBuilder.toString(), extraArg);
  }

  private TagResolver resolver(final List<Object> values) {
    return TagResolver.resolver(ARG_NAME, (args, ctx) -> {
      final int tagIdx = args.popOr("index required").asInt()
        .orElseThrow(() -> ctx.newException("tag index is not an int where it was expected to be"));

      final Object value = values.get(tagIdx);
      return switch (value) {
        case ComponentLike c -> Tag.selfClosingInserting(c);
        case ParsedWrapper p -> Tag.preProcessParsed(p.input());
        case StyleBuilderApplicable s -> Tag.styling(s);
        default -> Tag.selfClosingInserting(Component.text(String.valueOf(value)));
      };
    });
  }

  private record ParsedWrapper(@NotNull String input) {
    ParsedWrapper {
      requireNonNull(input, "input");
    }
  }
}
