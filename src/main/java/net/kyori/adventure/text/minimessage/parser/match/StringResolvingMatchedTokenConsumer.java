/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.parser.match;

import java.util.function.Predicate;
import net.kyori.adventure.text.minimessage.Placeholder;
import net.kyori.adventure.text.minimessage.Placeholder.StringPlaceholder;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.jetbrains.annotations.NotNull;

/**
 * A matched token consumer that produces a string and returns a copy of the string with placeholders resolved.
 *
 * @since 4.2.0
 */
public final class StringResolvingMatchedTokenConsumer extends MatchedTokenConsumer<String> {
  private final StringBuilder builder;
  private final Predicate<String> tagChecker;
  private final PlaceholderResolver placeholderResolver;

  /**
   * Creates a placeholder resolving matched token consumer.
   *
   * @param input the input
   * @since 4.2.0
   */
  public StringResolvingMatchedTokenConsumer(
    final @NotNull String input,
    final @NotNull Predicate<String> tagChecker,
    final @NotNull PlaceholderResolver placeholderResolver
  ) {
    super(input);
    this.builder = new StringBuilder(input.length());
    this.tagChecker = tagChecker;
    this.placeholderResolver = placeholderResolver;
  }

  @Override
  public void accept(final int start, final int end, final @NotNull TokenType tokenType) {
    super.accept(start, end, tokenType);

    if (tokenType != TokenType.OPEN_TAG) {
      // just add it normally, we don't care about other tags
      this.builder.append(this.input.substring(start, end));
    } else {
      // well, now we need to work out if it's a tag or a placeholder!
      final String match = this.input.substring(start, end);
      final String tag = this.input.substring(start + 1, end - 1);

      if (this.tagChecker.test(tag)) {
        // it's a tag, not a placeholder, so we don't care
        this.builder.append(match);
      } else {
        // we might care if it's a placeholder!
        if (this.placeholderResolver.canResolve(tag)) {
          final Placeholder placeholder = this.placeholderResolver.resolve(tag);

          if (placeholder instanceof StringPlaceholder) {
            // we only care about string placeholders!
            this.builder.append(((StringPlaceholder) placeholder).value());
            return;
          }
        }

        // if we get here, the placeholder wasn't found or was null
        this.builder.append(match);
      }
    }
  }

  @Override
  public @NotNull String result() {
    return this.builder.toString();
  }
}
