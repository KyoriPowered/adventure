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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.minimessage.parser.Token;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ElementNode {

  private final ElementNode parent;
  private final Token token;
  private final String sourceMessage;
  private final List<ElementNode> children = new ArrayList<>();

  public ElementNode(final ElementNode parent, final Token token, final String sourceMessage) {
    this.parent = parent;
    this.token = token;
    this.sourceMessage = sourceMessage;
  }

  public ElementNode parent() {
    return this.parent;
  }

  public Token token() {
    return this.token;
  }

  public String sourceMessage() {
    return this.sourceMessage;
  }

  public List<ElementNode> children() {
    return this.children;
  }

  public @NonNull StringBuilder buildToString(final @NonNull StringBuilder sb, final int indent) {
    final char[] in = this.getIndent(indent);
    sb.append(in).append("Node {\n");
    for(final ElementNode child : this.children) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }

  char @NonNull [] getIndent(final int indent) {
    final char[] c = new char[indent * 2];
    Arrays.fill(c, ' ');
    return c;
  }

  @Override
  public String toString() {
    return this.buildToString(new StringBuilder(), 0).toString();
  }
}
