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
package net.kyori.adventure.util.flag;

import net.kyori.adventure.key.Key;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A representation of a feature that can be toggled to one of several values.
 *
 * <p>Keys must be unique among all feature flag instances.</p>
 *
 * @param <V> the value type
 * @since 4.15.0
 */
@ApiStatus.NonExtendable
public interface FeatureFlag<V> extends Examinable {

  /**
   * Create a feature flag with a boolean value type.
   *
   * <p>Flag keys must not be reused between flag instances.</p>
   *
   * @param id the flag id
   * @param defaultValue the default value
   * @return the flag instance
   * @since 4.15.0
   */
  static FeatureFlag<Boolean> booleanFlag(final Key id, final boolean defaultValue) {
    return FeatureFlagImpl.flag(id, Boolean.class, defaultValue);
  }

  /**
   * Create a feature flag with an enum value type.
   *
   * <p>Flag keys must not be reused between flag instances.</p>
   *
   * @param id the flag id
   * @param enumClazz the value type
   * @param defaultValue the default value
   * @param <E> the enum type
   * @return the flag instance
   * @since 4.15.0
   */
  static <E extends Enum<E>> FeatureFlag<E> enumFlag(final Key id, final Class<E> enumClazz, final E defaultValue) {
    return FeatureFlagImpl.flag(id, enumClazz, defaultValue);
  }

  /**
   * Get the flag id.
   *
   * <p>This must be unique among feature flags.</p>
   *
   * @return the flag id
   * @since 4.15.0
   */
  @NotNull Key id();

  /**
   * Get the type of the flag value.
   *
   * @return the value type
   * @since 4.15.0
   */
  @NotNull Class<V> type();

  /**
   * Get a default value for the flag, if any is present.
   *
   * @return the default value
   * @since 4.15.0
   */
  @Nullable V defaultValue();
}
