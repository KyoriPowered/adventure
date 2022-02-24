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
package net.kyori.adventure.internal.properties;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Adventure properties.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public final class AdventureProperties {
  /**
   * Property for specifying whether debug mode is enabled.
   *
   * @since 4.10.0
   */
  public static final Property<Boolean> DEBUG = property("debug", Boolean::parseBoolean, false);
  /**
   * Property for specifying the default translation locale.
   *
   * @since 4.10.0
   */
  public static final Property<String> DEFAULT_TRANSLATION_LOCALE = property("defaultTranslationLocale", Function.identity(), null);
  /**
   * Property for specifying whether service load failures are fatal.
   *
   * @since 4.10.0
   */
  public static final Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL = property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE);
  /**
   * Property for specifying whether to warn when legacy formatting is detected.
   *
   * @since 4.10.0
   */
  public static final Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);

  private AdventureProperties() {
  }

  /**
   * Creates a new property.
   *
   * @param name the property name
   * @param parser the value parser
   * @param defaultValue the default value
   * @param <T> the value type
   * @return a property
   * @since 4.10.0
   */
  public static <T> @NotNull Property<T> property(final @NotNull String name, final @NotNull Function<String, T> parser, final @Nullable T defaultValue) {
    return AdventurePropertiesImpl.property(name, parser, defaultValue);
  }

  /**
   * A property.
   *
   * @param <T> the value type
   * @since 4.10.0
   */
  @ApiStatus.Internal
  @ApiStatus.NonExtendable
  public interface Property<T> {
    /**
     * Gets the value.
     *
     * @return the value
     * @since 4.10.0
     */
    @Nullable T value();
  }
}
