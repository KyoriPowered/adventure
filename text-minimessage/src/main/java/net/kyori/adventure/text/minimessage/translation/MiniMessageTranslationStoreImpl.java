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
package net.kyori.adventure.text.minimessage.translation;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.AbstractTranslationStore;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.Nullable;

final class MiniMessageTranslationStoreImpl extends AbstractTranslationStore.StringBased<String> implements MiniMessageTranslationStore {
  private final Translator translator;

  MiniMessageTranslationStoreImpl(final Key name, final MiniMessage miniMessage) {
    super(name);
    this.translator = new Translator(Objects.requireNonNull(miniMessage, "miniMessage"));
  }

  @Override
  protected String parse(final String string, final Locale locale) {
    return string;
  }

  @Override
  public @Nullable MessageFormat translate(final String key, final Locale locale) {
    return null;
  }

  @Override
  public @Nullable Component translate(final TranslatableComponent component, final Locale locale) {
    return this.translator.translate(component, locale);
  }

  private final class Translator extends MiniMessageTranslator {

    private Translator(final MiniMessage miniMessage) {
      super(miniMessage);
    }

    @Override
    protected @Nullable String getMiniMessageString(final String key, final Locale locale) {
      return MiniMessageTranslationStoreImpl.this.translationValue(key, locale);
    }

    @Override
    public Key name() {
      return MiniMessageTranslationStoreImpl.this.name();
    }

    @Override
    public TriState hasAnyTranslations() {
      return MiniMessageTranslationStoreImpl.this.hasAnyTranslations();
    }
  }
}
