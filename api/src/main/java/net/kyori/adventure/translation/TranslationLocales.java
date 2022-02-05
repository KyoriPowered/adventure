/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.Locale;
import java.util.function.Supplier;
import net.kyori.adventure.internal.properties.AdventureProperties;
import org.jetbrains.annotations.Nullable;

final class TranslationLocales {
  private static final Supplier<Locale> GLOBAL;

  static {
    final @Nullable String property = AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
    if (property == null || property.isEmpty()) {
      GLOBAL = () -> Locale.US;
    } else if (property.equals("system")) {
      GLOBAL = Locale::getDefault;
    } else {
      final Locale locale = Translator.parseLocale(property);
      GLOBAL = () -> locale;
    }
  }

  private TranslationLocales() {
  }

  static Locale global() {
    return GLOBAL.get();
  }
}
