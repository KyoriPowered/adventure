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

class CompletionContextImpl implements CompletionContext {
  final int marker;
  final String partial;

  private CompletionContextImpl(final int marker, final String partial) {
    this.marker = marker;
    this.partial = partial;
  }

  static CompletionContextImpl create(final String partialTag) {
    boolean isClosingTag = false;
    String tagName = null;
    final Map<String, String> namedArguments = new HashMap<>();
    final List<String> arguments = new ArrayList<>();
    CompletionState state = CompletionState.TAG_NAME;
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
                state = CompletionState.NAMED_ARGUMENT_NAME;
                tagName = partialTag.substring(marker, i);
                marker = i + 1;
                break;
              case ':':
                state = CompletionState.ARGUMENT_VALUE;
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
              state = CompletionState.NAMED_ARGUMENT_VALUE;
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
                if (state == CompletionState.NAMED_ARGUMENT_VALUE && !string) {
                  namedArguments.put(argName, partialTag.substring(marker, i));
                  marker = i + 1;
                  state = CompletionState.NAMED_ARGUMENT_NAME;
                }
                break;
              case ':':
                if (state == CompletionState.ARGUMENT_VALUE && !string) {
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

    final String partial = partialTag.substring(marker, length);

    switch (state) {
      case TAG_NAME:
        return new TagKeyImpl(marker, partial);
      case ARGUMENT_VALUE:
        return new SeqArgValueImpl(marker, partial, tagName, arguments);
      case NAMED_ARGUMENT_NAME:
        return new NamedArgKeyImpl(marker, partial, tagName, namedArguments);
      case NAMED_ARGUMENT_VALUE:
        return new NamedArgValueImpl(marker, partial, tagName, namedArguments, argName);
    }

    // Error state
    return new BadSyntaxImpl(marker, partial);
  }

  int offset() {
    return this.marker;
  }

  @Override
  public @NotNull String partial() {
    return this.partial;
  }

  final static class TagKeyImpl extends CompletionContextImpl implements TagKey {
    private TagKeyImpl(final int marker, final String partial) {
      super(marker, partial);
    }
  }

  final static class SeqArgValueImpl extends CompletionContextImpl implements SequentialArgumentValue {
    private final String tagKey;
    private final List<String> arguments;

    private SeqArgValueImpl(final int marker, final String partial, final String tagKey, final List<String> arguments) {
      super(marker, partial);
      this.tagKey = tagKey;
      this.arguments = arguments;
    }

    @Override
    public @NotNull String tagKey() {
      return this.tagKey;
    }

    @Override
    public @NotNull List<String> arguments() {
      return this.arguments;
    }
  }

  final static class NamedArgKeyImpl extends CompletionContextImpl implements NamedArgumentKey {
    private final String tagKey;
    private final Map<String, String> arguments;

    private NamedArgKeyImpl(final int marker, final String partial, final String tagKey, final Map<String, String> arguments) {
      super(marker, partial);
      this.tagKey = tagKey;
      this.arguments = arguments;
    }

    @Override
    public @NotNull String tagKey() {
      return this.tagKey;
    }

    @Override
    public @NotNull Map<String, String> arguments() {
      return this.arguments;
    }
  }

  final static class NamedArgValueImpl extends CompletionContextImpl implements NamedArgumentValue {
    private final String tagKey;
    private final Map<String, String> arguments;
    private final String argumentKey;

    private NamedArgValueImpl(final int marker, final String partial, final String tagKey, final Map<String, String> arguments, final String argumentKey) {
      super(marker, partial);
      this.tagKey = tagKey;
      this.arguments = arguments;
      this.argumentKey = argumentKey;
    }

    @Override
    public @NotNull String tagKey() {
      return this.tagKey;
    }

    @Override
    public @NotNull Map<String, String> arguments() {
      return this.arguments;
    }

    @Override
    public @Nullable String argKey() {
      return this.argumentKey;
    }
  }

  final static class BadSyntaxImpl extends CompletionContextImpl implements BadSyntax {
    BadSyntaxImpl(final int marker, final String partial) {
      super(marker, partial);
    }
  }

  enum CompletionState {
    TAG_NAME,
    NAMED_ARGUMENT_NAME,
    NAMED_ARGUMENT_VALUE,
    ARGUMENT_VALUE,
    BAD_SYNTAX
  }
}
