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
package net.kyori.adventure.translation;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

/**
 * A store of translation values.
 *
 * <p>Adventure provides two types of translation store for both component and message
 * format translators.</p>
 *
 * <p>If you wish to implement your own translation store, see
 * {@link AbstractTranslationStore} for a helpful abstraction.</p>
 *
 * @param <T> the type of the values.
 * @since 4.20.0
 */
public interface TranslationStore<T> extends Translator {

  /**
   * Creates a new translation store that uses component-based translation.
   *
   * @param name the name of the translation store
   * @return the translation store
   * @since 4.20.0
   */
  static TranslationStore<Component> component(final Key name) {
    return new ComponentTranslationStore(Objects.requireNonNull(name, "name"));
  }

  /**
   * Creates a new translation store that uses message-format-based translation.
   *
   * @param name the name of the translation store
   * @return the translation store
   * @since 4.20.0
   */
  static TranslationStore.StringBased<MessageFormat> messageFormat(final Key name) {
    return new MessageFormatTranslationStore(Objects.requireNonNull(name, "name"));
  }

  /**
   * Checks if any translations are explicitly registered for the specified key.
   *
   * @param key a translation key
   * @return whether the store contains a value for the translation key
   * @since 4.20.0
   */
  boolean contains(final String key);

  /**
   * Checks if any translations are explicitly registered for the specified key and locale.
   *
   * <p>Note that this method is different from {@link #canTranslate(String, Locale)} as
   * this method does not check for translations in other locales (e.g., the default
   * locale).
   * This means that if a translation exists in the default locale for a given key (but
   * not the provided locale), {@link #canTranslate(String, Locale)} would return
   * {@code true} but this method would return {@code false}.</p>
   *
   * @param key a translation key
   * @param locale the locale
   * @return whether the store contains a value for the translation key and locale
   * @see #canTranslate(String, Locale)
   * @since 4.20.0
   */
  boolean contains(final String key, final Locale locale);

  /**
   * {@inheritDoc}
   *
   * <p>Note that this method is different from {@link #contains(String, Locale)} as
   * this method checks for translations in other locales (e.g., the default locale).
   * This means that if a translation exists in the default locale for a given key (but
   * not the provided locale), {@link #contains(String, Locale)} would return
   * {@code false} but this method would return {@code true}.</p>
   *
   * @param key {@inheritDoc}
   * @param locale {@inheritDoc}
   * @return {@inheritDoc}
   * @see #contains(String, Locale)
   * @since 4.20.0
   */
  @Override
  default boolean canTranslate(final String key, final Locale locale) {
    return Translator.super.canTranslate(key, locale);
  }

  /**
   * Sets the default locale used by this store.
   *
   * @param locale the locale to use a default
   * @since 4.20.0
   */
  void defaultLocale(final Locale locale);

  /**
   * Registers a translation.
   *
   * @param key a translation key
   * @param locale a locale
   * @param translation the translation
   * @throws IllegalArgumentException if the translation key already exists
   * @since 4.20.0
   */
  void register(final String key, final Locale locale, final T translation);

  /**
   * Registers a map of translations.
   *
   * @param locale a locale
   * @param translations a map of translation keys to translations
   * @throws IllegalArgumentException if a translation key already exists
   * @see #register(String, Locale, T)
   * @since 4.20.0
   */
  void registerAll(final Locale locale, final Map<String, T> translations);

  /**
   * Registers translations with a set of keys and a mapping function to produce the translation from the key.
   *
   * @param locale a locale
   * @param keys the translation keys to register
   * @param function a function to transform a key into a message format
   * @throws IllegalArgumentException if a translation key already exists
   * @since 4.20.0
   */
  void registerAll(final Locale locale, final Set<String> keys, Function<String, T> function);

  /**
   * Unregisters a translation key.
   *
   * @param key a translation key
   * @since 4.0.0
   */
  void unregister(final String key);

  /**
   * An abstract, string-based translation store.
   *
   * <p>This class extends upon the standard abstract translation store by adding
   * support for reading from resource bundles.</p>
   *
   * @param <T> the type of the translation
   * @since 4.20.0
   */
  interface StringBased<T> extends TranslationStore<T> {
    /**
     * Registers a resource bundle of translations.
     *
     * @param locale a locale
     * @param path a path to the resource bundle
     * @param escapeSingleQuotes whether to escape single quotes
     * @throws IllegalArgumentException if a translation key already exists
     * @see #registerAll(Locale, ResourceBundle, boolean)
     * @since 4.20.0
     */
    void registerAll(final Locale locale, final Path path, final boolean escapeSingleQuotes);

    /**
     * Registers a resource bundle of translations.
     *
     * @param locale a locale
     * @param bundle a resource bundle
     * @param escapeSingleQuotes whether to escape single quotes
     * @throws IllegalArgumentException if a translation key already exists
     * @since 4.20.0
     */
    void registerAll(final Locale locale, final ResourceBundle bundle, final boolean escapeSingleQuotes);
  }
}
