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
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.util.TriState;
import net.kyori.examination.ExaminableProperty;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@NullMarked
final class GlobalTranslatorImpl implements GlobalTranslator {
  private static final Key NAME = Key.key("adventure", "global");
  static final GlobalTranslatorImpl INSTANCE = new GlobalTranslatorImpl();
  final TranslatableComponentRenderer<Locale> renderer = TranslatableComponentRenderer.usingTranslationSource(this);
  private final Set<Translator> sources = Collections.newSetFromMap(new ConcurrentHashMap<>());

  private GlobalTranslatorImpl() {
  }

  @Override
  public Key name() {
    return NAME;
  }

  @Override
  public Iterable<? extends Translator> sources() {
    return Collections.unmodifiableSet(this.sources);
  }

  @Override
  public boolean addSource(final Translator source) {
    requireNonNull(source, "source");
    if (source == this) throw new IllegalArgumentException("GlobalTranslationSource");
    return this.sources.add(source);
  }

  @Override
  public boolean removeSource(final Translator source) {
    requireNonNull(source, "source");
    return this.sources.remove(source);
  }

  @Override
  public TriState hasAnyTranslations() {
    if (!this.sources.isEmpty()) {
      return TriState.TRUE;
    }
    return TriState.FALSE;
  }

  @Override
  public @Nullable MessageFormat translate(final String key, final Locale locale) {
    requireNonNull(key, "key");
    requireNonNull(locale, "locale");
    for (final Translator source : this.sources) {
      final MessageFormat translation = source.translate(key, locale);
      if (translation != null) return translation;
    }
    return null;
  }

  @Override
  public @Nullable Component translate(final TranslatableComponent component, final Locale locale) {
    requireNonNull(component, "component");
    requireNonNull(locale, "locale");
    for (final Translator source : this.sources) {
      final Component translation = source.translate(component, locale);
      if (translation != null) return translation;
    }
    return null;
  }

  @Override
  public Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("sources", this.sources));
  }
}
