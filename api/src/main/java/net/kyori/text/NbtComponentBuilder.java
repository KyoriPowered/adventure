/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import org.checkerframework.checker.nullness.qual.NonNull;

/*
 * This can't be a child of NbtComponent.
 */

/**
 * An NBT component builder.
 */
public interface NbtComponentBuilder<C extends NbtComponent<C, B>, B extends NbtComponentBuilder<C, B>> extends ComponentBuilder<C, B> {
  /**
   * Sets the NBT path content.
   *
   * @param nbtPath the NBT path
   * @return this builder
   */
  @NonNull B nbtPath(final @NonNull String nbtPath);

  /**
   * Sets whether to interpret.
   *
   * @param interpret if we should be interpreting
   * @return this builder
   */
  @NonNull B interpret(final boolean interpret);
}
