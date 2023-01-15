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

import java.util.Objects;
import net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a node in the tree which has a text value.
 *
 * @since 4.10.0
 */

public abstract class ValueNode extends ElementNode {
  private final String value;

  /**
   * Creates a new element node.
   *
   * @param parent the parent of this node
   * @param token the token that created this node
   * @param sourceMessage the source message
   * @param value the value of this text node
   * @since 4.10.0
   */
  ValueNode(final @Nullable ElementNode parent, final @Nullable Token token, final @NotNull String sourceMessage, final @NotNull String value) {
    super(parent, token, sourceMessage);
    this.value = value;
  }

  abstract String valueName();

  /**
   * Returns the value of this text node.
   *
   * @return the value
   * @since 4.10.0
   */
  public @NotNull String value() {
    return this.value;
  }

  @Override
  public @NotNull Token token() {
    return Objects.requireNonNull(super.token(), "token is not set");
  }

  @Override
  public @NotNull StringBuilder buildToString(final @NotNull StringBuilder sb, final int indent) {
    final char[] in = this.ident(indent);
    sb.append(in).append(this.valueName()).append("('").append(this.value).append("')\n");
    return sb;
  }
}
