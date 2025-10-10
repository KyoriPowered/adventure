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
package net.kyori.adventure.text.minimessage;

import java.util.Collection;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The results for a completion.
 *
 * @since 123.123.123
 */
@ApiStatus.NonExtendable
public interface CompletionResult {
  /**
   * Get the index from where all the completions start.
   *
   * @return the start index
   * @since 123.123.123
   */
  int start();

  /**
   * Get the completions.
   *
   * @return the completions
   * @since 123.123.123
   */
  @NotNull Collection<Completion> completions();

  /**
   * A completion.
   *
   * @since 123.123.123
   */
  interface Completion {
    /**
     * Return the completion.
     *
     * @return the completion
     * @since 123.123.123
     */
    @NotNull String completion();

    /**
     * Return the completion component.
     *
     * @return the completion component
     * @since 123.123.123
     */
    @Nullable Component completionComponent();
  }

  /**
   * Builder for a completion result.
   *
   * @since 123.123.123
   */
  interface Builder {
    /**
     * Create a completion result.
     * Will replace the entire partial value.
     *
     * @param completion the completion
     * @since 123.123.123
     */
    default void add(final String completion) {
      this.add(completion, null, 0);
    }

    /**
     * Create a completion result.
     *
     * @param completion the completion
     * @param start the start index from where to complete
     * @since 123.123.123
     */
    default void add(final @NotNull String completion, final int start) {
      this.add(completion, null, start);
    }

    /**
     * Create a completion result.
     * Will replace the entire partial value.
     *
     * @param completion the completion
     * @param completionComponent the on hover display
     * @since 123.123.123
     */
    default void add(final @NotNull String completion, final @Nullable Component completionComponent) {
      this.add(completion, completionComponent, 0);
    }

    /**
     * Create a completion result.
     *
     * @param completion the completion
     * @param completionComponent the on hover display
     * @param start the start index from where to complete
     * @since 123.123.123
     */
    void add(final @NotNull String completion, final @Nullable Component completionComponent, final int start);
  }
}
