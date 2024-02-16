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
package net.kyori.adventure.text.minimessage.internal.serializer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A consumer of tokens used to generate MiniMessage output.
 *
 * @since 4.10.0
 */
public interface TokenEmitter {
  /**
   * Open a tag with or without arguments.
   *
   * @param token the token to emit
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter tag(final @NotNull String token); // TODO: some sort of TagFlags, with things like SELF_CLOSING, CLOSE_WITH_ARGUMENTS, etc?

  /**
   * Open a tag with or without arguments that cannot have children.
   *
   * <p>These sorts of tags will be closed even without any sort of closing indicator.</p>
   *
   * @param token the token to emit
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter selfClosingTag(final @NotNull String token); // TODO: some sort of TagFlags, with things like SELF_CLOSING, CLOSE_WITH_ARGUMENTS, etc?

  /**
   * Add arguments to the current tag.
   *
   * <p>Must be called after {@link #tag(String)}, but before any call to {@link #text(String)}.</p>
   *
   * @param args args to add
   * @return this emitter
   * @since 4.10.0
   */
  default @NotNull TokenEmitter arguments(final @NotNull String... args) {
    for (final String arg : args) {
      this.argument(arg);
    }
    return this;
  }

  /**
   * Add a single argument to the current tag.
   *
   * <p>Must be called after {@link #tag(String)}, but before any call to {@link #text(String)}.</p>
   *
   * @param arg argument to add
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter argument(final @NotNull String arg);

  /**
   * Add a single argument to the current tag.
   *
   * <p>Must be called after {@link #tag(String)}, but before any call to {@link #text(String)}.</p>
   *
   * @param arg argument to add
   * @param quotingPreference an argument-specific quoting instruction
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter argument(final @NotNull String arg, final @NotNull QuotingOverride quotingPreference);

  /**
   * Add a single argument to the current tag.
   *
   * <p>Must be called after {@link #tag(String)}, but before any call to {@link #text(String)}.</p>
   *
   * @param arg argument to add, serialized as a nested MiniMessage string
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter argument(final @NotNull Component arg);

  /**
   * Emit literal text.
   *
   * <p>Escaping will be automatically performed to ensure that none of the contents of {@code text} are parsed as tags.</p>
   *
   * @param text the text to parse
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter text(final @NotNull String text);

  /**
   * Explicitly end a token, only needed if there are multiple tokens within an {@link Emitable} for some reason.
   *
   * <p>Usually depth handling is performed internally.</p>
   *
   * @return this emitter
   * @since 4.10.0
   */
  @NotNull TokenEmitter pop();
}
