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
import net.kyori.adventure.text.minimessage.parser.TokenParser;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an erroneous node.
 *
 * @since 4.2.0
 */
public class ErrorNode extends ElementNode {

  private final String value;

  /**
   * Constructs a new error node.
   *
   * @param parent the parent node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public ErrorNode(final ElementNode parent, final Token token, final String sourceMessage) {
    super(parent, token, sourceMessage);
    this.value = TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex());
  }

  /**
   * Serializes this node to a string.
   *
   * @param sb the string builder to serialize into
   * @param indent the current indent level
   * @return the passed string builder, for chaining
   * @since 4.2.0
   */
  public @NonNull StringBuilder buildToString(final @NonNull StringBuilder sb, final int indent) {
    final char[] in = this.ident(indent);
    sb.append(in).append("ErrorNode('").append(this.value).append("')\n");
    return sb;
  }
}
