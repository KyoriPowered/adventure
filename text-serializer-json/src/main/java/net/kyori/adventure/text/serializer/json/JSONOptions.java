/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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

import net.kyori.option.Option;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

/**
 * Options that can apply to JSON serializers.
 *
 * <p>See serializer documentation for specific details on which flags are supported.</p>
 *
 * @since 4.15.0
 */
public final class JSONOptions {
  private JSONOptions() {
  }

  private static final int VERSION_INITIAL = 0;
  private static final int VERSION_1_16 = 2526; // 20w16a
  private static final int VERSION_1_20_3 = 3679; // 23w40a
  private static final int VERSION_1_20_5 = 3819; // 24w09a

  /**
   * Whether to emit RGB text.
   *
   * <p>If this attribute is disabled, colors in styles will be downsampled to the classic 16 colors.</p>
   *
   * @since 4.15.0
   * @sinceMinecraft 1.16
   */
  public static final Option<Boolean> EMIT_RGB = Option.booleanOption(key("emit/rgb"), true);
  /**
   * Control how hover event values should be emitted.
   *
   * @since 4.15.0
   */
  public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE = Option.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.MODERN_ONLY);

  /**
   * Whether to emit text components with no style and no children as plain text.
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT = Option.booleanOption(key("emit/compact_text_component"), true);

  /**
   * Whether to emit the hover event show entity action's entity UUID as an int array,
   * as understood by 1.20.3+, or as a string as understood by previous versions.
   *
   * @since 4.15.0
   */
  public static final Option<Boolean> EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY = Option.booleanOption(key("emit/hover_show_entity_id_as_int_array"), true);

  /**
   * Whether to be strict about accepting invalid hover/click events.
   *
   * <p>When enabled, this matches Vanilla as of 1.20.3.</p>
   *
   * @since 4.15.0
   */
  public static final Option<Boolean> VALIDATE_STRICT_EVENTS = Option.booleanOption(key("validate/strict_events"), true);
  /**
   * Whether to emit the default hover event item stack quantity of {@code 1}.
   *
   * <p>When enabled, this matches Vanilla as of 1.20.5.</p>
   *
   * @since 4.17.0
   */
  public static final Option<Boolean> EMIT_DEFAULT_ITEM_HOVER_QUANTITY = Option.booleanOption(key("emit/default_item_hover_quantity"), true);

  /**
   * How to emit the item data on {@code show_item} hover events.
   *
   * @since 4.17.0
   */
  public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE = Option.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);

  /**
   * Versioned by world data version.
   */
  private static final OptionState.Versioned BY_DATA_VERSION = OptionState.versionedOptionState()
    .version(
      VERSION_INITIAL,
      b -> b.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.LEGACY_ONLY)
        .value(EMIT_RGB, false)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
        .value(VALIDATE_STRICT_EVENTS, false)
        .value(EMIT_DEFAULT_ITEM_HOVER_QUANTITY, false)
        .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_LEGACY_NBT)
    )
    .version(
      VERSION_1_16,
      b -> b.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.MODERN_ONLY)
        .value(EMIT_RGB, true)
    )
    .version(
      VERSION_1_20_3,
      b -> b.value(EMIT_COMPACT_TEXT_COMPONENT, true)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, true)
        .value(VALIDATE_STRICT_EVENTS, true)
    )
    .version(
      VERSION_1_20_5,
      b -> b.value(EMIT_DEFAULT_ITEM_HOVER_QUANTITY, true)
        .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_DATA_COMPONENTS)
    )
    .build();

  /**
   * The combination of features that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components, but will not result in information being discarded.</p>
   */
  private static final OptionState MOST_COMPATIBLE = OptionState.optionState()
    .value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.BOTH)
    .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    .value(EMIT_COMPACT_TEXT_COMPONENT, false)
    .value(VALIDATE_STRICT_EVENTS, false)
    .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_EITHER)
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
  public static OptionState.@NotNull Versioned byDataVersion() {
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
  public static @NotNull OptionState compatibility() {
    return MOST_COMPATIBLE;
  }

  /**
   * Configure how to emit hover event values.
   *
   * @since 4.15.0
   */
  public enum HoverEventValueMode {
    /**
     * Only emit the 1.16+ modern hover events.
     *
     * @since 4.15.0
     */
    MODERN_ONLY,
    /**
     * Only emit the pre-1.16 hover event {@code value} field.
     *
     * @since 4.15.0
     */
    LEGACY_ONLY,
    /**
     * Include both modern and legacy hover event fields, for maximum compatibility.
     *
     * @since 4.15.0
     */
    BOTH,
  }

  /**
   * Configure how to emit show_item hovers.
   *
   * @since 4.17.0
   */
  public enum ShowItemHoverDataMode {
    /**
     * Only emit the pre-1.20.5 item nbt.
     *
     * @since 4.17.0
     */
    EMIT_LEGACY_NBT,
    /**
     * Only emit modern data components.
     *
     * @since 4.17.0
     */
    EMIT_DATA_COMPONENTS,
    /**
     * Emit whichever of legacy or modern data the item has.
     *
     * @since 4.17.0
     */
    EMIT_EITHER,
  }
}
