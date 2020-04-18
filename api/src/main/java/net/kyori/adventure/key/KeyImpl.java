/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/* package */ final class KeyImpl implements Examinable, Key {
  private static final IntPredicate NAMESPACE_PREDICATE = value -> value == '_' || value == '-' || (value >= 'a' && value <= 'z') || (value >= '0' && value <= '9') || value == '.';
  private static final IntPredicate VALUE_PREDICATE = value -> value == '_' || value == '-' || (value >= 'a' && value <= 'z') || (value >= '0' && value <= '9') || value == '/' || value == '.';
  private final String namespace;
  private final String value;

  /* package */ KeyImpl(final @NonNull String namespace, final @NonNull String value) {
    if(!namespaceValid(namespace)) throw new ParseException("Non [a-z0-9_.-] character in namespace of location: " + asString(namespace, value));
    if(!valueValid(value)) throw new ParseException("Non [a-z0-9/._-] character in path of location: " + asString(namespace, value));

    this.namespace = namespace;
    this.value = value;
  }

  /* package */ static boolean namespaceValid(final @NonNull String namespace) {
    return namespace.chars().allMatch(NAMESPACE_PREDICATE);
  }

  /* package */ static boolean valueValid(final @NonNull String value) {
    return value.chars().allMatch(VALUE_PREDICATE);
  }

  @Override
  public @NonNull String namespace() {
    return this.namespace;
  }

  @Override
  public @NonNull String value() {
    return this.value;
  }

  @Override
  public @NonNull String asString() {
    return asString(this.namespace, this.value);
  }

  private static @NonNull String asString(final @NonNull String namespace, final @NonNull String value) {
    return namespace + ':' + value;
  }

  @Override
  public @NonNull String toString() {
    return this.asString();
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("namespace", this.namespace),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) {
      return true;
    }
    if(!(other instanceof Key)) {
      return false;
    }
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
  public int compareTo(final @NonNull Key that) {
    final int value = this.value.compareTo(that.value());
    if(value != 0) {
      return clampCompare(value);
    }
    return clampCompare(this.namespace.compareTo(that.namespace()));
  }

  private static int clampCompare(final int value) {
    if(value < 0) return -1;
    if(value > 0) return 1;
    return value;
  }
}
