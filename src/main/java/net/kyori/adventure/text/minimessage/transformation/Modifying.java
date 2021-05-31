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
package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.parser.node.ElementNode;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Transformations implementing this interface can transform a whole subtree of nodes.
 *
 * @since 4.2.0
 */
public interface Modifying {

  /**
   * This method gets called once for every element in the sub tree, allowing you to do calculations beforehand.
   *
   * @param curr the current element in the sub tree
   * @since 4.2.0
   */
  void visit(ElementNode curr);

  /**
   * Applies this transformation for the current component.
   * This gets called after the component tree has been assembled, but you are free to modify it however you like.
   *
   * @param curr the current component
   * @param depth the depth of the tree the current component is at
   * @return the new parent
   * @since 4.2.0
   */
  Component apply(Component curr, int depth);
}
