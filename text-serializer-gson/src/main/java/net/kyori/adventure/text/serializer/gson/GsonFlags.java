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
package net.kyori.adventure.text.serializer.gson;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.flag.FeatureFlag;
import net.kyori.adventure.util.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

/**
 * Feature flags that apply to the Gson serializer.
 *
 * @since 4.15.0
 */
public final class GsonFlags {
  private GsonFlags() {
  }

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
   * Versioned by protocol version.
   */
  private static final FeatureFlagSet.Versioned BY_PROTOCOL_VERSION = FeatureFlagSet.versionedBuilder()
    .version(
      0 /* initial */,
      b -> b.value(EMIT_LEGACY_HOVER_EVENT, true)
        .value(EMIT_RGB, false)
        .value(EMIT_MODERN_HOVER_EVENT, false)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    )
    .version(
      713 /* 20w17a, for 1.16 */,
      b -> b.value(EMIT_LEGACY_HOVER_EVENT, false)
        .value(EMIT_RGB, true)
        .value(EMIT_MODERN_HOVER_EVENT, true)
    )
    .version(
      765 /* 1.20.3 */,
      b -> b.value(EMIT_COMPACT_TEXT_COMPONENT, true)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true)
    )
    .build();

  /**
   * The combination of flags that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components</p>
   */
  private static final FeatureFlagSet MOST_COMPATIBLE = FeatureFlagSet.builder()
    .value(EMIT_LEGACY_HOVER_EVENT, true)
    .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    .value(EMIT_COMPACT_TEXT_COMPONENT, false)
    .build();

  @SuppressWarnings("PatternValidation")
  private static Key key(final String value) {
    return Key.key("adventure", "gson/" + value);
  }

  /**
   * Get Gson flags delineated by game protocol version.
   *
   * @return the versioned flag set
   * @since 4.15.0
   */
  public static FeatureFlagSet.@NotNull Versioned byProtocolVersion() {
    return BY_PROTOCOL_VERSION;
  }

  /**
   * The combination of flags that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components</p>
   *
   * @return the most widely compatible feature flag set
   * @since 4.15.0
   */
  public static @NotNull FeatureFlagSet compatibility() {
    return MOST_COMPATIBLE;
  }
}
