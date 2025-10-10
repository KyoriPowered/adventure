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
   * Return whether this is a closing tag. Returns false for empty string completion
   *
   * @return Whether this is a closing tag
   * @since 123.123.123
   */
  boolean isClosingTag();

  /**
   * Get the tag name.
   * Will be null if still in TAG_NAME completion state.
   *
   * @return the tag name
   * @since 123.123.123
   */
  @Nullable String tagName();

  /**
   * Get the arguments list.
   * Can be null if no positional arguments have been input.
   *
   * @return the list of arguments
   * @since 123.123.123
   */
  @Nullable List<String> arguments();

  /**
   * Get the named arguments map.
   * Can be null if no named arguments have been input.
   *
   * @return the named arguments map.
   * @since 123.123.123
   */
  @Nullable Map<String, String> namedArguments();

  /**
   * Get the completion state.
   *
   * @return the completion type
   * @since 123.123.123
   */
  @NotNull CompletionState completionState();

  /**
   * The completion states.
   *
   * @since 123.123.123
   */
  enum CompletionState {
    TAG_NAME,
    NAMED_ARGUMENT_NAME,
    NAMED_ARGUMENT_VALUE,
    ARGUMENT_VALUE,
    BAD_SYNTAX
  }
}
