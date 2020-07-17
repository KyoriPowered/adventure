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
package net.kyori.adventure.text.feature.translation;

import net.kyori.adventure.util.UTF8;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A registry of translations.
 */
public interface TranslationRegistry extends Translator {
  /**
   * Gets the shared, global translation registry.
   *
   * @return the translation registry
   */
  static TranslationRegistry get() {
    return TranslationRegistryImpl.INSTANCE;
  }

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
    final List<IllegalArgumentException> errors = new LinkedList<>();
    for(final Map.Entry<String, MessageFormat> entry : formats.entrySet()) {
      try {
        this.register(entry.getKey(), locale, entry.getValue());
      } catch(final IllegalArgumentException e) {
        errors.add(e);
      }
    }
    final int size = errors.size();
    if(size == 1) {
      throw errors.get(0);
    } else if(size > 1) {
      throw new IllegalArgumentException(String.format("Invalid key (and %d more)", size - 1), errors.get(0));
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
    final List<IllegalArgumentException> errors = new LinkedList<>();
    for(final String key : resourceBundle.keySet()) {
      String format = resourceBundle.getString(key);
      if(escapeSingleQuotes) {
        format = format.replaceAll("'", "''");
      }
      try {
        this.register(key, locale, new MessageFormat(format, locale));
      } catch(final IllegalArgumentException e) {
        errors.add(e);
      }
    }
    final int size = errors.size();
    if(size == 1) {
      throw errors.get(0);
    } else if(size > 1) {
      throw new IllegalArgumentException(String.format("Invalid key (and %d more)", size - 1), errors.get(0));
    }
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
      resourceBundle = ResourceBundle.getBundle(resourceBundlePath, locale, new UTF8());
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
