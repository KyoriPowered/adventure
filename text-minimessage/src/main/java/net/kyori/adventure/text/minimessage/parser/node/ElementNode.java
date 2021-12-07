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
package net.kyori.adventure.text.minimessage.parser.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node in the tree.
 *
 * @since 4.2.0
 */
public class ElementNode {
  private final @Nullable ElementNode parent;
  private final @Nullable Token token;
  private final String sourceMessage;
  private final List<ElementNode> children = new ArrayList<>();

  /**
   * Creates a new element node.
   *
   * @param parent the parent of this node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  ElementNode(final @Nullable ElementNode parent, final @Nullable Token token, final @NotNull String sourceMessage) {
    this.parent = parent;
    this.token = token;
    this.sourceMessage = sourceMessage;
  }

  /**
   * Returns the parent of this node, if present.
   *
   * @return the parent or null
   * @since 4.2.0
   */
  public @Nullable ElementNode parent() {
    return this.parent;
  }

  /**
   * Returns the token that lead to the creation of this token.
   *
   * @return the token
   * @since 4.2.0
   */
  public @Nullable Token token() {
    return this.token;
  }

  /**
   * Returns the source message of this node.
   *
   * @return the source message
   * @since 4.2.0
   */
  public @NotNull String sourceMessage() {
    return this.sourceMessage;
  }

  /**
   * Returns the children of this node.
   *
   * @return the children of this node
   * @since 4.2.0
   */
  public List<ElementNode> children() {
    return this.children;
  }

  /**
   * Adds a child to this node.
   *
   * <p>This method will attempt to join text tokens together if possible.</p>
   *
   * @param childNode the child node to add.
   * @since 4.2.0
   */
  public void addChild(final ElementNode childNode) {
    final int last = this.children.size() - 1;
    if (!(childNode instanceof TextNode) || this.children.isEmpty() || !(this.children.get(last) instanceof TextNode)) {
      this.children.add(childNode);
    } else {
      final TextNode lastNode = (TextNode) this.children.remove(last);
      if (lastNode.token().endIndex() == childNode.token().startIndex()) {
        final Token replace = new Token(lastNode.token().startIndex(), childNode.token().endIndex(), TokenType.TEXT);
        this.children.add(new TextNode(this, replace, lastNode.sourceMessage()));
      } else {
        // These nodes aren't adjacent in the string, so put the last one back
        this.children.add(lastNode);
        this.children.add(childNode);
      }
    }
  }

  /**
   * Serializes this node to a string.
   *
   * @param sb the string builder to serialize into
   * @param indent the current indent level
   * @return the passed string builder, for chaining
   * @since 4.2.0
   */
  public @NotNull StringBuilder buildToString(final @NotNull StringBuilder sb, final int indent) {
    final char[] in = this.ident(indent);
    sb.append(in).append("Node {\n");
    for (final ElementNode child : this.children) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }

  char @NotNull [] ident(final int indent) {
    final char[] c = new char[indent * 2];
    Arrays.fill(c, ' ');
    return c;
  }

  @Override
  public String toString() {
    return this.buildToString(new StringBuilder(), 0).toString();
  }
}
