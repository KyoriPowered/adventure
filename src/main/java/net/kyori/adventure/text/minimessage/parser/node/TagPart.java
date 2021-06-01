/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.parser.node;

import java.util.Map;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenParser;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an inner part of a tag.
 *
 * @since 4.2.0
 */
public final class TagPart {

  private final @NonNull String value;
  private final @NonNull Token token;

  /**
   * Constructs a new tag part.
   *
   * @param sourceMessage the source message
   * @param token the token that creates this tag part
   * @since 4.2.0
   */
  public TagPart(
    final @NonNull String sourceMessage,
    final @NonNull Token token,
    final @NonNull Map<String, Template> templates
  ) {
    String v = unquoteAndEscape(sourceMessage, token.startIndex(), token.endIndex());
    if(isTag(v)) {
      final String text = v.substring(1, v.length() - 1);
      final Template template = templates.get(text);
      if(template instanceof Template.StringTemplate) {
        v = ((Template.StringTemplate) template).value();
      }
    }

    this.value = v;
    this.token = token;
  }

  /**
   * Returns the value of this tag part.
   *
   * @return the value
   * @since 4.2.0
   */
  public @NonNull String value() {
    return this.value;
  }

  /**
   * Returns the token that created this tag part.
   *
   * @return the token
   * @since 4.2.0
   */
  public @NonNull Token token() {
    return this.token;
  }

  private static boolean isTag(final @NonNull String text) {
    return text.charAt(0) == '<' || text.charAt(text.length() - 1) == '>';
  }

  /**
   * Removes leading/trailing quotes from the given string, if necessary, and removes escaping {@code '\'} characters.
   *
   * @param text the input text
   * @param start the starting index of the substring
   * @param end the ending index of the substring
   * @return the output substring
   * @since 4.2.0
   */
  public static @NonNull String unquoteAndEscape(final @NonNull String text, final int start, final int end) {
    if(start == end) {
      return "";
    }

    int startIndex = start;
    int endIndex = end;

    final char firstChar = text.charAt(startIndex);
    final char lastChar = text.charAt(endIndex - 1);
    if(firstChar == '\'' || firstChar == '"') {
      startIndex++;
    }
    if(lastChar == '\'' || lastChar == '"') {
      endIndex--;
    }

    return TokenParser.unescape(text, startIndex, endIndex);
  }

  @Override
  public String toString() {
    return this.value;
  }
}
