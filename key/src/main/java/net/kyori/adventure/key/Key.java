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
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An identifying object used to fetch and/or store unique objects.
 *
 * <p>A key consists of:</p>
 * <dl>
 *   <dt>namespace</dt>
 *   <dd>in most cases this should be your plugin or organization name</dd>
 *   <dt>value</dt>
 *   <dd>what this key leads to, e.g "translations" or "entity.firework_rocket.blast"</dd>
 * </dl>
 *
 * <p>Valid characters for namespaces are <a href="https://regexr.com/5ibbm">{@code [a-z0-9_.-]}</a>.</p>
 *
 * <p>Valid characters for values are <a href="https://regexr.com/5if3m">{@code [a-z0-9/._-]}</a>.</p>
 *
 * <p>Some examples of possible custom keys:</p>
 * <ul>
 *   <li> my_plugin:translations</li>
 *   <li> my_plugin:weapon.amazing-weapon_damage-attribute</li>
 *   <li> my_organization:music.song_1</li>
 *   <li> my_organization:item.magic_button</li>
 * </ul>
 *
 * @since 4.0.0
 */
public interface Key extends Comparable<Key>, Examinable, Namespaced, Keyed {
  /**
   * The namespace for Minecraft.
   *
   * @since 4.0.0
   */
  String MINECRAFT_NAMESPACE = "minecraft";
  /**
   * The default namespace and value separator.
   *
   * @since 4.12.0
   */
  char DEFAULT_SEPARATOR = ':';

  /**
   * Creates a key.
   *
   * <p>This will parse {@code string} as a key, using {@code :} as a separator between the namespace and the value.</p>
   *
   * <p>The namespace is optional. If you do not provide one (for example, if you provide just {@code player} or {@code :player}
   * as the string) then {@link #MINECRAFT_NAMESPACE} will be used as a namespace and {@code string} will be used as the value,
   * removing the colon if necessary.</p>
   *
   * @param string the string
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.0.0
   */
  static @NotNull Key key(final @NotNull @Pattern("(" + KeyImpl.NAMESPACE_PATTERN + ":)?" + KeyImpl.VALUE_PATTERN) String string) {
    return key(string, DEFAULT_SEPARATOR);
  }

  /**
   * Creates a key.
   *
   * <p>This will parse {@code string} as a key, using {@code character} as a separator between the namespace and the value.</p>
   *
   * <p>The namespace is optional. If you do not provide one (for example, if you provide {@code player} or {@code character + "player"}
   * as the string) then {@link #MINECRAFT_NAMESPACE} will be used as a namespace and {@code string} will be used as the value,
   * removing the provided separator character if necessary.</p>
   *
   * @param string the string
   * @param character the character that separates the namespace from the value
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.0.0
   */
  @SuppressWarnings("PatternValidation") // impossible to validate since the character is variable
  static @NotNull Key key(final @NotNull String string, final char character) {
    final int index = string.indexOf(character);
    final String namespace = index >= 1 ? string.substring(0, index) : MINECRAFT_NAMESPACE;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return key(namespace, value);
  }

  /**
   * Creates a key.
   *
   * @param namespaced the namespace source
   * @param value the value
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 4.4.0
   */
  static @NotNull Key key(final @NotNull Namespaced namespaced, final @NotNull @Pattern(KeyImpl.VALUE_PATTERN) String value) {
    return key(namespaced.namespace(), value);
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
  static @NotNull Key key(final @NotNull @Pattern(KeyImpl.NAMESPACE_PATTERN) String namespace, final @NotNull @Pattern(KeyImpl.VALUE_PATTERN) String value) {
    return new KeyImpl(namespace, value);
  }

  /**
   * Gets the comparator.
   *
   * <p>The {@link #value() value} is compared first, followed by the {@link #namespace() namespace}.</p>
   *
   * @return a comparator for keys
   * @since 4.10.0
   */
  static @NotNull Comparator<? super Key> comparator() {
    return KeyImpl.COMPARATOR;
  }

  /**
   * Checks if {@code string} can be parsed into a {@link Key}.
   *
   * @param string the input string
   * @return {@code true} if {@code string} can be parsed into a {@link Key}, {@code false} otherwise
   * @since 4.12.0
   */
  static boolean parseable(final @Nullable String string) {
    if (string == null) {
      return false;
    }
    final int index = string.indexOf(DEFAULT_SEPARATOR);
    final String namespace = index >= 1 ? string.substring(0, index) : MINECRAFT_NAMESPACE;
    final String value = index >= 0 ? string.substring(index + 1) : string;
    return parseableNamespace(namespace) && parseableValue(value);
  }

  /**
   * Checks if {@code value} is a valid namespace.
   *
   * @param namespace the string to check
   * @return {@code true} if {@code value} is a valid namespace, {@code false} otherwise
   * @since 4.12.0
   */
  static boolean parseableNamespace(final @NotNull String namespace) {
    for (int i = 0, length = namespace.length(); i < length; i++) {
      if (!allowedInNamespace(namespace.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if {@code value} is a valid value.
   *
   * @param value the string to check
   * @return {@code true} if {@code value} is a valid value, {@code false} otherwise
   * @since 4.12.0
   */
  static boolean parseableValue(final @NotNull String value) {
    for (int i = 0, length = value.length(); i < length; i++) {
      if (!allowedInValue(value.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if {@code value} is a valid character in a namespace.
   *
   * @param character the character to check
   * @return {@code true} if {@code value} is a valid character in a namespace, {@code false} otherwise
   * @since 4.12.0
   */
  static boolean allowedInNamespace(final char character) {
    return KeyImpl.allowedInNamespace(character);
  }

  /**
   * Checks if {@code value} is a valid character in a value.
   *
   * @param character the character to check
   * @return {@code true} if {@code value} is a valid character in a value, {@code false} otherwise
   * @since 4.12.0
   */
  static boolean allowedInValue(final char character) {
    return KeyImpl.allowedInValue(character);
  }

  /**
   * Gets the namespace.
   *
   * @return the namespace
   * @since 4.0.0
   */
  @Override
  @NotNull String namespace();

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   */
  @NotNull String value();

  /**
   * Returns the string representation of this key.
   *
   * @return the string representation
   * @since 4.0.0
   */
  @NotNull String asString();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("namespace", this.namespace()),
      ExaminableProperty.of("value", this.value())
    );
  }

  @Override
  default int compareTo(final @NotNull Key that) {
    return comparator().compare(this, that);
  }

  @Override
  default @NotNull Key key() {
    return this;
  }
}
