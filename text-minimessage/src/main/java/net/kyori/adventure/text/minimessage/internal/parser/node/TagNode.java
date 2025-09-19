/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Node that represents a tag.
 *
 * @since 4.10.0
 */
public final class TagNode extends ElementNode {
  private final List<TagPart> parts;
  private @Nullable Tag tag = null;

  /**
   * Creates a new element node.
   *
   * @param parent the parent of this node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @param tagProvider the tag provider
   * @since 4.10.0
   */
  public TagNode(
      final @NotNull ElementNode parent,
      final @NotNull Token token,
      final @NotNull String sourceMessage,
      final TokenParser.@NotNull TagProvider tagProvider
  ) {
    super(parent, token, sourceMessage);
    this.parts = genParts(token, sourceMessage, tagProvider);

    // Assert the tag node has parts.
    if (this.parts.isEmpty()) {
      throw new ParsingExceptionImpl("Tag has no parts? " + this, this.sourceMessage(), this.token());
    }
  }

  private static @NotNull List<TagPart> genParts(
    final @NotNull Token token,
    final @NotNull String sourceMessage,
    final TokenParser.@NotNull TagProvider tagProvider
  ) {
    final ArrayList<TagPart> parts = new ArrayList<>();

    if (token.childTokens() != null) {
      for (final Token childToken : token.childTokens()) {
        parts.add(new TagPart(sourceMessage, childToken, tagProvider));
      }
    }

    return parts;
  }

  /**
   * Returns the parts of this tag.
   *
   * @return the parts
   * @since 4.10.0
   */
  public @NotNull List<TagPart> parts() {
    return this.parts;
  }

  /**
   * Returns the name of this tag.
   *
   * @return the name
   * @since 4.10.0
   */
  public @NotNull String name() {
    return this.parts.get(0).value();
  }

  @Override
  public @NotNull Token token() {
    return Objects.requireNonNull(super.token(), "token is not set");
  }

  /**
   * Gets the tag attached to this tag node.
   *
   * @return the tag for this tag node
   * @since 4.10.0
   */
  public @NotNull Tag tag() {
    return Objects.requireNonNull(this.tag, "no tag set");
  }

  /**
   * Sets the tag logic that is represented by this tag node.
   *
   * @param tag the tag logic
   * @since 4.10.0
   */
  public void tag(final @NotNull Tag tag) {
    this.tag = tag;
  }

  @Override
  public @NotNull StringBuilder buildToString(final @NotNull StringBuilder sb, final int indent) {
    final char[] in = this.ident(indent);
    sb.append(in).append("TagNode(");

    final int size = this.parts.size();
    for (int i = 0; i < size; i++) {
      final TagPart part = this.parts.get(i);
      sb.append('\'').append(part.value()).append('\'');
      if (i != size - 1) {
        sb.append(", ");
      }
    }

    sb.append(") {\n");

    for (final ElementNode child : this.children()) {
      child.buildToString(sb, indent + 1);
    }
    sb.append(in).append("}\n");
    return sb;
  }
}
