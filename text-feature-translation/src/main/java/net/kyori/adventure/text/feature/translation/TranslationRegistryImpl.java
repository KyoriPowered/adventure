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

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/* package */ final class TranslationRegistryImpl extends TranslatableComponentRenderer implements TranslationRegistry {
  /* package */ static final TranslationRegistry INSTANCE = new TranslationRegistryImpl();
  /* package */ static final Translation EMPTY = new Translation("");

  /* package */ static final class Translation implements Examinable {
    private final String key;
    private final Map<Locale, MessageFormat> formats;

    private Translation(final @NonNull String key) {
      this.key = requireNonNull(key, "translation key");
      this.formats = new ConcurrentHashMap<>();
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("key", this.key),
        ExaminableProperty.of("formats", this.formats)
      );
    }

    private void register(final @NonNull Locale locale, final @NonNull MessageFormat format) {
      this.formats.put(locale, format);
    }

    private @Nullable MessageFormat translate(final @NonNull Locale locale) {
      return this.formats.get(locale);
    }
  }

  private final Map<String, Translation> translations = new ConcurrentHashMap<>();

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
    return this.translations.getOrDefault(key, EMPTY).translate(locale);
  }
}
