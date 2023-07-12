/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class TranslationRegistryImpl implements Examinable, TranslationRegistry {
  private final Key name;
  private final Map<String, Translation> translations = new ConcurrentHashMap<>();
  private Locale defaultLocale = Locale.US; // en_us

  TranslationRegistryImpl(final Key name) {
    this.name = name;
  }

  @Override
  public void register(final @NotNull String key, final @NotNull Locale locale, final @NotNull MessageFormat format) {
    this.translations.computeIfAbsent(key, Translation::new).register(locale, format);
  }

  @Override
  public void unregister(final @NotNull String key) {
    this.translations.remove(key);
  }

  @Override
  public @NotNull Key name() {
    return this.name;
  }

  @Override
  public boolean contains(final @NotNull String key) {
    return this.translations.containsKey(key);
  }

  @Override
  public @NotNull TriState hasAnyTranslations() {
    if (!this.translations.isEmpty()) {
      return TriState.TRUE;
    }
    return TriState.FALSE;
  }

  @Override
  public @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale) {
    final Translation translation = this.translations.get(key);
    if (translation == null) return null;
    return translation.translate(locale);
  }

  @Override
  public void defaultLocale(final @NotNull Locale defaultLocale) {
    this.defaultLocale = requireNonNull(defaultLocale, "defaultLocale");
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("translations", this.translations));
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof TranslationRegistryImpl)) return false;

    final TranslationRegistryImpl that = (TranslationRegistryImpl) other;

    return this.name.equals(that.name)
      && this.translations.equals(that.translations)
      && this.defaultLocale.equals(that.defaultLocale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.translations, this.defaultLocale);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  final class Translation implements Examinable {
    private final String key;
    private final Map<Locale, MessageFormat> formats;

    Translation(final @NotNull String key) {
      this.key = requireNonNull(key, "translation key");
      this.formats = new ConcurrentHashMap<>();
    }

    void register(final @NotNull Locale locale, final @NotNull MessageFormat format) {
      if (this.formats.putIfAbsent(requireNonNull(locale, "locale"), requireNonNull(format, "message format")) != null) {
        throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
      }
    }

    @Nullable MessageFormat translate(final @NotNull Locale locale) {
      MessageFormat format = this.formats.get(requireNonNull(locale, "locale"));
      if (format == null) {
        format = this.formats.get(new Locale(locale.getLanguage())); // try without country
        if (format == null) {
          format = this.formats.get(TranslationRegistryImpl.this.defaultLocale); // try local default locale
          if (format == null) {
            format = this.formats.get(TranslationLocales.global()); // try global default locale
          }
        }
      }
      return format;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("formats", this.formats)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if (this == other) return true;
      if (!(other instanceof Translation)) return false;
      final Translation that = (Translation) other;
      return this.key.equals(that.key) &&
        this.formats.equals(that.formats);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.key, this.formats);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }
}
