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
import java.util.Comparator;
import java.util.Objects;
import java.util.OptionalInt;

import static java.util.Objects.requireNonNull;

record KeyImpl(String namespace, String value) implements Key {
  static final Comparator<? super Key> COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);

  KeyImpl {
    KeyImpl.checkError("namespace", namespace, namespace, value, Key.checkNamespace(namespace), KeyPattern.NAMESPACE_PATTERN);
    KeyImpl.checkError("value", value, namespace, value, Key.checkValue(value), KeyPattern.VALUE_PATTERN);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // It's okay, this is internal, and it's fine anyway.
  static void checkError(final String name, final String checkPart, final String namespace, final String value, final OptionalInt index, final String pattern) {
    requireNonNull(checkPart, name);
    if (index.isPresent()) {
      final int indexValue = index.getAsInt();
      final char character = checkPart.charAt(indexValue);
      throw new InvalidKeyException(namespace, value, String.format(
        "Non " + pattern + " character in %s of Key[%s] at index %d ('%s', bytes: %s)",
        name,
        asString(namespace, value),
        indexValue,
        character,
        Arrays.toString(String.valueOf(character).getBytes(StandardCharsets.UTF_8))
      ));
    }
  }

  static boolean allowedInNamespace(final char character) {
    return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
  }

  static boolean allowedInValue(final char character) {
    return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.' || character == '/';
  }

  @Override
  public String asString() {
    return asString(this.namespace, this.value);
  }

  private static String asString(final String namespace, final String value) {
    return namespace + ':' + value;
  }

  @Override
  public String toString() {
    return this.asString();
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof Key that)) return false;
    return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
  }

  @Override
  public int compareTo(final Key that) {
    return Key.super.compareTo(that);
  }
}
