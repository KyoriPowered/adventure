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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.option.Option;
import net.kyori.option.OptionSchema;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;

/**
 * Options that can apply to {@linkplain NBTComponentSerializer NBT serializers}.
 *
 * <p>See serializer documentation for specific details on which flags are supported.</p>
 *
 * @since 4.24.0
 */
public final class NBTSerializerOptions {

  /**
   * How to emit shadow colour data.
   *
   * @since 4.24.0
   */
  public static final Option<ShadowColorEmitMode> SHADOW_COLOR_MODE;

  /**
   * Control how hover event values should be emitted.
   *
   * @since 4.24.0
   */
  public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE;

  /**
   * Control how click event values should be emitted.
   *
   * @since 4.24.0
   */
  public static final Option<ClickEventValueMode> EMIT_CLICK_EVENT_TYPE;

  /**
   * Whether to emit the default hover event item stack quantity of {@code 1}.
   *
   * <p>When enabled, this matches Vanilla as of 1.21.6.</p>
   *
   * @since 4.24.0
   */
  public static final Option<Boolean> EMIT_DEFAULT_ITEM_HOVER_QUANTITY; // TODO: Find out in which version it was added and add it to the option state

  /**
   * How to emit show item hovers in {@code hoverEvent} (camelCase) fields.
   *
   * @since 4.24.0
   */
  public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE;

  private static final OptionSchema SCHEMA;
  private static final OptionState.Versioned BY_DATA_VERSION;

  private static final int VERSION_23W40A = 3679;
  private static final int VERSION_24W09A = 3819;
  private static final int VERSION_24W44A = 4174;
  private static final int VERSION_25W02A = 4298;

  static {
    OptionSchema.Mutable schema = OptionSchema.emptySchema();
    SHADOW_COLOR_MODE = schema.enumOption(key("emit/shadow_color"), ShadowColorEmitMode.class, ShadowColorEmitMode.EMIT_INTEGER);
    EMIT_HOVER_EVENT_TYPE = schema.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.SNAKE_CASE);
    EMIT_CLICK_EVENT_TYPE = schema.enumOption(key("emit/click_value_mode"), ClickEventValueMode.class, ClickEventValueMode.SNAKE_CASE);
    EMIT_DEFAULT_ITEM_HOVER_QUANTITY = schema.booleanOption(key("emit/default_item_hover_quantity"), true);
    SHOW_ITEM_HOVER_DATA_MODE = schema.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);
    SCHEMA = schema.frozenView();

    BY_DATA_VERSION = SCHEMA.versionedStateBuilder()
      .version(
        VERSION_23W40A,
        builder -> builder.value(SHADOW_COLOR_MODE, ShadowColorEmitMode.NONE)
          .value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.CAMEL_CASE)
          .value(EMIT_CLICK_EVENT_TYPE, ClickEventValueMode.CAMEL_CASE)
          .value(EMIT_DEFAULT_ITEM_HOVER_QUANTITY, false)
          .value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_LEGACY_NBT)
      )
      .version(
        VERSION_24W09A,
        builder -> builder.value(SHOW_ITEM_HOVER_DATA_MODE, ShowItemHoverDataMode.EMIT_DATA_COMPONENTS)
      )
      .version(
        VERSION_24W44A,
        builder -> builder.value(SHADOW_COLOR_MODE, ShadowColorEmitMode.EMIT_ARRAY)
      )
      .version(
        VERSION_25W02A,
        builder -> builder.value(EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.SNAKE_CASE)
          .value(EMIT_CLICK_EVENT_TYPE, ClickEventValueMode.SNAKE_CASE)
      )
      .build();
  }

  private NBTSerializerOptions() {
  }

  private static String key(final String value) {
    return "adventure:nbt/" + value;
  }

  /**
   * A schema of available options.
   *
   * @return the schema of known NBT serializer options
   * @since 4.24.0
   */
  public static @NotNull OptionSchema schema() {
    return SCHEMA;
  }

  /**
   * NBT serializer options delineated by world data version.
   *
   * @return the versioned option state
   * @since 4.24.0
   */
  public static OptionState.@NotNull Versioned byDataVersion() {
    return BY_DATA_VERSION;
  }

  /**
   * Configure how to emit hover event values.
   *
   * @since 4.24.0
   */
  public enum HoverEventValueMode {
    /**
     * Only emit the 1.21.5+ hover events using the {@code hover_event} field.
     *
     * @since 4.24.0
     */
    SNAKE_CASE,
    /**
     * Only emit the 1.16+ hover events using the {@code hoverEvent} field.
     *
     * @since 4.24.0
     */
    CAMEL_CASE,
    /**
     * Include both camel and snake case hover event fields, for maximum compatibility.
     *
     * @since 4.24.0
     */
    BOTH
  }

  /**
   * Configure how to emit click event values.
   *
   * @since 4.24.0
   */
  public enum ClickEventValueMode {
    /**
     * Only emit the 1.21.5+ click events using the {@code click_event} field.
     *
     * @since 4.24.0
     */
    SNAKE_CASE,
    /**
     * Only emit the pre-1.21.5 click events using the {@code clickEvent} field.
     *
     * @since 4.24.0
     */
    CAMEL_CASE,
    /**
     * Include both camel and snake case click event fields, for maximum compatibility.
     *
     * @since 4.24.0
     */
    BOTH,
  }

  /**
   * How text shadow colors should be emitted.
   *
   * @since 4.24.0
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
     * @since 4.24.0
     */
    EMIT_INTEGER,
    /**
     * Emit a colour as 4-element float array of the RGBA components of the colour.
     *
     * @since 4.24.0
     */
    EMIT_ARRAY
  }

  /**
   * Configure how to emit show item hovers in {@code hoverEvent} (camelCase) fields.
   *
   * @since 4.24.0
   */
  public enum ShowItemHoverDataMode {
    /**
     * Only emit the pre-1.20.5 item NBT.
     *
     * @since 4.24.0
     */
    EMIT_LEGACY_NBT,
    /**
     * Only emit modern data components.
     *
     * @since 4.24.0
     */
    EMIT_DATA_COMPONENTS,
    /**
     * Emit whichever of legacy or modern data the item has.
     *
     * @since 4.24.0
     */
    EMIT_EITHER,
  }
}
