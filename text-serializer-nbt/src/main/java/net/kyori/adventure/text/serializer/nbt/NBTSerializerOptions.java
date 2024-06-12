package net.kyori.adventure.text.serializer.nbt;

import net.kyori.option.Option;

public final class NBTSerializerOptions {

  public static final Option<Boolean> EMIT_COMPACT_TEXT_COMPONENT = Option.booleanOption(key("emit/compact_text_component"), true);

  /**
   * How to emit the item data on {@code show_item} hover events.
   *
   * @since 4.18.0
   */
  public static final Option<ShowItemHoverDataMode> SHOW_ITEM_HOVER_DATA_MODE = Option.enumOption(key("emit/show_item_hover_data"), ShowItemHoverDataMode.class, ShowItemHoverDataMode.EMIT_EITHER);

  public static final Option<Boolean> EMIT_MODERN_HOVER = Option.booleanOption(key("emit/modern_hover"), true);
  public static final Option<Boolean> EMIT_LEGACY_HOVER = Option.booleanOption(key("emit/legacy_hover"), false);

  public static final Option<Boolean> SERIALIZE_COMPONENT_TYPES = Option.booleanOption(key("serialize/component-types"), true);

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
}
