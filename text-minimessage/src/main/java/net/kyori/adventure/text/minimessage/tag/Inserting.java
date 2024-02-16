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
import org.jetbrains.annotations.NotNull;

/**
 * A tag that inserts a {@link Component} into the output.
 *
 * @since 4.10.0
 */
public /* non-sealed */ interface Inserting extends Tag {
  /**
   * Returns the component this tag produces.
   *
   * @return the component this tag produces
   * @since 4.10.0
   */
  @NotNull Component value();

  /**
   * Get whether this tag allows children.
   *
   * <p>If children are not allowed, this tag will be auto-closing, and
   * should not be closed explicitly. In strict mode, a closing tag will be
   * an error. In lenient mode, the closing tag will be interpreted as literal text.</p>
   *
   * @return whether this tag will allow following to become children
   * @since 4.10.0
   */
  default boolean allowsChildren() {
    return true;
  }
}
