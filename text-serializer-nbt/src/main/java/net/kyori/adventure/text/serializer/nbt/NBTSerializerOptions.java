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
 * @since 4.18.0
 */
public final class NBTSerializerOptions {
  /**
   * Whether to emit text components with no style and no children as plain text.
   *
   * @since 4.18.0
   * @sinceMinecraft 1.20.3
   */
  public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT = Option.booleanOption(key("emit/compact_text_component"), true);

  /**
   * How to emit the item data on {@code show_item} hover events.
   *
   * @since 4.18.0
   */
  public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE = Option.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);

  /**
   * Whether to emit RGB text.
   *
   * <p>If this attribute is disabled, colors in styles will be downsampled to the classic 16 colors.</p>
   *
   * @since 4.18.0
   * @sinceMinecraft 1.16
   */
  public static final Option<Boolean> EMIT_RGB = Option.booleanOption(key("emit/rgb"), true);

  /**
   * Whether to serialize the types of {@linkplain net.kyori.adventure.text.Component components}.
   *
   * @since 4.18.0
   */
  public static final Option<Boolean> SERIALIZE_COMPONENT_TYPES = Option.booleanOption(key("serialize/component-types"), true);


  /**
   * Control how hover event values should be emitted.
   *
   * @since 4.18.0
   */
  public static final Option<HoverEventValueMode> EMIT_HOVER_EVENT_TYPE = Option.enumOption(key("emit/hover_value_mode"), HoverEventValueMode.class, HoverEventValueMode.MODERN_ONLY);

  private NBTSerializerOptions() {
  }

  private static String key(final String value) {
    return "adventure:nbt/" + value;
  }

  /**
   * Configure how to emit show_item hovers.
   *
   * @since 4.18.0
   */
  public enum ShowItemHoverDataMode {
    /**
     * Only emit the pre-1.20.5 item nbt.
     *
     * @since 4.18.0
     */
    EMIT_LEGACY_NBT,
    /**
     * Only emit modern data components.
     *
     * @since 4.18.0
     */
    EMIT_DATA_COMPONENTS,
    /**
     * Emit whichever of legacy or modern data the item has.
     *
     * @since 4.18.0
     */
    EMIT_EITHER,
  }

  /**
   * Configure how to emit hover event values.
   *
   * @since 4.18.0
   */
  public enum HoverEventValueMode {
    /**
     * Only emit the 1.16+ modern hover events.
     *
     * @since 4.18.0
     */
    MODERN_ONLY,
    /**
     * Only emit the pre-1.16 hover event {@code value} field.
     *
     * @since 4.18.0
     */
    LEGACY_ONLY,
    /**
     * Include both modern and legacy hover event fields, for maximum compatibility.
     *
     * @since 4.18.0
     */
    BOTH,
  }
}
