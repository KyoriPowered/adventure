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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.option.Option;

/**
 * Options that can apply to {@linkplain NBTComponentSerializer NBT serializers}.
 *
 * <p>See serializer documentation for specific details on which flags are supported.</p>
 *
 * @since 4.24.0
 */
public final class NBTSerializerOptions {

  /**
   * Whether to emit text components with no style and no children as plain text.
   *
   * @since 4.24.0
   * @sinceMinecraft 1.20.3
   */
  public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT = Option.booleanOption(key("emit/compact_text_component"), true);

  /**
   * How to emit shadow colour data.
   *
   * @since 4.24.0
   */
  public static final Option<ShadowColorEmitMode> SHADOW_COLOR_MODE = Option.enumOption(key("emit/shadow_color"), ShadowColorEmitMode.class, ShadowColorEmitMode.EMIT_INTEGER);

  /**
   * Control how hover event values should be emitted.
   *
   * @since 4.24.0
   */
  public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE = Option.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.SNAKE_CASE);

  /**
   * Control how click event values should be emitted.
   *
   * @since 4.24.0
   */
  public static final Option<ClickEventValueMode> EMIT_CLICK_EVENT_TYPE = Option.enumOption(key("emit/click_value_mode"), ClickEventValueMode.class, ClickEventValueMode.SNAKE_CASE);

  /**
   * Whether to emit the default hover event item stack quantity of {@code 1}.
   *
   * <p>When enabled, this matches Vanilla as of 1.20.5.</p>
   *
   * @since 4.24.0
   */
  public static final Option<Boolean> EMIT_DEFAULT_ITEM_HOVER_QUANTITY = Option.booleanOption(key("emit/default_item_hover_quantity"), true);

  /**
   * Whether to emit the default interpret value ({@code false}) of NBT components.
   *
   * @since 4.24.0
   */
  public static final Option<Boolean> EMIT_DEFAULT_NBT_INTERPRET_VALUE = Option.booleanOption(key("emit/default_nbt_interpret_value"), true);

  /**
   * Control how entity ids of show entity hover events should be emitted.
   *
   * @since 4.24.0
   */
  public static final Option<ShowEntityUUIDEmitMode> EMIT_SHOW_ENTITY_UUID_TYPE = Option.enumOption(key("emit/show_entity_uuid"), ShowEntityUUIDEmitMode.class, ShowEntityUUIDEmitMode.EMIT_INT_ARRAY);

  private NBTSerializerOptions() {
  }

  private static String key(final String value) {
    return "adventure:nbt/" + value;
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
   * Configure how to emit entity ids of show entity hover events.
   *
   * @since 4.24.0
   */
  public enum ShowEntityUUIDEmitMode {
    /**
     * Emit as a string.
     *
     * @since 4.24.0
     */
    EMIT_STRING,
    /**
     * Emit as an int array.
     *
     * @since 4.24.0
     */
    EMIT_INT_ARRAY,
    /**
     * Emit as an int list.
     *
     * @since 4.24.0
     */
    EMIT_LIST
  }
}
