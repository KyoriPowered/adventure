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
package net.kyori.adventure.text.minimessage.internal.parser.node;

import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a string of chars.
 *
 * @since 4.10.0
 */
public final class TextNode extends ValueNode {
  private static boolean isEscape(final int escape) {
    return escape == TokenParser.TAG_START || escape == TokenParser.ESCAPE;
  }

  /**
   * Creates a new text node.
   *
   * @param parent the parent of this node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @since 4.10.0
   */
  public TextNode(
    final @Nullable ElementNode parent,
    final @NotNull Token token,
    final @NotNull String sourceMessage
  ) {
    super(parent, token, sourceMessage, TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex(), TextNode::isEscape));
  }

  @Override
  String valueName() {
    return "TextNode";
  }
}
