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

import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Context used to get Tag Completions.
 *
 * @since 123.123.123
 */
@ApiStatus.NonExtendable
public interface CompletionContext {
  /**
   * Get the unfinished partial value to complete.
   *
   * @return the partial value
   * @since 123.123.123
   */
  @NotNull String partial();

  /**
   * Completion context for a tag key.
   *
   * @since 123.123.123
   */
  interface TagKey extends CompletionContext {
  }

  /**
   * Completion context for the value of a sequential argument.
   *
   * @since 123.123.123
   */
  interface SequentialArgumentValue extends CompletionContext {
    /**
     * Get the tag key.
     *
     * @return the tag key
     * @since 123.123.123
     */
    @NotNull String tagKey();

    /**
     * Get the arguments list.
     *
     * @return the list of arguments
     * @since 123.123.123
     */
    @NotNull List<String> arguments();
  }

  /**
   * Completion context for a named argument key.
   *
   * @since 123.123.123
   */
  interface NamedArgumentKey extends CompletionContext {
    /**
     * Get the tag key.
     *
     * @return the tag key
     * @since 123.123.123
     */
    @NotNull String tagKey();

    /**
     * Get the arguments list.
     *
     * @return the map of named arguments
     * @since 123.123.123
     */
    @NotNull Map<String, String> arguments();
  }

  /**
   * Completion context for the value of a named argument.
   *
   * @since 123.123.123
   */
  interface NamedArgumentValue extends CompletionContext {
    /**
     * Get the tag key.
     *
     * @return the tag key
     * @since 123.123.123
     */
    @NotNull String tagKey();

    /**
     * Get the arguments list.
     *
     * @return the map of named arguments
     * @since 123.123.123
     */
    @NotNull Map<String, String> arguments();

    /**
     * Get the key of the argument whose value is currently being parsed.
     * Returns null in every state except NAMED_TAG_VALUE
     *
     * @return the argument key
     */
    @Nullable String argKey();
  }

  /**
   * Completion context generation when bad syntax is encountered.
   *
   * @since 123.123.123
   */
  interface BadSyntax extends CompletionContext {
  }
}
