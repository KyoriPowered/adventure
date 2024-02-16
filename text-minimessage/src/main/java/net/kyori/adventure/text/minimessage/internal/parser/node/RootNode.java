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
package net.kyori.adventure.text.minimessage.internal.parser.node;

import net.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the root node of a tree.
 *
 * @since 4.10.0
 */
public final class RootNode extends ElementNode implements Node.Root {
  private final String beforePreprocessing;

  /**
   * Creates a new root node.
   *
   * @param sourceMessage the source message
   * @param beforePreprocessing the source message before handling preProcess tags
   * @since 4.10.0
   */
  public RootNode(final @NotNull String sourceMessage, final @NotNull String beforePreprocessing) {
    super(null, null, sourceMessage);
    this.beforePreprocessing = beforePreprocessing;
  }

  @Override
  public @NotNull String input() {
    return this.beforePreprocessing;
  }
}
