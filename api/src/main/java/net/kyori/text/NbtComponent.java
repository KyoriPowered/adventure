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

/**
 * An NBT component.
 */
public interface NbtComponent<C extends NbtComponent<C, B>, B extends NbtComponentBuilder<C, B>> extends BuildableComponent<C, B> {
  /**
   * Gets the NBT path.
   *
   * @return the NBT path
   */
  @NonNull String nbtPath();

  /**
   * Sets the NBT path.
   *
   * @param nbtPath the NBT path
   * @return a component
   */
  @NonNull C nbtPath(final @NonNull String nbtPath);

  /**
   * Gets if we should be interpreting.
   *
   * @return if we should be interpreting
   */
  boolean interpret();

  /**
   * Sets if we should be interpreting.
   *
   * @param interpret if we should be interpreting.
   * @return a component
   */
  @NonNull C interpret(final boolean interpret);
}
