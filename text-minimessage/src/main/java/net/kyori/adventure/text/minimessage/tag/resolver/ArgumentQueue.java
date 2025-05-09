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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A queue of {@link Tag} arguments.
 *
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface ArgumentQueue {
  /**
   * Pop an argument, throwing an exception if no argument was present.
   *
   * <p>After an invocation of {@code pop()}, the internal argument pointer will be advanced to the next argument.</p>
   *
   * @return the popped argument
   * @since 4.10.0
   */
  Tag.@NotNull Argument pop();

  /**
   * Pop an argument, throwing an exception if no argument was present.
   *
   * <p>After an invocation of {@code popOr()}, the internal argument pointer will be advanced to the next argument.</p>
   *
   * @param errorMessage the error to throw if the argument is not present
   * @return the popped argument
   * @since 4.10.0
   */
  Tag.@NotNull Argument popOr(final @NotNull String errorMessage);

  /**
   * Pop an argument, throwing an exception if no argument was present.
   *
   * <p>After an invocation of {@code popOr()}, the internal argument pointer will be advanced to the next argument.</p>
   *
   * @param errorMessage the error to throw if the argument is not present
   * @return the popped argument
   * @since 4.10.0
   */
  Tag.@NotNull Argument popOr(final @NotNull Supplier<String> errorMessage);

  /**
   * Peek at the next argument without advancing the iteration pointer.
   *
   * @return the next argument, if any is available.
   * @since 4.10.0
   */
  Tag.@Nullable Argument peek();

  /**
   * Get whether another argument is available to be popped.
   *
   * @return whether another argument is available
   * @since 4.10.0
   */
  boolean hasNext();

  /**
   * Reset index to the beginning, to begin another attempt.
   *
   * @since 4.10.0
   */
  void reset();
}
