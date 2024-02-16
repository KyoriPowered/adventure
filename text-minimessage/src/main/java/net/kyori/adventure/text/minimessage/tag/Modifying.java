/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A tag that can transform a whole subtree of nodes.
 *
 * @since 4.10.0
 */
@ApiStatus.OverrideOnly
public /* non-sealed */ interface Modifying extends Tag {
  /**
   * Method called once for every element in the subtree, allowing calculations to be made before {@link #apply(Component, int) application}.
   *
   * @param current the current element in the subtree
   * @param depth depth in the tree this node is at
   * @since 4.10.0
   */
  default void visit(final @NotNull Node current, final int depth) {
  }

  /**
   * Called after the entire tree has been {@link #visit(Node, int) visited}.
   *
   * <p>This allows for finalizing calculations made during the tree visit, but before actual application to the child components of this tag.</p>
   *
   * @since 4.10.0
   */
  default void postVisit() {
  }

  /**
   * Applies this transformation for the current component.
   *
   * <p>This gets called after the component tree has been assembled, however, the tree can still be modified at this point if desired.</p>
   *
   * @param current the current component
   * @param depth the depth of the tree the current component is at
   * @return the new parent
   * @since 4.10.0
   */
  Component apply(final @NotNull Component current, final int depth);
}
