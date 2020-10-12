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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A registry of translations.
 *
 * @since 4.0.0
 */
public interface TranslationRegistry extends TranslationSource {
  /**
   * A pattern which matches a single quote.
   *
   * @since 4.0.0
   */
  Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

  /**
   * Creates a new standalone translation registry.
   *
   * @return a translation registry
   * @since 4.0.0
   */
  static @NonNull TranslationRegistry create(final Key name) {
    return new TranslationRegistryImpl(requireNonNull(name, "name"));
  }

  /**
   * Gets a message format from a key and locale.
   *
   * <p>If a translation for {@code locale} is not found, we will then try {@code locale} without a country code, and then finally fallback to a default locale.</p>
   *
   * @param locale a locale
   * @param key a translation key
   * @return a message format or {@code null} to skip translation
   * @since 4.0.0
   */
  @Override
  @Nullable MessageFormat translate(final @NonNull String key, final @NonNull Locale locale);

  /**
   * Sets the default locale used by this registry.
   *
   * @param locale the locale to use a default
   * @since 4.0.0
   */
  void defaultLocale(final @NonNull Locale locale);

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
   * @since 4.0.0
   */
  void register(final @NonNull String key, final @NonNull Locale locale, final @NonNull MessageFormat format);

  /**
   * Registers a map of translations.
   *
   * @param locale a locale
   * @param formats a map of translation keys to formats
   * @throws IllegalArgumentException if a translation key is already exists
   * @see #register(String, Locale, MessageFormat)
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @since 4.0.0
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
   * @param path a path to the resource bundle
   * @param escapeSingleQuotes whether to escape single quotes
   * @throws IllegalArgumentException if a translation key is already exists
   * @see #registerAll(Locale, ResourceBundle, boolean)
   * @since 4.0.0
   */
  default void registerAll(final @NonNull Locale locale, final @NonNull Path path, final boolean escapeSingleQuotes) {
    final PropertyResourceBundle bundle;
    try(final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      bundle = new PropertyResourceBundle(reader);
    } catch(final IOException e) {
      return;
    }
    this.registerAll(locale, bundle, escapeSingleQuotes);
  }

  /**
   * Unregisters a translation key.
   *
   * @param key a translation key
   * @since 4.0.0
   */
  void unregister(final @NonNull String key);
}
