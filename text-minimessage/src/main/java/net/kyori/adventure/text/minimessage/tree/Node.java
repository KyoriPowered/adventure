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
package net.kyori.adventure.text.minimessage.tree;

import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A node in the MiniMessage parse tree.
 *
 * <p>This API is currently incomplete -- it will be expanded in future versions based on user interest.</p>
 *
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface Node {
  /**
   * Get a human-readable representation of this node and its descendants for debugging purposes.
   *
   * @return the human-readable representation of this node tree
   * @since 4.10.0
   */
  @Override
  @NotNull String toString();

  /**
   * Get children of this node.
   *
   * <p>The returned list is unmodifiable.</p>
   *
   * @return a list of children
   * @since 4.10.0
   */
  @NotNull List<? extends Node> children();

  /**
   * Get the parent of this node.
   *
   * <p>If this node is at the root of the tree, this may be {@code null}.</p>
   *
   * @return this node's parent
   * @since 4.10.0
   */
  @Nullable Node parent();

  /**
   * The root node of a parse.
   *
   * @since 4.10.0
   */
  @ApiStatus.NonExtendable
  interface Root extends Node {
    /**
     * Get the original provided message which produced this node.
     *
     * @return the input message
     * @since 4.10.0
     */
    @NotNull String input();
  }
}
