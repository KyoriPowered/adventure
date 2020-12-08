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

import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.intellij.lang.annotations.Pattern;

/**
 * A key.
 *
 * @since 4.0.0
 */
public interface Key extends Comparable<Key>, Examinable {
  /**
   * The namespace for Minecraft.
   *
   * @since 4.0.0
   */
  String MINECRAFT_NAMESPACE = "minecraft";

  /**
   * Creates a key.
   *
   * @param string the string
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.0.0
   */
  static @NonNull Key key(final @NonNull @Pattern("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+") String string) {
    return key(string, ':');
  }

  /**
   * Creates a key.
   *
   * @param string the string
   * @param character the character
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.0.0
   */
  @SuppressWarnings("PatternValidation") // impossible to validate since the character is variable
  static @NonNull Key key(final @NonNull String string, final char character) {
    final int index = string.indexOf(character);
    final String namespace = index >= 1 ? string.substring(0, index) : MINECRAFT_NAMESPACE;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return key(namespace, value);
  }

  /**
   * Creates a key.
   *
   * @param namespace the namespace
   * @param value the value
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.0.0
   */
  static @NonNull Key key(final @NonNull @Pattern("[a-z0-9_\\-.]+") String namespace, final @NonNull @Pattern("[a-z0-9_\\-./]+") String value) {
    return new KeyImpl(namespace, value);
  }

  /**
   * Gets the namespace.
   *
   * @return the namespace
   * @since 4.0.0
   */
  @NonNull String namespace();

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   */
  @NonNull String value();

  /**
   * Returns the string representation of this key.
   *
   * @return the string representation
   * @since 4.0.0
   */
  @NonNull String asString();

  @Override
  default @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("namespace", this.namespace()),
      ExaminableProperty.of("value", this.value())
    );
  }

  @Override
  default int compareTo(final @NonNull Key that) {
    final int value = this.value().compareTo(that.value());
    if(value != 0) {
      return KeyImpl.clampCompare(value);
    }
    return KeyImpl.clampCompare(this.namespace().compareTo(that.namespace()));
  }
}
