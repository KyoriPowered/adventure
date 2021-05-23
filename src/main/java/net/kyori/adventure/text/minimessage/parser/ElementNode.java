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
package net.kyori.adventure.text.minimessage.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementNode {

  private final ElementNode parent;
  private final List<ElementNode> children = new ArrayList<>();

  public ElementNode(final ElementNode parent) {
    this.parent = parent;
  }

  public ElementNode getParent() {
    return this.parent;
  }

  public List<ElementNode> getChildren() {
    return this.children;
  }

  @Override
  public String toString() {
    return buildToString(new StringBuilder(), 0).toString();
  }

  public StringBuilder buildToString(StringBuilder sb, int indent) {
    final char[] in = getIndent(indent);
    sb.append(in).append("Node {\n");
    for(final ElementNode child : this.children) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }

  public char[] getIndent(int indent) {
    final char[] c = new char[indent * 2];
    Arrays.fill(c, ' ');
    return c;
  }

  public static final class RawTextNode extends ElementNode {

    private final String value;

    public RawTextNode(final ElementNode parent, final String value) {
      super(parent);
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }

    public StringBuilder buildToString(StringBuilder sb, int indent) {
      final char[] in = getIndent(indent);
      sb.append(in).append("TextNode('").append(this.value).append("')\n");
      return sb;
    }
  }

  public static final class TagNode extends ElementNode {

    private final List<String> parts;

    public TagNode(final ElementNode parent, final List<String> parts) {
      super(parent);
      this.parts = parts;
    }

    public List<String> getParts() {
      return this.parts;
    }

    public StringBuilder buildToString(StringBuilder sb, int indent) {
      final char[] in = getIndent(indent);
      sb.append(in).append("TagNode(");

      final int size = this.parts.size();
      for(int i = 0; i < size; i++) {
        final String part = this.parts.get(i);
        sb.append('\'').append(part).append('\'');
        if(i != size - 1) {
          sb.append(", ");
        }
      }

      sb.append(") {\n");

      for(final ElementNode child : this.getChildren()) {
        child.buildToString(sb, indent + 1);
      }
      sb.append(in).append("}\n");
      return sb;
    }
  }
}
