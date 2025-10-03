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
package net.kyori.adventure.text.serializer.json;

import net.kyori.option.Option;
import net.kyori.option.OptionSchema;
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
  private static final int VERSION_1_21_4 = 4174; // 24w44a
  private static final int VERSION_1_21_5 = 4298; // 25w02a
  private static final int VERSION_1_21_6 = 4422; // 25w15a

  // todo(5.0): move these options out of the global schema
  private static final OptionSchema.Mutable UNSAFE_SCHEMA = OptionSchema.globalSchema();

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
  public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE = UNSAFE_SCHEMA.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.SNAKE_CASE);

  /**
   * Control how click event values should be emitted.
   *
   * @since 4.20.0
   */
  public static final Option<ClickEventValueMode> EMIT_CLICK_EVENT_TYPE = Option.enumOption(key("emit/click_value_mode"), ClickEventValueMode.class, ClickEventValueMode.SNAKE_CASE);

  /**
   * Whether to emit text components with no style and no children as plain text.
   *
   * @since 4.15.0
   * @sinceMinecraft 1.20.3
   */
  public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT = UNSAFE_SCHEMA.booleanOption(key("emit/compact_text_component"), true);

  /**
   * Whether to emit the hover event show entity action's entity UUID as an int array,
   * as understood by 1.20.3+, or as a string as understood by previous versions.
   *
   * @since 4.15.0
   */
  public static final Option<Boolean> EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY = UNSAFE_SCHEMA.booleanOption(key("emit/hover_show_entity_id_as_int_array"), true);

  /**
   * Whether to emit the hover event show entity action's entity type key as a {@code type} and UUID as an {@code id}, as it was before 1.21.5.
   *
   * @since 4.20.0
   */
  public static final Option<Boolean> EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID = UNSAFE_SCHEMA.booleanOption(key("emit/hover_show_entity_key_as_type_and_uuid_as_id"), false);

  /**
   * Whether to be strict about accepting invalid hover/click events.
   *
   * <p>When enabled, this matches Vanilla as of 1.20.3.</p>
   *
   * @since 4.15.0
   */
  public static final Option<Boolean> VALIDATE_STRICT_EVENTS = UNSAFE_SCHEMA.booleanOption(key("validate/strict_events"), true);

  /**
   * Whether to emit the default hover event item stack quantity of {@code 1}.
   *
   * <p>When enabled, this matches Vanilla as of 1.20.5.</p>
   *
   * @since 4.17.0
   */
  public static final Option<Boolean> EMIT_DEFAULT_ITEM_HOVER_QUANTITY = UNSAFE_SCHEMA.booleanOption(key("emit/default_item_hover_quantity"), true);

  /**
   * How to emit the item data on {@code show_item} hover events.
   *
   * @since 4.17.0
   */
  public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE = UNSAFE_SCHEMA.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);

  /**
   * How to emit shadow colour data.
   *
   * @since 4.18.0
   */
  public static final Option<ShadowColorEmitMode> SHADOW_COLOR_MODE = UNSAFE_SCHEMA.enumOption(key("emit/shadow_color"), ShadowColorEmitMode.class, ShadowColorEmitMode.EMIT_INTEGER);

  /**
   * Whether to emit the page in a {@code change_page} click event as a string.
   *
   * @since 4.22.0
   */
  public static final Option<Boolean> EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING = UNSAFE_SCHEMA.booleanOption(key("emit/change_page_click_event_page_as_string"), false);

  /**
   * Whether to prepend {@code https://} to {@code open_url} click event URIs without a valid scheme.
   *
   * <p>As of minecraft 1.21.5 URIs that are not a {@code http://} or {@code https://} scheme fail to parse.</p>
   *
   * @since 4.25.0
   */
  public static final Option<Boolean> EMIT_CLICK_URL_HTTPS = UNSAFE_SCHEMA.booleanOption(key("emit/click_url_https"), false);

  // aim for compatibility? or something
  private static final OptionSchema SCHEMA = OptionSchema.childSchema(UNSAFE_SCHEMA).frozenView();

  /**
   * Versioned by world data version.
   */
  private static final OptionState.Versioned BY_DATA_VERSION = SCHEMA.versionedStateBuilder()
    .version(
      VERSION_INITIAL,
      b -> b.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.VALUE_FIELD)
        .value(EMIT_CLICK_EVENT_TYPE, ClickEventValueMode.CAMEL_CASE)
        .value(EMIT_RGB, false)
        .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
        .value(EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID, true)
        .value(VALIDATE_STRICT_EVENTS, false)
        .value(EMIT_DEFAULT_ITEM_HOVER_QUANTITY, false)
        .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_LEGACY_NBT)
        .value(SHADOW_COLOR_MODE, ShadowColorEmitMode.NONE)
        .value(EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING, true)
    )
    .version(
      VERSION_1_16,
      b -> b.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.CAMEL_CASE)
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
    .version(
      VERSION_1_21_4,
      b -> b.value(SHADOW_COLOR_MODE, ShadowColorEmitMode.EMIT_INTEGER)
    )
    .version(
      VERSION_1_21_5,
      b -> b.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.SNAKE_CASE)
        .value(EMIT_CLICK_EVENT_TYPE, ClickEventValueMode.SNAKE_CASE)
        .value(EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID, false)
        .value(EMIT_CLICK_URL_HTTPS, true)
    )
    .version(
      VERSION_1_21_6,
      b -> b.value(EMIT_CHANGE_PAGE_CLICK_EVENT_PAGE_AS_STRING, false)
    )
    .build();

  /**
   * The combination of features that can be understood by modern clients, as well as as far back as possible.
   *
   * <p>This may provide a less efficient representation of components, but will not result in information being discarded.</p>
   */
  private static final OptionState MOST_COMPATIBLE = SCHEMA.stateBuilder()
    .value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.ALL)
    .value(EMIT_CLICK_EVENT_TYPE, ClickEventValueMode.BOTH)
    .value(EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, false)
    .value(EMIT_COMPACT_TEXT_COMPONENT, false)
    .value(VALIDATE_STRICT_EVENTS, false)
    .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_EITHER)
    .value(SHADOW_COLOR_MODE, ShadowColorEmitMode.EMIT_INTEGER)
    .value(EMIT_CLICK_URL_HTTPS, true)
    .build();

  private static String key(final String value) {
    return "adventure:json/" + value;
  }

  /**
   * A schema of available options.
   *
   * @return the schema of known json options
   * @since 4.20.0
   */
  public static @NotNull OptionSchema schema() {
    return SCHEMA;
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
     * Only emit the 1.21.5+ hover events using the {@code hover_event} field.
     *
     * @since 4.20.0
     */
    SNAKE_CASE,
    /**
     * Only emit the 1.16+ hover events using the {@code hoverEvent} field.
     *
     * @since 4.15.0
     */
    CAMEL_CASE,
    /**
     * Only emit the pre-1.16 hover event {@code value} field.
     *
     * @since 4.15.0
     */
    VALUE_FIELD,
    /**
     * Include all hover event fields, for maximum compatibility.
     *
     * @since 4.15.0
     */
    ALL;

    /**
     * Only emit the 1.16+ hover events using the {@code hoverEvent} field.
     *
     * @deprecated use {@link #CAMEL_CASE} instead
     */
    public static final @Deprecated HoverEventValueMode MODERN_ONLY = CAMEL_CASE;
    /**
     * Only emit the pre-1.16 hover event {@code value} field.
     *
     * @deprecated use {@link #VALUE_FIELD} instead
     */
    public static final @Deprecated HoverEventValueMode LEGACY_ONLY = VALUE_FIELD;
    /**
     * Include all hover event fields, for maximum compatibility.
     *
     * @deprecated use {@link #ALL} instead
     */
    public static final @Deprecated HoverEventValueMode BOTH = ALL;
  }

  /**
   * Configure how to emit click event values.
   *
   * @since 4.20.0
   */
  public enum ClickEventValueMode {
    /**
     * Only emit the 1.21.5+ click events using the {@code click_event} field.
     *
     * @since 4.20.0
     */
    SNAKE_CASE,
    /**
     * Only emit the pre-1.21.5 click events using the {@code clickEvent} field.
     *
     * @since 4.20.0
     */
    CAMEL_CASE,
    /**
     * Include both camel and snake case click event fields, for maximum compatibility.
     *
     * @since 4.20.0
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

  /**
   * How text shadow colors should be emitted.
   *
   * @since 4.18.0
   * @sinceMinecraft 1.21.4
   */
  public enum ShadowColorEmitMode {
    /**
     * Do not emit shadow colours.
     */
    NONE,
    /**
     * Emit as a single packed integer value containing, in order, ARGB bytes.
     *
     * @since 4.18.0
     */
    EMIT_INTEGER,
    /**
     * Emit a colour as 4-element float array of the RGBA components of the colour.
     *
     * @since 4.18.0
     */
    EMIT_ARRAY

  }
}
