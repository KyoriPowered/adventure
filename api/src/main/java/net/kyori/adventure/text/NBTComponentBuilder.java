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
package net.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This can't be a child of NBTComponent.
 */

/**
 * An NBT component builder.
 *
 * @param <C> component type
 * @param <B> builder type
 * @since 4.0.0
 */
public interface NBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends ComponentBuilder<C, B> {
  /**
   * Sets the NBT path content.
   *
   * @param nbtPath the NBT path
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B nbtPath(final @NotNull String nbtPath);

  /**
   * Sets whether to interpret.
   *
   * @param interpret if we should be interpreting
   * @return this builder
   * @since 4.0.0
   */
  @Contract("_ -> this")
  @NotNull B interpret(final boolean interpret);

  /**
   * Sets the separator.
   *
   * @param separator the separator
   * @return this builder
   * @since 4.8.0
   */
  @Contract("_ -> this")
  @NotNull B separator(final @Nullable ComponentLike separator);
}
