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

import java.util.Locale;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslatorTest {
  @Test
  void testParseLocaleWithLanguage() {
    assertEquals(Locale.GERMAN, Translator.parseLocale("de"));
    assertEquals(Locale.ENGLISH, Translator.parseLocale("en"));
  }

  @Test
  void testParseLocaleWithLanguageAndCountry() {
    assertEquals(Locale.GERMANY, Translator.parseLocale("de_de"));
    assertEquals(Locale.US, Translator.parseLocale("en_us"));
  }

  @Test
  void testParseLocaleWithLanguageAndCountryAndVariant() {
    assertEquals(new Locale("en", "ie", "euro"), Translator.parseLocale("en_ie_euro"));
    assertEquals(new Locale("de", "de", "euro"), Translator.parseLocale("de_de_euro"));
  }
}
