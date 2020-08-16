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
package net.kyori.adventure.translation;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A registry of translations.
 */
public interface TranslationRegistry extends TranslationSource {
  /**
   * A pattern which matches a single quote.
   */
  Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

  /**
   * Creates a new standalone translation registry.
   *
   * @return a translation registry
   */
  static @NonNull TranslationRegistry create() {
    return new TranslationRegistryImpl();
  }

  /**
   * Gets the shared, global translation registry.
   *
   * @return the translation registry
   */
  static @NonNull TranslationRegistry get() {
    return TranslationRegistryImpl.INSTANCE;
  }

  /**
   * Gets a message format from a key and locale.
   *
   * <p>If a translation for {@code locale} is not found, we will then try {@code locale} without a country code, and then finally fallback to {@link Locale#US en_us}.</p>
   *
   * @param locale a locale
   * @param key a translation key
   * @return a message format or {@code null} to skip translation
   */
  @Override
  @Nullable MessageFormat translate(final @NonNull String key, final @NonNull Locale locale);

  /**
   * Registers a translation.
   *
   * <pre>
   *   final TranslationRegistry registry;
   *   registry.register("myplugin.hello", Locale.US, new MessageFormat("Hi, {0}. How are you?"));
   * </pre>
   *
   * @param key a translation key
   * @param locale a locale
   * @param format a translation format
   * @throws IllegalArgumentException if the translation key is already exists
   */
  void register(final @NonNull String key, final @NonNull Locale locale, final @NonNull MessageFormat format);

  /**
   * Registers a map of translations.
   *
   * @param locale a locale
   * @param formats a map of translation keys to formats
   * @throws IllegalArgumentException if a translation key is already exists
   * @see #register(String, Locale, MessageFormat)
   */
  default void registerAll(final @NonNull Locale locale, final @NonNull Map<String, MessageFormat> formats) {
    this.registerAll(locale, formats.keySet(), formats::get);
  }

  /**
   * Registers a resource bundle of translations.
   *
   * @param locale a locale
   * @param keys the translation keys to register
   * @param function a function to transform a key into a message format
   * @throws IllegalArgumentException if a translation key is already exists
   */
  default void registerAll(final @NonNull Locale locale, final @NonNull Set<String> keys, final Function<String, MessageFormat> function) {
    List<IllegalArgumentException> errors = null;
    for(final String key : keys) {
      try {
        this.register(key, locale, function.apply(key));
      } catch(final IllegalArgumentException e) {
        if(errors == null) {
          errors = new LinkedList<>();
        }
        errors.add(e);
      }
    }
    if(errors != null) {
      final int size = errors.size();
      if(size == 1) {
        throw errors.get(0);
      } else if(size > 1) {
        throw new IllegalArgumentException(String.format("Invalid key (and %d more)", size - 1), errors.get(0));
      }
    }
  }

  /**
   * Registers a resource bundle of translations.
   *
   * @param locale a locale
   * @param resourceBundle a resource bundle
   * @param escapeSingleQuotes whether to escape single quotes
   * @throws IllegalArgumentException if a translation key is already exists
   */
  default void registerAll(final @NonNull Locale locale, final @NonNull ResourceBundle resourceBundle, final boolean escapeSingleQuotes) {
    this.registerAll(locale, resourceBundle.keySet(), key -> {
      final String format = resourceBundle.getString(key);
      return new MessageFormat(
        escapeSingleQuotes
          ? SINGLE_QUOTE_PATTERN.matcher(format).replaceAll("''")
          : format,
        locale
      );
    });
  }

  /**
   * Registers a resource bundle of translations.
   *
   * @param locale a locale
   * @param resourceBundlePath a resource bundle path
   * @param escapeSingleQuotes whether to escape single quotes
   * @throws IllegalArgumentException if a translation key is already exists
   * @see #registerAll(Locale, ResourceBundle, boolean)
   */
  default void registerAll(final @NonNull Locale locale, final @NonNull String resourceBundlePath, final boolean escapeSingleQuotes) {
    final ResourceBundle resourceBundle;
    try {
      resourceBundle = ResourceBundle.getBundle(resourceBundlePath, locale, UTF8ResourceBundleControl.get());
    } catch(final MissingResourceException e) {
      return;
    }
    this.registerAll(locale, resourceBundle, escapeSingleQuotes);
  }

  /**
   * Unregisters a translation key.
   *
   * @param key a translation key
   */
  void unregister(final @NonNull String key);
}
