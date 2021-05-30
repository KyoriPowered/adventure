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
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Node that represents a tag.
 *
 * @since 4.2.0
 */
public final class TagNode extends ElementNode {

  private final List<TagPart> parts;
  private @Nullable Transformation transformation = null;

  /**
   * Creates a new element node.
   *
   * @param parent        the parent of this node
   * @param token         the token that created this node
   * @param sourceMessage the source message
   * @since 4.2.0
   */
  public TagNode(final ElementNode parent, final Token token, final String sourceMessage) {
    super(parent, token, sourceMessage);
    this.parts = genParts(token, sourceMessage);
  }

  private static List<TagPart> genParts(final Token token, final String sourceMessage) {
    final ArrayList<TagPart> parts = new ArrayList<>();

    if(token.childTokens() != null) {
      for(final Token childToken : token.childTokens()) {
        parts.add(new TagPart(sourceMessage, childToken));
      }
    }

    return parts;
  }

  /**
   * Returns the parts of this tag.
   *
   * @return the parts
   * @since 4.2.0
   */
  public List<TagPart> parts() {
    return this.parts;
  }

  /**
   * Returns the name of this tag.
   *
   * @return the name
   * @since 4.2.0
   */
  public String name() {
    if(this.parts.isEmpty()) {
      throw new ParsingException("Tag has no parts? " + this, this.sourceMessage(), this.token());
    }
    return this.parts.get(0).value();
  }

  /**
   * Gets the transformation attached to this tag node.
   *
   * @return the transformation for this tag node
   * @since 4.2.0
   */
  public @NonNull Transformation transformation() {
    return Objects.requireNonNull(this.transformation, "no transformation set");
  }

  /**
   * Sets the transformation that is represented by this tag.
   *
   * @param transformation the transformation
   * @since 4.2.0
   */
  public void transformation(final @NonNull Transformation transformation) {
    this.transformation = transformation;
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
    sb.append(in).append("TagNode(");

    final int size = this.parts.size();
    for(int i = 0; i < size; i++) {
      final TagPart part = this.parts.get(i);
      sb.append('\'').append(part.value()).append('\'');
      if(i != size - 1) {
        sb.append(", ");
      }
    }

    sb.append(") {\n");

    for(final ElementNode child : this.children()) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }
}
