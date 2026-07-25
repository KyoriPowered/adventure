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
package net.kyori.adventure.key;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record KeyFormatImpl(String namespace, char separator) implements KeyFormat {
  static final KeyFormat MINECRAFT_INSTANCE = KeyFormat.keyFormat(Key.MINECRAFT_NAMESPACE, Key.DEFAULT_SEPARATOR);

  static String asString(final String namespace, final char separator, final String value) {
    return namespace + separator + value;
  }

  @SuppressWarnings("PatternValidation") // impossible to validate since the character is variable
  static Key parseKey(final String string, final char separator, final String fallbackNamespace) {
    final int index = string.indexOf(separator);
    final String namespace = index >= 1 ? string.substring(0, index) : fallbackNamespace;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return Key.key(namespace, value);
  }

  @Override
  public Key key(final String value) {
    return Key.key(this.namespace, value);
  }

  @Override
  public Key parse(final String string) {
    return parseKey(requireNonNull(string, "string"), this.separator, this.namespace);
  }

  @Override
  public boolean parseable(final @Nullable String string) {
    if (string == null) {
      return false;
    }
    final int index = string.indexOf(this.separator);
    final String namespace = index >= 1 ? string.substring(0, index) : this.namespace;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return Key.parseableNamespace(namespace) && Key.parseableValue(value);
  }

  @Override
  public String asString(final Key key) {
    requireNonNull(key, "key");
    return asString(key.namespace(), this.separator, key.value());
  }

  @Override
  public String asMinimalString(final Key key) {
    requireNonNull(key, "key");
    if (key.namespace().equals(this.namespace)) {
      return key.value();
    }
    return this.asString(key);
  }

  static final class BuilderImpl implements Builder {
    private String namespace = Key.MINECRAFT_NAMESPACE;
    private char separator = Key.DEFAULT_SEPARATOR;

    @Override
    public Builder namespace(@KeyPattern.Namespace final String namespace) {
      requireNonNull(namespace, "namespace");
      Key.checkNamespace(namespace).ifPresent(index -> {
        if (index == -1) {
          throw new IllegalArgumentException(String.format("'%s' is not a valid namespace", namespace));
        } else {
          final char character = namespace.charAt(index);
          throw new IllegalArgumentException(String.format(
            "Illegal character in namespace '%s' at index %d ('%s', bytes: %s)",
            namespace,
            index,
            character,
            Arrays.toString(String.valueOf(character).getBytes(StandardCharsets.UTF_8))
          ));
        }
      });
      this.namespace = namespace;
      return this;
    }

    @Override
    public Builder separator(final char separator) {
      this.separator = separator;
      return this;
    }

    @Override
    public KeyFormat build() {
      return new KeyFormatImpl(this.namespace, this.separator);
    }
  }
}
