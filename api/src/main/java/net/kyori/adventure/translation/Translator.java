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
import java.util.ResourceBundle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A message translator.
 *
 * <p>To see how to create a {@link Translator} with a {@link ResourceBundle}
 * see {@link TranslationRegistry#registerAll(Locale, ResourceBundle, boolean)}</p>
 *
 * <p>To bypass vanilla's {@link MessageFormat}-based translation system,
 * see {@link #translate(TranslatableComponent, Locale)}</p>
 *
 * <p>After creating a {@link Translator} you can add it to the {@link GlobalTranslator}
 * to enable automatic translations by the platforms.</p>
 *
 * @see TranslationRegistry
 * @since 4.0.0
 */
public interface Translator {
  /**
   * Parses a {@link Locale} from a {@link String}.
   *
   * @param string the string
   * @return a locale
   * @since 4.0.0
   */
  static @Nullable Locale parseLocale(final @NotNull String string) {
    final String[] segments = string.split("_", 3); // language_country_variant
    final int length = segments.length;
    if (length == 1) {
      return new Locale(string); // language
    } else if (length == 2) {
      return new Locale(segments[0], segments[1]); // language + country
    } else if (length == 3) {
      return new Locale(segments[0], segments[1], segments[2]); // language + country + variant
    }
    return null;
  }

  /**
   * A key identifying this translation source.
   *
   * <p>Intended to be used for display to users.</p>
   *
   * @return an identifier for this translation source
   * @since 4.0.0
   */
  @NotNull Key name();

  /**
   * Checks if this translator has any translations.
   *
   * @return {@link TriState#TRUE} if any, {@link TriState#NOT_SET} if unknown, or {@link TriState#FALSE} if none
   * @since 4.15.0
   */
  default @NotNull TriState hasAnyTranslations() {
    return TriState.NOT_SET;
  }

  /**
   * Gets a message format from a key and locale.
   *
   * <p>When used in the {@link GlobalTranslator}, this method is called only if
   * {@link #translate(TranslatableComponent, Locale)} returns {@code null}.</p>
   *
   * @param locale a locale
   * @param key a translation key
   * @return a message format or {@code null} to skip translation
   * @since 4.0.0
   */
  @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale locale);

  /**
   * Gets a translated component from a translatable component and locale.
   *
   * @param locale a locale
   * @param component a translatable component
   * @return a translated component or {@code null} to use {@link #translate(String, Locale)} instead (if available)
   * @since 4.13.0
   */
  default @Nullable Component translate(final @NotNull TranslatableComponent component, final @NotNull Locale locale) {
    return null;
  }
}
