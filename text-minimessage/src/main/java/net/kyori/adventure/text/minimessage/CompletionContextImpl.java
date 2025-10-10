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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kyori.adventure.text.minimessage.CompletionContext.CompletionState.ARGUMENT_VALUE;
import static net.kyori.adventure.text.minimessage.CompletionContext.CompletionState.NAMED_ARGUMENT_NAME;
import static net.kyori.adventure.text.minimessage.CompletionContext.CompletionState.NAMED_ARGUMENT_VALUE;
import static net.kyori.adventure.text.minimessage.CompletionContext.CompletionState.TAG_NAME;

class CompletionContextImpl implements CompletionContext {
  private final boolean isClosingTag;
  private final String tagName;
  private final Map<String, String> namedArguments;
  private final List<String> arguments;
  private final CompletionState state;
  private final String partial;
  private final int offset;

  CompletionContextImpl(final String partialTag) {
    boolean isClosingTag = false;
    String tagName = null;
    final Map<String, String> namedArguments = new HashMap<>();
    final List<String> arguments = new ArrayList<>();
    CompletionState state = TAG_NAME;
    boolean escaped = false;
    boolean string = false;
    int stringChar = -1;
    int marker = 0;
    String argName = "";

    final int length = partialTag.length();
    for (int i = 0; i < length; i++) {
      final int codePoint = partialTag.codePointAt(i);

      // Check if closing tag.
      if (i == 0 && codePoint == '/') {
        isClosingTag = true;
        marker = 1;
        continue;
      }

      if (!escaped) {
        // Tag name ends when we encounter a space or a colon -> save it
        switch (state) {
          case TAG_NAME:
            switch (codePoint) {
              case ' ':
                state = NAMED_ARGUMENT_NAME;
                tagName = partialTag.substring(marker, i);
                marker = i + 1;
                break;
              case ':':
                state = ARGUMENT_VALUE;
                tagName = partialTag.substring(marker, i);
                marker = i + 1;
                break;
            }
            break;
          // A named argument name ends when we encounter an equals sign
          case NAMED_ARGUMENT_NAME:
            if (codePoint == '=') {
              argName = partialTag.substring(marker, i);
              marker = i + 1;
              state = NAMED_ARGUMENT_VALUE;
            }
            break;

          case ARGUMENT_VALUE:
          case NAMED_ARGUMENT_VALUE:
            switch (codePoint) {
              // Keep track of whether we are in a string
              case '\"':
              case '\'':
                if (string) {
                  if (codePoint == stringChar) {
                    string = false;
                  }
                } else {
                  string = true;
                  stringChar = codePoint;
                }
                break;
              case ' ':
                if (state == NAMED_ARGUMENT_VALUE && !string) {
                  namedArguments.put(argName, partialTag.substring(marker, i));
                  marker = i + 1;
                  state = NAMED_ARGUMENT_NAME;
                }
                break;
              case ':':
                if (state == ARGUMENT_VALUE && !string) {
                  arguments.add(partialTag.substring(marker, i));
                  marker = i + 1;
                }
                break;
            }
            break;
        }
      }

      // Set escape value for next iteration.
      escaped = codePoint == '\\';
    }

    this.offset = marker;
    this.partial = partialTag.substring(marker, length);
    this.isClosingTag = isClosingTag;
    this.tagName = tagName;
    this.namedArguments = namedArguments;
    this.arguments = arguments;
    this.state = state;
  }

  int offset() {
    return this.offset;
  }

  @Override
  public @NotNull String partial() {
    return this.partial;
  }

  @Override
  public boolean isClosingTag() {
    return this.isClosingTag;
  }

  @Override
  public @Nullable String tagName() {
    return this.tagName;
  }

  @Override
  public @Nullable List<String> arguments() {
    return this.arguments.isEmpty() ? null : this.arguments;
  }

  @Override
  public @Nullable Map<String, String> namedArguments() {
    return this.namedArguments.isEmpty() ? null : this.namedArguments;
  }

  @Override
  public @NotNull CompletionState completionState() {
    return this.state;
  }
}
