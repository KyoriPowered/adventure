/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class KeyImpl implements Key {
  static final Comparator<? super Key> COMPARATOR = Comparator.comparing(Key::value).thenComparing(Key::namespace);

  static final String NAMESPACE_PATTERN = "[a-z0-9_\\-.]+";
  static final String VALUE_PATTERN = "[a-z0-9_\\-./]+";

  private final String namespace;
  private final String value;

  KeyImpl(final @NotNull String namespace, final @NotNull String value) {
    if (!Key.parseableNamespace(namespace)) throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9_.-] character in namespace of Key[%s]", asString(namespace, value)));
    if (!Key.parseableValue(value)) throw new InvalidKeyException(namespace, value, String.format("Non [a-z0-9/._-] character in value of Key[%s]", asString(namespace, value)));
    this.namespace = requireNonNull(namespace, "namespace");
    this.value = requireNonNull(value, "value");
  }

  static boolean allowedInNamespace(final char character) {
    return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
  }

  static boolean allowedInValue(final char character) {
    return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.' || character == '/';
  }

  @Override
  public @NotNull String namespace() {
    return this.namespace;
  }

  @Override
  public @NotNull String value() {
    return this.value;
  }

  @Override
  public @NotNull String asString() {
    return asString(this.namespace, this.value);
  }

  private static @NotNull String asString(final @NotNull String namespace, final @NotNull String value) {
    return namespace + ':' + value;
  }

  @Override
  public @NotNull String toString() {
    return this.asString();
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("namespace", this.namespace),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof Key)) return false;
    final Key that = (Key) other;
    return Objects.equals(this.namespace, that.namespace()) && Objects.equals(this.value, that.value());
  }

  @Override
  public int hashCode() {
    int result = this.namespace.hashCode();
    result = (31 * result) + this.value.hashCode();
    return result;
  }

  @Override
  public int compareTo(final @NotNull Key that) {
    return Key.super.compareTo(that);
  }
}
