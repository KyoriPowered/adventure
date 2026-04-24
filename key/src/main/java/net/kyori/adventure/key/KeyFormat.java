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

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A factory to create {@link Key}s using configured defaults.
 *
 * <p>The configuration options include:</p>
 * <dl>
 *   <dt>namespace</dt>
 *   <dd>The namespace to be used as fallback, when no other namespace is specified</dd>
 *   <dt>separator</dt>
 *   <dd>The character separating namespace and value</dd>
 * </dl>
 *
 * <p>Example usage of this class:</p>
 * <pre>{@code
 *   KeyFormat format = keyFormat("adventure", ':');
 *   Key key = format.key("translations");
 *   format.asString(key); // -> "adventure:translations"
 * }</pre>
 *
 * @see Key#key(String)
 * @since 5.2.0
 */
public sealed interface KeyFormat extends Namespaced permits KeyFormatImpl {
  /**
   * Gets the default minecraft key format instance.
   *
   * <p>This uses {@link Key#MINECRAFT_NAMESPACE} and {@link Key#DEFAULT_SEPARATOR}.</p>
   *
   * @return the instance
   * @since 5.2.0
   */
  static KeyFormat minecraft() {
    return KeyFormatImpl.MINECRAFT_INSTANCE;
  }

  /**
   * Creates a key format with a fallback namespace from a {@link Namespaced} instance.
   *
   * @param namespaced the namespaced instance to get the namespace from
   * @return the key format
   * @throws IllegalArgumentException if the namespace contains an invalid character
   * @see #namespace(String)
   * @since 5.2.0
   */
  @SuppressWarnings("PatternValidation") // The namespace is tested later.
  static KeyFormat namespace(final Namespaced namespaced) {
    return namespace(requireNonNull(namespaced, "namespaced").namespace());
  }

  /**
   * Creates a key format with a fallback {@code namespace} and the {@link Key#DEFAULT_SEPARATOR}.
   *
   * @param namespace the namespace to be used as fallback in {@link #parse(String)}
   * @return the key format
   * @throws IllegalArgumentException if the namespace contains an invalid character
   * @since 5.2.0
   */
  static KeyFormat namespace(@KeyPattern.Namespace final String namespace) {
    return keyFormat(namespace, Key.DEFAULT_SEPARATOR);
  }

  /**
   * Creates a key format.
   *
   * @param namespaced the namespaced instance to get the namespace from
   * @param separator  the separator to bse used when parsing keys
   * @return the key format
   * @throws IllegalArgumentException if the namespace contains an invalid character
   * @see #keyFormat(String, char)
   * @since 5.2.0
   */
  @SuppressWarnings("PatternValidation") // The namespace is tested later.
  static KeyFormat keyFormat(final Namespaced namespaced, final char separator) {
    return keyFormat(requireNonNull(namespaced, "namespaced").namespace(), separator);
  }

  /**
   * Creates a key format.
   *
   * @param namespace the namespace to be used as fallback in {@link #parse(String)}
   * @param separator the separator to be used when parsing keys using {@link #parse(String)}
   * @return the key format
   * @throws IllegalArgumentException if the namespace contains an invalid character
   * @since 5.2.0
   */
  static KeyFormat keyFormat(@KeyPattern.Namespace final String namespace, final char separator) {
    return keyFormat()
      .namespace(namespace)
      .separator(separator)
      .build();
  }

  /**
   * Creates a key format builder.
   *
   * @return the builder
   * @since 5.2.0
   */
  static KeyFormat.Builder keyFormat() {
    return new KeyFormatImpl.BuilderImpl();
  }

  /**
   * Gets the separator of this key format.
   *
   * @return the separator
   * @since 5.2.0
   */
  char separator();

  /**
   * Gets the fallback namespace of this key format.
   *
   * @return the namespace
   * @since 5.2.0
   */
  @KeyPattern.Namespace
  @Override
  String namespace();

  /**
   * Creates a key from this format.
   *
   * <p>This will create a key with {@link #namespace()} and {@code value}.</p>
   *
   * @param value the value of the key
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 5.2.0
   */
  Key key(@KeyPattern.Value final String value);

  /**
   * Creates a key from this format.
   *
   * <p>This will parse {@code string} as a key, using {@link #separator()} as a separator between namespace and value.</p>
   *
   * <p>The namespace is optional. If you do not provide one (for example, if you provide just {@code player} or {@code separator + "player"}
   * as the string) then {@link #namespace()} will be used as a namespace and {@code string} will be used as the value,
   * removing the separator if necessary.</p>
   *
   * @param string the string
   * @return the key
   * @throws InvalidKeyException if the namespace or value contains an invalid character
   * @since 5.2.0
   */
  Key parse(final String string);

  /**
   * Checks if {@code string} can be parsed into a {@link Key} using this formats {@link #separator()}.
   * If no namespace is specified in {@code string}, the fallback namespace {@link #namespace()} is used.
   *
   * @param string the string
   * @return {@code true} if {@code string} can be parsed into a key using this format, {@code false} otherwise
   * @since 5.2.0
   */
  boolean parseable(final @Nullable String string);

  /**
   * Turns a {@link Key} into its string representation using the configuration of this format.
   *
   * @param key the key
   * @return the string representation of the key
   * @since 5.2.0
   */
  String asString(final Key key);

  /**
   * Turns a {@link Key} into its string representation in minimal form using the configuration of this format.
   *
   * <p>If the {@link Key#namespace()} of the key is {@link #namespace()}, only the {@link Key#value()} will be returned.</p>
   *
   * @param key the key
   * @return the minimal string representation of the key
   * @since 5.2.0
   */
  String asMinimalString(final Key key);

  /**
   * A builder for a key format.
   *
   * @since 5.2.0
   */
  sealed interface Builder permits KeyFormatImpl.BuilderImpl {
    /**
     * Sets the fallback namespace of this builder from a {@link Namespaced} instance.
     *
     * @param namespaced the namespaced instance
     * @return this builder
     * @throws IllegalArgumentException if the namespace contains an invalid character
     * @see #namespace(String)
     * @since 5.2.0
     */
    @Contract("_ -> this")
    @SuppressWarnings("PatternValidation") // The namespace is tested later.
    default Builder namespace(final Namespaced namespaced) {
      return this.namespace(requireNonNull(namespaced, "namespaced").namespace());
    }

    /**
     * Sets the fallback namespace of this builder.
     *
     * <p>This namespace is used in {@link #parse(String)} when the input string doesn't specify a namespace.</p>
     *
     * <p>Defaults to {@link Key#MINECRAFT_NAMESPACE}</p>
     *
     * @param namespace the namespace
     * @return this builder
     * @throws IllegalArgumentException if the namespace contains an invalid character
     * @since 5.2.0
     */
    @Contract("_ -> this")
    Builder namespace(@KeyPattern.Namespace final String namespace);

    /**
     * Sets the separator of this builder.
     *
     * <p>The separator is used to separate namespace and value in {@link #parse(String)}.</p>
     *
     * <p>Defaults to {@link Key#DEFAULT_SEPARATOR}</p>
     *
     * @param separator the separator
     * @return this builder
     * @since 5.2.0
     */
    @Contract("_ -> this")
    Builder separator(final char separator);

    /**
     * Builds the key format.
     *
     * @return the key format
     * @since 5.2.0
     */
    @Contract(value = "-> new", pure = true)
    KeyFormat build();
  }
}
