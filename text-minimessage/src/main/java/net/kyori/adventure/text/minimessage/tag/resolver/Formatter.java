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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.standard.ProgressTag;
import org.jetbrains.annotations.NotNull;

/**
 * Tag resolvers producing tags that insert formatted values.
 *
 * <p>These are effectively placeholders.</p>
 *
 * @since 4.11.0
 */
public final class Formatter {
  private Formatter() {
  }

  /**
   * Creates a replacement that inserts a number as a component. The component will be formatted by the provided DecimalFormat.
   *
   * <p>This tag accepts a locale, a format pattern, both or nothing as arguments. The locale has to be provided as first argument.</p>
   *
   * <p>Refer to {@link Locale} for usable locale tags. Refer to {@link DecimalFormat} for usable patterns.</p>
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param number the number
   * @return the placeholder
   * @since 4.11.0
   */
  public static @NotNull TagResolver number(final @NotNull String key, final @NotNull Number number) {
    return TagResolver.resolver(key, (argumentQueue, context) -> {
      final NumberFormat decimalFormat;
      if (argumentQueue.hasNext()) {
        final String locale = argumentQueue.pop().value();
        if (argumentQueue.hasNext()) {
          final String format = argumentQueue.pop().value();
          decimalFormat = new DecimalFormat(format, new DecimalFormatSymbols(Locale.forLanguageTag(locale)));
        } else {
          if (locale.contains(".")) {
            decimalFormat = new DecimalFormat(locale, DecimalFormatSymbols.getInstance());
          } else {
            decimalFormat = DecimalFormat.getInstance(Locale.forLanguageTag(locale));
          }
        }
      } else {
        decimalFormat = DecimalFormat.getInstance();
      }
      return Tag.inserting(context.deserialize(decimalFormat.format(number)));
    });
  }

  /**
   * Creates a replacement that inserts a date or a time as a component. The component will be formatted by the provided Date Format.
   *
   * <p>This tag expects a format as attribute. Refer to {@link DateTimeFormatter} for usable patterns.</p>
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param time the time
   * @return the placeholder
   * @since 4.11.0
   */
  public static @NotNull TagResolver date(final @NotNull String key, final @NotNull TemporalAccessor time) {
    return TagResolver.resolver(key, (argumentQueue, context) -> {
      final String format = argumentQueue.popOr("Format expected.").value();
      return Tag.inserting(context.deserialize(DateTimeFormatter.ofPattern(format).format(time)));
    });
  }

  /**
   * Creates a replacement that inserts a choice formatted text. The component will be formatted by the provided ChoiceFormat.
   *
   * <p>This tag expects a format as attribute. Refer to {@link ChoiceFormat} for usable patterns.</p>
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param number the number
   * @return the placeholder
   * @since 4.11.0
   */
  public static @NotNull TagResolver choice(final @NotNull String key, final Number number) {
    return TagResolver.resolver(key, (argumentQueue, context) -> {
      final String format = argumentQueue.popOr("Format expected.").value();
      final ChoiceFormat choiceFormat = new ChoiceFormat(format);
      return Tag.inserting(context.deserialize(choiceFormat.format(number)));
    });
  }

  /**
   * Creates a replacement that inserts a progress bar. The component will be formatted by the provided fill and remaining colors and length
   *
   * <p>This tag expects a text to use to denote progress and can accept length, filled color and empty color. The text has to be provided as first argument.
   *    In case the fill and empty colors and length are not provided, they will default to {@link ProgressTag#DEFAULT_FILLED_COLOR}, {@link ProgressTag#DEFAULT_REMAINING_COLOR} and {@value ProgressTag#DEFAULT_LENGTH} respectively.</p>
   *
   * <p>This replacement is auto-closing, so its style will not influence the style of following components.</p>
   *
   * @param key the key
   * @param progressSupplier the supplier for the progress value
   * @return the placeholder
   * @since 4.13.0
   */
  public static @NotNull TagResolver progress(final @NotNull String key, final @NotNull Supplier<Double> progressSupplier) {
    return TagResolver.resolver(key, (args, ctx) -> ProgressTag.createProgressProvided(args, ctx, progressSupplier));
  }
}
