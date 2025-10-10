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

import java.util.Set;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface that creates autocompletions.
 *
 * @since 123.123.123
 */
@FunctionalInterface
public interface Completer {
  /**
   * Add completions to builder from context.
   *
   * @param completionContext the context
   * @param builder           the builder
   * @since 123.123.123
   */
  void complete(final @NotNull CompletionContext completionContext, final CompletionResult.@NotNull Builder builder);

  /**
   * Get a completer that doesn't add any completions.
   *
   * @return the completer.
   * @since 123.123.123
   */
  static Completer none() {
    return (context, builder) -> {
    };
  }

  /**
   * Get a completer that completes only the tag key.
   *
   * @param key the key
   * @return the completer
   * @since 123.123.123
   */
  static Completer noArgs(final String key) {
    return (context, builder) -> {
      if (context instanceof CompletionContext.TagKey) {
        if (key.toLowerCase().startsWith(context.partial().toLowerCase())) {
          builder.add(key);
        }
      }
    };
  }

  /**
   * Get a completer that completes only the tag keys.
   *
   * @param keys the keys
   * @return the completer
   * @since 123.123.123
   */
  static Completer noArgs(final Set<String> keys) {
    return (context, builder) -> {
      if (context instanceof CompletionContext.TagKey) {
        keys.stream()
          .filter(key -> key.toLowerCase().startsWith(context.partial().toLowerCase()))
          .forEach(builder::add);
      }
    };
  }

  /**
   * Get a completer that completes tag keys, argument names and named argument values.
   *
   * @param tagKeys        the tag keys
   * @param argKeys        the argument keys
   * @param valueCompleter the named argument value completer
   * @return the completer
   * @since 123.123.123
   */
  static Completer named(final Set<String> tagKeys, final Set<String> argKeys, final BiConsumer<CompletionContext.NamedArgumentValue, CompletionResult.Builder> valueCompleter) {
    return (context, builder) -> {
      if (context instanceof CompletionContext.TagKey) {
        tagKeys.stream()
          .filter(key -> key.toLowerCase().startsWith(context.partial().toLowerCase()))
          .forEach(builder::add);
      } else if (context instanceof CompletionContext.NamedArgumentKey) {
        final CompletionContext.NamedArgumentKey keyContext = (CompletionContext.NamedArgumentKey) context;
        if (tagKeys.contains(keyContext.tagKey().toLowerCase())) {
          argKeys.stream()
            .filter(key -> key.toLowerCase().startsWith(context.partial().toLowerCase()))
            .filter(key -> keyContext.arguments().containsKey(key.toLowerCase()))
            .forEach(builder::add);
        }
      } else if (context instanceof CompletionContext.NamedArgumentValue) {
        final CompletionContext.NamedArgumentValue valueContext = (CompletionContext.NamedArgumentValue) context;
        if (tagKeys.contains(valueContext.tagKey().toLowerCase())) {
          valueCompleter.accept((CompletionContext.NamedArgumentValue) context, builder);
        }
      }
    };
  }

  /**
   * Get a completer that completes tag keys and sequential argument values.
   *
   * @param tagKeys                the tag keys
   * @param argumentValueCompleter the sequential argument values
   * @return the completer
   * @since 123.123.123
   */
  static Completer sequential(final Set<String> tagKeys, final BiConsumer<CompletionContext.SequentialArgumentValue, CompletionResult.Builder> argumentValueCompleter) {
    return (context, builder) -> {
      if (context instanceof CompletionContext.TagKey) {
        tagKeys.stream()
          .filter(key -> key.toLowerCase().startsWith(context.partial().toLowerCase()))
          .forEach(builder::add);
      } else if (context instanceof CompletionContext.SequentialArgumentValue) {
        final CompletionContext.SequentialArgumentValue valueContext = (CompletionContext.SequentialArgumentValue) context;
        if (tagKeys.contains(valueContext.tagKey().toLowerCase())) {
          argumentValueCompleter.accept(valueContext, builder);
        }
      }
    };
  }
}
