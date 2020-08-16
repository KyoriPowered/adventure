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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class TranslationRegistryImpl implements Examinable, TranslationRegistry {
  static final TranslationRegistry INSTANCE = new TranslationRegistryImpl();
  private final Map<String, Translation> translations = new ConcurrentHashMap<>();

  TranslationRegistryImpl() {
  }

  @Override
  public void register(final @NonNull String key, final @NonNull Locale locale, final @NonNull MessageFormat format) {
    this.translations.computeIfAbsent(key, Translation::new).register(locale, format);
  }

  @Override
  public void unregister(final @NonNull String key) {
    this.translations.remove(key);
  }

  @Override
  public @Nullable MessageFormat translate(final @NonNull String key, final @NonNull Locale locale) {
    final Translation translation = this.translations.get(key);
    if(translation == null) return null;
    return translation.translate(locale);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("translations", this.translations));
  }

  static final class Translation implements Examinable {
    private final String key;
    private final Map<Locale, MessageFormat> formats;

    Translation(final @NonNull String key) {
      this.key = requireNonNull(key, "translation key");
      this.formats = new ConcurrentHashMap<>();
    }

    void register(final @NonNull Locale locale, final @NonNull MessageFormat format) {
      if(this.formats.putIfAbsent(requireNonNull(locale, "locale"), requireNonNull(format, "message format")) != null) {
        throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
      }
    }

    @Nullable MessageFormat translate(final @NonNull Locale locale) {
      MessageFormat format = this.formats.get(locale);
      if(format == null) {
        format = this.formats.get(new Locale(locale.getLanguage())); // without country
        if(format == null) {
          format = this.formats.get(Locale.US); // fallback to en_us
        }
      }
      return format;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("formats", this.formats)
      );
    }
  }
}
