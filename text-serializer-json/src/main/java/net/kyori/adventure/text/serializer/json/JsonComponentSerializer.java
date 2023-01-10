package net.kyori.adventure.text.serializer.json;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A generic JSON component serializer.
 *
 * @since 4.13.0
 */
public interface JsonComponentSerializer extends ComponentSerializer<Component, Component, String> {
  /**
   * Gets a component serializer for JSON serialization and deserialization.
   *
   * @return a JSON component serializer
   * @since 4.13.0
   */
  static @NotNull JsonComponentSerializer json() {
    return JsonComponentSerializerImpl.Instances.INSTANCE;
  }

  /**
   * Gets a component serializer for legacy JSON serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a JSON component serializer
   * @since 4.13.0
   */
  static @NotNull JsonComponentSerializer jsonLegacy() {
    return JsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
  }

  /**
   * A {@link JsonComponentSerializer} service provider.
   *
   * @since 4.13.0
   */
  @ApiStatus.Internal
  @PlatformAPI
  interface Provider {
    /**
     * Provides a standard {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.13.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull JsonComponentSerializer json();

    /**
     * Provides a legacy {@link JsonComponentSerializer}.
     *
     * @return a {@link JsonComponentSerializer}
     * @since 4.13.0
     */
    @ApiStatus.Internal
    @PlatformAPI
    @NotNull JsonComponentSerializer jsonLegacy();
  }
}
