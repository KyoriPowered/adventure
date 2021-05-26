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

import net.kyori.adventure.text.minimessage.parser.Token;

public final class TagPart {

  private final String value;
  private final Token token;

  public TagPart(final String sourceMessage, final Token token) {
    this.value = unquote(sourceMessage.substring(token.startIndex(), token.endIndex()));
    this.token = token;
  }

  public String value() {
    return this.value;
  }

  public Token token() {
    return this.token;
  }

  public static String unquote(final String text) {
    int startIndex = 0;
    int endIndex = text.length();

    if(endIndex == 0) {
      return text;
    }

    final char first = text.charAt(startIndex);
    final char last = text.charAt(endIndex - 1);
    if(first == '\'' || first == '"') {
      startIndex++;
    }
    if(last == '\'' || last == '"') {
      endIndex--;
    }

    return unescape(text, startIndex, endIndex);
  }

  private static String unescape(final String text, final int start, final int end) {
    int from = start;
    int i = text.indexOf('\\', from);
    if(i == -1 || i >= end) {
      return text.substring(from, end);
    }

    final StringBuilder sb = new StringBuilder();
    while(i != -1 && i < end) {
      sb.append(text, from, i);
      i++;
      final int codePoint = text.codePointAt(i);
      sb.appendCodePoint(codePoint);

      i++;
      if(!Character.isBmpCodePoint(codePoint)) {
        i++;
      }

      from = i;
      i = text.indexOf('\\', from);
    }

    sb.append(text, from, end);

    return sb.toString();
  }
}
