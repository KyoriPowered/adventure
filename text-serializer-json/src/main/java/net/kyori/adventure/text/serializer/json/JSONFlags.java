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
package net.kyori.adventure.text.serializer.json;

import net.kyori.featureflag.FeatureFlag;
import net.kyori.featureflag.FeatureFlagConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Feature flags that can apply to JSON serializers.
 *
 * <p>See serializer documentation for specific details on which flags are supported.</p>
 *
 * @since 4.15.0
 */
public final class JSONFlags {
  private JSONFlags() {
  }

  private static final int VERSION_INITIAL = 0;
  private static final int VERSION_1_16 = 2526; // 20w16a
  private static final int VERSION_1_20_3 = 3679; // 23w40a

  /**
   * Whether to emit RGB text.
   *
   * <p>If this attribute is disabled, colors in styles will be downsampled to the classic 16 colors.</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.16
   */
  public static final FeatureFlag<Boolean> EMIT_RGB = FeatureFlag.booleanFlag(key("emit/rgb"), true);
  /**
   * Whether to emit the legacy hover event style used before Minecraft 1.16.
   *
   * @since 4.15.0
   */
  public static final FeatureFlag<Boolean> EMIT_LEGACY_HOVER_EVENT = FeatureFlag.booleanFlag(key("emit/legacy_hover"), false);
  /**
   * Whether to emit the hover event contents as updated in 1.16.
   *
   * @since 4.15.0
   * @sinceMinecraft 1.16
   */
  public static final FeatureFlag<Boolean> EMIT_MODERN_HOVER_EVENT = FeatureFlag.booleanFlag(key("emit/modern_hover"), true);

  /**
   * Whether to emit text components with no style and no children as plain text.
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  public static final FeatureFlag<Boolean> EMIT_COMPACT_TEXT_COMPONENT = FeatureFlag.booleanFlag(key("emit/compact_text_component"), true);

  /**
   * Whether to emit the hover event show entity action's entity UUID as an int array,
   * as understood by 1.20.3+, or as a string as understood by previous versions.
   */
  public static final FeatureFlag<Boolean> EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY = FeatureFlag.booleanFlag(key("emit/hover_show_entity_id_as_int_array"), true);

  /**
   * Versioned by world data version.
   */
  private static final FeatureFlagConfig.Versioned BY_DATA_VERSION = FeatureFlagConfig.versionedFeatureFlagConfig()
    .version(
      VERSION_INITIAL,
      b -> b.value(EMIT_LEGACY_HOVER_EVENT, true)
        .value(EMIT_RGB, false)
        .value(EMIT_MODERN_HOVER_EVENT, false)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    )
    .version(
      VERSION_1_16,
      b -> b.value(EMIT_LEGACY_HOVER_EVENT, false)
        .value(EMIT_RGB, true)
        .value(EMIT_MODERN_HOVER_EVENT, true)
    )
    .version(
      VERSION_1_20_3,
      b -> b.value(EMIT_COMPACT_TEXT_COMPONENT, true)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true)
    )
    .build();

  /**
   * The combination of features that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components, but will not result in information being discarded.</p>
   */
  private static final FeatureFlagConfig MOST_COMPATIBLE = FeatureFlagConfig.featureFlagConfig()
    .value(EMIT_LEGACY_HOVER_EVENT, true)
    .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    .value(EMIT_COMPACT_TEXT_COMPONENT, false)
    .build();

  private static String key(final String value) {
    return "adventure:json/" + value;
  }

  /**
   * Get JSON flags delineated by world data version.
   *
   * @return the versioned flag set
   * @since 4.15.0
   */
  public static FeatureFlagConfig.@NotNull Versioned byDataVersion() {
    return BY_DATA_VERSION;
  }

  /**
   * The combination of flags that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components.</p>
   *
   * @return the most widely compatible feature flag set
   * @since 4.15.0
   */
  public static @NotNull FeatureFlagConfig compatibility() {
    return MOST_COMPATIBLE;
  }
}
