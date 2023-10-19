/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Parser context for use within transformations.
 *
 * <p>This allows operating recursive parses, for cases where messages may include parse-specific tags.</p>
 *
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
@NullMarked
public interface Context {

  /**
   * Deserializes a MiniMessage string using all the settings of this context.
   *
   * @param message the message to parse
   * @return the parsed message
   * @since 4.10.0
   */
  Component deserialize(final String message);

  /**
   * Deserializes a MiniMessage string using all the settings of this context.
   *
   * @param message the message to parse
   * @param resolver additional tag resolver, added to all other resolvers in this parse, but taking priority in the event of a name overlap
   * @return the parsed message
   * @since 4.10.0
   */
  Component deserialize(final String message, final TagResolver resolver);

  /**
   * Deserializes a MiniMessage string using all the settings of this context.
   *
   * @param message the message to parse
   * @param resolvers additional tag resolvers, added to all other resolvers in this parse, but taking priority in the event of a name overlap
   * @return the parsed message
   * @since 4.10.0
   */
  Component deserialize(final String message, final TagResolver... resolvers);

  /**
   * Create a new parsing exception.
   *
   * @param message a detail message describing the error
   * @param tags the tag parts which caused the error
   * @return the new parsing exception
   * @since 4.10.0
   */
  ParsingException newException(
    final String message,
    final ArgumentQueue tags
  );

  /**
   * Create a new parsing exception without reference to a specific location.
   *
   * @param message a detail message describing the error
   * @return the new parsing exception
   * @since 4.10.0
   */
  ParsingException newException(final String message);

  /**
   * Create a new parsing exception.
   *
   * @param message a detail message describing the error
   * @param cause the cause
   * @param args arguments that caused the errors
   * @return the new parsing exception
   * @since 4.10.0
   */
  ParsingException newException(
    final String message,
    final @Nullable Throwable cause,
    final ArgumentQueue args
  );
}
