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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An abstraction to ease the construction of translation stores with generic values.
 *
 * @param <T> the value of the translation store
 * @since 4.20.0
 */
public abstract class AbstractTranslationStore<T> implements Examinable, TranslationStore<T> {
  private final @NotNull Key name;
  private final Map<String, Translation> translations = new ConcurrentHashMap<>();
  private volatile @NotNull Locale defaultLocale = Locale.US;

  /**
   * Creates a new abstract translation store with a given name.
   *
   * @param name the name
   * @since 4.20.0
   */
  protected AbstractTranslationStore(final @NotNull Key name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  /**
   * Returns a translation for a given key and locale, if any.
   *
   * @param key the key
   * @param locale the locale
   * @return the translation, or {@code null} if none exists for this key
   * @since 4.20.0
   */
  protected @Nullable T translationValue(final @NotNull String key, final @NotNull Locale locale) {
    final Translation translation = this.translations.get(requireNonNull(key, "key"));
    if (translation == null) return null;
    return translation.translate(requireNonNull(locale, "locale"));
  }

  @Override
  public final boolean contains(final @NotNull String key) {
    return this.translations.containsKey(key);
  }

  @Override
  public final boolean contains(final @NotNull String key, final @NotNull Locale locale) {
    final Translation translation = this.translations.get(requireNonNull(key, "key"));
    if (translation == null) return false;
    return translation.translations.get(requireNonNull(locale, "locale")) != null;
  }

  @Override
  public final void defaultLocale(final @NotNull Locale locale) {
    this.defaultLocale = requireNonNull(locale, "locale");
  }

  @Override
  public final void register(final @NotNull String key, final @NotNull Locale locale, final @NotNull T translation) {
    this.translations.computeIfAbsent(key, Translation::new).register(locale, translation);
  }

  @Override
  public final void registerAll(final @NotNull Locale locale, final @NotNull Map<String, T> translations) {
    this.registerAll(locale, translations.keySet(), translations::get);
  }

  @Override
  public final void registerAll(final @NotNull Locale locale, final @NotNull Set<String> keys, final Function<String, T> function) {
    IllegalArgumentException firstError = null;
    int errorCount = 0;
    for (final String key : keys) {
      try {
        this.register(key, locale, function.apply(key));
      } catch (final IllegalArgumentException e) {
        if (firstError == null) {
          firstError = e;
        }
        errorCount++;
      }
    }
    if (firstError != null) {
      if (errorCount == 1) {
        throw firstError;
      } else if (errorCount > 1) {
        throw new IllegalArgumentException(String.format("Invalid key (and %d more)", errorCount - 1), firstError);
      }
    }
  }

  @Override
  public final void unregister(final @NotNull String key) {
    this.translations.remove(key);
  }

  @Override
  public final @NotNull Key name() {
    return this.name;
  }

  @Override
  public final @NotNull TriState hasAnyTranslations() {
    return TriState.byBoolean(!this.translations.isEmpty());
  }

  @Override
  public final @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("translations", this.translations));
  }

  @Override
  public final boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof AbstractTranslationStore)) return false;

    final AbstractTranslationStore<?> that = (AbstractTranslationStore<?>) other;

    return this.name.equals(that.name)
      && this.translations.equals(that.translations)
      && this.defaultLocale.equals(that.defaultLocale);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.name, this.translations, this.defaultLocale);
  }

  @Override
  public final String toString() {
    return Internals.toString(this);
  }

  private final class Translation implements Examinable {
    private final String key;
    private final Map<Locale, T> translations;

    private Translation(final @NotNull String key) {
      this.key = requireNonNull(key, "key");
      this.translations = new ConcurrentHashMap<>();
    }

    private @Nullable T translate(final @NotNull Locale locale) {
      T format = this.translations.get(requireNonNull(locale, "locale"));
      if (format == null) {
        format = this.translations.get(new Locale(locale.getLanguage())); // try without country
        if (format == null) {
          format = this.translations.get(AbstractTranslationStore.this.defaultLocale); // try local default locale
          if (format == null) {
            format = this.translations.get(TranslationLocales.global()); // try global default locale
          }
        }
      }
      return format;
    }

    private void register(final @NotNull Locale locale, final @NotNull T translation) {
      if (this.translations.putIfAbsent(requireNonNull(locale, "locale"), requireNonNull(translation, "translation")) != null) {
        throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
      }
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("translations", this.translations)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (!(other instanceof AbstractTranslationStore.Translation)) return false;
      final AbstractTranslationStore<?>.Translation that = (AbstractTranslationStore<?>.Translation) other;
      return this.key.equals(that.key) &&
        this.translations.equals(that.translations);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.key, this.translations);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  /**
   * An abstract, string-based translation store.
   *
   * <p>This class extends upon the standard abstract translation store by adding
   * support for reading from resource bundles.</p>
   *
   * @param <T> the type of the translation
   * @since 4.20.0
   */
  public abstract static class StringBased<T> extends AbstractTranslationStore<T> implements TranslationStore.StringBased<T> {
    private static final Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

    /**
     * Creates a new abstract, string-based translation store with a given name.
     *
     * @param name the name
     * @since 4.20.0
     */
    protected StringBased(final @NotNull Key name) {
      super(name);
    }

    /**
     * Parses a string into the format required for this translation store.
     *
     * @param string the string
     * @param locale the locale for the string, if needed
     * @return the parsed type
     * @since 4.20.0
     */
    protected abstract @NotNull T parse(final @NotNull String string, final @NotNull Locale locale);

    @Override
    public final void registerAll(final @NotNull Locale locale, final @NotNull Path path, final boolean escapeSingleQuotes) {
      try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        this.registerAll(locale, new PropertyResourceBundle(reader), escapeSingleQuotes);
      } catch (final IOException e) {
        // ignored
      }
    }

    @Override
    public final void registerAll(final @NotNull Locale locale, final @NotNull ResourceBundle bundle, final boolean escapeSingleQuotes) {
      this.registerAll(locale, bundle.keySet(), key -> {
        final String format = bundle.getString(key);
        return this.parse(
          escapeSingleQuotes
            ? SINGLE_QUOTE_PATTERN.matcher(format).replaceAll("''")
            : format,
          locale
        );
      });
    }
  }
}
